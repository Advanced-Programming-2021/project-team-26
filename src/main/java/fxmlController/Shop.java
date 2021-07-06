package fxmlController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.cards.Card;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import static fxmlController.App.getFXML;

public class Shop extends MenuParent implements Initializable {
    @FXML
    private GridPane cards;

    public Shop() {
        super("Shop Menu");
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
        Map<String, Card> allCards = Card.getAllCards();
        int i = 0;
        for (Card card : allCards.values()) {
            ImageView imageView = new ImageView();
            imageView.setFitHeight(Size.CARD_HEIGHT_IN_SHOP.getValue());
            imageView.setFitWidth(Size.CARD_WIDTH_IN_SHOP.getValue());
            imageView.setImage(card.getImage());
            cards.add(imageView, i % 15, i / 15);
            i++;
        }
    }
}
