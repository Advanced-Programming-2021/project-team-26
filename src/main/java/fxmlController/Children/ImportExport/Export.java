package fxmlController.Children.ImportExport;

import Utilities.Alert;
import Utilities.GetFXML;
import controller.Database;
import fxmlController.App;
import fxmlController.CardInfoInShop;
import fxmlController.MenuParent;
import fxmlController.Size;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import model.cards.Card;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class Export extends MenuParent implements Initializable {
    private static Card currentCard;
    public static Card getCurrentCard() {
        return currentCard;
    }
    @FXML
    GridPane cards;

    @FXML
    Label alertLabel;

    @FXML
    Button exportBtn;

    public Export(){
        super("Export page");
    }
    @Override
    public void run() throws Exception {
        Parent root = GetFXML.getFXML("Export", "ImportExport");
        scene = new Scene(root,Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue());
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
                        currentCard = allCards.get(index);
                        alertLabel.setText("Card Selected !!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    public void exportCard(MouseEvent mouseEvent) {
        if(alertLabel.getText().startsWith("Card")) {
            Database.getInstance().writeCard(currentCard);
            Alert.getInstance().successfulPrint("Card Exported successfully");
        }
        else{
            Alert.getInstance().errorPrint("Please select a Card");
        }
    }
}