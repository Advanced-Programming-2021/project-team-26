package fxmlController;

import controller.Database;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.Deck;
import model.cards.Card;

import javax.swing.text.Element;
import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;

import static fxmlController.Welcome.getFXML;

public class DeckMenu implements Initializable {
    private final HashMap<String, Deck> allDecks = Database.getInstance().getCurrentUser().getAllDecks();

    @FXML
    private GridPane mainDeck;
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
        Deck defaultDeck = allDecks.get("defaultDeck");

        for (int i = 0; i < defaultDeck.getMainDeck().size(); i++) {
            Rectangle rectangle = new Rectangle(Size.CARD_WIDTH.getValue(), Size.CARD_HEIGHT.getValue());
            mainDeck.add(rectangle, i / 15, i % 15);
        }

//        for (int i = 0; i < Size.MAIN_DECK_WIDTH.getValue(); i += Size.CARD_WIDTH.getValue()) {
//            for (int j = 0; j < Size.MAIN_DECK_HEIGHT.getValue(); j += Size.CARD_HEIGHT.getValue()) {
//                Rectangle rectangle = new Rectangle(i, j, Size.CARD_WIDTH.getValue(), Size.CARD_HEIGHT.getValue());
//                rectangle.setFill();
//                mainDeck.add(rectangle, i / Size.CARD_WIDTH.getValue(), j / Size.CARD_HEIGHT.getValue());
//            }
//        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        setMainDeck();
    }
}
