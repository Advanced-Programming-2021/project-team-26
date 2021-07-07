package fxmlController.Children;

import Utilities.GetFXML;
import fxmlController.App;
import fxmlController.MenuParent;
import fxmlController.Size;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class Welcome extends MenuParent {

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
}