package Utilities;

import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

import java.util.Objects;

public class Alert {

    private static Alert print;

    private Alert() {
    }

    public static Alert getInstance() {
        if (print == null)
            print = new Alert();
        return print;
    }

    public void errorPrint(String errorMessage) {
        Platform.runLater(() -> {
            javafx.scene.control.Alert dialog = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, errorMessage, ButtonType.OK);
            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/DialogPane.css")).toExternalForm());
            dialogPane.getStyleClass().add("DialogPane");
            dialog.show();
        });
    }

    public void successfulPrint(String message) {
        Platform.runLater(() -> {
            javafx.scene.control.Alert dialog = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION, message, ButtonType.OK);
            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/DialogPane.css")).toExternalForm());
            dialogPane.getStyleClass().add("DialogPane");
            dialog.show();
        });
    }
}
