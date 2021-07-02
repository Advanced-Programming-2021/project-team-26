package fxmlController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static fxmlController.Welcome.getFXML;

public class DeckMenu implements Initializable {
    @FXML
    private Pane mainDeck;

    @FXML
    private GridPane sideDeck;

    public void run() throws IOException {
        Stage primaryStage = Welcome.getStage();
        AnchorPane root = (AnchorPane) getFXML("deckMenu");
        primaryStage.setTitle("Deck Menu");
        primaryStage.setScene(new Scene(root, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue()));
        primaryStage.show();
    }

    private void setMainDeck() {
//        for (int i = 0; i < 60; i++) {
//            Rectangle rectangle = new Rectangle();
//            rectangle.setHeight(Size.CARD_HEIGHT.getValue());
//            rectangle.setWidth(Size.CARD_WIDTH.getValue());
//            rectangle.setFill(Color.RED);
//            mainDeck.getChildren().add(rectangle);
//        }

        for (int i = 0; i < Size.MAIN_DECK_WIDTH.getValue(); i += Size.CARD_WIDTH.getValue()) {
            for (int j = 0; j < Size.MAIN_DECK_HEIGHT.getValue(); j += Size.CARD_HEIGHT.getValue()) {
                Rectangle rectangle = new Rectangle(i, j, Size.CARD_WIDTH.getValue(), Size.CARD_HEIGHT.getValue());
                rectangle.setFill(Color.ORANGE);
                mainDeck.getChildren().add(rectangle);
            }
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        setMainDeck();
    }
}
