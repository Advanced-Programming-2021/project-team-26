package fxmlController;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.cards.Card;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import static fxmlController.App.getFXML;

public class Shop extends MenuParent implements Initializable {
    private static Card cardToShow;

    @FXML
    private GridPane cards;

    public Shop() {
        super("Shop Menu");
    }

    public static Card getCardToShow() {
        return cardToShow;
    }

    public void run() throws IOException {
        AnchorPane root = (AnchorPane) getFXML("shop");
        this.scene = new Scene(root, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue());
        App.pushMenu(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        setCards();
    }

    private void setCards() {
        Map<String, Card> allCardsMap = Card.getAllCards();
        ArrayList<Card> allCards = new ArrayList<>(allCardsMap.values());

        int i = 0;
        for (Card card : allCards) {
            ImageView imageView = new ImageView();
            imageView.setFitHeight(Size.CARD_HEIGHT_IN_SHOP.getValue());
            imageView.setFitWidth(Size.CARD_WIDTH_IN_SHOP.getValue());
            imageView.setImage(card.getImage());
            cards.add(imageView, i % 14, i / 14);
            i++;
        }

        for (Node element : cards.getChildren()) {
            element.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    int index = GridPane.getRowIndex(element) * 14 + GridPane.getColumnIndex(element);
                    try {
                        cardToShow = allCards.get(index);
                        new CardInfoInShop().run();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
