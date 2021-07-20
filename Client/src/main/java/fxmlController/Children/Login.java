package fxmlController.Children;

import Utilities.GetFXML;
import controller.UserController;
import fxmlController.App;
import fxmlController.MenuParent;
import fxmlController.Size;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import Utilities.Alert;

import java.io.IOException;

//import static fxmlController.App.buttonClick;

public class Login extends MenuParent {
    @FXML
    private TextField password;
    @FXML
    private TextField username;

    public Login() {
        super("Login page");
    }

    public void run() throws IOException {
        Parent root = GetFXML.getFXML("login");
        this.scene = new Scene(root, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue());
        App.pushMenu(this);
    }

    @FXML
    void login(ActionEvent event) {
//        if (App.isAreSoundsActive()) buttonClick.play();
        try {
            boolean result = UserController.getInstance().loginUser(username.getText(), password.getText());
            if (result) {
                Alert.getInstance().successfulPrint("User logged successfully");
                App.popMenu();
                new MainMenu().run();
            }
        } catch (Exception e) {
            Alert.getInstance().errorPrint(e.getMessage());
        }
    }
}

