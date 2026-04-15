package appli.accueil;

import appli.StartApplication;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import repository.DossierInscriptionRepository;
import session.SessionUtilisateur;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Controller de l'accueil dédié à la secrétaire (AccueilSecretaireView.fxml).
 *
 * Variante de AccueilView réservée au rôle Secrétaire. Affiche :
 *  - Le nom complet et le rôle de l'utilisateur connecté
 *  - Une horloge temps réel (mise à jour toutes les secondes)
 *  - Les statistiques dossiers (acceptés, refusés, en attente, total)
 *  - Des cartes de navigation vers les modules autorisés pour ce rôle
 */
public class AccueilSecretaireController {

    private final DossierInscriptionRepository dossierRepo = new DossierInscriptionRepository();

    @FXML private Label utilisateurLabel;
    @FXML private Label dateHeureLabel;
    @FXML private Label bienvenuLabel;

    // Stats
    @FXML private Label lblNbAccepte;
    @FXML private Label lblNbRefuse;
    @FXML private Label lblNbAttente;
    @FXML private Label lblNbTotal;

    @FXML
    public void initialize() {
        String nomComplet = SessionUtilisateur.getInstance().getNomComplet();
        utilisateurLabel.setText("👤 " + nomComplet + " — Secrétaire");
        bienvenuLabel.setText("Bonjour, " + SessionUtilisateur.getInstance().getUtilisateurConnecte().getPrenom() + " !");

        chargerStatistiques();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> mettreAJourDateHeure()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        mettreAJourDateHeure();
    }

    @FXML private void handleFicheEtudiante(MouseEvent e)  { naviguerVers("accueil/FicheEtudiante"); }
    @FXML private void handleDossiers(MouseEvent e)         { naviguerVers("accueil/DossierInscription"); }
    @FXML private void handleFilieres(MouseEvent e)         { naviguerVers("accueil/Filiere"); }
    @FXML private void handleEspaceSecretaire(MouseEvent e) { naviguerVers("accueil/EspaceSecretaire"); }

    @FXML
    private void handleDeconnexion() {
        SessionUtilisateur.getInstance().deconnecter();
        naviguerVers("accueil/Login");
    }

    private void naviguerVers(String page) {
        try { StartApplication.changeScene(page); } catch (IOException e) { System.err.println("Erreur navigation : " + e.getMessage()); }
    }

    private void chargerStatistiques() {
        int accepte = dossierRepo.countByStatut("Validé");
        int refuse  = dossierRepo.countByStatut("Refusé");
        int attente = dossierRepo.countByStatut("En attente");
        int total   = accepte + refuse + attente;

        lblNbAccepte.setText(String.valueOf(accepte));
        lblNbRefuse.setText(String.valueOf(refuse));
        lblNbAttente.setText(String.valueOf(attente));
        lblNbTotal.setText(String.valueOf(total));
    }

    private void mettreAJourDateHeure() {
        String dateHeure = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy - HH:mm:ss", Locale.FRENCH));
        dateHeure = dateHeure.substring(0, 1).toUpperCase() + dateHeure.substring(1);
        dateHeureLabel.setText("📅 " + dateHeure);
    }
}
