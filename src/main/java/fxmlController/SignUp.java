package fxmlController;

import controller.UserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import view.Printt;

import java.io.IOException;

import static fxmlController.Welcome.getFXML;

public class SignUp {
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField nickname;

    public void run() throws IOException {
        Stage primaryStage = Welcome.getStage();
        Parent root = getFXML("signUp");
        primaryStage.setTitle("Sign up page");
        primaryStage.setScene(new Scene(root, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue()));
        primaryStage.show();
    }

    @FXML
    void signup(ActionEvent event) {
        try {
           boolean result = UserController.getInstance().addNewUser(username.getText(), password.getText(), nickname.getText());
           if (result) Printt.getInstance().successfulPrint("User created successfully");
            //TODO load login page

        }catch (Exception e){
            Printt.getInstance().errorPrint(e.getMessage());
        }
    }
}