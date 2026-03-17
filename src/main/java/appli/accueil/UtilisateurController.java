package appli.accueil;

import appli.StartApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Utilisateur;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import repository.UtilisateurRepository;
import session.SessionUtilisateur;

public class UtilisateurController {

    @FXML private TextField nomTextField;
    @FXML private TextField prenomTextField;
    @FXML private TextField emailTextField;
    @FXML private ComboBox<String> roleCombo;
    @FXML private TextField rechercheTextField;
    @FXML private Label messageLabel;
    @FXML private Label compteurLabel;
    @FXML private Label statutLabel;
    @FXML private Button ajouterBtn;
    @FXML private Button modifierBtn;
    @FXML private Button supprimerBtn;
    @FXML private Button annulerBtn;
    @FXML private VBox mdpBox;
    @FXML private PasswordField mdpField;
    @FXML private PasswordField mdpConfirmField;

    @FXML private TableView<Utilisateur> utilisateurTableView;
    @FXML private TableColumn<Utilisateur, Integer> idColumn;
    @FXML private TableColumn<Utilisateur, String> nomColumn;
    @FXML private TableColumn<Utilisateur, String> prenomColumn;
    @FXML private TableColumn<Utilisateur, String> emailColumn;
    @FXML private TableColumn<Utilisateur, String> roleColumn;
    @FXML private TableColumn<Utilisateur, Void> actionsColumn;

    private UtilisateurRepository utilisateurRepo;
    private ObservableList<Utilisateur> utilisateursList;
    private Utilisateur utilisateurSelectionne;

    @FXML
    public void initialize() {
        utilisateurRepo = new UtilisateurRepository();
        utilisateursList = FXCollections.observableArrayList();

        roleCombo.setItems(FXCollections.observableArrayList(
            "Secrétaire", "Professeur", "Gestionnaire de stock", "Admin"
        ));

        idColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleIntegerProperty(c.getValue().getIdUtilisateur()).asObject());
        nomColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getNom()));
        prenomColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getPrenom()));
        emailColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail()));
        roleColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getRole()));

        roleColumn.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String role, boolean empty) {
                super.updateItem(role, empty);
                if (empty || role == null) { setText(null); setStyle(""); }
                else {
                    setText(role);
                    switch (role) {
                        case "Admin"                 -> setStyle("-fx-text-fill: #8e44ad; -fx-font-weight: bold;");
                        case "Professeur"            -> setStyle("-fx-text-fill: #2980b9; -fx-font-weight: bold;");
                        case "Secrétaire"            -> setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                        case "Gestionnaire de stock" -> setStyle("-fx-text-fill: #e67e22; -fx-font-weight: bold;");
                        default                      -> setStyle("");
                    }
                }
            }
        });

        boolean estAdmin = "Admin".equals(SessionUtilisateur.getInstance().getRole());
        mdpBox.setVisible(estAdmin);
        mdpBox.setManaged(estAdmin);

        ajouterBoutonsActions();
        chargerDonnees();

        utilisateurTableView.getSelectionModel().selectedItemProperty().addListener((obs, old, nw) -> {
            if (nw != null) {
                utilisateurSelectionne = nw;
                remplirFormulaire(nw);
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
                    Utilisateur u = getTableView().getItems().get(getIndex());
                    utilisateurSelectionne = u;
                    remplirFormulaire(u);
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
    private void handleModifier() {
        if (utilisateurSelectionne == null) { afficherErreur("Sélectionnez un utilisateur !"); return; }
        if (nomTextField.getText().trim().isEmpty()) { afficherErreur("Le nom est obligatoire !"); return; }
        if (prenomTextField.getText().trim().isEmpty()) { afficherErreur("Le prénom est obligatoire !"); return; }
        if (emailTextField.getText().trim().isEmpty()) { afficherErreur("L'email est obligatoire !"); return; }

        String nouveauMdp = mdpField.getText();
        if (!nouveauMdp.isEmpty()) {
            if (!nouveauMdp.equals(mdpConfirmField.getText())) {
                afficherErreur("Les mots de passe ne correspondent pas !"); return;
            }
            String mdpHashe = new BCryptPasswordEncoder().encode(nouveauMdp);
            utilisateurRepo.changerMotDePasse(utilisateurSelectionne.getIdUtilisateur(), mdpHashe);
        }

        utilisateurSelectionne.setNom(nomTextField.getText().trim());
        utilisateurSelectionne.setPrenom(prenomTextField.getText().trim());
        utilisateurSelectionne.setEmail(emailTextField.getText().trim());
        if (roleCombo.getValue() != null) utilisateurSelectionne.setRole(roleCombo.getValue());

        utilisateurRepo.mettreAJourUtilisateur(utilisateurSelectionne);
        afficherSucces("Utilisateur modifié avec succès !");
        chargerDonnees();
        viderFormulaire();
    }

    @FXML
    private void handleSupprimer() {
        if (utilisateurSelectionne == null) { afficherErreur("Sélectionnez un utilisateur !"); return; }
        confirmerSuppression(utilisateurSelectionne);
    }

    private void confirmerSuppression(Utilisateur u) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer " + u.getNom() + " " + u.getPrenom() + " ?");
        alert.setContentText("Cette action est irréversible.");
        alert.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                utilisateurRepo.supprimerUtilisateurParEmail(u.getEmail());
                afficherSucces("Utilisateur supprimé !");
                chargerDonnees();
                viderFormulaire();
            }
        });
    }

    @FXML
    private void handleAnnuler() { viderFormulaire(); }

    @FXML
    private void handleRecherche() {
        String terme = rechercheTextField.getText().trim().toLowerCase();
        if (terme.isEmpty()) { chargerDonnees(); return; }
        ObservableList<Utilisateur> filtered = FXCollections.observableArrayList();
        for (Utilisateur u : utilisateurRepo.getTousLesUtilisateurs()) {
            if (u.getNom().toLowerCase().contains(terme)
                || u.getPrenom().toLowerCase().contains(terme)
                || u.getEmail().toLowerCase().contains(terme)) {
                filtered.add(u);
            }
        }
        utilisateursList.setAll(filtered);
        utilisateurTableView.setItems(utilisateursList);
        mettreAJourCompteur();
    }

    @FXML
    private void handleActualiser() { chargerDonnees(); afficherSucces("Données actualisées !"); }

    @FXML
    public void handleRetour(ActionEvent event) {
        StartApplication.goBack();
    }

    private void chargerDonnees() {
        utilisateursList.setAll(utilisateurRepo.getTousLesUtilisateurs());
        utilisateurTableView.setItems(utilisateursList);
        mettreAJourCompteur();
        statutLabel.setText("Actualisé à " + java.time.LocalTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    private void remplirFormulaire(Utilisateur u) {
        nomTextField.setText(u.getNom());
        prenomTextField.setText(u.getPrenom());
        emailTextField.setText(u.getEmail());
        roleCombo.setValue(u.getRole());
        ajouterBtn.setDisable(true);
    }

    private void viderFormulaire() {
        nomTextField.clear(); prenomTextField.clear(); emailTextField.clear();
        roleCombo.setValue(null);
        mdpField.clear(); mdpConfirmField.clear();
        utilisateurSelectionne = null;
        ajouterBtn.setDisable(false);
        modifierBtn.setDisable(true);
        supprimerBtn.setDisable(true);
        utilisateurTableView.getSelectionModel().clearSelection();
        messageLabel.setText("");
    }

    private void mettreAJourCompteur() {
        int n = utilisateursList.size();
        compteurLabel.setText("(" + n + " utilisateur" + (n > 1 ? "s" : "") + ")");
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
