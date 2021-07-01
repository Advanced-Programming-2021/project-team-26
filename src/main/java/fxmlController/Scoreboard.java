package fxmlController;

import controller.ScoreBoardController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static fxmlController.Welcome.getFXML;

public class Scoreboard implements Initializable {

    @FXML
    private TableView<ScoreBoardController> table;
    @FXML
    private TableColumn<ScoreBoardController, Integer> rank;
    @FXML
    private TableColumn<ScoreBoardController, String> name;
    @FXML
    private TableColumn<ScoreBoardController, Integer> score;

    public void run() throws IOException {
        Stage primaryStage = Welcome.getStage();
        Parent root = getFXML("scoreboard");
        primaryStage.setTitle("Score Board");
        primaryStage.setScene(new Scene(root, Welcome.WIDTH, Welcome.HEIGHT));
        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        rank.setCellValueFactory(new PropertyValueFactory<>("rank"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        score.setCellValueFactory(new PropertyValueFactory<>("score"));
        ScoreBoardController.getAndSetDataFromUser();
        ObservableList<ScoreBoardController> list = FXCollections.observableArrayList(ScoreBoardController.getScoreBoardControllers());
        table.setItems(list);
    }
}
