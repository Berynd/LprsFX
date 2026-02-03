package appli.accueil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import model.Fourniture;
import repository.FournitureRepository;

import java.util.Optional;

public class FournitureController {

    @FXML private TextField libelleTextField;
    @FXML private TextArea descriptionTextArea;
    @FXML private TextField stockTextField;
    @FXML private TextField rechercheTextField;
    @FXML private Label messageLabel;
    @FXML private Label compteurLabel;
    @FXML private Label statutLabel;
    
    @FXML private Button ajouterBtn;
    @FXML private Button modifierBtn;
    @FXML private Button supprimerBtn;
    @FXML private Button annulerBtn;
    @FXML private Button ajouterStockBtn;
    @FXML private Button retirerStockBtn;
    @FXML private Button retourBtn;
    
    @FXML private TableView<Fourniture> fournitureTableView;
    @FXML private TableColumn<Fourniture, Integer> idColumn;
    @FXML private TableColumn<Fourniture, String> libelleColumn;
    @FXML private TableColumn<Fourniture, String> descriptionColumn;
    @FXML private TableColumn<Fourniture, Integer> stockColumn;
    @FXML private TableColumn<Fourniture, Void> actionsColumn;

    private FournitureRepository fournitureRepository;
    private ObservableList<Fourniture> fournituresList;
    private Fourniture fournitureSelectionnee;

    @FXML
    public void initialize() {
        fournitureRepository = new FournitureRepository();
        fournituresList = FXCollections.observableArrayList();
        
        // Configuration des colonnes AVEC LAMBDA
        idColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getIdFourniture()).asObject()
        );
        
        libelleColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLibelle())
        );
        
        descriptionColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription())
        );
        
        stockColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getStockActuel()).asObject()
        );
        
        // Colorer le stock en rouge si faible
        stockColumn.setCellFactory(column -> new TableCell<Fourniture, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
                    if (item <= 10) {
                        setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
                    } else if (item <= 20) {
                        setStyle("-fx-background-color: #e67e22; -fx-text-fill: white;");
                    } else {
                        setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
                    }
                }
            }
        });
        
        // Ajouter des boutons d'action dans la colonne
        ajouterBoutonsActions();
        
        // Charger les données
        chargerDonnees();
        
        // Debug : afficher le nombre de fournitures chargées
        System.out.println("=== DEBUG FOURNITURES ===");
        System.out.println("Nombre de fournitures chargées : " + fournituresList.size());
        for (Fourniture f : fournituresList) {
            System.out.println("Fourniture : ID=" + f.getIdFourniture() + ", Libelle=" + f.getLibelle() + ", Stock=" + f.getStockActuel());
        }
        
        // Listener sur la sélection
        fournitureTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fournitureSelectionnee = newSelection;
                remplirFormulaire(newSelection);
                modifierBtn.setDisable(false);
                supprimerBtn.setDisable(false);
                ajouterStockBtn.setDisable(false);
                retirerStockBtn.setDisable(false);
            }
        });
        
        // Validation numérique pour le stock
        stockTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                stockTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void ajouterBoutonsActions() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("✏️");
            private final Button plusBtn = new Button("➕");
            private final Button minusBtn = new Button("➖");
            private final Button deleteBtn = new Button("🗑️");
            private final HBox pane = new HBox(3, editBtn, plusBtn, minusBtn, deleteBtn);

            {
                editBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 9px; -fx-padding: 3 5;");
                plusBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 9px; -fx-padding: 3 5;");
                minusBtn.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-size: 9px; -fx-padding: 3 5;");
                deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 9px; -fx-padding: 3 5;");
                
                editBtn.setOnAction(event -> {
                    Fourniture fourniture = getTableView().getItems().get(getIndex());
                    fournitureSelectionnee = fourniture;
                    remplirFormulaire(fourniture);
                    modifierBtn.setDisable(false);
                    supprimerBtn.setDisable(false);
                    ajouterStockBtn.setDisable(false);
                    retirerStockBtn.setDisable(false);
                });
                
                plusBtn.setOnAction(event -> {
                    Fourniture fourniture = getTableView().getItems().get(getIndex());
                    ajouterStockRapide(fourniture);
                });
                
                minusBtn.setOnAction(event -> {
                    Fourniture fourniture = getTableView().getItems().get(getIndex());
                    retirerStockRapide(fourniture);
                });
                
                deleteBtn.setOnAction(event -> {
                    Fourniture fourniture = getTableView().getItems().get(getIndex());
                    confirmerSuppression(fourniture);
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
        if (libelleTextField.getText().trim().isEmpty()) {
            afficherErreur("Veuillez saisir le libellé de la fourniture !");
            return;
        }
        
        if (stockTextField.getText().trim().isEmpty()) {
            afficherErreur("Veuillez saisir le stock initial !");
            return;
        }
        
        int stock;
        try {
            stock = Integer.parseInt(stockTextField.getText().trim());
            if (stock < 0) {
                afficherErreur("Le stock ne peut pas être négatif !");
                return;
            }
        } catch (NumberFormatException e) {
            afficherErreur("Le stock doit être un nombre valide !");
            return;
        }
        
        // Créer et ajouter la fourniture
        Fourniture nouvelleFourniture = new Fourniture(
            libelleTextField.getText().trim(),
            descriptionTextArea.getText().trim(),
            stock
        );
        
        if (fournitureRepository.ajouterFourniture(nouvelleFourniture)) {
            afficherSucces("Fourniture ajoutée avec succès !");
            chargerDonnees();
            viderFormulaire();
        } else {
            afficherErreur("Erreur lors de l'ajout de la fourniture.");
        }
    }

    @FXML
    private void handleModifier() {
        messageLabel.setText("");
        
        if (fournitureSelectionnee == null) {
            afficherErreur("Veuillez sélectionner une fourniture !");
            return;
        }
        
        if (libelleTextField.getText().trim().isEmpty()) {
            afficherErreur("Veuillez saisir le libellé de la fourniture !");
            return;
        }
        
        if (stockTextField.getText().trim().isEmpty()) {
            afficherErreur("Veuillez saisir le stock !");
            return;
        }
        
        int stock;
        try {
            stock = Integer.parseInt(stockTextField.getText().trim());
            if (stock < 0) {
                afficherErreur("Le stock ne peut pas être négatif !");
                return;
            }
        } catch (NumberFormatException e) {
            afficherErreur("Le stock doit être un nombre valide !");
            return;
        }
        
        // Mettre à jour l'objet
        fournitureSelectionnee.setLibelle(libelleTextField.getText().trim());
        fournitureSelectionnee.setDescription(descriptionTextArea.getText().trim());
        fournitureSelectionnee.setStockActuel(stock);
        
        if (fournitureRepository.mettreAJourFourniture(fournitureSelectionnee)) {
            afficherSucces("Fourniture modifiée avec succès !");
            chargerDonnees();
            viderFormulaire();
        } else {
            afficherErreur("Erreur lors de la modification de la fourniture.");
        }
    }

    @FXML
    private void handleSupprimer() {
        if (fournitureSelectionnee == null) {
            afficherErreur("Veuillez sélectionner une fourniture !");
            return;
        }
        
        confirmerSuppression(fournitureSelectionnee);
    }

    @FXML
    private void handleAjouterStock() {
        if (fournitureSelectionnee == null) {
            afficherErreur("Veuillez sélectionner une fourniture !");
            return;
        }
        
        ajouterStockRapide(fournitureSelectionnee);
    }

    @FXML
    private void handleRetirerStock() {
        if (fournitureSelectionnee == null) {
            afficherErreur("Veuillez sélectionner une fourniture !");
            return;
        }
        
        retirerStockRapide(fournitureSelectionnee);
    }

    private void ajouterStockRapide(Fourniture fourniture) {
        TextInputDialog dialog = new TextInputDialog("10");
        dialog.setTitle("Ajouter au stock");
        dialog.setHeaderText("Ajouter au stock de : " + fourniture.getLibelle());
        dialog.setContentText("Quantité à ajouter :");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(quantiteStr -> {
            try {
                int quantite = Integer.parseInt(quantiteStr);
                if (quantite > 0) {
                    if (fournitureRepository.ajouterAuStock(fourniture.getIdFourniture(), quantite)) {
                        afficherSucces("Stock augmenté de " + quantite + " unités !");
                        chargerDonnees();
                        viderFormulaire();
                    } else {
                        afficherErreur("Erreur lors de l'ajout au stock.");
                    }
                } else {
                    afficherErreur("La quantité doit être positive !");
                }
            } catch (NumberFormatException e) {
                afficherErreur("Quantité invalide !");
            }
        });
    }

    private void retirerStockRapide(Fourniture fourniture) {
        TextInputDialog dialog = new TextInputDialog("10");
        dialog.setTitle("Retirer du stock");
        dialog.setHeaderText("Retirer du stock de : " + fourniture.getLibelle());
        dialog.setContentText("Quantité à retirer :");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(quantiteStr -> {
            try {
                int quantite = Integer.parseInt(quantiteStr);
                if (quantite > 0) {
                    if (fournitureRepository.retirerDuStock(fourniture.getIdFourniture(), quantite)) {
                        afficherSucces("Stock diminué de " + quantite + " unités !");
                        chargerDonnees();
                        viderFormulaire();
                    } else {
                        afficherErreur("Stock insuffisant ou erreur !");
                    }
                } else {
                    afficherErreur("La quantité doit être positive !");
                }
            } catch (NumberFormatException e) {
                afficherErreur("Quantité invalide !");
            }
        });
    }

    private void confirmerSuppression(Fourniture fourniture) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer la fourniture : " + fourniture.getLibelle());
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette fourniture ?\nStock actuel : " + fourniture.getStockActuel() + " unités");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (fournitureRepository.supprimerFourniture(fourniture.getIdFourniture())) {
                    afficherSucces("Fourniture supprimée avec succès !");
                    chargerDonnees();
                    viderFormulaire();
                } else {
                    afficherErreur("Erreur lors de la suppression de la fourniture.");
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
            fournituresList.setAll(fournitureRepository.rechercherFournitureParLibelle(recherche));
            fournitureTableView.setItems(fournituresList);
            mettreAJourCompteur();
        }
    }

    @FXML
    private void handleStockFaible() {
        fournituresList.setAll(fournitureRepository.getFournituresEnRupture(10));
        fournitureTableView.setItems(fournituresList);
        mettreAJourCompteur();
        
        if (fournituresList.isEmpty()) {
            afficherSucces("Aucune fourniture en stock faible !");
        } else {
            afficherErreur(fournituresList.size() + " fourniture(s) en stock faible !");
        }
    }

    @FXML
    private void handleActualiser() {
        chargerDonnees();
        viderFormulaire();
        afficherSucces("Données actualisées !");
    }


    private void chargerDonnees() {
        fournituresList.setAll(fournitureRepository.getToutesLesFournitures());
        fournitureTableView.setItems(fournituresList);
        mettreAJourCompteur();
        statutLabel.setText("Dernière actualisation : " + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    private void remplirFormulaire(Fourniture fourniture) {
        libelleTextField.setText(fourniture.getLibelle());
        descriptionTextArea.setText(fourniture.getDescription());
        stockTextField.setText(String.valueOf(fourniture.getStockActuel()));
        ajouterBtn.setDisable(true);
    }

    private void viderFormulaire() {
        libelleTextField.clear();
        descriptionTextArea.clear();
        stockTextField.clear();
        fournitureSelectionnee = null;
        ajouterBtn.setDisable(false);
        modifierBtn.setDisable(true);
        supprimerBtn.setDisable(true);
        ajouterStockBtn.setDisable(true);
        retirerStockBtn.setDisable(true);
        fournitureTableView.getSelectionModel().clearSelection();
        messageLabel.setText("");
    }

    private void mettreAJourCompteur() {
        int count = fournituresList.size();
        compteurLabel.setText("(" + count + " fourniture" + (count > 1 ? "s" : "") + ")");
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
    private void handleRetour() {
        // Fermer la fenêtre actuelle
        javafx.stage.Stage stage = (javafx.stage.Stage) retourBtn.getScene().getWindow();
        stage.close();
    }
}
