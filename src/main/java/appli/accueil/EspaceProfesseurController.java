package appli.accueil;

import appli.StartApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import repository.DemandeFournitureRepository;
import repository.RdvRepository;
import session.sessionUtilisateur;

import java.io.IOException;
import java.util.List;

public class EspaceProfesseurController {

    private final DemandeFournitureRepository demandeRepo = new DemandeFournitureRepository();
    private final RdvRepository rdvRepo = new RdvRepository();

    @FXML private Label lblNbDemandesTotal;
    @FXML private Label lblNbDemandesValidees;
    @FXML private Label lblNbDemandesRefusees;
    @FXML private Label lblNbDemandesAttente;
    @FXML private Label lblNbRdv;
    @FXML private Label lblNotification;

    @FXML
    public void initialize() {
        chargerStatistiques();
    }

    @FXML private void handleDemandes(MouseEvent e)    { naviguerVers("accueil/DemandeFourniture"); }
    @FXML private void handleRendezVous(MouseEvent e)  { naviguerVers("accueil/RendezVous"); }
    @FXML private void handleFournitures(MouseEvent e) { naviguerVers("accueil/Fourniture"); }
    @FXML private void handleDossiers(MouseEvent e)    { naviguerVers("accueil/DossierInscription"); }

    @FXML
    public void handleRetour(ActionEvent event) {
        StartApplication.goBack();
    }

    private void naviguerVers(String page) {
        try { StartApplication.changeScene(page); } catch (IOException e) { e.printStackTrace(); }
    }

    private void chargerStatistiques() {
        int idProf = sessionUtilisateur.getInstance().getUtilisateurConnecte().getIdUtilisateur();

        List<?> mesDemandes = demandeRepo.getDemandesParProfesseur(idProf);
        long validees = mesDemandes.stream()
            .filter(d -> "Validé".equalsIgnoreCase(((model.DemandeFourniture) d).getStatut()))
            .count();
        long refusees = mesDemandes.stream()
            .filter(d -> "Refusé".equalsIgnoreCase(((model.DemandeFourniture) d).getStatut()))
            .count();
        long attente = mesDemandes.stream()
            .filter(d -> "En attente".equalsIgnoreCase(((model.DemandeFourniture) d).getStatut()))
            .count();

        int mesRdv = rdvRepo.getRdvParProfesseur(idProf).size();

        lblNbDemandesTotal.setText(String.valueOf(mesDemandes.size()));
        lblNbDemandesValidees.setText(String.valueOf(validees));
        lblNbDemandesRefusees.setText(String.valueOf(refusees));
        lblNbDemandesAttente.setText(String.valueOf(attente));
        lblNbRdv.setText(String.valueOf(mesRdv));
        lblNotification.setText(
            "📝 Demandes : " + mesDemandes.size() +
            "  |  ✔ Validées : " + validees +
            "  |  ❌ Refusées : " + refusees +
            "  |  ⏳ En attente : " + attente +
            "  |  📅 Rendez-vous : " + mesRdv
        );
    }
}
