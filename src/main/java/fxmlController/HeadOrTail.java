package fxmlController;

import controller.HeadOrTailController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class HeadOrTail {
    private final HeadOrTailController controller;
    private final int turn;
    private Image B;
    private Image sharp;
    private Image throwingCoin;

    private String first;
    private String second;

    @FXML
    private Text sign;

    @FXML
    private ImageView gif;

    public HeadOrTail(HeadOrTailController controller, int turn) {
        this.controller = controller;
        this.turn = turn;
    }

    @FXML
    void IStartGame(ActionEvent event) {
//        TODO winner starts\
    }

    @FXML
    void OpponentStartsGame(ActionEvent event) {
//        TODO loser starts
    }

    @FXML
    void throwCoin(ActionEvent event) {
        sign.setText(controller.getSign(turn));
    }

    public void initialize() {
        B = new Image("file:" + System.getProperty("user.dir") + "/src/main/resources/images/B.jpg");
        sharp = new Image("file:" + System.getProperty("user.dir") + "/src/main/resources/images/sharp.jpg");
        throwingCoin = new Image("file:" + System.getProperty("user.dir") + "/src/main/resources/gifs/thorwCoin.gif");
    }

    public void playGif() {
        gif.setImage(throwingCoin);
    }

    public void setResult(int result) {
        if (result == 0)
            gif.setImage(B);
        else
            gif.setImage(sharp);
    }
}
