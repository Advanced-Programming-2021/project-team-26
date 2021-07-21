package controller;

import Utilities.GetFXML;
import fxmlController.App;
import fxmlController.MenuParent;
import fxmlController.Size;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AdminPanel extends MenuParent {

    public AdminPanel() {
        super("Admin Panel");
    }

    @Override
    public void run() throws IOException {
        Parent root = GetFXML.getFXML("adminPanel");
        scene = new Scene(root, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue());
        App.pushMenu(this);
    }


    public void increaseInventory(String cardName, int amount) {
        ShopController.increaseCardAmountInShop(amount, cardName);
    }

    public void decreaseInventory(String cardName, int amount) {
        ShopController.decreaseCardAmountInShop(amount, cardName);
    }

    public void disableShopping(String cardName) {
        ShopController.disableShopping(cardName);
    }

    public void enableShopping(String cardName) {
        ShopController.enableShopping(cardName);
    }
}
