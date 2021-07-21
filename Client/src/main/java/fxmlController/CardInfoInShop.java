package fxmlController;

import Utilities.Alert;
import Utilities.GetFXML;
import controller.Database;
import controller.ShopController;
import fxmlController.Children.Shop;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class CardInfoInShop extends MenuParent implements Initializable {
    ShopController shopController = new ShopController();

    @FXML
    private Button buyCard;

    @FXML
    private Pane toShowCard;

    @FXML
    private Text numberOfThisCardInAllCards;

    @FXML
    private Text cardPrice;

    @FXML
    private Text balance;

    public CardInfoInShop() {
        super("Card Information");
    }

    @FXML
    void buyCard(ActionEvent event) {
        if (Shop.getCurrentCard().getPrice() > shopController.getUserBalance()){
            Alert.getInstance().errorPrint("Not enough money");
            buyCard.setDisable(true);
        }

        try {
            shopController.buyCard(Shop.getCurrentCard());
        } catch (Exception e) {
            Alert.getInstance().errorPrint(e.getMessage());
        }
        setBalance(shopController.getUserBalance());
        setCardPrice(shopController.getPrice(Shop.getCurrentCard().getName()));
        setNumberOfThisCardInAllCards(shopController.getNumberOfThisCardInShop(Shop.getCurrentCard().getName()));
    }

    public void run() throws IOException {
        Parent root = GetFXML.getFXML("cardInfoInShop");
        this.scene = new Scene(root, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue());
        App.pushMenu(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        setCard();
        if (Shop.getCurrentCard().getPrice() > shopController.getUserBalance()) buyCard.setDisable(true);

        setBalance(shopController.getUserBalance());
        setCardPrice(shopController.getPrice(Shop.getCurrentCard().getName()));
        setNumberOfThisCardInAllCards(shopController.getNumberOfThisCardInShop(Shop.getCurrentCard().getName()));
    }

    private void setCard() {
//        Sounds.setCard.play();
        ImageView imageView = new ImageView();
        imageView.setFitHeight(Size.CARD_HEIGHT_IN_SHOP_INFO.getValue());
        imageView.setFitWidth(Size.CARD_WIDTH_IN_SHOP_INFO.getValue());
        imageView.setImage(Shop.getCurrentCard().getImage());
        toShowCard.getChildren().add(imageView);
    }

    private void setBalance(int amount) {
        balance.setText(String.valueOf(amount));
    }

    private void setNumberOfThisCardInAllCards(int amount) {
        numberOfThisCardInAllCards.setText(String.valueOf(amount));
    }

    private void setCardPrice(int price) {
        cardPrice.setText(String.valueOf(price));
    }
}
