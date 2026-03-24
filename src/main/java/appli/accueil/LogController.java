package appli.accueil;

import appli.StartApplication;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idLog"));
        heureColumn.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(c.getValue().getDateHeure().format(FMT)));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("nomUtilisateur"));
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("messageLog"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        pageColumn.setCellValueFactory(new PropertyValueFactory<>("page"));

        logTable.setItems(FXCollections.observableArrayList(logRepository.getTousLesLogs()));
    }

    @FXML
    public void handleRetour(ActionEvent event) {
        StartApplication.goBack();
    }
}
