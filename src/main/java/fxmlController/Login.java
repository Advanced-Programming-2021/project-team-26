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

public class Login extends MenuParent {
    @FXML
    private TextField password;
    @FXML
    private TextField username;

    public Login() {
        super("Login page");
    }

    public void run() throws IOException {
        Parent root = getFXML("login");
        this.scene = new Scene(root, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue());
        App.pushMenu(this);
    }

    @FXML
    void login(ActionEvent event) {
        try {
            boolean result = UserController.getInstance().loginUser(username.getText(), password.getText());
            if (result) Printt.getInstance().successfulPrint("User logged successfully");
            new MainMenu().run();
        } catch (Exception e) {
            Printt.getInstance().errorPrint(e.getMessage());
        }
    }
}
