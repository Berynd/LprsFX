package appli.ficheetudiante;

import appli.StartApplication;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import session.sessionUtilisateur;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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