package appli.accueil;

import appli.StartApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Utilisateur;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import repository.UtilisateurRepository;
import session.sessionUtilisateur;

import java.io.IOException;

public class LoginController {
    
    private UtilisateurRepository utilisateurRepository = new UtilisateurRepository();
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @FXML
    private TextField email;

    @FXML
    private PasswordField mdp;

    @FXML
    private Label erreur;

    @FXML
    private Button connexion;

    @FXML
    private Button oubliemdp;

    @FXML
    private Button inscription;

    @FXML
    public void initialize() {
        // Associer les actions aux boutons
        connexion.setOnAction(this::handleConnexion);
        inscription.setOnAction(this::handleInscription);
        oubliemdp.setOnAction(this::handleMotDePasseOublie);
        
        // Permettre la connexion avec la touche Entrée
        mdp.setOnAction(this::handleConnexion);
    }

    @FXML
    private void handleConnexion(ActionEvent event) {
        // Réinitialiser le message d'erreur
        erreur.setText("");
        erreur.setStyle("-fx-text-fill: red;");

        // Validation des champs
        if (email.getText().trim().isEmpty() || mdp.getText().isEmpty()) {
            erreur.setText("Veuillez remplir tous les champs !");
            return;
        }

        try {
            // Récupérer l'utilisateur par email
            Utilisateur utilisateur = utilisateurRepository.getUtilisateurParEmail(email.getText().trim());

            if (utilisateur == null) {
                erreur.setText("Email ou mot de passe incorrect !");
                return;
            }

            // Vérifier le mot de passe avec BCrypt
            if (encoder.matches(mdp.getText(), utilisateur.getmdp())) {
                // ✅ Connexion réussie
                System.out.println("Connexion réussie pour : " + utilisateur.getEmail());
                
                // Sauvegarder la session utilisateur
                sessionUtilisateur.getInstance().setUtilisateurConnecte(utilisateur);
                
                erreur.setStyle("-fx-text-fill: green;");
                erreur.setText("Connexion réussie !");

                // Redirection selon le rôle
                redirectionSelonRole(utilisateur.getRole());

            } else {
                // ❌ Mot de passe incorrect
                erreur.setText("Email ou mot de passe incorrect !");
            }

        } catch (Exception e) {
            erreur.setText("Erreur de connexion à la base de données !");
            e.printStackTrace();
        }
    }

    private void redirectionSelonRole(String role) {
        try {
            // Adapter selon les rôles de votre application
            switch (role) {
                case "Secrétaire":
                    // TODO: Remplacer par la vraie page
                    System.out.println("Redirection vers interface Secrétaire");
                    // StartApplication.changeScene("secretaire/Accueil");
                    break;
                    
                case "Professeur":
                    // TODO: Remplacer par la vraie page
                    System.out.println("Redirection vers interface Professeur");
                    // StartApplication.changeScene("professeur/Accueil");
                    break;
                    
                case "Gestionnaire de stock":
                    // TODO: Remplacer par la vraie page
                    System.out.println("Redirection vers interface Gestionnaire");
                    // StartApplication.changeScene("gestionnaire/Accueil");
                    break;

                case "Admin":
                    // TODO: Remplacer par la vraie page
                    System.out.println("Redirection vers interface Admin");
                    // StartApplication.changeScene("admin/Accueil");
                    break;
                    
                default:
                    System.out.println("Rôle inconnu : " + role);
                    erreur.setText("Rôle utilisateur invalide !");
            }
        } catch (Exception e) {
            erreur.setText("Erreur de redirection !");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleInscription(ActionEvent event) {
        try {
            StartApplication.changeScene("accueil/Inscription");
        } catch (IOException e) {
            e.printStackTrace();
            erreur.setText("Erreur lors de la redirection !");
        }
    }

    @FXML
    private void handleMotDePasseOublie(ActionEvent event) {
        // TODO: Implémenter la récupération de mot de passe
        erreur.setStyle("-fx-text-fill: orange;");
        erreur.setText("Fonctionnalité en cours de développement");
        
        // Exemple d'implémentation future :
        // StartApplication.changeScene("accueil/MotDePasseOublie");
    }
}
