package appli.accueil;

import appli.StartApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Log;
import repository.LogRepository;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller de la vue journal d'activité (LogView.fxml).
 *
 * Charge tous les logs depuis la base et les affiche dans un TableView.
 * Fonctionnalités :
 *  - Recherche en temps réel sur utilisateur, message, action ou page
 *  - Filtre par type d'action (ComboBox)
 *  - Coloration des actions : connexion=bleu, ajout=vert, modification=orange,
 *    suppression=refus=rouge
 *  - Retour à la ligne automatique dans la colonne "Message"
 *  - Compteur d'entrées affichées / total
 *
 * Le filtrage utilise une FilteredList pour ne pas recharger la BDD à chaque frappe.
 */
public class LogController {

    @FXML private TableView<Log>              logTable;
    @FXML private TableColumn<Log, Integer>   idColumn;
    @FXML private TableColumn<Log, String>    heureColumn;
    @FXML private TableColumn<Log, String>    userColumn;
    @FXML private TableColumn<Log, String>    messageColumn;
    @FXML private TableColumn<Log, String>    actionColumn;
    @FXML private TableColumn<Log, String>    pageColumn;

    @FXML private TextField          rechercheField;
    @FXML private ComboBox<String>   filtreActionCombo;
    @FXML private Label              compteurLabel;
    @FXML private Label              statutLabel;

    private final LogRepository logRepository = new LogRepository();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /** Liste source contenant tous les logs chargés depuis la BDD. */
    private ObservableList<Log> tousLesLogs;

    /** Vue filtrée de tousLesLogs, affectée au TableView. */
    private FilteredList<Log> logsFiltres;

    @FXML
    public void initialize() {
        // Colonnes redimensionnées pour remplir toute la largeur disponible
        logTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        configurerColonnes();
        chargerDonnees();
    }

    /** Configure les cell value factories et les rendus visuels de chaque colonne. */
    private void configurerColonnes() {
        idColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleIntegerProperty(c.getValue().getIdLog()).asObject());

        heureColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(
                c.getValue().getDateHeure() != null
                    ? c.getValue().getDateHeure().format(FMT) : ""));

        userColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getNomUtilisateur()));

        actionColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getAction()));

        pageColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getPage()));

        messageColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getMessageLog()));

        // Coloration de la colonne Action selon le type d'opération
        actionColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String action, boolean empty) {
                super.updateItem(action, empty);
                if (empty || action == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(action);
                    String base = "-fx-font-weight: bold; -fx-alignment: CENTER;";
                    switch (action.toUpperCase()) {
                        case "CONNEXION" -> setStyle(base + "-fx-text-fill: #1565c0;");
                        case "AJOUTER"   -> setStyle(base + "-fx-text-fill: #27ae60;");
                        case "MODIFIER"  -> setStyle(base + "-fx-text-fill: #e67e22;");
                        case "SUPPRIMER" -> setStyle(base + "-fx-text-fill: #e74c3c;");
                        case "VALIDER"   -> setStyle(base + "-fx-text-fill: #27ae60;");
                        case "REFUSER"   -> setStyle(base + "-fx-text-fill: #c0392b;");
                        default          -> setStyle(base + "-fx-text-fill: #555;");
                    }
                }
            }
        });

        // Retour à la ligne automatique pour les messages longs
        messageColumn.setCellFactory(col -> new TableCell<>() {
            private final Label label = new Label();
            {
                label.setWrapText(true);
                label.setMaxWidth(Double.MAX_VALUE);
                setGraphic(label);
            }
            @Override
            protected void updateItem(String msg, boolean empty) {
                super.updateItem(msg, empty);
                label.setText(empty || msg == null ? "" : msg);
            }
        });
    }

    /** Charge tous les logs depuis la BDD, initialise la FilteredList et remplit le ComboBox. */
    private void chargerDonnees() {
        List<Log> logs = logRepository.getTousLesLogs();
        tousLesLogs  = FXCollections.observableArrayList(logs);
        logsFiltres  = new FilteredList<>(tousLesLogs, p -> true); // pas de filtre au départ
        logTable.setItems(logsFiltres);

        // Remplissage du ComboBox avec les types d'actions distincts trouvés dans les logs
        List<String> actions = logs.stream()
            .map(Log::getAction)
            .filter(a -> a != null && !a.isBlank())
            .distinct()
            .sorted()
            .toList();
        filtreActionCombo.setItems(FXCollections.observableArrayList(actions));

        mettreAJourCompteur();
        statutLabel.setText("Actualisé à " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    /** Déclenché à chaque frappe dans le champ de recherche. */
    @FXML
    private void handleRecherche() {
        appliquerFiltres();
    }

    /** Déclenché lors de la sélection d'une action dans le ComboBox. */
    @FXML
    private void handleFiltreAction() {
        appliquerFiltres();
    }

    /** Réinitialise la recherche et le filtre d'action. */
    @FXML
    private void handleEffacerFiltre() {
        rechercheField.clear();
        filtreActionCombo.setValue(null);
        appliquerFiltres();
    }

    /** Recharge les données depuis la BDD (utile si de nouveaux logs ont été créés). */
    @FXML
    private void handleActualiser() {
        chargerDonnees();
    }

    /**
     * Combine le filtre texte et le filtre par action pour mettre à jour le prédicat
     * de la FilteredList. Aucune requête BDD n'est effectuée ici.
     */
    private void appliquerFiltres() {
        String terme        = rechercheField.getText().trim().toLowerCase();
        String actionFiltre = filtreActionCombo.getValue();

        logsFiltres.setPredicate(log -> {
            // Filtre texte : recherche dans tous les champs textuels
            boolean matchTerme = terme.isEmpty()
                || (log.getNomUtilisateur() != null && log.getNomUtilisateur().toLowerCase().contains(terme))
                || (log.getMessageLog()     != null && log.getMessageLog().toLowerCase().contains(terme))
                || (log.getAction()         != null && log.getAction().toLowerCase().contains(terme))
                || (log.getPage()           != null && log.getPage().toLowerCase().contains(terme));

            // Filtre action : null = "toutes les actions"
            boolean matchAction = actionFiltre == null || actionFiltre.equals(log.getAction());

            return matchTerme && matchAction;
        });

        mettreAJourCompteur();
    }

    /** Affiche le nombre d'entrées visibles sur le total chargé. */
    private void mettreAJourCompteur() {
        int affiche = logsFiltres  != null ? logsFiltres.size()   : 0;
        int total   = tousLesLogs  != null ? tousLesLogs.size()   : 0;
        if (affiche == total) {
            compteurLabel.setText(total + " entrée" + (total > 1 ? "s" : ""));
        } else {
            compteurLabel.setText(affiche + " / " + total + " entrées");
        }
    }

    @FXML
    public void handleRetour(ActionEvent event) {
        StartApplication.goBack();
    }
}
