package appli.accueil;

import appli.StartApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Etudiant;
import model.Salle;
import repository.EtudiantRepository;
import java.io.IOException;

public class FicheEtudianteController {

    @FXML private TextField nomTextField;
    @FXML private TextField rechercheTextField;
    @FXML private Label messageLabel;
    @FXML private Label compteurLabel;
    @FXML private Label statutLabel;
    @FXML private Button ajouterBtn;
    @FXML private Button modifierBtn;
    @FXML private Button supprimerBtn;
    @FXML private Button annulerBtn;
    @FXML private Button retourBtn;
    @FXML private TableView<Salle> salleTableView;
    @FXML private TableColumn<Salle, Integer> idColumn;
    @FXML private TableColumn<Salle, String> nomColumn;
    @FXML private TableColumn<Salle, Void> actionsColumn;

    public TextField emailField;
    public TextField nomField;
    public TextField prenomField;
    public TextField telephoneField;
    public TextArea adresseArea;
    public TextField diplomeField;
    public Button btnEffacer;
    public Button btnEnregistrer;
    public Button btnRetour;

    @FXML
    private void handleAjouter() {
        messageLabel.setText("");
        // Créer et ajouter l'étudiant
        Etudiant nouvelEtudiant = new Etudiant(nomTextField.getText().trim());

        if (EtudiantRepository.ajouterEtudiant(nouvelEtudiant)) {
            afficherSucces("Salle ajoutée avec succès !");
            chargerDonnees();
            viderFormulaire();
        } else {
            afficherErreur("Erreur lors de l'ajout de la salle.");
        }
    }


    private void afficherErreur(String message) {
        messageLabel.setStyle("-fx-text-fill: #e74c3c;");
        messageLabel.setText("❌ " + message);
    }

    private void afficherSucces(String message) {
        messageLabel.setStyle("-fx-text-fill: #27ae60;");
        messageLabel.setText("✅ " + message);
    }

    private void chargerDonnees() {
        sallesList.setAll(salleRepository.getToutesLesSalles());
        salleTableView.setItems(sallesList);
        mettreAJourCompteur();
        statutLabel.setText("Dernière actualisation : " + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    private void viderFormulaire() {
        nomTextField.clear();
        salleSelectionnee = null;
        ajouterBtn.setDisable(false);
        modifierBtn.setDisable(true);
        supprimerBtn.setDisable(true);
        salleTableView.getSelectionModel().clearSelection();
        messageLabel.setText("");
    }

    @FXML
    public void retour(ActionEvent actionEvent) throws IOException {
        StartApplication.changeScene("accueil/Accueil");
    }
}