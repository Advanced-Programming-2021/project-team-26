package fxmlController.Children;

import Utilities.GetFXML;
import fxmlController.App;
import fxmlController.CardInfoInShop;
import fxmlController.MenuParent;
import fxmlController.Size;
import javafx.event.ActionEvent;
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


public class Shop extends MenuParent implements Initializable {
    private static Card currentCard;

    @FXML
    private GridPane cards;

    public Shop() {
        super("Shop Menu");
    }

    public static Card getCurrentCard() {
        return currentCard;
    }

    public void run() throws IOException {
        AnchorPane root = (AnchorPane) GetFXML.getFXML("shop");
        this.scene = new Scene(root, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue());
        App.pushMenu(this);
    }

    public void openAdminPanel(ActionEvent event) throws IOException {
        new AdminPanel().run();
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
                        currentCard = allCards.get(index);
                        new CardInfoInShop().run();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
