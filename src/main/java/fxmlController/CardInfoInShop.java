package fxmlController;

import Utitlties.GetFXML;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import model.cards.Card;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class CardInfoInShop extends MenuParent implements Initializable {


    @FXML
    private Pane toShowCard;

    public CardInfoInShop() {
        super("Card Information");
    }

    public void run() throws IOException {
        Parent root = GetFXML.getFXML("cardInfoInShop");
        this.scene = new Scene(root, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue());
        App.pushMenu(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        setCard();
    }

    private void setCard() {
       ImageView imageView = new ImageView();
       imageView.setFitHeight(Size.CARD_HEIGHT_IN_SHOP_INFO.getValue());
       imageView.setFitWidth(Size.CARD_WIDTH_IN_SHOP_INFO.getValue());
       imageView.setImage(Shop.getCardToShow().getImage());
       toShowCard.getChildren().add(imageView);
    }
}
