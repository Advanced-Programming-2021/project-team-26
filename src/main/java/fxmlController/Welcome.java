package fxmlController;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Welcome extends Application {
    public final static int WIDTH = 1280;
    public final static int HEIGHT = 720;
    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Parent getFXML(String name) throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(Welcome.class.getResource("/fxml/" + name + ".fxml")));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        Parent root = getFXML("welcome");
        primaryStage.setTitle("Welcome to Yu Gi Oh!");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @FXML
    void openLoginPage(ActionEvent event) {
    }

    @FXML
    void openSignUpPage(ActionEvent event) throws IOException {
        new SignUp().run();
    }
}
