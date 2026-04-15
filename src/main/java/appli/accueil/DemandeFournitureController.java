package appli.accueil;

import appli.StartApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import model.DemandeFourniture;
import model.Fourniture;
import model.FournitureDemandeFourniture;
import model.Utilisateur;
import repository.DemandeFournitureRepository;
import repository.FournitureDemandeFournitureRepository;
import repository.FournitureRepository;
import repository.UtilisateurRepository;
import service.LogService;
import session.SessionUtilisateur;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Controller de la gestion des demandes de fournitures (DemandeFournitureView.fxml).
 *
 * Flux de création d'une demande :
 *  1. Le professeur sélectionne des fournitures une par une (combo + quantité) → liste temporaire
 *  2. Il saisit une raison puis clique "Envoyer" → crée la DemandeFourniture en BDD puis insère
 *     les lignes dans la table de liaison (FournitureDemandeFourniture)
 *
 * Flux de validation (Gestionnaire/Admin) :
 *  - Valider : décrémente le stock de chaque fourniture concernée puis passe le statut à "Validé"
 *  - Refuser : demande une justification puis passe à "Refusé" (stock non modifié)
 *
 * Le statut est coloré : vert=Validé, rouge=Refusé, orange=En attente.
 */
public class DemandeFournitureController {

    // Formulaire de création
    @FXML private TextArea raisonArea;
    @FXML private ComboBox<Fourniture> fournitureCombo;
    @FXML private TextField quantiteTextField;
    @FXML private ListView<String> fournituresListView;
    @FXML private Button ajouterFournitureBtn;
    @FXML private Button envoyerBtn;
    @FXML private Button annulerBtn;
    @FXML private Label messageLabel;
    @FXML private Label compteurLabel;
    @FXML private Label statutLabel;

    // Table
    @FXML private TableView<DemandeFourniture> demandeTableView;
    @FXML private TableColumn<DemandeFourniture, Integer> idColumn;
    @FXML private TableColumn<DemandeFourniture, String> dateColumn;
    @FXML private TableColumn<DemandeFourniture, String> professeurColumn;
    @FXML private TableColumn<DemandeFourniture, String> raisonColumn;
    @FXML private TableColumn<DemandeFourniture, String> statutColumn;
    @FXML private TableColumn<DemandeFourniture, Void> actionsColumn;

    private DemandeFournitureRepository demandeRepo;
    private FournitureDemandeFournitureRepository junctionRepo;
    private FournitureRepository fournitureRepo;
    private UtilisateurRepository utilisateurRepo;
    private ObservableList<DemandeFourniture> demandesList;
    // Fournitures sélectionnées pour la nouvelle demande: "libelle|idFourniture|quantite"
    private ObservableList<String> selectedFournitures;
    private int demandeEnCoursId = -1;

    @FXML
    public void initialize() {
        demandeRepo = new DemandeFournitureRepository();
        junctionRepo = new FournitureDemandeFournitureRepository();
        fournitureRepo = new FournitureRepository();
        utilisateurRepo = new UtilisateurRepository();
        demandesList = FXCollections.observableArrayList();
        selectedFournitures = FXCollections.observableArrayList();

        // Remplir combo fournitures
        fournitureCombo.setItems(FXCollections.observableArrayList(fournitureRepo.getToutesLesFournitures()));
        fournitureCombo.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Fourniture f) { return f == null ? "" : f.getLibelle(); }
            @Override public Fourniture fromString(String s) { return null; }
        });

        fournituresListView.setItems(selectedFournitures);

        // Colonnes table
        idColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleIntegerProperty(c.getValue().getIdDemandeFourniture()).asObject());
        dateColumn.setCellValueFactory(c -> {
            java.time.LocalDate d = c.getValue().getDate();
            return new javafx.beans.property.SimpleStringProperty(d != null ? d.toString() : "");
        });
        raisonColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getRaison()));
        statutColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getStatut()));
        professeurColumn.setCellValueFactory(c -> {
            Utilisateur u = utilisateurRepo.getUtilisateurById(c.getValue().getRefProfesseur());
            return new javafx.beans.property.SimpleStringProperty(
                u != null ? u.getNom() + " " + u.getPrenom() : "ID " + c.getValue().getRefProfesseur());
        });

        // Colorer statut
        statutColumn.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String statut, boolean empty) {
                super.updateItem(statut, empty);
                if (empty || statut == null) { setText(null); setStyle(""); }
                else {
                    setText(statut);
                    switch (statut) {
                        case "Validé"    -> setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                        case "Refusé"    -> setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                        default          -> setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
                    }
                }
            }
        });

        ajouterBoutonsActions();
        chargerDonnees();
    }

    private void ajouterBoutonsActions() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button validerBtn = new Button("✅ Valider");
            private final Button refuserBtn = new Button("❌ Refuser");
            private final Button supprimerBtn = new Button("🗑");
            private final HBox pane = new HBox(4, validerBtn, refuserBtn, supprimerBtn);
            {
                validerBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 10px;");
                refuserBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 10px;");
                supprimerBtn.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-font-size: 10px;");

                validerBtn.setOnAction(e -> {
                    DemandeFourniture d = getTableView().getItems().get(getIndex());
                    if (!"En attente".equals(d.getStatut())) {
                        afficherErreur("Ce dossier est déjà traité.");
                        return;
                    }
                    // Décrémenter le stock pour chaque fourniture
                    for (FournitureDemandeFourniture fd : junctionRepo.getFournituresDemande(d.getIdDemandeFourniture())) {
                        fournitureRepo.retirerDuStock(fd.getRefFourniture(), fd.getQuantite());
                    }
                    demandeRepo.validerDemande(d.getIdDemandeFourniture());
                    LogService.log("Demande #" + d.getIdDemandeFourniture() + " validée", "VALIDER", "DemandeFourniture");
                    afficherSucces("Demande validée et stocks mis à jour !");
                    chargerDonnees();
                });

                refuserBtn.setOnAction(e -> {
                    DemandeFourniture d = getTableView().getItems().get(getIndex());
                    if (!"En attente".equals(d.getStatut())) {
                        afficherErreur("Ce dossier est déjà traité.");
                        return;
                    }
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Refus de la demande");
                    dialog.setHeaderText("Justification du refus");
                    dialog.setContentText("Raison :");
                    dialog.showAndWait().ifPresent(justif -> {
                        demandeRepo.refuserDemande(d.getIdDemandeFourniture(), justif);
                        LogService.log("Demande #" + d.getIdDemandeFourniture() + " refusée", "REFUSER", "DemandeFourniture");
                        afficherSucces("Demande refusée.");
                        chargerDonnees();
                    });
                });

                supprimerBtn.setOnAction(e -> {
                    DemandeFourniture d = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer cette demande ?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait().ifPresent(r -> {
                        if (r == ButtonType.YES) {
                            junctionRepo.supprimerToutesFournituresDemande(d.getIdDemandeFourniture());
                            demandeRepo.supprimerDemande(d.getIdDemandeFourniture());
                            LogService.log("Demande #" + d.getIdDemandeFourniture() + " supprimée", "SUPPRIMER", "DemandeFourniture");
                            chargerDonnees();
                        }
                    });
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    /**
     * Ajoute la fourniture sélectionnée (avec sa quantité) à la liste temporaire.
     * Format interne de chaque entrée : "libelle × qté|idFourniture|qté"
     */
    @FXML
    private void handleAjouterFourniture() {
        Fourniture f = fournitureCombo.getValue();
        if (f == null) { afficherErreur("Sélectionnez une fourniture !"); return; }
        String qStr = quantiteTextField.getText().trim();
        if (qStr.isEmpty()) { afficherErreur("Entrez une quantité !"); return; }
        try {
            int q = Integer.parseInt(qStr);
            if (q <= 0) { afficherErreur("La quantité doit être positive !"); return; }
            selectedFournitures.add(f.getLibelle() + " × " + q + "|" + f.getIdFourniture() + "|" + q);
            fournitureCombo.setValue(null);
            quantiteTextField.clear();
            messageLabel.setText("");
        } catch (NumberFormatException e) {
            afficherErreur("Quantité invalide !");
        }
    }

    @FXML
    private void handleRetirerFourniture() {
        int idx = fournituresListView.getSelectionModel().getSelectedIndex();
        if (idx >= 0) selectedFournitures.remove(idx);
    }

    /**
     * Soumet la demande : insère la DemandeFourniture puis les lignes de liaison.
     * Vide ensuite la liste temporaire et recharge le tableau.
     */
    @FXML
    private void handleEnvoyer() {
        if (raisonArea.getText().trim().isEmpty()) { afficherErreur("La raison est obligatoire !"); return; }
        if (selectedFournitures.isEmpty()) { afficherErreur("Ajoutez au moins une fourniture !"); return; }

        int idProf = SessionUtilisateur.getInstance().getUtilisateurConnecte().getIdUtilisateur();
        DemandeFourniture d = new DemandeFourniture(LocalDate.now(), "En attente", raisonArea.getText().trim(), idProf);
        int idDemande = demandeRepo.ajouterDemande(d);
        if (idDemande < 0) { afficherErreur("Erreur lors de la création de la demande."); return; }

        // Ajouter les fournitures
        for (String item : selectedFournitures) {
            String[] parts = item.split("\\|");
            int idFourniture = Integer.parseInt(parts[1]);
            int quantite = Integer.parseInt(parts[2]);
            junctionRepo.ajouterFournitureADemande(idDemande, idFourniture, quantite);
        }

        LogService.log("Demande créée #" + idDemande, "AJOUTER", "DemandeFourniture");
        afficherSucces("Demande envoyée avec succès !");
        selectedFournitures.clear();
        raisonArea.clear();
        chargerDonnees();
    }

    @FXML
    private void handleAnnuler() {
        selectedFournitures.clear();
        raisonArea.clear();
        fournitureCombo.setValue(null);
        quantiteTextField.clear();
        messageLabel.setText("");
    }

    @FXML
    private void handleActualiser() { chargerDonnees(); afficherSucces("Actualisé !"); }

    @FXML
    public void handleRetour(ActionEvent event) {
        StartApplication.goBack();
    }

    private void chargerDonnees() {
        demandesList.setAll(demandeRepo.getToutesLesDemandes());
        demandeTableView.setItems(demandesList);
        int n = demandesList.size();
        compteurLabel.setText("(" + n + " demande" + (n > 1 ? "s" : "") + ")");
        statutLabel.setText("Actualisé à " + java.time.LocalTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    private void afficherErreur(String msg) {
        messageLabel.setStyle("-fx-text-fill: #e74c3c;");
        messageLabel.setText("❌ " + msg);
    }

    private void afficherSucces(String msg) {
        messageLabel.setStyle("-fx-text-fill: #27ae60;");
        messageLabel.setText("✅ " + msg);
    }
}
