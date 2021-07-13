package controller;

import Utilities.GetFXML;
import exceptions.NoPlayerAvailable;
import fxmlController.App;
import fxmlController.HeadOrTail;
import fxmlController.Size;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class HeadOrTailController {
    int winner = -1;
    int starter;
    User[] players = new User[2];
    HeadOrTail[] graphics = new HeadOrTail[2];
    Stage[] stages = new Stage[2];
    int round;
    boolean[] isSignSet = new boolean[]{false, false};

    public HeadOrTailController(User firstUser, User secondUser, int round) {
        players[0] = firstUser;
        players[1] = secondUser;

        graphics[0] = new HeadOrTail(this, 0);
        graphics[1] = new HeadOrTail(this, 1);

        stages[0] = new Stage();
        stages[1] = new Stage();

        this.round = round;
    }

    public void run() {
        FXMLLoader firstLoader = new FXMLLoader();
        firstLoader.setControllerFactory(type -> {
            try {
                if (type == HeadOrTail.class) {
                    return graphics[0];
                }
                return type.newInstance();
            } catch (Exception exc) {
                throw new RuntimeException(exc);
            }
        });
        firstLoader.setLocation(HeadOrTailController.class.getResource("/fxml/" + "headOrTail" + ".fxml"));

        FXMLLoader secondLoader = new FXMLLoader();
        secondLoader.setControllerFactory(type -> {
            try {
                if (type == HeadOrTail.class)
                    return graphics[1];

                return type.newInstance();
            } catch (Exception exc) {
                throw new RuntimeException(exc);
            }
        });
        secondLoader.setLocation(HeadOrTailController.class.getResource("/fxml/" + "headOrTail" + ".fxml"));

        try {
            Parent firstRoot = firstLoader.load();
            Parent secondRoot = secondLoader.load();

            stages[0].setScene(new Scene(firstRoot));
            stages[1].setScene(new Scene(secondRoot));
            App.getStage().close();
            stages[0].show();
            stages[1].show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSign(int turn) {
        String result;
        if (turn == 0) {
            isSignSet[0] = true;
            result = "B";
        } else {
            isSignSet[1] = true;
            result = "#";
        }

        if (isSignSet[0] && isSignSet[1])
            startThrowCoin();

        return result;
    }

    private void startThrowCoin() {
        graphics[0].playGif();
        graphics[1].playGif();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    int result = ThreadLocalRandom.current().nextInt(2);
                    winner = result;

                    graphics[0].setResult(result);
                    graphics[1].setResult(result);

                    try {
                        AnchorPane root = (AnchorPane) GetFXML.getFXML("whoStartsGame");
                        Button iStartButton = (Button) root.lookup("#iStartButton");
                        Button opStartButton = (Button) root.lookup("#opStartButton");

                        iStartButton.setOnAction(e -> {
                            starter = winner;
                            startGame();
                        });

                        opStartButton.setOnAction(e -> {
                            starter = 1 - winner;
                            startGame();
                        });
                        stages[winner].setScene(new Scene(root, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }, 2000);
    }

    private void startGame() {
        stages[0].close();
        stages[1].close();

        App.getStage().show();

        try {
            new GameController(players[starter], players[1 - starter], round).run();
        } catch (NoPlayerAvailable noPlayerAvailable) {
            noPlayerAvailable.printStackTrace();
        }
    }
}
