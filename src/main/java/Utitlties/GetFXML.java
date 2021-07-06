package Utitlties;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Objects;

public class GetFXML {
    public static Parent getFXML(String fxmlName) throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(GetFXML.class.getResource("/fxml/" + fxmlName + ".fxml")));
    }
    public static Parent getFXML(String fxmlName, String ... packageNames) throws IOException {
        StringBuilder pckgNames = new StringBuilder("");
        for (int i = 0 ; i < packageNames.length; i++)
            pckgNames.append(packageNames[i] + "/");
        return FXMLLoader.load(Objects.requireNonNull(GetFXML.class.getResource("/fxml/" + pckgNames + fxmlName + ".fxml")));
    }
}
