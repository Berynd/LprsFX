package appli;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class StartApplication extends Application {
    private static Stage mainStage;
    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(
                StartApplication.class.getResource("accueil/LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        mainStage.setTitle("cailloux");
        mainStage.setScene(scene);

        // 🔹 Plein écran forcé
        mainStage.setMaximized(true);
        mainStage.setFullScreenExitHint("");
        mainStage.setFullScreenExitKeyCombination(javafx.scene.input.KeyCombination.NO_MATCH);

        mainStage.show();
    }

    public static void changeScene(String nomDuFichierFxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                StartApplication.class.getResource(nomDuFichierFxml + "View.fxml"));

        mainStage.getScene().setRoot(loader.load());
    }

    public static void main(String[] args) {
        launch();
    }
}