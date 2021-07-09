package fxmlController;

import Utilities.Alert;
import controller.*;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.CardAddress;
import model.Owner;
import model.Place;
import model.cards.Card;
import model.cards.SpellTrap;
import model.cards.monster.Monster;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class GameView implements Initializable {

    public static final String MY_PHASE_STYLE = "-fx-background-color: #3ba8e2;";
    public static final String OPPONENT_PHASE_STYLE = "-fx-background-color: #e01313;";
    public static final String MY_CURRENT_PHASE_STYLE = "-fx-background-color: #3ba8e2;-fx-border-color: #001a82; -fx-border-width: 5";
    public static final String OPPONENT_CURRENT_PHASE_STYLE = "-fx-background-color: #e01313;-fx-border-color: #570000; -fx-border-width: 5";
    private final ArrayList<ImageView> myMonsters = new ArrayList<>();
    private final ArrayList<ImageView> mySpellTraps = new ArrayList<>();
    private final ArrayList<ImageView> oppMonsters = new ArrayList<>();
    private final ArrayList<ImageView> oppSpellTraps = new ArrayList<>();
    private final GameController gameController;
    private final int turn;
    public Button drawPhaseBut;
    public Button standbyPhaseBut;
    public Button mainPhase1But;
    public Button battlePhaseBut;
    public Button mainPhase2But;
    public Button endPhaseBut;
    public ImageView cardInfo;
    public TextArea cardDetails;

    @FXML
    private AnchorPane root;

    @FXML
    private ImageView opoSpellTrap4;

    @FXML
    private ImageView opoSpellTrap2;

    @FXML
    private ImageView opoSpellTrap1;

    @FXML
    private ImageView opoSpellTrap3;

    @FXML
    private ImageView opoSpellTrap5;

    @FXML
    private ImageView oppMonster5;

    @FXML
    private ImageView oppMonster3;

    @FXML
    private ImageView oppMonster1;

    @FXML
    private ImageView oppMonster2;

    @FXML
    private ImageView oppMonster4;

    @FXML
    private ImageView myMonster5;

    @FXML
    private ImageView myMonster3;

    @FXML
    private ImageView myMonster1;

    @FXML
    private ImageView myMonster2;

    @FXML
    private ImageView myMonster4;

    @FXML
    private ImageView mySpellTrap5;

    @FXML
    private ImageView mySpellTrap3;

    @FXML
    private ImageView mySpellTrap1;

    @FXML
    private ImageView mySpellTrap2;

    @FXML
    private ImageView mySpellTrap4;

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
        this.gameController = controller;
        this.turn = turn;
    }

    public void updatePhase() {
        if (gameController.getGame().getTurn() != turn) {
            updatePhaseOpponent();
            return;
        }

        standbyPhaseBut.setStyle(MY_PHASE_STYLE);
        drawPhaseBut.setStyle(MY_PHASE_STYLE);
        mainPhase1But.setStyle(MY_PHASE_STYLE);
        battlePhaseBut.setStyle(MY_PHASE_STYLE);
        mainPhase2But.setStyle(MY_PHASE_STYLE);
        endPhaseBut.setStyle(MY_PHASE_STYLE);

        Phase phase = gameController.getGame().getPhase();
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

        switch (gameController.getGame().getPhase()) {
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
        gameController.nextPhase(Phase.DRAW);
    }

    @FXML
    void standbyPhase(ActionEvent event) {
        gameController.nextPhase(Phase.STANDBY);
    }

    @FXML
    void mainPhase1(ActionEvent event) {
        gameController.nextPhase(Phase.MAIN1);
    }

    @FXML
    void battlePhase(ActionEvent event) {
        gameController.nextPhase(Phase.BATTLE);
    }

    @FXML
    void mainPhase2(ActionEvent event) {
        gameController.nextPhase(Phase.MAIN2);
    }

    @FXML
    void endPhase(ActionEvent event) {
        gameController.nextPhase(Phase.END);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myLP.setText(String.valueOf(gameController.getGame().getLifePoint(turn)));
        myNickname.setText(gameController.getGame().getUser(turn).getNickname());
        myProfile.setImage(gameController.getGame().getUser(turn).getProfileImage());
        myUsername.setText(gameController.getGame().getUser(turn).getUsername());

        opponentLP.setText(String.valueOf(gameController.getGame().getLifePoint(1 - turn)));
        opponentNickname.setText(gameController.getGame().getUser(1 - turn).getNickname());
        opponentProfile.setImage(gameController.getGame().getUser(1 - turn).getProfileImage());
        opponentUsername.setText(gameController.getGame().getUser(1 - turn).getUsername());

        initField();
    }

    private void initField() {
        myMonsters.add(myMonster1);
        myMonsters.add(myMonster2);
        myMonsters.add(myMonster3);
        myMonsters.add(myMonster4);
        myMonsters.add(myMonster5);

        mySpellTraps.add(mySpellTrap1);
        mySpellTraps.add(mySpellTrap2);
        mySpellTraps.add(mySpellTrap3);
        mySpellTraps.add(mySpellTrap4);
        mySpellTraps.add(mySpellTrap5);

        oppMonsters.add(oppMonster1);
        oppMonsters.add(oppMonster2);
        oppMonsters.add(oppMonster3);
        oppMonsters.add(oppMonster4);
        oppMonsters.add(oppMonster5);

        oppSpellTraps.add(opoSpellTrap1);
        oppSpellTraps.add(opoSpellTrap2);
        oppSpellTraps.add(opoSpellTrap3);
        oppSpellTraps.add(opoSpellTrap4);
        oppSpellTraps.add(opoSpellTrap5);
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
        ImageView imageView = new CardImageView(card);
        imageView.setFitHeight(Size.CARD_HEIGHT_IN_SHOP.getValue());
        imageView.setFitWidth(Size.CARD_WIDTH_IN_SHOP.getValue());
        int number = myHand.getChildren().size();

        myHand.add(imageView, number, 0);

        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.SECONDARY)) {
                    if (card instanceof Monster) setMonster(card, imageView);
                    else if (card instanceof SpellTrap) setSpellTrap(card, imageView);
                } else {
                    if (card instanceof Monster) summonMonster(card, imageView);
                    else if (card instanceof SpellTrap) activateSpellTrap(card, imageView);
                }
            }
        });
    }

    private void activateSpellTrap(Card card, ImageView imageView) {
        try {
            gameController.activateEffect(card, new CardAddress(Place.Hand, Owner.Me));
            myHand.getChildren().remove(imageView);
        } catch (Exception e) {
            Alert.getInstance().errorPrint(e.getMessage());
            e.printStackTrace();
        }
    }

    private void setSpellTrap(Card card, ImageView imageView) {
        try {
            gameController.set(card);
            mySpellTraps.get(gameController.getGame().getThisBoard().getSpellTrapZoneLastEmpty() - 1).setImage(Card.getUnknownImage());
            myHand.getChildren().remove(imageView);
        } catch (Exception e) {
            Alert.getInstance().errorPrint(e.getMessage());
            e.printStackTrace();
        }
    }

    private void summonMonster(Card card, ImageView imageView) {
        try {
            gameController.summon(card);
            myMonsters.get(gameController.getGame().getThisBoard().getMonsterZoneLastEmpty() - 1).setImage(card.getImage());
            myHand.getChildren().remove(imageView);
        } catch (Exception e) {
            Alert.getInstance().errorPrint(e.getMessage());
            e.printStackTrace();
        }
    }

    private void setMonster(Card card, ImageView imageView) {
        try {
            gameController.set(card);
            myMonsters.get(gameController.getGame().getThisBoard().getMonsterZoneLastEmpty() - 1).setImage(Card.getUnknownImage());
            myHand.getChildren().remove(imageView);
        } catch (Exception e) {
            Alert.getInstance().errorPrint(e.getMessage());
            e.printStackTrace();
        }
    }

    public void showOpponentMonsterZone() {
        HashMap<Integer, MonsterController> monsterZone = gameController.getGame().getBoard(1 - turn).getMonsterZoneMap();
        for (int i = 0; i < 5; i++) {
            if (!monsterZone.containsKey(i)) {
                ImageView imageView = oppMonsters.get(i);
                imageView.setImage(null);
            } else if (monsterZone.get(i).getPosition() == MonsterPosition.ATTACK) {
                ImageView imageView = oppMonsters.get(i);
                imageView.setImage(monsterZone.get(i).getMonster().getImage());
                imageView.setRotate(180);
            } else if (monsterZone.get(i).getPosition() == MonsterPosition.DEFENCE_UP) {
                ImageView imageView = oppMonsters.get(i);
                imageView.setImage(monsterZone.get(i).getMonster().getImage());
                imageView.setRotate(90);
            } else {
                ImageView imageView = oppMonsters.get(i);
                imageView.setImage(Card.getUnknownImage());
                imageView.setRotate(90);
            }
        }

    }

    public void showOpponentSpellTraps() {
        HashMap<Integer, SpellTrapController> spellTrapZone = gameController.getGame().getBoard(1 - turn).getSpellTrapZoneMap();
        for (int i = 0; i < 5; i++) {
            if (!spellTrapZone.containsKey(i)) {
                ImageView imageView = oppSpellTraps.get(i);
                imageView.setImage(null);
            } else if (spellTrapZone.get(i).getPosition() == SpellTrapPosition.UP) {
                ImageView imageView = oppSpellTraps.get(i);
                imageView.setImage(spellTrapZone.get(i).getCard().getImage());
                imageView.setRotate(180);
            } else {
                ImageView imageView = oppSpellTraps.get(i);
                imageView.setImage(Card.getUnknownImage());
            }
        }
    }

    class CardImageView extends ImageView {
        Card card;
        boolean isVisible;

        public CardImageView(Card card) {
            this(card, true);
        }

        public CardImageView(Card card, boolean isVisible) {
            this.card = card;
            this.isVisible = isVisible;
            if (isVisible)
                setImage(card.getImage());
            else
                setImage(Card.getUnknownImage());

            setOnMouseEntered(e -> {
                if (isVisible) {
                    cardInfo.setImage(card.getImage());
                    cardDetails.setText(card.getDescription());
                } else {
                    cardInfo.setImage(Card.getUnknownImage());
                    cardDetails.setText("");
                }
            });
        }
    }
}