package fxmlController.Children.ImportExport;

import Utilities.GetFXML;
import fxmlController.App;
import fxmlController.MenuParent;
import fxmlController.Size;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Export extends MenuParent{
    public Export(){
        super("Export page");
    }
    @Override
    public void run() throws Exception {
        Parent root = GetFXML.getFXML("Export", "ImportExport");
        scene = new Scene(root,Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue());
        App.pushMenu(this);
    }
}