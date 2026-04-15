package appli.accueil;

import appli.StartApplication;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import repository.DemandeFournitureRepository;
import repository.FournisseurRepository;
import repository.FournitureRepository;
import session.SessionUtilisateur;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Controller de l'accueil dédié au gestionnaire de stock (AccueilGestionnaireView.fxml).
 *
 * Variante de AccueilView réservée au rôle Gestionnaire de stock. Affiche :
 *  - Le nom complet et le rôle de l'utilisateur connecté
 *  - Une horloge temps réel (mise à jour toutes les secondes)
 *  - Les statistiques stock (total fournitures, ruptures ≤5, demandes en attente, fournisseurs)
 *  - Des cartes de navigation vers ses modules (Fournisseurs, Fournitures, Demandes, EspaceGestionnaire)
 */
public class AccueilGestionnaireController {

    private final FournitureRepository fournitureRepo     = new FournitureRepository();
    private final FournisseurRepository fournisseurRepo   = new FournisseurRepository();
    private final DemandeFournitureRepository demandeRepo = new DemandeFournitureRepository();

    @FXML private Label utilisateurLabel;
    @FXML private Label dateHeureLabel;
    @FXML private Label bienvenuLabel;

    // Stats
    @FXML private Label lblNbFournitures;
    @FXML private Label lblNbRupture;
    @FXML private Label lblNbDemandesAttente;
    @FXML private Label lblNbFournisseurs;

    @FXML
    public void initialize() {
        String nomComplet = SessionUtilisateur.getInstance().getNomComplet();
        utilisateurLabel.setText("👤 " + nomComplet + " — Gestionnaire de stock");
        bienvenuLabel.setText("Bonjour, " + SessionUtilisateur.getInstance().getUtilisateurConnecte().getPrenom() + " !");

        chargerStatistiques();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> mettreAJourDateHeure()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        mettreAJourDateHeure();
    }

    @FXML private void handleFournisseurs(MouseEvent e)      { naviguerVers("accueil/Fournisseur"); }
    @FXML private void handleFournitures(MouseEvent e)       { naviguerVers("accueil/Fourniture"); }
    @FXML private void handleDemandes(MouseEvent e)          { naviguerVers("accueil/DemandeFourniture"); }
    @FXML private void handleEspaceGestionnaire(MouseEvent e){ naviguerVers("accueil/EspaceGestionnaire"); }

    @FXML
    private void handleDeconnexion() {
        SessionUtilisateur.getInstance().deconnecter();
        naviguerVers("accueil/Login");
    }

    private void naviguerVers(String page) {
        try { StartApplication.changeScene(page); } catch (IOException e) { System.err.println("Erreur navigation : " + e.getMessage()); }
    }

    private void chargerStatistiques() {
        int totalFournitures  = fournitureRepo.getToutesLesFournitures().size();
        int enRupture         = fournitureRepo.getFournituresEnRupture(5).size();
        int demandesAttente   = demandeRepo.countByStatut("En attente");
        int totalFournisseurs = fournisseurRepo.getTousLesFournisseurs().size();

        lblNbFournitures.setText(String.valueOf(totalFournitures));
        lblNbRupture.setText(String.valueOf(enRupture));
        lblNbDemandesAttente.setText(String.valueOf(demandesAttente));
        lblNbFournisseurs.setText(String.valueOf(totalFournisseurs));
    }

    private void mettreAJourDateHeure() {
        String dateHeure = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy - HH:mm:ss", Locale.FRENCH));
        dateHeure = dateHeure.substring(0, 1).toUpperCase() + dateHeure.substring(1);
        dateHeureLabel.setText("📅 " + dateHeure);
    }
}
