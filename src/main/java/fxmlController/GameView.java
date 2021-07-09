package fxmlController;

import controller.GameController;
import controller.Phase;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.cards.Card;

import java.net.URL;
import java.util.ResourceBundle;

public class GameView implements Initializable {

    public static final String MY_PHASE_STYLE = "-fx-background-color: #3ba8e2;";
    public static final String OPPONENT_PHASE_STYLE = "-fx-background-color: #e01313;";
    public static final String MY_CURRENT_PHASE_STYLE = "-fx-background-color: #3ba8e2;-fx-border-color: #001a82; -fx-border-width: 5";
    public static final String OPPONENT_CURRENT_PHASE_STYLE = "-fx-background-color: #e01313;-fx-border-color: #570000; -fx-border-width: 5";
    private final GameController controller;
    private final int turn;
    public Button drawPhaseBut;
    public Button standbyPhaseBut;
    public Button mainPhase1But;
    public Button battlePhaseBut;
    public Button mainPhase2But;
    public Button endPhaseBut;

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

    public GameView(GameController controller, int turn) {
        this.controller = controller;
        this.turn = turn;
    }

    public void updatePhase() {
        if (controller.getGame().getTurn() != turn) {
            updatePhaseOpponent();
            return;
        }

        standbyPhaseBut.setStyle(MY_PHASE_STYLE);
        drawPhaseBut.setStyle(MY_PHASE_STYLE);
        mainPhase1But.setStyle(MY_PHASE_STYLE);
        battlePhaseBut.setStyle(MY_PHASE_STYLE);
        mainPhase2But.setStyle(MY_PHASE_STYLE);
        endPhaseBut.setStyle(MY_PHASE_STYLE);

        Phase phase = controller.getGame().getPhase();
        boolean found = false;
        if (Phase.STANDBY.compareTo(phase) == 0) {
            found = true;
            standbyPhaseBut.setOnAction(null);
            standbyPhaseBut.setStyle(MY_CURRENT_PHASE_STYLE);
        } else
            standbyPhaseBut.setOnAction(null);
        if (Phase.DRAW.compareTo(phase) == 0) {
            found = true;
            drawPhaseBut.setOnAction(null);
            drawPhaseBut.setStyle(MY_CURRENT_PHASE_STYLE);
        } else {
            drawPhaseBut.setOnAction(found ? this::drawPhase : null);
        }
        if (Phase.MAIN1.compareTo(phase) == 0) {
            found = true;
            mainPhase1But.setOnAction(null);
            mainPhase1But.setStyle(MY_CURRENT_PHASE_STYLE);
        } else
            mainPhase1But.setOnAction(found ? this::mainPhase1 : null);
        if (Phase.BATTLE.compareTo(phase) == 0) {
            found = true;
            battlePhaseBut.setOnAction(null);
            battlePhaseBut.setStyle(MY_CURRENT_PHASE_STYLE);
        } else
            battlePhaseBut.setOnAction(found ? this::battlePhase : null);
        if (Phase.MAIN2.compareTo(phase) == 0) {
            found = true;
            mainPhase2But.setOnAction(null);
            mainPhase2But.setStyle(MY_CURRENT_PHASE_STYLE);
        } else
            mainPhase2But.setOnAction(found ? this::mainPhase2 : null);
        if (Phase.END.compareTo(phase) == 0) {
            endPhaseBut.setOnAction(null);
            endPhaseBut.setStyle(MY_CURRENT_PHASE_STYLE);
        } else
            endPhaseBut.setOnAction(found ? this::endPhase : null);
    }


    private void updatePhaseOpponent() {
        standbyPhaseBut.setStyle(OPPONENT_PHASE_STYLE);
        drawPhaseBut.setStyle(OPPONENT_PHASE_STYLE);
        mainPhase1But.setStyle(OPPONENT_PHASE_STYLE);
        battlePhaseBut.setStyle(OPPONENT_PHASE_STYLE);
        mainPhase2But.setStyle(OPPONENT_PHASE_STYLE);
        endPhaseBut.setStyle(OPPONENT_PHASE_STYLE);

        standbyPhaseBut.setOnAction(null);
        drawPhaseBut.setOnAction(null);
        mainPhase1But.setOnAction(null);
        battlePhaseBut.setOnAction(null);
        mainPhase2But.setOnAction(null);
        endPhaseBut.setOnAction(null);

        switch (controller.getGame().getPhase()) {
            case DRAW:
                drawPhaseBut.setStyle(OPPONENT_CURRENT_PHASE_STYLE);
                break;
            case STANDBY:
                standbyPhaseBut.setStyle(OPPONENT_CURRENT_PHASE_STYLE);
                break;
            case MAIN1:
                mainPhase1But.setStyle(OPPONENT_CURRENT_PHASE_STYLE);
                break;
            case BATTLE:
                battlePhaseBut.setStyle(OPPONENT_CURRENT_PHASE_STYLE);
                break;
            case MAIN2:
                mainPhase2But.setStyle(OPPONENT_CURRENT_PHASE_STYLE);
                break;
            case END:
                endPhaseBut.setStyle(OPPONENT_CURRENT_PHASE_STYLE);
                break;
        }
    }

    @FXML
    public void drawPhase(ActionEvent event) {
        controller.nextPhase(Phase.DRAW);
    }

    @FXML
    void standbyPhase(ActionEvent event) {
        controller.nextPhase(Phase.STANDBY);
    }

    @FXML
    void mainPhase1(ActionEvent event) {
        controller.nextPhase(Phase.MAIN1);
    }

    @FXML
    void battlePhase(ActionEvent event) {
        controller.nextPhase(Phase.BATTLE);
    }

    @FXML
    void mainPhase2(ActionEvent event) {
        controller.nextPhase(Phase.MAIN2);
    }

    @FXML
    void endPhase(ActionEvent event) {
        controller.nextPhase(Phase.END);
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

    public void moveFromDeckToHand(Card card) {
        ImageView imageView = new ImageView();
        imageView.setImage(card.getImage());
        imageView.setFitHeight(Size.CARD_HEIGHT_IN_SHOP.getValue());
        imageView.setFitWidth(Size.CARD_WIDTH_IN_SHOP.getValue());
        int number = myHand.getChildren().size();

        myHand.add(imageView, number, 0);
    }
}