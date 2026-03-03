package appli.accueil;

import appli.StartApplication;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import session.sessionUtilisateur;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class AccueilController {

    @FXML private Label utilisateurLabel;
    @FXML private Label dateHeureLabel;

    @FXML private VBox carteSalles;
    @FXML private VBox carteFournisseurs;
    @FXML private VBox carteFournitures;
    @FXML private VBox carteUtilisateurs;
    @FXML private VBox carteDemandes;
    @FXML private VBox carteRendezVous;
    @FXML private VBox carteFicheEtudiante;
    @FXML private VBox carteDossiers;
    @FXML private VBox carteFilieres;
    @FXML private VBox carteEspaceSecretaire;



    @FXML
    public void initialize() {
        String nomComplet = sessionUtilisateur.getInstance().getNomComplet();
        String role = sessionUtilisateur.getInstance().getRole();
        utilisateurLabel.setText("👤 " + nomComplet + " — " + role);

        appliquerDroitsRole(role);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> mettreAJourDateHeure()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        mettreAJourDateHeure();
    }

    /**
     * Accès par rôle :
     *
     * Admin             : tout
     * Gestionnaire      : Fournisseurs, Fournitures, Demandes
     * Secrétaire        : Fiches Étudiantes, Dossiers d'inscription, Filières
     * Professeur        : Fournitures, Demandes, Rendez-vous, Dossiers d'inscription
     */
    private void appliquerDroitsRole(String role) {
        masquer(carteSalles, carteFournisseurs, carteFournitures, carteUtilisateurs,
                carteDemandes, carteRendezVous, carteFicheEtudiante, carteDossiers, carteFilieres,carteEspaceSecretaire);

        switch (role) {
            case "Admin" ->
                afficher(carteSalles, carteFournisseurs, carteFournitures, carteUtilisateurs,
                         carteDemandes, carteRendezVous, carteFicheEtudiante, carteDossiers, carteFilieres, carteEspaceSecretaire);

            case "Gestionnaire de stock" ->
                afficher(carteFournisseurs, carteFournitures, carteDemandes);

            case "Secrétaire" ->
                afficher(carteFicheEtudiante, carteDossiers, carteFilieres,carteEspaceSecretaire);

            case "Professeur" ->
                afficher(carteFournitures, carteDemandes, carteRendezVous, carteDossiers);
        }
    }

    private void afficher(VBox... cartes) {
        for (VBox c : cartes) { c.setVisible(true); c.setManaged(true); }
    }

    private void masquer(VBox... cartes) {
        for (VBox c : cartes) { c.setVisible(false); c.setManaged(false); }
    }

    // --- Navigation ---

    @FXML private void handleGestionSalles(MouseEvent e)      { naviguerVers("accueil/Salle"); }
    @FXML private void handleGestionFournisseurs(MouseEvent e) { naviguerVers("accueil/Fournisseur"); }
    @FXML private void handleGestionFournitures(MouseEvent e)  { naviguerVers("accueil/Fourniture"); }
    @FXML private void handleGestionUtilisateurs(MouseEvent e) { naviguerVers("accueil/Utilisateur"); }
    @FXML private void handleDemandes(MouseEvent e)            { naviguerVers("accueil/DemandeFourniture"); }
    @FXML private void handleRendezVous(MouseEvent e)          { naviguerVers("accueil/RendezVous"); }
    @FXML private void handleFicheEtudiante(MouseEvent e)      { naviguerVers("accueil/FicheEtudiante"); }
    @FXML private void handleDossiers(MouseEvent e)            { naviguerVers("accueil/DossierInscription"); }
    @FXML private void handleFilieres(MouseEvent e)            { naviguerVers("accueil/Filiere"); }
    @FXML private void handleEspaceSecretaire(MouseEvent e)    { naviguerVers("accueil/EspaceSecretaire"); }

    @FXML
    private void handleDeconnexion() {
        sessionUtilisateur.getInstance().deconnecter();
        naviguerVers("accueil/Login");
    }

    private void naviguerVers(String page) {
        try {
            StartApplication.changeScene(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mettreAJourDateHeure() {
        String dateHeure = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy - HH:mm:ss", Locale.FRENCH));
        dateHeure = dateHeure.substring(0, 1).toUpperCase() + dateHeure.substring(1);
        dateHeureLabel.setText("📅 " + dateHeure);
    }
}
