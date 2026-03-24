package appli.accueil;

import appli.StartApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import model.Etudiant;
import model.Rdv;
import model.Salle;
import model.Utilisateur;
import repository.EtudiantRepository;
import repository.RdvRepository;
import repository.SalleRepository;
import repository.UtilisateurRepository;
import service.LogService;
import session.SessionUtilisateur;

import java.io.IOException;
import java.util.List;

public class RendezVousController {

    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> demiJourneeCombo;
    @FXML private ComboBox<Etudiant> etudiantCombo;
    @FXML private ComboBox<Salle> salleCombo;
    @FXML private Label messageLabel;
    @FXML private Label compteurLabel;
    @FXML private Label statutLabel;
    @FXML private Button ajouterBtn;
    @FXML private Button annulerBtn;
    @FXML private Button verifierDispoBtn;

    @FXML private TableView<Rdv> rdvTableView;
    @FXML private TableColumn<Rdv, Integer> idColumn;
    @FXML private TableColumn<Rdv, String> dateColumn;
    @FXML private TableColumn<Rdv, String> demiJourneeColumn;
    @FXML private TableColumn<Rdv, String> etudiantColumn;
    @FXML private TableColumn<Rdv, String> salleColumn;
    @FXML private TableColumn<Rdv, Void> actionsColumn;

    private RdvRepository rdvRepo;
    private EtudiantRepository etudiantRepo;
    private SalleRepository salleRepo;
    private UtilisateurRepository utilisateurRepo;
    private ObservableList<Rdv> rdvList;
    private List<Etudiant> tousEtudiants;
    private List<Salle> toutesSalles;

    @FXML
    public void initialize() {
        rdvRepo = new RdvRepository();
        etudiantRepo = new EtudiantRepository();
        salleRepo = new SalleRepository();
        utilisateurRepo = new UtilisateurRepository();
        rdvList = FXCollections.observableArrayList();

        tousEtudiants = etudiantRepo.getTousLesEtudiants();
        toutesSalles = salleRepo.getToutesLesSalles();

        // Demi-journée
        demiJourneeCombo.setItems(FXCollections.observableArrayList("Matin", "Après-midi"));

        // Étudiants
        etudiantCombo.setItems(FXCollections.observableArrayList(tousEtudiants));
        etudiantCombo.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Etudiant e) { return e == null ? "" : e.getNom() + " " + e.getPrenom(); }
            @Override public Etudiant fromString(String s) { return null; }
        });

        // Salles
        salleCombo.setItems(FXCollections.observableArrayList(toutesSalles));
        salleCombo.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Salle s) { return s == null ? "" : s.getNom(); }
            @Override public Salle fromString(String s) { return null; }
        });

        // Colonnes
        idColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleIntegerProperty(c.getValue().getIdRdv()).asObject());
        dateColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getDate()));
        demiJourneeColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getDemiJournee()));
        etudiantColumn.setCellValueFactory(c -> {
            Etudiant e = etudiantRepo.getEtudiantById(c.getValue().getRefEtudiant());
            return new javafx.beans.property.SimpleStringProperty(e != null ? e.getNom() + " " + e.getPrenom() : "?");
        });
        salleColumn.setCellValueFactory(c -> {
            Salle s = salleRepo.getSalleParId(c.getValue().getRefSalle());
            return new javafx.beans.property.SimpleStringProperty(s != null ? s.getNom() : "?");
        });

        ajouterBoutonsActions();
        chargerDonnees();
    }

    private void ajouterBoutonsActions() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button supprimerBtn = new Button("❌ Annuler");
            private final HBox pane = new HBox(supprimerBtn);
            {
                supprimerBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 10px;");
                supprimerBtn.setOnAction(e -> {
                    Rdv rdv = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Annuler ce rendez-vous ?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait().ifPresent(r -> {
                        if (r == ButtonType.YES) {
                            rdvRepo.supprimerRdv(rdv.getIdRdv());
                            LogService.log("RDV #" + rdv.getIdRdv() + " annulé", "SUPPRIMER", "RendezVous");
                            chargerDonnees();
                        }
                    });
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    @FXML
    private void handleVerifierDispo() {
        if (datePicker.getValue() == null || demiJourneeCombo.getValue() == null || salleCombo.getValue() == null) {
            afficherErreur("Sélectionnez une date, une demi-journée et une salle pour vérifier !");
            return;
        }
        String date = datePicker.getValue().toString();
        String demi = demiJourneeCombo.getValue();
        Salle salle = salleCombo.getValue();
        boolean dispo = rdvRepo.isSalleDisponible(salle.getIdSalle(), date, demi);
        if (dispo) {
            afficherSucces("Salle disponible pour ce créneau !");
        } else {
            afficherErreur("Salle déjà occupée sur ce créneau !");
        }
    }

    @FXML
    private void handleAjouter() {
        if (datePicker.getValue() == null) { afficherErreur("Sélectionnez une date !"); return; }
        if (demiJourneeCombo.getValue() == null) { afficherErreur("Sélectionnez une demi-journée !"); return; }
        if (etudiantCombo.getValue() == null) { afficherErreur("Sélectionnez un étudiant !"); return; }
        if (salleCombo.getValue() == null) { afficherErreur("Sélectionnez une salle !"); return; }

        String date = datePicker.getValue().toString();
        String demi = demiJourneeCombo.getValue();
        Salle salle = salleCombo.getValue();

        if (!rdvRepo.isSalleDisponible(salle.getIdSalle(), date, demi)) {
            afficherErreur("La salle " + salle.getNom() + " est déjà occupée ce créneau !");
            return;
        }

        Rdv rdv = new Rdv();
        rdv.setDate(date);
        rdv.setDemiJournee(demi);
        rdv.setRefEtudiant(etudiantCombo.getValue().getIdEtudiant());
        rdv.setRefProfesseur(SessionUtilisateur.getInstance().getUtilisateurConnecte().getIdUtilisateur());
        rdv.setRefSalle(salle.getIdSalle());

        int id = rdvRepo.ajouterRdv(rdv);
        if (id > 0) {
            LogService.log("RDV créé le " + date + " " + demi + " salle " + salle.getNom(), "AJOUTER", "RendezVous");
            afficherSucces("Rendez-vous créé avec succès !");
            chargerDonnees();
            viderFormulaire();
        } else {
            afficherErreur("Erreur lors de la création du rendez-vous.");
        }
    }

    @FXML
    private void handleAnnuler() { viderFormulaire(); }

    @FXML
    private void handleActualiser() { chargerDonnees(); afficherSucces("Actualisé !"); }

    @FXML
    public void handleRetour(ActionEvent event) {
        StartApplication.goBack();
    }

    private void chargerDonnees() {
        int idUser = SessionUtilisateur.getInstance().getUtilisateurConnecte().getIdUtilisateur();
        String role = SessionUtilisateur.getInstance().getRole();
        if ("Professeur".equals(role)) {
            rdvList.setAll(rdvRepo.getRdvParProfesseur(idUser));
        } else {
            rdvList.setAll(rdvRepo.getTousLesRdv());
        }
        rdvTableView.setItems(rdvList);
        int n = rdvList.size();
        compteurLabel.setText("(" + n + " rendez-vous)");
        statutLabel.setText("Actualisé à " + java.time.LocalTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    private void viderFormulaire() {
        datePicker.setValue(null);
        demiJourneeCombo.setValue(null);
        etudiantCombo.setValue(null);
        salleCombo.setValue(null);
        messageLabel.setText("");
    }

    private void afficherErreur(String msg) {
        messageLabel.setStyle("-fx-text-fill: #e74c3c;");
        messageLabel.setText("❌ " + msg);
    }

    private void afficherSucces(String msg) {
        messageLabel.setStyle("-fx-text-fill: #27ae60;");
        messageLabel.setText("✅ " + msg);
    }
}
