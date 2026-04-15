package appli;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Point d'entrée de l'application JavaFX LprsFX.
 *
 * Gère le cycle de vie de la fenêtre principale et la navigation entre les vues.
 * Un historique de navigation (pile) permet de revenir à la page précédente
 * via goBack(), à la manière d'un bouton "retour" de navigateur.
 *
 * Toutes les vues FXML se trouvent dans src/main/resources/appli/accueil/.
 * Pour naviguer, utiliser : StartApplication.changeScene("accueil/NomDeLaVue")
 */
public class StartApplication extends Application {

    /** Fenêtre principale de l'application (une seule instance). */
    private static Stage mainStage;

    /** Pile des pages visitées, pour permettre le retour arrière. */
    private static final Deque<String> history = new ArrayDeque<>();

    /** Nom de la page actuellement affichée (sans le suffixe "View.fxml"). */
    private static String currentPage = null;

    /**
     * Point d'entrée JavaFX. Charge la vue de connexion et affiche la fenêtre.
     */
    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;

        // Chargement de la vue de connexion au démarrage
        FXMLLoader fxmlLoader = new FXMLLoader(
                StartApplication.class.getResource("accueil/LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        mainStage.setTitle("LprsFX");
        mainStage.setScene(scene);

        // Démarrage en plein écran, sans possibilité d'en sortir par une touche
        mainStage.setMaximized(true);
        mainStage.setFullScreenExitHint("");
        mainStage.setFullScreenExitKeyCombination(javafx.scene.input.KeyCombination.NO_MATCH);

        mainStage.show();
    }

    /**
     * Navigue vers une nouvelle vue en remplaçant le contenu de la scène.
     * La page courante est empilée dans l'historique pour permettre un retour.
     *
     * @param nomDuFichierFxml chemin relatif de la vue, sans le suffixe "View.fxml"
     *                         (ex : "accueil/Login", "accueil/FicheEtudiante")
     * @throws IOException si le fichier FXML est introuvable ou mal formé
     */
    public static void changeScene(String nomDuFichierFxml) throws IOException {
        // Sauvegarde de la page courante dans l'historique avant de changer
        if (currentPage != null) {
            history.push(currentPage);
        }
        currentPage = nomDuFichierFxml;
        FXMLLoader loader = new FXMLLoader(
                StartApplication.class.getResource(nomDuFichierFxml + "View.fxml"));
        mainStage.getScene().setRoot(loader.load());
    }

    /**
     * Revient à la page précédente dans l'historique de navigation.
     * Si l'historique est vide (on est sur la première page), ne fait rien.
     * Les erreurs de chargement sont loguées mais ne font pas planter l'application.
     */
    public static void goBack() {
        if (!history.isEmpty()) {
            try {
                currentPage = history.pop();
                FXMLLoader loader = new FXMLLoader(
                        StartApplication.class.getResource(currentPage + "View.fxml"));
                mainStage.getScene().setRoot(loader.load());
            } catch (IOException e) {
                System.err.println("Erreur lors du retour à la page précédente : " + e.getMessage());
            }
        }
    }

    /** Point d'entrée Java principal — lance l'application JavaFX. */
    public static void main(String[] args) {
        launch();
    }
}
