package fxmlController.Children;

import Utilities.GetFXML;
import fxmlController.App;
import fxmlController.MenuParent;
import fxmlController.Size;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class Welcome extends MenuParent {
    private static final Image play = new Image("file:" + System.getProperty("user.dir") + "/src/main/resources/Assets/play.jpg");
    private static final Image pause = new Image("file:" + System.getProperty("user.dir") + "/src/main/resources/Assets/pause.jpg");

    @FXML
    private ImageView playAndPause;

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

    public void muteOrPlay(MouseEvent mouseEvent) {
        if (App.isAreSoundsActive()) {
            App.mediaView.getMediaPlayer().pause();
            playAndPause.setImage(pause);
        } else {
            App.mediaView.getMediaPlayer().play();
            playAndPause.setImage(play);
        }
        App.setAreSoundsActive(!App.isAreSoundsActive());
    }
}