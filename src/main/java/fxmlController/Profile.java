package fxmlController;

import controller.Database;
import controller.UserController;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.User;
import view.Printt;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class Profile {
    public TextField usernameText;
    public TextField nicknameText;
    public ImageView profileImage;

    public void run() throws IOException {
        Parent root = Welcome.getFXML("profile");
        Scene scene = new Scene(root);
        Welcome.getStage().setScene(scene);
    }

    public void initialize() {
        User currentUser = Database.getInstance().getCurrentUser();
        usernameText.setText(currentUser.getUsername());
        nicknameText.setText(currentUser.getNickname());
        nicknameText.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                UserController.getInstance().changeNickname(newValue);
                nicknameText.setStyle("");
            } catch (Exception e) {
                nicknameText.setStyle("-fx-text-fill: red;");
            }
        });
        String imagePath = currentUser.getProfileImagePath();
        Image image = new Image(imagePath);
        profileImage.setImage(image);
    }

    public void changeProfile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("choose character");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("image", "*.png", "*.jpg"));
        File file = fileChooser.showOpenDialog(Welcome.getStage());
        if (file != null) {
            try {
                profileImage.setImage(new Image(file.toURL().toExternalForm()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Printt.getInstance().errorPrint("loading image failed");
            }
        } else {
            Printt.getInstance().errorPrint("loading image failed");
        }
    }

    public void changePassword() {
        Stage stage = new Stage();

        Button change = new Button("change");
        PasswordField oldPassword = new PasswordField();
        oldPassword.setPromptText("old password");
        PasswordField newPassword = new PasswordField();
        newPassword.setPromptText("new password");
        PasswordField repeatPassword = new PasswordField();
        repeatPassword.setPromptText("repeat password");
        change.setOnAction(event -> {
            try {
                UserController.getInstance().changePassword(oldPassword.getText(), newPassword.getText(), repeatPassword.getText());
                Printt.getInstance().successfulPrint("password changed successfully!");
                stage.close();
            } catch (Exception e) {
                Printt.getInstance().errorPrint(e.getMessage());
            }
        });

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.getChildren().addAll(
                oldPassword, newPassword, repeatPassword, change
        );
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
