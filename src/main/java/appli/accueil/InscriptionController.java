package appli.accueil;

import appli.StartApplication;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import repository.UtilisateurRepository;
import model.Utilisateur;
import java.io.IOException;


public class InscriptionController {
    private UtilisateurRepository utilisateurRepository = new UtilisateurRepository();

    @FXML
    private Button connexion;

    @FXML
    private TextField emailText;

    @FXML
    private Label erreur;

    @FXML
    private Button inscription;

    @FXML
    private PasswordField mdpTexte;

    @FXML
    private PasswordField mdpconfirmerTexte;

    @FXML
    private TextField nomText;

    @FXML
    private TextField prenomText;

    @FXML
    private ComboBox<String> roleCombo;

    @FXML
    public void initialize() {
        ObservableList<String> roles = FXCollections.observableArrayList(
                "Secrétaire",
                "Professeur",
                "Gestionnaire de stock"
        );
        roleCombo.setItems(roles);
    }

    @FXML
    void onHelloButtonClickConnexion(ActionEvent event) throws IOException {
        StartApplication.changeScene("accueil/Login");
    }

    @FXML
    void onHelloButtonClickInscription(ActionEvent event) {
        erreur.setText("");
        erreur.setStyle("-fx-text-fill: red;");

        if (nomText.getText().trim().isEmpty() || prenomText.getText().trim().isEmpty() ||
                emailText.getText().trim().isEmpty() || mdpTexte.getText().isEmpty() ||
                mdpconfirmerTexte.getText().isEmpty() || roleCombo.getValue() == null) {
            erreur.setText("Veuillez remplir tous les champs !");
            return;
        }

        if (!mdpTexte.getText().equals(mdpconfirmerTexte.getText())) {
            erreur.setText("Les mots de passe ne correspondent pas !");
            return;
        }

        try {
            Utilisateur existant = utilisateurRepository.getUtilisateurParEmail(emailText.getText().trim());
            if (existant != null) {
                erreur.setText("Un utilisateur avec cet email existe déjà !");
                return;
            }
        } catch (Exception e) {
            erreur.setText("Erreur de vérification de l'email !");
            e.printStackTrace();
            return;
        }

        String mdpHash;
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            mdpHash = encoder.encode(mdpTexte.getText());
        } catch (Exception e) {
            erreur.setText("Erreur lors de l'encodage du mot de passe !");
            e.printStackTrace();
            return;
        }

        Utilisateur nouvelUtilisateur = new Utilisateur(
                nomText.getText().trim(),
                prenomText.getText().trim(),
                emailText.getText().trim(),
                mdpHash,
                roleCombo.getValue()
        );

        try {
            utilisateurRepository.ajouterUtilisateur(nouvelUtilisateur);

            erreur.setStyle("-fx-text-fill: green;");
            erreur.setText("Inscription réussie ! Redirection...");

            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(e -> {
                try {
                    StartApplication.changeScene("accueil/Login");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    erreur.setStyle("-fx-text-fill: red;");
                    erreur.setText("Erreur de redirection !");
                }
            });
            pause.play();

        } catch (Exception e) {
            e.printStackTrace();
            erreur.setText("Erreur lors de l'inscription : " + e.getMessage());
        }
    }
}
