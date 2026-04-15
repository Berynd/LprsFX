package appli.accueil;

import appli.StartApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import model.Etudiant;
import repository.EtudiantRepository;
import service.LogService;

import java.io.IOException;

/**
 * Controller de la gestion des fiches étudiantes (FicheEtudianteView.fxml).
 *
 * Permet de créer, modifier, supprimer et rechercher des étudiants.
 * Chaque ligne du tableau dispose de boutons d'action inline (éditer / supprimer).
 * La sélection d'une ligne remplit le formulaire et active les boutons Modifier/Supprimer.
 * Toutes les opérations sont journalisées via LogService.
 */
public class FicheEtudianteController {

    @FXML private TextField nomTextField;
    @FXML private TextField prenomTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField telephoneTextField;
    @FXML private TextArea adresseArea;
    @FXML private TextField diplomeTextField;
    @FXML private TextField rechercheTextField;
    @FXML private Label messageLabel;
    @FXML private Label compteurLabel;
    @FXML private Label statutLabel;
    @FXML private Button ajouterBtn;
    @FXML private Button modifierBtn;
    @FXML private Button supprimerBtn;
    @FXML private Button annulerBtn;

    @FXML private TableView<Etudiant> etudiantTableView;
    @FXML private TableColumn<Etudiant, Integer> idColumn;
    @FXML private TableColumn<Etudiant, String> nomColumn;
    @FXML private TableColumn<Etudiant, String> prenomColumn;
    @FXML private TableColumn<Etudiant, String> emailColumn;
    @FXML private TableColumn<Etudiant, String> telephoneColumn;
    @FXML private TableColumn<Etudiant, Void> actionsColumn;

    private EtudiantRepository etudiantRepository;
    private ObservableList<Etudiant> etudiantsList;
    private Etudiant etudiantSelectionne;

    @FXML
    public void initialize() {
        etudiantRepository = new EtudiantRepository();
        etudiantsList = FXCollections.observableArrayList();
        // Configuration des colonnes et chargement initial des données

        idColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleIntegerProperty(c.getValue().getIdEtudiant()).asObject());
        nomColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getNom()));
        prenomColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getPrenom()));
        emailColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail()));
        telephoneColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getTelephone()));

        ajouterBoutonsActions();
        chargerDonnees();

        etudiantTableView.getSelectionModel().selectedItemProperty().addListener((obs, old, nw) -> {
            if (nw != null) {
                etudiantSelectionne = nw;
                remplirFormulaire(nw);
                modifierBtn.setDisable(false);
                supprimerBtn.setDisable(false);
            }
        });
    }

    /** Injecte des boutons ✏/❌ dans la colonne Actions de chaque ligne. */
    private void ajouterBoutonsActions() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("✏");
            private final Button deleteBtn = new Button("❌");
            private final HBox pane = new HBox(5, editBtn, deleteBtn);
            {
                editBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 10px;");
                deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 10px;");
                editBtn.setOnAction(e -> {
                    Etudiant etudiant = getTableView().getItems().get(getIndex());
                    etudiantSelectionne = etudiant;
                    remplirFormulaire(etudiant);
                    modifierBtn.setDisable(false);
                    supprimerBtn.setDisable(false);
                });
                deleteBtn.setOnAction(e -> confirmerSuppression(getTableView().getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    /** Crée un nouvel étudiant en base avec les valeurs du formulaire. */
    @FXML
    private void handleAjouter() {
        if (!validerFormulaire()) return;

        Etudiant e = new Etudiant(0,
            nomTextField.getText().trim(),
            prenomTextField.getText().trim(),
            emailTextField.getText().trim(),
            telephoneTextField.getText().trim(),
            adresseArea.getText().trim(),
            diplomeTextField.getText().trim()
        );

        int id = etudiantRepository.ajouterEtudiant(e);
        if (id > 0) {
            LogService.log("Étudiant ajouté : " + e.getNom() + " " + e.getPrenom(), "AJOUTER", "FicheEtudiante");
            afficherSucces("Étudiant ajouté avec succès !");
            chargerDonnees();
            viderFormulaire();
        } else {
            afficherErreur("Erreur lors de l'ajout de l'étudiant.");
        }
    }

    /** Sauvegarde les modifications du formulaire sur l'étudiant sélectionné. */
    @FXML
    private void handleModifier() {
        if (etudiantSelectionne == null) { afficherErreur("Sélectionnez un étudiant !"); return; }
        if (!validerFormulaire()) return;

        etudiantSelectionne.setNom(nomTextField.getText().trim());
        etudiantSelectionne.setPrenom(prenomTextField.getText().trim());
        etudiantSelectionne.setEmail(emailTextField.getText().trim());
        etudiantSelectionne.setTelephone(telephoneTextField.getText().trim());
        etudiantSelectionne.setAdresse(adresseArea.getText().trim());
        etudiantSelectionne.setDernierDiplome(diplomeTextField.getText().trim());

        if (etudiantRepository.modifierEtudiant(etudiantSelectionne)) {
            LogService.log("Étudiant modifié : " + etudiantSelectionne.getNom() + " " + etudiantSelectionne.getPrenom(), "MODIFIER", "FicheEtudiante");
            afficherSucces("Étudiant modifié avec succès !");
            chargerDonnees();
            viderFormulaire();
        } else {
            afficherErreur("Erreur lors de la modification.");
        }
    }

    @FXML
    private void handleSupprimer() {
        if (etudiantSelectionne == null) { afficherErreur("Sélectionnez un étudiant !"); return; }
        confirmerSuppression(etudiantSelectionne);
    }

    /** Affiche une boîte de confirmation avant de supprimer l'étudiant (et ses dossiers liés). */
    private void confirmerSuppression(Etudiant etudiant) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer " + etudiant.getNom() + " " + etudiant.getPrenom() + " ?");
        alert.setContentText("Cette action supprimera aussi les dossiers d'inscription liés.");
        alert.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                if (etudiantRepository.supprimerEtudiant(etudiant.getIdEtudiant())) {
                    LogService.log("Étudiant supprimé : " + etudiant.getNom() + " " + etudiant.getPrenom(), "SUPPRIMER", "FicheEtudiante");
                    afficherSucces("Étudiant supprimé !");
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
        String terme = rechercheTextField.getText().trim();
        if (terme.isEmpty()) {
            chargerDonnees();
        } else {
            etudiantsList.setAll(etudiantRepository.rechercherEtudiant(terme));
            etudiantTableView.setItems(etudiantsList);
            mettreAJourCompteur();
        }
    }

    @FXML
    private void handleActualiser() {
        chargerDonnees();
        viderFormulaire();
        afficherSucces("Données actualisées !");
    }

    @FXML
    private void handleDossiers() throws IOException {
        StartApplication.changeScene("accueil/DossierInscription");
    }

    @FXML
    public void handleRetour(ActionEvent event) {
        StartApplication.goBack();
    }

    /** Recharge tous les étudiants depuis la BDD et met à jour le tableau et le compteur. */
    private void chargerDonnees() {
        etudiantsList.setAll(etudiantRepository.getTousLesEtudiants());
        etudiantTableView.setItems(etudiantsList);
        mettreAJourCompteur();
        statutLabel.setText("Actualisé à " + java.time.LocalTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    /** Copie les données de l'étudiant dans les champs du formulaire et désactive le bouton Ajouter. */
    private void remplirFormulaire(Etudiant e) {
        nomTextField.setText(e.getNom());
        prenomTextField.setText(e.getPrenom());
        emailTextField.setText(e.getEmail());
        telephoneTextField.setText(e.getTelephone());
        adresseArea.setText(e.getAdresse());
        diplomeTextField.setText(e.getDernierDiplome());
        ajouterBtn.setDisable(true);
    }

    /** Réinitialise le formulaire et remet les boutons dans leur état par défaut. */
    private void viderFormulaire() {
        nomTextField.clear(); prenomTextField.clear(); emailTextField.clear();
        telephoneTextField.clear(); adresseArea.clear(); diplomeTextField.clear();
        etudiantSelectionne = null;
        ajouterBtn.setDisable(false);
        modifierBtn.setDisable(true);
        supprimerBtn.setDisable(true);
        etudiantTableView.getSelectionModel().clearSelection();
        messageLabel.setText("");
    }

    private boolean validerFormulaire() {
        if (nomTextField.getText().trim().isEmpty()) { afficherErreur("Le nom est obligatoire !"); return false; }
        if (prenomTextField.getText().trim().isEmpty()) { afficherErreur("Le prénom est obligatoire !"); return false; }
        if (emailTextField.getText().trim().isEmpty()) { afficherErreur("L'email est obligatoire !"); return false; }
        return true;
    }

    private void mettreAJourCompteur() {
        int n = etudiantsList.size();
        compteurLabel.setText("(" + n + " étudiant" + (n > 1 ? "s" : "") + ")");
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
