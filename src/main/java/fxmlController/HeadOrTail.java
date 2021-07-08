package fxmlController;

import Utilities.GetFXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HeadOrTail extends MenuParent implements Initializable {
    private Image B;
    private Image sharp;

    private String first;
    private String second;

    @FXML
    private Text sign;

    @FXML
    private ImageView gif;

    public HeadOrTail() {
        super("Head Or Tail");
    }

    @FXML
    void throwCoin(ActionEvent event) {
        Object source = event.getSource();

        sign.setText("hh");
    }

    public void run() throws IOException {
        Parent firstRoot = GetFXML.getFXML("headOrTail");
        this.scene = new Scene(firstRoot, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue());
        App.pushMenu(this);

        Parent secondRoot = GetFXML.getFXML("headOrTail");
        Stage stage = new Stage();
        stage.setScene(new Scene(secondRoot, Size.MAIN_WIDTH.getValue(), Size.MAIN_HEIGHT.getValue()));
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        B = new Image("D:/IdeaProjects/project-team-26/src/main/resources/images/B.jpg");
//        sharp = new Image("D:/IdeaProjects/project-team-26/src/main/resources/images/sharp.jpg");
    }
}
