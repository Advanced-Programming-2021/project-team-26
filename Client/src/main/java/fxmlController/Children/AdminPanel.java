package fxmlController.Children;

import Utilities.Alert;
import Utilities.GetFXML;
import com.google.gson.Gson;
import controller.Database;
import controller.NetworkController;
import controller.ShopController;
import fxmlController.App;
import fxmlController.MenuParent;
import fxmlController.Size;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import model.Request;
import model.Response;

import java.io.IOException;

public class AdminPanel extends MenuParent {

    @FXML
    public TextField cardName;
    @FXML
    public TextField decreaseAmount;
    @FXML
    public TextField increaseAmount;

    public AdminPanel() {
        super("Admin Panel");
    }

    @Override
    public void run() throws IOException {
        Parent root = GetFXML.getFXML("adminPanel");
        scene = new Scene(root, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue());
        App.pushMenu(this);
    }

    public void increaseInventory(ActionEvent event) {
        Request request = new Request("AdminPanel", "increaseInventory");
        request.addParameter("cardName", cardName.getText());
        request.addParameter("amount", Integer.parseInt(increaseAmount.getText()));
        Response response = NetworkController.getInstance().sendAndReceive(request);
        Alert.getInstance().successfulPrint(response.getMessage());
    }

    public void decreaseInventory(ActionEvent event) {
        Request request = new Request("AdminPanel", "decreaseInventory");
        request.addParameter("cardName", cardName.getText());
        request.addParameter("amount", Integer.parseInt(decreaseAmount.getText()));
        Response response = NetworkController.getInstance().sendAndReceive(request);
        Alert.getInstance().successfulPrint(response.getMessage());
    }

    public void disableShopping(ActionEvent event) {
        Request request = new Request("AdminPanel", "disableShopping");
        request.addParameter("cardName", cardName.getText());
        Response response = NetworkController.getInstance().sendAndReceive(request);
        Alert.getInstance().successfulPrint(response.getMessage());
    }

    public void enableShopping(ActionEvent event) {
        Request request = new Request("AdminPanel", "enableShopping");
        request.addParameter("cardName", cardName.getText());
        Response response = NetworkController.getInstance().sendAndReceive(request);
        Alert.getInstance().successfulPrint(response.getMessage());
    }

}
