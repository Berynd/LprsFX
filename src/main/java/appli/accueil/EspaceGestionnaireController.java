package appli.accueil;

import appli.StartApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import repository.DemandeFournitureRepository;
import repository.FournisseurRepository;
import repository.FournitureRepository;

import java.io.IOException;

public class EspaceGestionnaireController {

    private final FournitureRepository fournitureRepo     = new FournitureRepository();
    private final FournisseurRepository fournisseurRepo   = new FournisseurRepository();
    private final DemandeFournitureRepository demandeRepo = new DemandeFournitureRepository();

    @FXML private Label lblNbFournitures;
    @FXML private Label lblNbRupture;
    @FXML private Label lblNbDemandesAttente;
    @FXML private Label lblNbFournisseurs;
    @FXML private Label lblNotification;

    @FXML
    public void initialize() {
        chargerStatistiques();
    }

    @FXML private void handleFournisseurs(MouseEvent e) { naviguerVers("accueil/Fournisseur"); }
    @FXML private void handleFournitures(MouseEvent e)  { naviguerVers("accueil/Fourniture"); }
    @FXML private void handleDemandes(MouseEvent e)     { naviguerVers("accueil/DemandeFourniture"); }

    @FXML
    public void handleRetour(ActionEvent event) throws IOException {
        StartApplication.changeScene("accueil/Accueil");
    }

    private void naviguerVers(String page) {
        try { StartApplication.changeScene(page); } catch (IOException e) { e.printStackTrace(); }
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
        lblNotification.setText(
            "📋 Fournitures : " + totalFournitures +
            "  |  ⚠ En rupture (≤5) : " + enRupture +
            "  |  ⏳ Demandes en attente : " + demandesAttente +
            "  |  📦 Fournisseurs : " + totalFournisseurs
        );
    }
}
