package appli.accueil;

import appli.StartApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
        if (nomText.getText().isEmpty() || prenomText.getText().isEmpty() ||
                emailText.getText().isEmpty() || mdpTexte.getText().isEmpty() ||
                mdpconfirmerTexte.getText().isEmpty() || roleCombo.getValue() == null) {

            erreur.setText("Veuillez remplir tous les champs !");
            return;
        }

        if (!mdpTexte.getText().equals(mdpconfirmerTexte.getText())) {
            erreur.setText("Les mots de passe ne correspondent pas !");
            return;
        }

        if (utilisateurRepository.getUtilisateurParEmail(emailText.getText()) != null) {
            erreur.setText("Un utilisateur avec cet email existe déjà !");
            return;
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Utilisateur nouvelUtilisateur = new Utilisateur(
                nomText.getText(),
                prenomText.getText(),
                emailText.getText(),
                encoder.encode(mdpTexte.getText()),
                roleCombo.getValue()
        );

        utilisateurRepository.ajouterUtilisateur(nouvelUtilisateur);

        try {
            StartApplication.changeScene("accueil/Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}