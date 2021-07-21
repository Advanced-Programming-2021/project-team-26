package fxmlController;

import Utilities.GetFXML;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class Tv extends MenuParent{
    public Tv() {
        super("Tv page");
    }

    @Override
    public void run() throws IOException {
        Parent root = GetFXML.getFXML("tv");
        scene = new Scene(root, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue());
        App.pushMenu(this);
    }
}
