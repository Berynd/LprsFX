package appli.accueil;

import appli.StartApplication;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Log;
import repository.LogRepository;

import java.time.format.DateTimeFormatter;

public class LogController {

    @FXML private TableView<Log> logTable;
    @FXML private TableColumn<Log, Integer> idColumn;
    @FXML private TableColumn<Log, String> heureColumn;
    @FXML private TableColumn<Log, String> userColumn;
    @FXML private TableColumn<Log, String> messageColumn;
    @FXML private TableColumn<Log, String> actionColumn;
    @FXML private TableColumn<Log, String> pageColumn;

    private final LogRepository logRepository = new LogRepository();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleIntegerProperty(c.getValue().getIdLog()).asObject());
        heureColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getDateHeure().format(FMT)));
        userColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getNomUtilisateur()));
        messageColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getMessageLog()));
        actionColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getAction()));
        pageColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getPage()));

        logTable.setItems(FXCollections.observableArrayList(logRepository.getTousLesLogs()));
    }

    @FXML
    public void handleRetour(ActionEvent event) {
        StartApplication.goBack();
    }
}
