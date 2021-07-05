package fxmlController;

import controller.Database;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Deck;
import model.cards.Card;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
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
            ImageView imageView = new ImageView();
            imageView.setFitHeight(Size.CARD_HEIGHT_IN_DECK.getValue());
            imageView.setFitWidth(Size.CARD_WIDTH_IN_DECK.getValue());
            imageView.setImage(Card.getCard(defaultDeck.getMainDeck().get(i)).getImage());
            mainDeck.add(imageView, i % 10, i / 10);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        setMainDeck();
    }
}