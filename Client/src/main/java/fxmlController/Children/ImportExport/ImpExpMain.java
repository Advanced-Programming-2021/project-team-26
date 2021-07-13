package fxmlController.Children.ImportExport;

import Utilities.GetFXML;
import fxmlController.App;
import fxmlController.MenuParent;
import fxmlController.Size;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.IOException;

public class ImpExpMain extends MenuParent{

    public ImpExpMain(){
        super("Import | Export");
    }
    @Override
    public void run() throws IOException {
        Parent root = GetFXML.getFXML("ImpExpMain", "ImportExport");
        scene = new Scene(root);
        App.pushMenu(this);
    }

    public void importCard(ActionEvent actionEvent) throws Exception {
        new Import().run();
    }

    public void exportCard(ActionEvent actionEvent) throws Exception {
        new Export().run();
    }
}