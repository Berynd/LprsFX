package appli.accueil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import model.Salle;
import repository.SalleRepository;

public class SalleController {

    @FXML private TextField nomTextField;
    @FXML private TextField rechercheTextField;
    @FXML private Label messageLabel;
    @FXML private Label compteurLabel;
    @FXML private Label statutLabel;
    
    @FXML private Button ajouterBtn;
    @FXML private Button modifierBtn;
    @FXML private Button supprimerBtn;
    @FXML private Button annulerBtn;
    
    @FXML private TableView<Salle> salleTableView;
    @FXML private TableColumn<Salle, Integer> idColumn;
    @FXML private TableColumn<Salle, String> nomColumn;
    @FXML private TableColumn<Salle, Void> actionsColumn;

    private SalleRepository salleRepository;
    private ObservableList<Salle> sallesList;
    private Salle salleSelectionnee;

    @FXML
    public void initialize() {
        salleRepository = new SalleRepository();
        sallesList = FXCollections.observableArrayList();
        
        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idSalle"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        
        // Ajouter des boutons d'action dans la colonne
        ajouterBoutonsActions();
        
        // Charger les données
        chargerDonnees();
        
        // Listener sur la sélection
        salleTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                salleSelectionnee = newSelection;
                remplirFormulaire(newSelection);
                modifierBtn.setDisable(false);
                supprimerBtn.setDisable(false);
            }
        });
    }

    private void ajouterBoutonsActions() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("✏️");
            private final Button deleteBtn = new Button("🗑️");
            private final HBox pane = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 10px;");
                deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 10px;");
                
                editBtn.setOnAction(event -> {
                    Salle salle = getTableView().getItems().get(getIndex());
                    salleSelectionnee = salle;
                    remplirFormulaire(salle);
                    modifierBtn.setDisable(false);
                    supprimerBtn.setDisable(false);
                });
                
                deleteBtn.setOnAction(event -> {
                    Salle salle = getTableView().getItems().get(getIndex());
                    confirmerSuppression(salle);
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
            afficherErreur("Veuillez saisir le nom de la salle !");
            return;
        }
        
        // Vérifier si la salle existe déjà
        if (salleRepository.salleExiste(nomTextField.getText().trim())) {
            afficherErreur("Une salle avec ce nom existe déjà !");
            return;
        }
        
        // Créer et ajouter la salle
        Salle nouvelleSalle = new Salle(nomTextField.getText().trim());
        
        if (salleRepository.ajouterSalle(nouvelleSalle)) {
            afficherSucces("Salle ajoutée avec succès !");
            chargerDonnees();
            viderFormulaire();
        } else {
            afficherErreur("Erreur lors de l'ajout de la salle.");
        }
    }

    @FXML
    private void handleModifier() {
        messageLabel.setText("");
        
        if (salleSelectionnee == null) {
            afficherErreur("Veuillez sélectionner une salle !");
            return;
        }
        
        if (nomTextField.getText().trim().isEmpty()) {
            afficherErreur("Veuillez saisir le nom de la salle !");
            return;
        }
        
        // Mettre à jour l'objet
        salleSelectionnee.setNom(nomTextField.getText().trim());
        
        if (salleRepository.mettreAJourSalle(salleSelectionnee)) {
            afficherSucces("Salle modifiée avec succès !");
            chargerDonnees();
            viderFormulaire();
        } else {
            afficherErreur("Erreur lors de la modification de la salle.");
        }
    }

    @FXML
    private void handleSupprimer() {
        if (salleSelectionnee == null) {
            afficherErreur("Veuillez sélectionner une salle !");
            return;
        }
        
        confirmerSuppression(salleSelectionnee);
    }

    private void confirmerSuppression(Salle salle) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer la salle : " + salle.getNom());
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette salle ?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (salleRepository.supprimerSalle(salle.getIdSalle())) {
                    afficherSucces("Salle supprimée avec succès !");
                    chargerDonnees();
                    viderFormulaire();
                } else {
                    afficherErreur("Erreur lors de la suppression de la salle.");
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
            sallesList.setAll(salleRepository.rechercherSalleParNom(recherche));
            salleTableView.setItems(sallesList);
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
        sallesList.setAll(salleRepository.getToutesLesSalles());
        salleTableView.setItems(sallesList);
        mettreAJourCompteur();
        statutLabel.setText("Dernière actualisation : " + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    private void remplirFormulaire(Salle salle) {
        nomTextField.setText(salle.getNom());
        ajouterBtn.setDisable(true);
    }

    private void viderFormulaire() {
        nomTextField.clear();
        salleSelectionnee = null;
        ajouterBtn.setDisable(false);
        modifierBtn.setDisable(true);
        supprimerBtn.setDisable(true);
        salleTableView.getSelectionModel().clearSelection();
        messageLabel.setText("");
    }

    private void mettreAJourCompteur() {
        int count = sallesList.size();
        compteurLabel.setText("(" + count + " salle" + (count > 1 ? "s" : "") + ")");
    }

    private void afficherErreur(String message) {
        messageLabel.setStyle("-fx-text-fill: #e74c3c;");
        messageLabel.setText("❌ " + message);
    }

    private void afficherSucces(String message) {
        messageLabel.setStyle("-fx-text-fill: #27ae60;");
        messageLabel.setText("✅ " + message);
    }
}
