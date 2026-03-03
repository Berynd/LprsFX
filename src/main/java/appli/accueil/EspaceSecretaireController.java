package appli.accueil;

import appli.StartApplication;
import repository.DossierInscriptionRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class EspaceSecretaireController {

    private final DossierInscriptionRepository dossierRepo = new DossierInscriptionRepository();

    @FXML
    private Button carteCreeEtudiant;
    @FXML
    private Button carteCreeDossierInscription;
    @FXML
    private Button carteListeEtudiants;
    @FXML
    private Button carteListeDossiers;

    // Statistiques
    @FXML
    private Label lblNbAccepte;
    @FXML
    private Label lblNbRefuse;
    @FXML
    private Label lblNbEtudiants;
    @FXML
    private Label lblNbDossier;
    @FXML
    private Label lblNotification;

    @FXML
    public void initialize() {
        chargerStatistiques();
    }

    @FXML
    private void handleCreerEtudiant(MouseEvent e) {
        naviguerVers("accueil/FicheEtudiante");
    }

    @FXML
    private void handleCreerUnDossierInscription(MouseEvent e) {
        naviguerVers("accueil/DossierInscription");
    }

    @FXML
    private void handleListeDesEtudiants(MouseEvent e) {
        naviguerVers("accueil/FicheEtudiante");
    }

    @FXML
    private void handleListeDesDossiers(MouseEvent e) {
        naviguerVers("accueil/ListeDossiers");
    }

    @FXML
    public void handleRetour(ActionEvent event) throws IOException {
        StartApplication.changeScene("accueil/Accueil");
    }

    private void naviguerVers(String page) {
        try {
            StartApplication.changeScene(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void chargerStatistiques() {
        int accepte = dossierRepo.countByStatut("VALIDÉ");
        int refuse = dossierRepo.countByStatut("REFUSÉ");
        int attente = dossierRepo.countByStatut("EN_ATTENTE");

        int total = accepte + refuse + attente;

        lblNbAccepte.setText(String.valueOf(accepte));
        lblNbRefuse.setText(String.valueOf(refuse));
        lblNbEtudiants.setText(String.valueOf(attente));
        lblNbDossier.setText(String.valueOf(total));

        // Notification complète
        lblNotification.setText(
                "✔ Acceptés : " + accepte +
                        " | ❌ Refusés : " + refuse +
                        " | ⏳ En attente : " + attente +
                        " | 📁 Total dossiers : " + total
        );
    }


}