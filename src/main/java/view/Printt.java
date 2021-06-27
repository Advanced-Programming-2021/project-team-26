package view;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Printt {

    private static Printt print;


    private Printt() {

    }

    public static Printt getInstance() {
        if (print == null)
            print = new Printt();
        return print;
    }

    public void errorPrint(String errorMessage) {
        Platform.runLater(() -> {
            Alert dialog = new Alert(Alert.AlertType.ERROR, errorMessage, ButtonType.OK);
            dialog.show();
        });
    }

    public void successfulPrint(String message) {
        Platform.runLater(() -> {
            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK);
            dialog.show();
        });
    }
}
