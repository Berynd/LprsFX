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
        System.out.println("=== DEBUT INSCRIPTION ===");
        
        // Réinitialiser le message d'erreur
        erreur.setText("");
        erreur.setStyle("-fx-text-fill: red;");
        
        // Validation des champs
        System.out.println("Validation des champs...");
        if (nomText.getText().trim().isEmpty() || prenomText.getText().trim().isEmpty() ||
                emailText.getText().trim().isEmpty() || mdpTexte.getText().isEmpty() ||
                mdpconfirmerTexte.getText().isEmpty() || roleCombo.getValue() == null) {

            erreur.setText("Veuillez remplir tous les champs !");
            System.out.println("ERREUR: Champs manquants");
            return;
        }
        System.out.println("Champs validés ✓");

        // Vérification de la correspondance des mots de passe
        System.out.println("Vérification correspondance mots de passe...");
        if (!mdpTexte.getText().equals(mdpconfirmerTexte.getText())) {
            erreur.setText("Les mots de passe ne correspondent pas !");
            System.out.println("ERREUR: Mots de passe différents");
            return;
        }
        System.out.println("Mots de passe correspondent ✓");

        // Vérification email existant
        System.out.println("Vérification email existant...");
        try {
            Utilisateur existant = utilisateurRepository.getUtilisateurParEmail(emailText.getText().trim());
            if (existant != null) {
                erreur.setText("Un utilisateur avec cet email existe déjà !");
                System.out.println("ERREUR: Email déjà utilisé - " + emailText.getText());
                return;
            }
            System.out.println("Email disponible ✓");
        } catch (Exception e) {
            erreur.setText("Erreur de vérification de l'email !");
            System.err.println("ERREUR lors de la vérification email:");
            e.printStackTrace();
            return;
        }

        // Encodage du mot de passe
        System.out.println("Encodage du mot de passe...");
        String mdpClair = mdpTexte.getText();
        String mdpHash = "";
        
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            mdpHash = encoder.encode(mdpClair);
            System.out.println("Mot de passe clair: " + mdpClair);
            System.out.println("Hash BCrypt (longueur=" + mdpHash.length() + "): " + mdpHash);
        } catch (Exception e) {
            erreur.setText("Erreur lors de l'encodage du mot de passe !");
            System.err.println("ERREUR encodage BCrypt:");
            e.printStackTrace();
            return;
        }

        // Création de l'utilisateur
        System.out.println("Création de l'objet Utilisateur...");
        Utilisateur nouvelUtilisateur = new Utilisateur(
                nomText.getText().trim(),
                prenomText.getText().trim(),
                emailText.getText().trim(),
                mdpHash,
                roleCombo.getValue()
        );
        System.out.println("Utilisateur créé: " + nouvelUtilisateur);

        // Ajout en base de données
        System.out.println("Ajout en base de données...");
        try {
            utilisateurRepository.ajouterUtilisateur(nouvelUtilisateur);
            
            // Vérification que l'utilisateur a bien été ajouté
            Utilisateur verification = utilisateurRepository.getUtilisateurParEmail(emailText.getText().trim());
            if (verification != null) {
                System.out.println("✓✓✓ INSCRIPTION REUSSIE ✓✓✓");
                System.out.println("Utilisateur vérifié en base: " + verification);
                
                erreur.setStyle("-fx-text-fill: green;");
                erreur.setText("Inscription réussie ! Redirection...");
                
                // Petit délai pour voir le message
                new Thread(() -> {
                    try {
                        Thread.sleep(1500);
                        javafx.application.Platform.runLater(() -> {
                            try {
                                StartApplication.changeScene("accueil/Login");
                            } catch (IOException e) {
                                e.printStackTrace();
                                erreur.setStyle("-fx-text-fill: red;");
                                erreur.setText("Erreur de redirection !");
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
                
            } else {
                System.err.println("ERREUR: Utilisateur non trouvé après insertion !");
                erreur.setText("Erreur: l'utilisateur n'a pas été enregistré correctement");
            }
            
        } catch (Exception e) {
            System.err.println("ERREUR lors de l'ajout:");
            e.printStackTrace();
            erreur.setText("Erreur lors de l'inscription : " + e.getMessage());
        }
        
        System.out.println("=== FIN INSCRIPTION ===");
    }
}
