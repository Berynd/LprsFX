package appli.accueil;

import appli.StartApplication;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import session.SessionUtilisateur;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Controller de la page d'accueil principale (AccueilView.fxml).
 *
 * Cette vue est commune à tous les rôles. Elle affiche des "cartes" de navigation
 * et masque celles que le rôle connecté n'est pas autorisé à voir.
 *
 * Droits par rôle :
 *  - Admin            : accès à tout
 *  - Gestionnaire     : Fournisseurs, Fournitures, Demandes, Espace gestionnaire
 *  - Secrétaire       : Fiches étudiantes, Dossiers d'inscription, Filières, Espace secrétaire
 *  - Professeur       : Fournitures, Demandes, Rendez-vous, Dossiers, Espace professeur
 */
public class AccueilController {

    @FXML private Hyperlink log;
    @FXML private Label utilisateurLabel;
    @FXML private Label dateHeureLabel;

    // Cartes de navigation (une par module)
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
    @FXML private VBox carteEspaceProfesseur;
    @FXML private VBox carteEspaceGestionnaire;

    @FXML
    public void initialize() {
        // Affichage du nom et du rôle de l'utilisateur connecté
        String nomComplet = SessionUtilisateur.getInstance().getNomComplet();
        String role       = SessionUtilisateur.getInstance().getRole();
        utilisateurLabel.setText("👤 " + nomComplet + " — " + role);

        // Application des restrictions d'accès selon le rôle
        appliquerDroitsRole(role);

        // Horloge temps réel : mise à jour toutes les secondes
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> mettreAJourDateHeure()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        mettreAJourDateHeure();
    }

    /**
     * Masque toutes les cartes puis réaffiche uniquement celles autorisées pour le rôle.
     * Cette logique évite d'exposer des fonctionnalités à des rôles non habilités.
     */
    private void appliquerDroitsRole(String role) {
        // D'abord on masque tout
        masquer(carteSalles, carteFournisseurs, carteFournitures, carteUtilisateurs,
                carteDemandes, carteRendezVous, carteFicheEtudiante, carteDossiers, carteFilieres,
                carteEspaceSecretaire, carteEspaceProfesseur, carteEspaceGestionnaire);

        // Puis on affiche uniquement ce qui est autorisé pour ce rôle
        switch (role) {
            case "Admin" ->
                afficher(carteSalles, carteFournisseurs, carteFournitures, carteUtilisateurs,
                         carteDemandes, carteRendezVous, carteFicheEtudiante, carteDossiers, carteFilieres,
                         carteEspaceSecretaire, carteEspaceProfesseur, carteEspaceGestionnaire);

            case "Gestionnaire de stock" ->
                afficher(carteFournisseurs, carteFournitures, carteDemandes, carteEspaceGestionnaire);

            case "Secrétaire" ->
                afficher(carteFicheEtudiante, carteDossiers, carteFilieres, carteEspaceSecretaire);

            case "Professeur" ->
                afficher(carteFournitures, carteDemandes, carteRendezVous, carteDossiers, carteEspaceProfesseur);
        }
    }

    /** Rend les cartes visibles et les inclut dans le layout. */
    private void afficher(VBox... cartes) {
        for (VBox c : cartes) { c.setVisible(true); c.setManaged(true); }
    }

    /** Masque les cartes et les exclut du layout (n'occupent plus de place). */
    private void masquer(VBox... cartes) {
        for (VBox c : cartes) { c.setVisible(false); c.setManaged(false); }
    }

    // --- Handlers de navigation ---

    @FXML private void handleGestionSalles(MouseEvent e)       { naviguerVers("accueil/Salle"); }
    @FXML private void handleGestionFournisseurs(MouseEvent e)  { naviguerVers("accueil/Fournisseur"); }
    @FXML private void handleGestionFournitures(MouseEvent e)   { naviguerVers("accueil/Fourniture"); }
    @FXML private void handleGestionUtilisateurs(MouseEvent e)  { naviguerVers("accueil/Utilisateur"); }
    @FXML private void handleDemandes(MouseEvent e)             { naviguerVers("accueil/DemandeFourniture"); }
    @FXML private void handleRendezVous(MouseEvent e)           { naviguerVers("accueil/RendezVous"); }
    @FXML private void handleFicheEtudiante(MouseEvent e)       { naviguerVers("accueil/FicheEtudiante"); }
    @FXML private void handleDossiers(MouseEvent e)             { naviguerVers("accueil/DossierInscription"); }
    @FXML private void handleFilieres(MouseEvent e)             { naviguerVers("accueil/Filiere"); }
    @FXML private void handleEspaceSecretaire(MouseEvent e)     { naviguerVers("accueil/EspaceSecretaire"); }
    @FXML private void handleEspaceProfesseur(MouseEvent e)     { naviguerVers("accueil/EspaceProfesseur"); }
    @FXML private void handleEspaceGestionnaire(MouseEvent e)   { naviguerVers("accueil/EspaceGestionnaire"); }
    @FXML private void handleLog(MouseEvent e)                  { naviguerVers("accueil/Log"); }

    /** Déconnecte l'utilisateur et revient à la page de login. */
    @FXML
    private void handleDeconnexion() {
        SessionUtilisateur.getInstance().deconnecter();
        naviguerVers("accueil/Login");
    }

    /** Navigue vers une page en loguant l'erreur si le fichier FXML est introuvable. */
    private void naviguerVers(String page) {
        try {
            StartApplication.changeScene(page);
        } catch (IOException e) {
            System.err.println("Erreur navigation : " + e.getMessage());
        }
    }

    /** Met à jour l'horloge affichée dans le header. */
    private void mettreAJourDateHeure() {
        String dateHeure = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy - HH:mm:ss", Locale.FRENCH));
        // Mise en majuscule du premier caractère (ex : "lundi" → "Lundi")
        dateHeure = dateHeure.substring(0, 1).toUpperCase() + dateHeure.substring(1);
        dateHeureLabel.setText("📅 " + dateHeure);
    }
}
