package appli.accueil;

import appli.StartApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Etudiant;
import repository.EtudiantRepository;
import java.io.IOException;

public class FicheEtudianteController {

    public TextField emailField;
    public TextField nomField;
    public TextField prenomField;
    public TextField telephoneField;
    public TextArea adresseArea;
    public TextField diplomeField;
    public Button btnEffacer;
    public Button btnEnregistrer;
    public Button btnRetour;


    @FXML
public void retour(ActionEvent actionEvent) throws IOException {
    StartApplication.changeScene("accueil/Accueil");
}
}