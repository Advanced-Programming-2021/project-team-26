package fxmlController;

import controller.UserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import view.Printt;

import java.io.IOException;

import static fxmlController.App.getFXML;

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
        Parent root = getFXML("signUp");
        this.scene = new Scene(root, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue());
        App.pushMenu(this);
    }

    @FXML
    void signup(ActionEvent event) {
        try {
            boolean result = UserController.getInstance().addNewUser(username.getText(), password.getText(), nickname.getText());
            if (result) {
                Printt.getInstance().successfulPrint("User created successfully");
                //TODO load login page
                App.popMenu();
                new Login().run();
            }
        }catch (Exception e){
            Printt.getInstance().errorPrint(e.getMessage());
        }
    }
}