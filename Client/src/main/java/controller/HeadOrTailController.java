package controller;

import Utilities.GetFXML;
import exceptions.NoPlayerAvailable;
import fxmlController.App;
import fxmlController.HeadOrTail;
import fxmlController.SimpleMenu;
import fxmlController.Size;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import model.Request;
import model.Response;
import model.User;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class HeadOrTailController {
    int winner = -1;
    int starter;
    User[] players = new User[2];
    HeadOrTail graphic;
    SimpleMenu menu;
    int round;
    int myTurn;
    boolean[] isSignSet = new boolean[]{false, false};

    public HeadOrTailController(User firstUser, User secondUser, int round, int turn) {
        players[0] = firstUser;
        players[1] = secondUser;
        this.myTurn = turn;

        graphic = new HeadOrTail(this, myTurn);
        menu = new SimpleMenu("Head or Tail");
        this.round = round;

    }

    public void run() {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(type -> {
            try {
                if (type == HeadOrTail.class) {
                    return graphic;
                }
                return type.newInstance();
            } catch (Exception exc) {
                throw new RuntimeException(exc);
            }
        });
        loader.setLocation(HeadOrTailController.class.getResource("/fxml/" + "headOrTail" + ".fxml"));

        try {
            Parent root = loader.load();
            menu.setScene(new Scene(root));
            App.pushMenu(menu, false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSign(int turn) {
        Request request = new Request("HeadOrTail", "throwCoin");
        Response response = NetworkController.getInstance().sendAndReceive(request);
        return response.getMessage();
    }

    public void startThrowCoin() {
        Response response = NetworkController.getInstance().getResponse();
        Platform.runLater(graphic::playGif);
        int result = Integer.parseInt(response.getMessage());

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    winner = result;
                    graphic.setResult(result);

                    if (winner == myTurn) {
                        try {
                            AnchorPane root = (AnchorPane) GetFXML.getFXML("whoStartsGame");
                            Button iStartButton = (Button) root.lookup("#iStartButton");
                            Button opStartButton = (Button) root.lookup("#opStartButton");

                            Request request = new Request("", "");
                            iStartButton.setOnAction(e -> {
                                starter = winner;
                                request.addParameter("starter", String.valueOf(starter));
                                NetworkController.getInstance().sendRequest(request);
                                startGame();
                            });

                            opStartButton.setOnAction(e -> {
                                starter = 1 - winner;
                                request.addParameter("starter", String.valueOf(starter));
                                NetworkController.getInstance().sendRequest(request);
                                startGame();
                            });
                            App.popMenu();
                            menu.setScene(new Scene(root, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue()));
                            App.pushMenu(menu, false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Response response1 = NetworkController.getInstance().getResponse();
                        starter = Integer.parseInt(response1.getData("starter"));
                        startGame();
                    }
                });
            }
        }, 2000);
    }

    private void startGame() {
        App.popMenu();

        try {
            if (starter == myTurn)
                myTurn = 0;
            else
                myTurn = 1;
            new GameController(players[starter], players[1 - starter], round, myTurn).run();
        } catch (NoPlayerAvailable noPlayerAvailable) {
            noPlayerAvailable.printStackTrace();
        }
    }
}
