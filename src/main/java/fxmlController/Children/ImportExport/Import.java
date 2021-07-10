package fxmlController.Children.ImportExport;

import Utilities.Alert;
import Utilities.GetFXML;
import controller.Database;
import fxmlController.App;
import fxmlController.MenuParent;
import fxmlController.Size;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import model.cards.Card;

import javax.swing.text.html.ListView;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Import extends MenuParent {
    Card singleCard = null;
    ArrayList<Card> csvCards = null;
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
        String fileName = selectedFile.getCanonicalPath();
        if (selectedFile != null){
            Alert.getInstance().successfulPrint("file successfully selected");
        }
//        if (fileName.endsWith(".json")){
//
//        }else( fileName.endsWith(".csv")){
//            ;
//        }
    }

    public void importCard(ActionEvent event){
        Alert.getInstance().successfulPrint("Cards/card imported");
        if(singleCard != null){
            Database.getInstance().readCard(singleCard.getName());
        }else if(csvCards != null){
            for (Card csvCard : csvCards) {
                Database.getInstance().readCard(csvCard.getName());
            }
        }else{
            Alert.getInstance().errorPrint("Please select a card");
        }
    }
}