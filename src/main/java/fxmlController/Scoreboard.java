package fxmlController;

import controller.ScoreBoardController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.util.ArrayList;

import static fxmlController.Welcome.getFXML;

public class Scoreboard {
    public void run() throws IOException {
        Stage primaryStage = Welcome.getStage();
        Parent root = getFXML("scoreboard");
        primaryStage.setTitle("Score Board");
        primaryStage.setScene(new Scene(root, Welcome.WIDTH, Welcome.HEIGHT));
        primaryStage.show();

        ArrayList<User> sortedUsers = new ScoreBoardController().sortUsersBasedScore();
        showScoreBoard(sortedUsers);
    }

    @FXML
    private TableView<?> table;

    @FXML
    private TableColumn<?, ?> rank;

    @FXML
    private TableColumn<?, ?> name;

    @FXML
    private TableColumn<?, ?> score;

    private void showScoreBoard(ArrayList<User> sortedUsers) {
        //TODO
    }
}
