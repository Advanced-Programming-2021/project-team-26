package fxmlController;

import Utilities.GetFXML;
import controller.Database;
import controller.ScoreBoardController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Scoreboard extends MenuParent implements Initializable {

    @FXML
    public TableColumn<ScoreBoardController, Boolean> isOnline;
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
        Parent root = GetFXML.getFXML("scoreboard");
        this.scene = new Scene(root, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue());
        App.pushMenu(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        rank.setCellValueFactory(new PropertyValueFactory<>("rank"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        score.setCellValueFactory(new PropertyValueFactory<>("score"));
        isOnline.setCellValueFactory(new PropertyValueFactory<>("isOnline"));
        ObservableList<ScoreBoardController> list = FXCollections.observableArrayList(ScoreBoardController.getAndSetDataFromUser());
        customiseFactory(name);
        table.setItems(list);
    }

    private void customiseFactory(TableColumn<ScoreBoardController, String> cell) {
        cell.setCellFactory(column -> {
            return new TableCell<ScoreBoardController, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    setText(empty ? "" : getItem());
                    setGraphic(null);

                    TableRow<ScoreBoardController> currentRow = getTableRow();

                    if (!isEmpty()) {
                        if (item.equals(Database.getInstance().getCurrentUser().getUsername()))
                            currentRow.setStyle("-fx-background-color:green");
                    }
                }
            };
        });
    }
}