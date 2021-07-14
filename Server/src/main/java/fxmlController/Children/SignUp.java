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
import model.Response;

import java.io.IOException;


public class SignUp extends MenuParent {
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField nickname;

    public SignUp() {
        super("Sign up page");
    }

    public void run() throws IOException {
        Parent root = GetFXML.getFXML("signUp");
        scene = new Scene(root, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue());
        App.pushMenu(this);
    }

    @FXML
    void signup(ActionEvent event) {
        try {
            boolean result = UserController.getInstance().addNewUser(username.getText(), password.getText(), nickname.getText());
            if (result) {
                Alert.getInstance().successfulPrint("User created successfully");
                //TODO load login page
                App.popMenu();
                new Login().run();
            }
        }catch (Exception e){
            Alert.getInstance().errorPrint(e.getMessage());
        }
    }
}