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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static fxmlController.App.getFXML;

public class Scoreboard extends MenuParent implements Initializable {

    @FXML
    private TableView<ScoreBoardController> table;
    @FXML
    private TableColumn<ScoreBoardController, Integer> rank;
    @FXML
    private TableColumn<ScoreBoardController, String> name;
    @FXML
    private TableColumn<ScoreBoardController, Integer> score;

    public Scoreboard() {
        super("Score Board");
    }

    public void run() throws IOException {
        Parent root = getFXML("scoreboard");
        this.scene = new Scene(root, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue());
        App.pushMenu(this);
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
