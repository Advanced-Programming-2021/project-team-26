package fxmlController.Children.ImportExport;

import Utilities.GetFXML;
import fxmlController.App;
import fxmlController.MenuParent;
import fxmlController.Size;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;

import javax.swing.text.html.ListView;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Import extends MenuParent {

    public Import(){
        super("Import");
    }
    @Override
    public void run() throws Exception {
        Parent root = GetFXML.getFXML("Import", "ImportExport");
        scene = new Scene(root, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue());
        App.pushMenu(this);
    }

    public void select(ActionEvent actionEvent) throws IOException {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);
    }

    public void importCard(ActionEvent event){
    }
}