package appli.accueil;

import appli.StartApplication;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import session.sessionUtilisateur;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class AccueilController {

    @FXML private Label utilisateurLabel;
    @FXML private Label dateHeureLabel;

    @FXML
    public void initialize() {
        // Afficher le nom de l'utilisateur connecté
        if (sessionUtilisateur.getInstance().estConnecte()) {
            String nomComplet = sessionUtilisateur.getInstance().getNomComplet();
            String role = sessionUtilisateur.getInstance().getRole();
            utilisateurLabel.setText("👤 " + nomComplet + " (" + role + ")");
        }
        
        // Mettre à jour l'heure en temps réel
        mettreAJourDateHeure();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> mettreAJourDateHeure()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void mettreAJourDateHeure() {
        LocalDateTime maintenant = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy - HH:mm:ss", Locale.FRENCH);
        String dateHeure = maintenant.format(formatter);
        // Capitaliser le premier caractère
        dateHeure = dateHeure.substring(0, 1).toUpperCase() + dateHeure.substring(1);
        dateHeureLabel.setText("📅 " + dateHeure);
    }

    @FXML
    private void handleGestionSalles(MouseEvent event) {
        naviguerVers("accueil/Salle");
    }

    @FXML
    private void handleGestionFournisseurs(MouseEvent event) {
        naviguerVers("accueil/Fournisseur");
    }

    @FXML
    private void handleGestionFournitures(MouseEvent event) {
        naviguerVers("accueil/Fourniture");
    }

    @FXML
    private void handleGestionUtilisateurs(MouseEvent event) {
        naviguerVers("accueil/Utilisateur");
    }

    @FXML
    private void handleDemandes(MouseEvent event) {
        naviguerVers("accueil/DemandeFourniture");
    }

    @FXML
    private void handleRendezVous(MouseEvent event) {
        naviguerVers("accueil/RendezVous");
    }

    @FXML
    private void handleDeconnexion() {
        // Déconnecter l'utilisateur
        sessionUtilisateur.getInstance().deconnecter();
        
        // Rediriger vers la page de connexion
        naviguerVers("accueil/Login");
    }

    private void naviguerVers(String page) {
        try {
            StartApplication.changeScene(page);
        } catch (IOException e) {
            System.err.println("Erreur lors de la navigation vers : " + page);
            e.printStackTrace();
            
            // Afficher une alerte à l'utilisateur
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Erreur de navigation");
            alert.setHeaderText("Impossible d'accéder à cette page");
            alert.setContentText("La page '" + page + "' n'est pas encore disponible ou une erreur s'est produite.");
            alert.showAndWait();
        }
    }
}
