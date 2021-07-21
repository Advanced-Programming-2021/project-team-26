package fxmlController.Children;

import Utilities.Alert;
import Utilities.GetFXML;
import controller.MainMenuController;
import controller.UserController;
import fxmlController.*;
import fxmlController.Children.ImportExport.ImpExpMain;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

import static fxmlController.App.buttonClick;

public class MainMenu extends MenuParent {

    public MainMenu() {
        super("Yu Gi Oh!");
    }

    public void run() throws IOException {
        Parent root = GetFXML.getFXML("mainMenu");
        this.scene = new Scene(root);
        App.pushMenu(this, false);
    }

    public void newTwoPlayerGame() {
        if (App.isAreSoundsActive()) buttonClick.play();

        Object[] result = getSecondPlayerAndRound();
        String secondPlayer = (String) result[0];
        Integer round = (Integer) result[1];
        try {
            //MainMenuController.getInstance().createNewGameWithRealPlayer(secondPlayer, round);
        } catch (Exception e) {
            Alert.getInstance().errorPrint(e.getMessage());
        }
    }

    private Object[] getSecondPlayerAndRound() {
        Stage stage = new Stage();

        final String[] secondPlayerTmp = new String[1];
        final Integer[] roundsTmp = new Integer[1];

        TextField secondPlayerText = new TextField();
        secondPlayerText.setPromptText("second player");
        ChoiceBox<Integer> roundsChoice = new ChoiceBox<>();
        roundsChoice.setAccessibleText("rounds:");
        roundsChoice.getItems().addAll(1, 3);
        roundsChoice.setValue(1);
        Button confirm = new Button("start");
        confirm.setOnAction(e -> {
            secondPlayerTmp[0] = secondPlayerText.getText();
            roundsTmp[0] = roundsChoice.getValue();
            stage.close();
        });

        GridPane root = new GridPane();
        root.setPadding(new Insets(10));
        root.setHgap(5);
        root.setVgap(10);
        root.setAlignment(Pos.CENTER);
        root.add(new Label("second player:"), 0, 0);
        root.add(secondPlayerText, 1, 0);
        root.add(new Label("rounds"), 0, 1);
        root.add(roundsChoice, 1, 1);
        root.add(confirm, 0, 2);
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.showAndWait();
        return new Object[]{secondPlayerTmp[0], roundsTmp[0]};
    }

    public void newOnePlayerGame() {

        Integer rounds = getRound();

        try {
            MainMenuController.getInstance().creatNewGameWithAI(rounds);
        } catch (Exception e) {
            Alert.getInstance().errorPrint(e.getMessage());
        }
    }

    private Integer getRound() {
        Stage stage = new Stage();

        final Integer[] roundsTmp = new Integer[1];

        ChoiceBox<Integer> roundsChoice = new ChoiceBox<>();
        roundsChoice.setAccessibleText("rounds:");
        roundsChoice.getItems().addAll(1, 3);
        roundsChoice.setValue(1);
        Button confirm = new Button("start");
        confirm.setOnAction(e -> {
            roundsTmp[0] = roundsChoice.getValue();
            stage.close();
        });

        GridPane root = new GridPane();
        root.setPadding(new Insets(10));
        root.setHgap(5);
        root.setVgap(10);
        root.setAlignment(Pos.CENTER);
        root.add(new Label("rounds"), 0, 0);
        root.add(roundsChoice, 1, 0);
        root.add(confirm, 0, 1);
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.showAndWait();
        return roundsTmp[0];
    }

    public void openDeck() throws IOException {
        if (App.isAreSoundsActive()) buttonClick.play();
        new DeckMenu().run();
    }

    public void openProfile() throws IOException {
        if (App.isAreSoundsActive()) buttonClick.play();
        new Profile().run();
    }

    public void openScoreBoard() throws IOException {
        if (App.isAreSoundsActive()) buttonClick.play();
        new Scoreboard().run();
    }

    public void openImportExport() throws IOException {
        if (App.isAreSoundsActive()) buttonClick.play();
        new ImpExpMain().run();
    }

//    public void logout() {
//        if (App.isAreSoundsActive()) buttonClick.play();
//        Alert.getInstance().successfulPrint(UserController.getInstance().logout());
//    }
}
