package appli.accueil;

import appli.StartApplication;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import model.DemandeFourniture;
import repository.DemandeFournitureRepository;
import repository.RdvRepository;
import session.SessionUtilisateur;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Controller de l'accueil dédié au professeur (AccueilProfesseurView.fxml).
 *
 * Variante de AccueilView réservée au rôle Professeur. Affiche :
 *  - Le nom complet et le rôle de l'utilisateur connecté
 *  - Une horloge temps réel (mise à jour toutes les secondes)
 *  - Les statistiques personnelles du professeur (demandes et RDV filtrés sur son id)
 *  - Des cartes de navigation vers ses modules (Fournitures, Demandes, RDV, Dossiers, EspaceProfesseur)
 */
public class AccueilProfesseurController {

    private final DemandeFournitureRepository demandeRepo = new DemandeFournitureRepository();
    private final RdvRepository rdvRepo = new RdvRepository();

    @FXML private Label utilisateurLabel;
    @FXML private Label dateHeureLabel;
    @FXML private Label bienvenuLabel;

    // Stats
    @FXML private Label lblNbDemandesTotal;
    @FXML private Label lblNbValidees;
    @FXML private Label lblNbRefusees;
    @FXML private Label lblNbAttente;
    @FXML private Label lblNbRdv;

    @FXML
    public void initialize() {
        String nomComplet = SessionUtilisateur.getInstance().getNomComplet();
        utilisateurLabel.setText("👤 " + nomComplet + " — Professeur");
        bienvenuLabel.setText("Bonjour, " + SessionUtilisateur.getInstance().getUtilisateurConnecte().getPrenom() + " !");

        chargerStatistiques();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> mettreAJourDateHeure()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        mettreAJourDateHeure();
    }

    @FXML private void handleFournitures(MouseEvent e)      { naviguerVers("accueil/Fourniture"); }
    @FXML private void handleDemandes(MouseEvent e)         { naviguerVers("accueil/DemandeFourniture"); }
    @FXML private void handleRendezVous(MouseEvent e)       { naviguerVers("accueil/RendezVous"); }
    @FXML private void handleDossiers(MouseEvent e)         { naviguerVers("accueil/DossierInscription"); }
    @FXML private void handleEspaceProfesseur(MouseEvent e) { naviguerVers("accueil/EspaceProfesseur"); }

    @FXML
    private void handleDeconnexion() {
        SessionUtilisateur.getInstance().deconnecter();
        naviguerVers("accueil/Login");
    }

    private void naviguerVers(String page) {
        try { StartApplication.changeScene(page); } catch (IOException e) { System.err.println("Erreur navigation : " + e.getMessage()); }
    }

    private void chargerStatistiques() {
        int idProf = SessionUtilisateur.getInstance().getUtilisateurConnecte().getIdUtilisateur();
        List<DemandeFourniture> mesDemandes = demandeRepo.getDemandesParProfesseur(idProf);

        long validees = mesDemandes.stream().filter(d -> "Validé".equalsIgnoreCase(d.getStatut())).count();
        long refusees = mesDemandes.stream().filter(d -> "Refusé".equalsIgnoreCase(d.getStatut())).count();
        long attente  = mesDemandes.stream().filter(d -> "En attente".equalsIgnoreCase(d.getStatut())).count();
        int mesRdv    = rdvRepo.getRdvParProfesseur(idProf).size();

        lblNbDemandesTotal.setText(String.valueOf(mesDemandes.size()));
        lblNbValidees.setText(String.valueOf(validees));
        lblNbRefusees.setText(String.valueOf(refusees));
        lblNbAttente.setText(String.valueOf(attente));
        lblNbRdv.setText(String.valueOf(mesRdv));
    }

    private void mettreAJourDateHeure() {
        String dateHeure = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy - HH:mm:ss", Locale.FRENCH));
        dateHeure = dateHeure.substring(0, 1).toUpperCase() + dateHeure.substring(1);
        dateHeureLabel.setText("📅 " + dateHeure);
    }
}
