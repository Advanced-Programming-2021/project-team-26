package controller;

import Utilities.Alert;
import Utilities.GetFXML;
import com.google.gson.Gson;
import fxmlController.App;
import fxmlController.MenuParent;
import fxmlController.Size;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import model.Request;
import model.Response;
import model.User;

public class MainMenuController {
    private static MainMenuController instance;
    private static boolean cancelGame;

    private MainMenuController() {

    }

    public static MainMenuController getInstance() {
        if (instance == null)
            instance = new MainMenuController();
        return instance;
    }

    public static void stopWaiting(ActionEvent actionEvent) {
        cancelGame = true;
        Platform.runLater(() -> App.popMenu());
    }

    public void creatNewGameWithAI(Integer round) {
        //TODO load duel menu
    }

    public void createNewGameWithRealPlayer(int round) {
        Request request = new Request("MainMenuController", "newGame");
        request.addParameter("round", String.valueOf(round));

        Response response = NetworkController.getInstance().sendAndReceive(request);
        if (!response.isSuccess())
            throw new RuntimeException(response.getMessage());

        cancelGame = false;
        loadWaitingPage();

        new Thread(() -> waitForOpponent(round)).start();
    }

    private void waitForOpponent(int round) {
        while (true) {
            if (cancelGame) {
                Request request = new Request("MainMenuController", "cancelGame");
                Response response = NetworkController.getInstance().sendAndReceive(request);
                if (response.isSuccess())
                    break;
                else {
                    cancelGame = false;
                    Alert.getInstance().errorPrint(response.getMessage());
                }
            }
            Request request = new Request("MainMenuController", "foundPlayer");
            Response response = NetworkController.getInstance().sendAndReceive(request);

            if (response.isSuccess()) {
                int turn = Integer.parseInt(response.getData("turn"));
                User[] users = new User[2];
                users[turn] = Database.getInstance().getCurrentUser();
                users[1 - turn] = new Gson().fromJson(response.getData("user"), User.class);
                stopWaiting(null);
                Platform.runLater(() -> new HeadOrTailController(users[0], users[1], round, turn).run());
                break;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Platform.runLater(App::popMenu);
    }

    private void loadWaitingPage() {

        MenuParent menu = new MenuParent("Waiting Page") {
            @Override
            public void run() throws Exception {
                Parent root = GetFXML.getFXML("waitingPage");
                Button cancel = (Button) root.lookup("#cancel");
                cancel.setOnAction(MainMenuController::stopWaiting);
                this.scene = new Scene(root, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue());
                App.pushMenu(this, false);
            }
        };
        try {
            menu.run();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
