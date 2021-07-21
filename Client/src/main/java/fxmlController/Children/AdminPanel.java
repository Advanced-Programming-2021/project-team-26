package fxmlController.Children;

import Utilities.GetFXML;
import fxmlController.App;
import fxmlController.MenuParent;
import fxmlController.Size;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.awt.*;
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


}
