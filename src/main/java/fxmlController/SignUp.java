package fxmlController;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static fxmlController.Welcome.getFXML;

public class SignUp {
    public void run() throws IOException {
        Stage primaryStage = Welcome.getStage();
        Parent root = getFXML("signUp");
        primaryStage.setTitle("Sign up page");
        primaryStage.setScene(new Scene(root, Welcome.WIDTH, Welcome.HEIGHT));
        primaryStage.show();
    }
}
