package fxmlController;

import Utilities.Alert;
import Utilities.GetFXML;
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

import java.io.File;
import java.io.IOException;

public class Profile extends MenuParent {
    public TextField usernameText;
    public TextField nicknameText;
    public ImageView profileImage;

    public Profile() {
        super("Profile");
    }

    public void run() throws IOException {
        Parent root = GetFXML.getFXML("profile");
        this.scene = new Scene(root);
        App.pushMenu(this);
    }

    public void initialize() {
        User currentUser = Database.getInstance().getCurrentUser();
        usernameText.setText(currentUser.getUsername());
        nicknameText.setText(currentUser.getNickname());
        nicknameText.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                UserController.getInstance().changeNickname(newValue);
                nicknameText.setStyle("-fx-background-color: yellow; -fx-background-radius: 50;");
            } catch (Exception e) {
                nicknameText.setStyle("-fx-background-color: yellow; -fx-background-radius: 50; -fx-text-fill: red;");
            }
        });

        Image image = currentUser.getProfileImage();
        profileImage.setImage(image);
    }

    public void changeProfile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("choose character");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("image", "*.png", "*.jpg"));
        File file = fileChooser.showOpenDialog(App.getStage());
        if (file != null) {
            try {
                UserController.getInstance().changeProfile(file);
                profileImage.setImage(Database.getInstance().getCurrentUser().getProfileImage());
            } catch (Exception e) {
                e.printStackTrace();
                Alert.getInstance().errorPrint(e.getMessage());
            }
        } else {
            Alert.getInstance().errorPrint("loading image failed");
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
                Alert.getInstance().successfulPrint("password changed successfully!");
                stage.close();
            } catch (Exception e) {
                Alert.getInstance().errorPrint(e.getMessage());
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
