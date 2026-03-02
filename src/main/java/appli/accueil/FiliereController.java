package appli.accueil;

import appli.StartApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import model.Filiere;
import repository.FiliereRepository;

import java.io.IOException;

public class FiliereController {

    @FXML private TextField nomTextField;
    @FXML private TextField rechercheTextField;
    @FXML private Label messageLabel;
    @FXML private Label compteurLabel;
    @FXML private Label statutLabel;
    @FXML private Button ajouterBtn;
    @FXML private Button modifierBtn;
    @FXML private Button supprimerBtn;
    @FXML private Button annulerBtn;

    @FXML private TableView<Filiere> filiereTableView;
    @FXML private TableColumn<Filiere, Integer> idColumn;
    @FXML private TableColumn<Filiere, String> nomColumn;
    @FXML private TableColumn<Filiere, Void> actionsColumn;

    private FiliereRepository filiereRepo;
    private ObservableList<Filiere> filieresList;
    private Filiere filiereSelectionnee;

    @FXML
    public void initialize() {
        filiereRepo = new FiliereRepository();
        filieresList = FXCollections.observableArrayList();

        idColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleIntegerProperty(c.getValue().getIdFiliere()).asObject());
        nomColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getNom()));

        ajouterBoutonsActions();
        chargerDonnees();

        filiereTableView.getSelectionModel().selectedItemProperty().addListener((obs, old, nw) -> {
            if (nw != null) {
                filiereSelectionnee = nw;
                nomTextField.setText(nw.getNom());
                ajouterBtn.setDisable(true);
                modifierBtn.setDisable(false);
                supprimerBtn.setDisable(false);
            }
        });
    }

    private void ajouterBoutonsActions() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("✏");
            private final Button deleteBtn = new Button("❌");
            private final HBox pane = new HBox(5, editBtn, deleteBtn);
            {
                editBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 10px;");
                deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 10px;");
                editBtn.setOnAction(e -> {
                    Filiere f = getTableView().getItems().get(getIndex());
                    filiereSelectionnee = f;
                    nomTextField.setText(f.getNom());
                    ajouterBtn.setDisable(true);
                    modifierBtn.setDisable(false);
                    supprimerBtn.setDisable(false);
                });
                deleteBtn.setOnAction(e -> confirmerSuppression(getTableView().getItems().get(getIndex())));
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    @FXML
    private void handleAjouter() {
        String nom = nomTextField.getText().trim();
        if (nom.isEmpty()) { afficherErreur("Le nom de la filière est obligatoire !"); return; }

        Filiere f = new Filiere();
        f.setNom(nom);

        int id = filiereRepo.ajouterFiliere(f);
        if (id > 0) {
            afficherSucces("Filière ajoutée avec succès !");
            chargerDonnees();
            viderFormulaire();
        } else {
            afficherErreur("Erreur lors de l'ajout.");
        }
    }

    @FXML
    private void handleModifier() {
        if (filiereSelectionnee == null) { afficherErreur("Sélectionnez une filière !"); return; }
        String nom = nomTextField.getText().trim();
        if (nom.isEmpty()) { afficherErreur("Le nom est obligatoire !"); return; }

        filiereSelectionnee.setNom(nom);
        if (filiereRepo.modifierFiliere(filiereSelectionnee)) {
            afficherSucces("Filière modifiée avec succès !");
            chargerDonnees();
            viderFormulaire();
        } else {
            afficherErreur("Erreur lors de la modification.");
        }
    }

    @FXML
    private void handleSupprimer() {
        if (filiereSelectionnee == null) { afficherErreur("Sélectionnez une filière !"); return; }
        confirmerSuppression(filiereSelectionnee);
    }

    private void confirmerSuppression(Filiere f) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer la filière : " + f.getNom() + " ?");
        alert.setContentText("Attention : les dossiers liés à cette filière seront affectés.");
        alert.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                if (filiereRepo.supprimerFiliere(f.getIdFiliere())) {
                    afficherSucces("Filière supprimée !");
                    chargerDonnees();
                    viderFormulaire();
                } else {
                    afficherErreur("Erreur lors de la suppression.");
                }
            }
        });
    }

    @FXML
    private void handleAnnuler() { viderFormulaire(); }

    @FXML
    private void handleRecherche() {
        String terme = rechercheTextField.getText().trim().toLowerCase();
        if (terme.isEmpty()) { chargerDonnees(); return; }
        ObservableList<Filiere> filtered = FXCollections.observableArrayList();
        for (Filiere f : filiereRepo.getToutesLesFilieres()) {
            if (f.getNom().toLowerCase().contains(terme)) filtered.add(f);
        }
        filieresList.setAll(filtered);
        filiereTableView.setItems(filieresList);
        mettreAJourCompteur();
    }

    @FXML
    private void handleActualiser() { chargerDonnees(); afficherSucces("Données actualisées !"); }

    @FXML
    public void handleRetour(ActionEvent event) throws IOException {
        StartApplication.changeScene("accueil/Accueil");
    }

    private void chargerDonnees() {
        filieresList.setAll(filiereRepo.getToutesLesFilieres());
        filiereTableView.setItems(filieresList);
        mettreAJourCompteur();
        statutLabel.setText("Actualisé à " + java.time.LocalTime.now()
            .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    private void viderFormulaire() {
        nomTextField.clear();
        rechercheTextField.clear();
        filiereSelectionnee = null;
        ajouterBtn.setDisable(false);
        modifierBtn.setDisable(true);
        supprimerBtn.setDisable(true);
        filiereTableView.getSelectionModel().clearSelection();
        messageLabel.setText("");
    }

    private void mettreAJourCompteur() {
        int n = filieresList.size();
        compteurLabel.setText("(" + n + " filière" + (n > 1 ? "s" : "") + ")");
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
