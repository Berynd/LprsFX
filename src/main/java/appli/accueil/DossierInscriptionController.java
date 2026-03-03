package appli.accueil;

import appli.StartApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import model.DossierInscription;
import model.Etudiant;
import model.Filiere;
import repository.DossierInscriptionRepository;
import repository.EtudiantRepository;
import repository.FiliereRepository;
import session.SessionUtilisateur;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class DossierInscriptionController {

    @FXML private ComboBox<Etudiant> etudiantCombo;
    @FXML private ComboBox<Filiere> filiereCombo;
    @FXML private TextArea motivationArea;
    @FXML private Label messageLabel;
    @FXML private Label compteurLabel;
    @FXML private Label statutLabel;
    @FXML private Button ajouterBtn;
    @FXML private Button annulerBtn;

    @FXML private TableView<DossierInscription> dossierTableView;
    @FXML private TableColumn<DossierInscription, Integer> idColumn;
    @FXML private TableColumn<DossierInscription, String> etudiantColumn;
    @FXML private TableColumn<DossierInscription, String> filiereColumn;
    @FXML private TableColumn<DossierInscription, String> dateColumn;
    @FXML private TableColumn<DossierInscription, String> statutColumn;
    @FXML private TableColumn<DossierInscription, Void> actionsColumn;

    private DossierInscriptionRepository dossierRepo;
    private EtudiantRepository etudiantRepo;
    private FiliereRepository filiereRepo;
    private ObservableList<DossierInscription> dossiersList;
    private List<Etudiant> tousEtudiants;
    private List<Filiere> tousFillieres;

    @FXML
    public void initialize() {
        dossierRepo = new DossierInscriptionRepository();
        etudiantRepo = new EtudiantRepository();
        filiereRepo = new FiliereRepository();
        dossiersList = FXCollections.observableArrayList();

        tousEtudiants = etudiantRepo.getTousLesEtudiants();
        tousFillieres = filiereRepo.getToutesLesFilieres();

        // Remplir les combos
        etudiantCombo.setItems(FXCollections.observableArrayList(tousEtudiants));
        etudiantCombo.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Etudiant e) { return e == null ? "" : e.getNom() + " " + e.getPrenom(); }
            @Override public Etudiant fromString(String s) { return null; }
        });

        filiereCombo.setItems(FXCollections.observableArrayList(tousFillieres));
        filiereCombo.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Filiere f) { return f == null ? "" : f.getNom(); }
            @Override public Filiere fromString(String s) { return null; }
        });

        // Configurer colonnes
        idColumn.setCellValueFactory(c ->
                new javafx.beans.property.SimpleIntegerProperty(c.getValue().getIdDossierInscription()).asObject());
        dateColumn.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getDateCreation()));
        statutColumn.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getStatut()));
        etudiantColumn.setCellValueFactory(c -> {
            Etudiant e = etudiantRepo.getEtudiantById(c.getValue().getRefEtudiant());
            return new javafx.beans.property.SimpleStringProperty(e != null ? e.getNom() + " " + e.getPrenom() : "?");
        });
        filiereColumn.setCellValueFactory(c -> {
            Filiere f = filiereRepo.getFiliereById(c.getValue().getRefFiliere());
            return new javafx.beans.property.SimpleStringProperty(f != null ? f.getNom() : "?");
        });

        // Colorer le statut
        statutColumn.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String statut, boolean empty) {
                super.updateItem(statut, empty);
                if (empty || statut == null) { setText(null); setStyle(""); }
                else {
                    setText(statut);
                    switch (statut) {
                        case "VALIDÉ" -> setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                        case "REFUSÉ" -> setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                        default -> setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
                    }
                }
            }
        });

        ajouterBoutonsActions();
        chargerDonnees();
    }

    private void ajouterBoutonsActions() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button validerBtn = new Button("✅");
            private final Button refuserBtn = new Button("❌");
            private final Button supprimerBtn = new Button("🗑");
            private final HBox pane = new HBox(4, validerBtn, refuserBtn, supprimerBtn);
            {
                validerBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 10px;");
                refuserBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 10px;");
                supprimerBtn.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-font-size: 10px;");

                validerBtn.setOnAction(e -> {
                    DossierInscription d = getTableView().getItems().get(getIndex());
                    if (dossierRepo.changerStatut(d.getIdDossierInscription(), "Validé")) {
                        afficherSucces("Dossier validé !");
                        chargerDonnees();
                    }
                });

                refuserBtn.setOnAction(e -> {
                    DossierInscription d = getTableView().getItems().get(getIndex());
                    if (dossierRepo.changerStatut(d.getIdDossierInscription(), "Refusé")) {
                        afficherSucces("Dossier refusé.");
                        chargerDonnees();
                    }
                });

                supprimerBtn.setOnAction(e -> {
                    DossierInscription d = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer ce dossier ?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait().ifPresent(r -> {
                        if (r == ButtonType.YES) {
                            dossierRepo.supprimerDossierInscription(d.getIdDossierInscription());
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
    private void handleAjouter() {
        if (etudiantCombo.getValue() == null) { afficherErreur("Sélectionnez un étudiant !"); return; }
        if (filiereCombo.getValue() == null) { afficherErreur("Sélectionnez une filière !"); return; }
        if (motivationArea.getText().trim().isEmpty()) { afficherErreur("La motivation est obligatoire !"); return; }

        DossierInscription d = new DossierInscription(0);
        d.setDateCreation(LocalDate.now().toString());
        d.setStatut("En attente");
        d.setMotivation(motivationArea.getText().trim());
        d.setRefEtudiant(etudiantCombo.getValue().getIdEtudiant());
        d.setRefFiliere(filiereCombo.getValue().getIdFiliere());
        d.setRefSecretaire(SessionUtilisateur.getInstance().getUtilisateurConnecte().getIdUtilisateur());

        int id = dossierRepo.ajouterDossierInscription(d);
        if (id > 0) {
            afficherSucces("Dossier créé avec succès !");
            chargerDonnees();
            viderFormulaire();

            // Retour à l’espace secrétaire pour mise à jour stats
            try {
                StartApplication.changeScene("accueil/Accueil");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            afficherErreur("Erreur lors de la création du dossier.");
        }
    }

    @FXML
    private void handleAnnuler() { viderFormulaire(); }

    @FXML
    private void handleActualiser() { chargerDonnees(); afficherSucces("Données actualisées !"); }

    @FXML
    public void handleRetour(ActionEvent event) {
        StartApplication.goBack();
    }

    private void chargerDonnees() {
        dossiersList.setAll(dossierRepo.getTousDossiers());
        dossierTableView.setItems(dossiersList);

        int n = dossiersList.size();
        compteurLabel.setText("(" + n + " dossier" + (n > 1 ? "s" : "") + ")");
        statutLabel.setText("Actualisé à " + java.time.LocalTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    private void viderFormulaire() {
        etudiantCombo.setValue(null);
        filiereCombo.setValue(null);
        motivationArea.clear();
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