package controller;

import Utilities.GetFXML;
import com.google.gson.Gson;
import exceptions.NoPlayerAvailable;
import exceptions.NotSupportedRoundNumber;
import fxmlController.App;
import fxmlController.Size;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.Request;
import model.Response;
import model.User;

import java.io.IOException;
import java.util.Objects;

public class MainMenuController {
    private static MainMenuController instance;

    private MainMenuController() {

    }

    public static MainMenuController getInstance() {
        if (instance == null)
            instance = new MainMenuController();
        return instance;
    }

    public void creatNewGameWithAI(Integer round) {
        if (round != 1 && round != 3)
            throw new NotSupportedRoundNumber();

        GameController gameController = null;
        try {
            gameController = new GameController(Database.getInstance().getCurrentUser(), round);
        } catch (NoPlayerAvailable ignored) {

        }
        //TODO load duel menu
    }

    public void createNewGameWithRealPlayer(int round) {
        Request request = new Request("MainMenuController", "newGame");
        request.addParameter("round", String.valueOf(round));

        Response response = NetworkController.getInstance().sendAndReceive(request);
        if (!response.isSuccess())
            throw new RuntimeException(response.getMessage());

        int turn = Integer.parseInt(response.getData("turn"));
        if (response.getData("user") != null) {
            User first = Database.getInstance().getCurrentUser();
            User second = new Gson().fromJson(response.getData("user"), User.class);
            new HeadOrTailController(first, second, round, turn).run();
            return;
        }

        loadWaitingPage();

        while (true) {
            request = new Request("MainMenuController", "foundPlayer");
            response = NetworkController.getInstance().sendAndReceive(request);

            if (response.isSuccess()) {
                User first = Database.getInstance().getCurrentUser();
                User second = new Gson().fromJson(response.getData("user"), User.class);
                new HeadOrTailController(first, second, round, turn).run();
                return;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadWaitingPage() {
        Stage stage = App.getStage();
        Parent root = null;
        try {
            root = GetFXML.getFXML("waitingPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Waiting Page");
        stage.setScene(new Scene(root, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue()));
        stage.show();
    }

    public void stopWaiting(ActionEvent actionEvent) {

    }

}
