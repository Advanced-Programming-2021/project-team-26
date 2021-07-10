package fxmlController.Children;

import Utilities.GetFXML;
import controller.GameController;
import exceptions.NoPlayerAvailable;
import fxmlController.App;
import fxmlController.MenuParent;
import fxmlController.Size;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import model.User;

import java.io.IOException;

public class Welcome extends MenuParent {
    public static MediaPlayer buttonClick = new MediaPlayer(new Media(MainMenu.class.getResource("/Assets/Sounds/buttonClick.mp3").toExternalForm()));

    public Welcome() {
        super("Welcome to Yu Gi Oh!");
    }

    @Override
    public void run() throws IOException {
        Parent root = GetFXML.getFXML("welcome");
        scene = new Scene(root, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue());
        App.pushMenu(this);
    }


    @FXML
    void openLoginPage(ActionEvent event) throws IOException {
        new Login().run();
    }

    @FXML
    void openSignUpPage(ActionEvent event) throws IOException {
        new SignUp().run();
    }

    @FXML
    void newGame(ActionEvent event) throws NoPlayerAvailable {
        new GameController(User.getUserByUsername("c"), User.getUserByUsername("b"), 1).run();
    }
}