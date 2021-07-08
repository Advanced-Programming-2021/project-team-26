package controller;

import fxmlController.App;
import fxmlController.HeadOrTail;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.util.Random;

public class HeadOrTailController {
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
                if (type == HeadOrTail.class) {
                    return graphics[1];
                }
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

        graphics[0].initialize();
        graphics[1].initialize();
    }

    public String getSign(int turn) {
        String result;
        if (turn == 0) {
            isSignSet[0] = true;
            result = "#";
        } else {
            isSignSet[1] = true;
            result = "B";
        }

        if (isSignSet[0] && isSignSet[1]) {
            startThrowCoin();
        }

        return result;
    }

    private void startThrowCoin() {
        graphics[0].playGif();
        graphics[1].playGif();

        System.out.println("im here");

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("im there");

        int result = new Random().nextInt(2);

        graphics[0].setResult(result);
        graphics[1].setResult(result);

        System.out.println("im over there");
    }
}
