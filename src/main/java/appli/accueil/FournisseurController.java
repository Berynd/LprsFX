package appli.accueil;

import appli.StartApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import model.Fournisseur;
import repository.FournisseurRepository;

import java.io.IOException;

public class FournisseurController {

    @FXML private TextField nomTextField;
    @FXML private TextField contactTextField;
    @FXML private TextField rechercheTextField;
    @FXML private Label messageLabel;
    @FXML private Label compteurLabel;
    @FXML private Label statutLabel;
    
    @FXML private Button ajouterBtn;
    @FXML private Button modifierBtn;
    @FXML private Button supprimerBtn;
    @FXML private Button annulerBtn;
    @FXML private Button retourBtn;
    
    @FXML private TableView<Fournisseur> fournisseurTableView;
    @FXML private TableColumn<Fournisseur, Integer> idColumn;
    @FXML private TableColumn<Fournisseur, String> nomColumn;
    @FXML private TableColumn<Fournisseur, String> contactColumn;
    @FXML private TableColumn<Fournisseur, Void> actionsColumn;

    private FournisseurRepository fournisseurRepository;
    private ObservableList<Fournisseur> fournisseursList;
    private Fournisseur fournisseurSelectionne;

    @FXML
    public void initialize() {
        fournisseurRepository = new FournisseurRepository();
        fournisseursList = FXCollections.observableArrayList();
        
        // Configuration des colonnes AVEC LAMBDA
        idColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getIdFournisseur()).asObject()
        );
        
        nomColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNom())
        );
        
        contactColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getContact())
        );
        
        // Ajouter des boutons d'action dans la colonne
        ajouterBoutonsActions();
        
        // Charger les données
        chargerDonnees();
        
        // Debug : afficher le nombre de fournisseurs chargés
        System.out.println("=== DEBUG FOURNISSEURS ===");
        System.out.println("Nombre de fournisseurs chargés : " + fournisseursList.size());
        for (Fournisseur f : fournisseursList) {
            System.out.println("Fournisseur : ID=" + f.getIdFournisseur() + ", Nom=" + f.getNom() + ", Contact=" + f.getContact());
        }
        
        // Listener sur la sélection
        fournisseurTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fournisseurSelectionne = newSelection;
                remplirFormulaire(newSelection);
                modifierBtn.setDisable(false);
                supprimerBtn.setDisable(false);
            }
        });
    }

    private void ajouterBoutonsActions() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("🔄️");
            private final Button deleteBtn = new Button("❌");
            private final HBox pane = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 10px;");
                deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 10px;");
                
                editBtn.setOnAction(event -> {
                    Fournisseur fournisseur = getTableView().getItems().get(getIndex());
                    fournisseurSelectionne = fournisseur;
                    remplirFormulaire(fournisseur);
                    modifierBtn.setDisable(false);
                    supprimerBtn.setDisable(false);
                });
                
                deleteBtn.setOnAction(event -> {
                    Fournisseur fournisseur = getTableView().getItems().get(getIndex());
                    confirmerSuppression(fournisseur);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    @FXML
    private void handleAjouter() {
        messageLabel.setText("");
        
        // Validation
        if (nomTextField.getText().trim().isEmpty()) {
            afficherErreur("Veuillez saisir le nom du fournisseur !");
            return;
        }
        
        if (contactTextField.getText().trim().isEmpty()) {
            afficherErreur("Veuillez saisir les informations de contact !");
            return;
        }
        
        // Créer et ajouter le fournisseur
        Fournisseur nouveauFournisseur = new Fournisseur(
            nomTextField.getText().trim(),
            contactTextField.getText().trim()
        );
        
        if (fournisseurRepository.ajouterFournisseur(nouveauFournisseur)) {
            afficherSucces("Fournisseur ajouté avec succès !");
            chargerDonnees();
            viderFormulaire();
        } else {
            afficherErreur("Erreur lors de l'ajout du fournisseur.");
        }
    }

    @FXML
    private void handleModifier() {
        messageLabel.setText("");
        
        if (fournisseurSelectionne == null) {
            afficherErreur("Veuillez sélectionner un fournisseur !");
            return;
        }
        
        if (nomTextField.getText().trim().isEmpty()) {
            afficherErreur("Veuillez saisir le nom du fournisseur !");
            return;
        }
        
        if (contactTextField.getText().trim().isEmpty()) {
            afficherErreur("Veuillez saisir les informations de contact !");
            return;
        }
        
        // Mettre à jour l'objet
        fournisseurSelectionne.setNom(nomTextField.getText().trim());
        fournisseurSelectionne.setContact(contactTextField.getText().trim());
        
        if (fournisseurRepository.mettreAJourFournisseur(fournisseurSelectionne)) {
            afficherSucces("Fournisseur modifié avec succès !");
            chargerDonnees();
            viderFormulaire();
        } else {
            afficherErreur("Erreur lors de la modification du fournisseur.");
        }
    }

    @FXML
    private void handleSupprimer() {
        if (fournisseurSelectionne == null) {
            afficherErreur("Veuillez sélectionner un fournisseur !");
            return;
        }
        
        confirmerSuppression(fournisseurSelectionne);
    }

    private void confirmerSuppression(Fournisseur fournisseur) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le fournisseur : " + fournisseur.getNom());
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce fournisseur ?\n\nAttention : Cela supprimera également toutes les associations avec les fournitures.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (fournisseurRepository.supprimerFournisseur(fournisseur.getIdFournisseur())) {
                    afficherSucces("Fournisseur supprimé avec succès !");
                    chargerDonnees();
                    viderFormulaire();
                } else {
                    afficherErreur("Erreur lors de la suppression du fournisseur.");
                }
            }
        });
    }

    @FXML
    private void handleAnnuler() {
        viderFormulaire();
    }

    @FXML
    private void handleRecherche() {
        String recherche = rechercheTextField.getText().trim();
        
        if (recherche.isEmpty()) {
            chargerDonnees();
        } else {
            fournisseursList.setAll(fournisseurRepository.rechercherFournisseurParNom(recherche));
            fournisseurTableView.setItems(fournisseursList);
            mettreAJourCompteur();
        }
    }

    @FXML
    private void handleActualiser() {
        chargerDonnees();
        viderFormulaire();
        afficherSucces("Données actualisées !");
    }



    private void chargerDonnees() {
        fournisseursList.setAll(fournisseurRepository.getTousLesFournisseurs());
        fournisseurTableView.setItems(fournisseursList);
        mettreAJourCompteur();
        statutLabel.setText("Dernière actualisation : " + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    private void remplirFormulaire(Fournisseur fournisseur) {
        nomTextField.setText(fournisseur.getNom());
        contactTextField.setText(fournisseur.getContact());
        ajouterBtn.setDisable(true);
    }

    private void viderFormulaire() {
        nomTextField.clear();
        contactTextField.clear();
        fournisseurSelectionne = null;
        ajouterBtn.setDisable(false);
        modifierBtn.setDisable(true);
        supprimerBtn.setDisable(true);
        fournisseurTableView.getSelectionModel().clearSelection();
        messageLabel.setText("");
    }

    private void mettreAJourCompteur() {
        int count = fournisseursList.size();
        compteurLabel.setText("(" + count + " fournisseur" + (count > 1 ? "s" : "") + ")");
    }

    private void afficherErreur(String message) {
        messageLabel.setStyle("-fx-text-fill: #e74c3c;");
        messageLabel.setText("❌ " + message);
    }

    private void afficherSucces(String message) {
        messageLabel.setStyle("-fx-text-fill: #27ae60;");
        messageLabel.setText("✅ " + message);
    }

    @FXML
    public void retour(ActionEvent actionEvent) throws IOException {
        StartApplication.changeScene("accueil/Accueil");
    }
}
