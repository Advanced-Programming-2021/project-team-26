package fxmlController.Children.ImportExport;

import Utilities.Alert;
import Utilities.GetFXML;
import controller.Database;
import fxmlController.App;
import fxmlController.MenuParent;
import fxmlController.Size;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import model.cards.Card;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Import extends MenuParent {
    public TextArea description;
    Card singleCard = null;
    ArrayList<Card> csvCards = null;

    public Import() {
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
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("json", "*.json"));
        File selectedFile = fc.showOpenDialog(null);
        String fileName = selectedFile.getCanonicalPath();
        if (selectedFile != null) {
            Alert.getInstance().successfulPrint("file successfully selected");
        }
        if (fileName.endsWith(".json")) {
            Card card = Database.getInstance().readCard(selectedFile);
            description.setText(card.getName() + "\n" + card.getDescription());
        }
    }
}