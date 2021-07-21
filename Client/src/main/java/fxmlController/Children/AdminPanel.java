package fxmlController.Children;

import Utilities.GetFXML;
import fxmlController.App;
import fxmlController.MenuParent;
import fxmlController.Size;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class AdminPanel extends MenuParent {

    public AdminPanel(String title) {
        super("Admin Panel");
    }

    @Override
    public void run() throws IOException {
        Parent root = GetFXML.getFXML("adminPanel");
        scene = new Scene(root, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue());
        App.pushMenu(this);
    }
}
