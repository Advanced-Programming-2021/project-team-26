package fxmlController;

import controller.GameController;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.cards.Card;

import java.net.URL;
import java.util.ResourceBundle;

public class GameView implements Initializable {

    private final GameController controller;
    private final int turn;

    @FXML
    private AnchorPane root;

    @FXML
    private ProgressBar opponentLPProgress;

    @FXML
    private ProgressBar myLPProgress;

    @FXML
    private ImageView opponentProfile;

    @FXML
    private ImageView myProfile;

    @FXML
    private Text myUsername;

    @FXML
    private Text myNickname;

    @FXML
    private Text opponentNickname;

    @FXML
    private Text opponentUsername;

    @FXML
    private Text opponentLP;

    @FXML
    private Text myLP;

    @FXML
    private GridPane opponentHand;

    @FXML
    private GridPane myHand;

    @FXML
    private ImageView myDeckImage;

    @FXML
    void battlePhase(ActionEvent event) {

    }

    @FXML
    void endPhase(ActionEvent event) {

    }

    @FXML
    void mainPhase1(ActionEvent event) {

    }

    @FXML
    void mainPhase2(ActionEvent event) {

    }

    @FXML
    void standbyPhase(ActionEvent event) {

    }

    public GameView(GameController controller, int turn) {
        this.controller = controller;
        this.turn = turn;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myLP.setText(String.valueOf(controller.getGame().getLifePoint(turn)));
        myNickname.setText(controller.getGame().getUser(turn).getNickname());
        myProfile.setImage(controller.getGame().getUser(turn).getProfileImage());
        myUsername.setText(controller.getGame().getUser(turn).getUsername());

        opponentLP.setText(String.valueOf(controller.getGame().getLifePoint(1 - turn)));
        opponentNickname.setText(controller.getGame().getUser(1 - turn).getNickname());
        opponentProfile.setImage(controller.getGame().getUser(1 - turn).getProfileImage());
        opponentUsername.setText(controller.getGame().getUser(1 - turn).getUsername());
    }

    private void move(int currentX, int currentY, int destX, int destY, Node node) {
        SequentialTransition sequentialTransition = new SequentialTransition();
        sequentialTransition.setNode(node);
        TranslateTransition translateTransition = new TranslateTransition();

        translateTransition.setFromX(currentX);
        translateTransition.setToX(destX);
        translateTransition.setFromY(currentY);
        translateTransition.setToY(destY);

        sequentialTransition.getChildren().add(translateTransition);
        sequentialTransition.play();
    }

    public void moveFromDeckToHand(Card card){
        ImageView imageView = new ImageView();
        imageView.setImage(card.getImage());
        imageView.setFitHeight(Size.CARD_HEIGHT_IN_SHOP.getValue());
        imageView.setFitWidth(Size.CARD_WIDTH_IN_SHOP.getValue());
        int number = myHand.getChildren().size();

        myHand.add(imageView, number, 0);
    }
}