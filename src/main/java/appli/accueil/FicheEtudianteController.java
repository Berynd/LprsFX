package appli.accueil;

import appli.StartApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Etudiant;
import model.Salle;
import repository.EtudiantRepository;
import java.io.IOException;

public class FicheEtudianteController {

    @FXML
    private TextField nomTextField;
    @FXML
    private TextField rechercheTextField;
    @FXML
    private Label messageLabel;
    @FXML
    private Label compteurLabel;
    @FXML
    private Label statutLabel;
    @FXML
    private Button ajouterBtn;
    @FXML
    private Button modifierBtn;
    @FXML
    private Button supprimerBtn;
    @FXML
    private Button annulerBtn;
    @FXML
    private Button retourBtn;
    @FXML
    private TableView<Salle> salleTableView;
    @FXML
    private TableColumn<Salle, Integer> idColumn;
    @FXML
    private TableColumn<Salle, String> nomColumn;
    @FXML
    private TableColumn<Salle, Void> actionsColumn;
    private Etudiant etudiantSelectionnee;

    public TextField emailField;
    public TextField nomField;
    public TextField prenomField;
    public TextField telephoneField;
    public TextArea adresseArea;
    public TextField diplomeField;
    public Button btnEffacer;
    public Button btnEnregistrer;
    public Button btnRetour;

}