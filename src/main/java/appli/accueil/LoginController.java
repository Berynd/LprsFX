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
import service.LogService;
import session.SessionUtilisateur;

import java.io.IOException;

/**
 * Controller de la page de connexion (LoginView.fxml).
 *
 * Gère l'authentification des utilisateurs :
 *  1. L'email est recherché en base via UtilisateurRepository.
 *  2. Le mot de passe saisi est comparé au hash BCrypt stocké.
 *  3. En cas de succès, l'utilisateur est placé en session et redirigé
 *     vers son espace selon son rôle.
 *
 * Rôles supportés : Admin, Secrétaire, Professeur, Gestionnaire de stock.
 */
public class LoginController {

    private final UtilisateurRepository utilisateurRepository = new UtilisateurRepository();
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @FXML private TextField    email;
    @FXML private PasswordField mdp;
    @FXML private Label        erreur;
    @FXML private Button       connexion;
    @FXML private Button       oubliemdp;
    @FXML private Button       inscription;

    /** Initialisation : branchement des actions et raccourci clavier Entrée sur le champ mdp. */
    @FXML
    public void initialize() {
        connexion.setOnAction(this::handleConnexion);
        inscription.setOnAction(this::handleInscription);
        oubliemdp.setOnAction(this::handleMotDePasseOublie);
        // Permet de valider la connexion en appuyant sur Entrée depuis le champ mot de passe
        mdp.setOnAction(this::handleConnexion);
    }

    /** Tente de connecter l'utilisateur avec l'email et le mot de passe saisis. */
    @FXML
    private void handleConnexion(ActionEvent event) {
        // Réinitialisation du message d'erreur
        erreur.setText("");
        erreur.setStyle("-fx-text-fill: red;");

        // Validation basique : aucun champ vide
        if (email.getText().trim().isEmpty() || mdp.getText().isEmpty()) {
            erreur.setText("Veuillez remplir tous les champs !");
            return;
        }

        try {
            // Recherche du compte en base par email
            Utilisateur utilisateur = utilisateurRepository.getUtilisateurParEmail(email.getText().trim());

            if (utilisateur == null) {
                erreur.setText("Email ou mot de passe incorrect !");
                return;
            }

            // Vérification du mot de passe : comparaison du saisi avec le hash BCrypt stocké
            if (encoder.matches(mdp.getText(), utilisateur.getMdp())) {
                // Connexion réussie : enregistrement de l'utilisateur en session
                SessionUtilisateur.getInstance().setUtilisateurConnecte(utilisateur);

                erreur.setStyle("-fx-text-fill: green;");
                erreur.setText("Connexion réussie !");

                // Journalisation de la connexion
                LogService.log("Connexion réussie", "CONNEXION", "Login");

                // Redirection vers l'espace correspondant au rôle
                redirectionSelonRole(utilisateur.getRole());

            } else {
                erreur.setText("Email ou mot de passe incorrect !");
            }

        } catch (Exception e) {
            erreur.setText("Erreur de connexion à la base de données !");
            System.err.println("Erreur connexion : " + e.getMessage());
        }
    }

    /**
     * Redirige l'utilisateur vers son espace d'accueil selon son rôle.
     * Tous les rôles arrivent sur AccueilView qui adapte son contenu dynamiquement.
     */
    private void redirectionSelonRole(String role) {
        try {
            switch (role) {
                case "Admin"                 -> StartApplication.changeScene("accueil/Accueil");
                case "Secrétaire"            -> StartApplication.changeScene("accueil/AccueilSecretaire");
                case "Professeur"            -> StartApplication.changeScene("accueil/AccueilProfesseur");
                case "Gestionnaire de stock" -> StartApplication.changeScene("accueil/AccueilGestionnaire");
                default                      -> erreur.setText("Rôle utilisateur invalide !");
            }
        } catch (Exception e) {
            erreur.setText("Erreur de redirection !");
            System.err.println("Erreur redirection : " + e.getMessage());
        }
    }

    /** Navigue vers la page d'inscription d'un nouveau compte. */
    @FXML
    private void handleInscription(ActionEvent event) {
        try {
            StartApplication.changeScene("accueil/Inscription");
        } catch (IOException e) {
            erreur.setText("Erreur lors de la redirection !");
            System.err.println("Erreur redirection inscription : " + e.getMessage());
        }
    }

    /** Affiche un message invitant à contacter un administrateur. */
    @FXML
    private void handleMotDePasseOublie(ActionEvent event) {
        erreur.setStyle("-fx-text-fill: orange;");
        erreur.setText("Contactez un administrateur pour réinitialiser votre mot de passe.");
    }
}
