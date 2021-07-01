package fxmlController;

import controller.UserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import view.Printt;

import java.io.IOException;

import static fxmlController.Welcome.getFXML;

public class Login {
    public void run() throws IOException {
        Stage primaryStage = Welcome.getStage();
        Parent root = getFXML("login");
        primaryStage.setTitle("Login page");
        primaryStage.setScene(new Scene(root, Welcome.WIDTH, Welcome.HEIGHT));
        primaryStage.show();
    }

    @FXML
    private TextField password;

    @FXML
    private TextField username;

    @FXML
    void login(ActionEvent event) {
        try {
            boolean result = UserController.getInstance().loginUser(username.getText(), password.getText());
            if (result) Printt.getInstance().successfulPrint("User logged successfully");
            new MainMenu().run();
        }catch (Exception e){
            Printt.getInstance().errorPrint(e.getMessage());
        }
    }
}
