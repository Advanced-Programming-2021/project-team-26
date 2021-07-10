package fxmlController;

import Utilities.Alert;
import Utilities.GetFXML;
import controller.*;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.CardAddress;
import model.Game;
import model.Owner;
import model.Place;
import model.cards.Card;
import model.cards.SpellTrap;
import model.cards.monster.Monster;
import model.cards.spell.Spell;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GameView implements Initializable {

    public static final String MY_PHASE_STYLE = "-fx-background-color: #3ba8e2;";
    public static final String OPPONENT_PHASE_STYLE = "-fx-background-color: #e01313;";
    public static final String MY_CURRENT_PHASE_STYLE = "-fx-background-color: #3ba8e2;-fx-border-color: #001a82; -fx-border-width: 5";
    public static final String OPPONENT_CURRENT_PHASE_STYLE = "-fx-background-color: #e01313;-fx-border-color: #570000; -fx-border-width: 5";
    private final ArrayList<CardImageView> myMonsters = new ArrayList<>();
    private final ArrayList<CardImageView> mySpellTraps = new ArrayList<>();
    private final ArrayList<CardImageView> oppMonsters = new ArrayList<>();
    private final ArrayList<CardImageView> oppSpellTraps = new ArrayList<>();
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
    public ImageView myGraveyard;
    public ImageView oppGraveyard;
    public ImageView field;

    @FXML
    private AnchorPane root;

    @FXML
    private CardImageView opoSpellTrap4;

    @FXML
    private CardImageView opoSpellTrap2;

    @FXML
    private CardImageView opoSpellTrap1;

    @FXML
    private CardImageView opoSpellTrap3;

    @FXML
    private CardImageView opoSpellTrap5;

    @FXML
    private CardImageView oppMonster5;

    @FXML
    private CardImageView oppMonster3;

    @FXML
    private CardImageView oppMonster1;

    @FXML
    private CardImageView oppMonster2;

    @FXML
    private CardImageView oppMonster4;

    @FXML
    private CardImageView myMonster5;

    @FXML
    private CardImageView myMonster3;

    @FXML
    private CardImageView myMonster1;

    @FXML
    private CardImageView myMonster2;

    @FXML
    private CardImageView myMonster4;

    @FXML
    private CardImageView mySpellTrap5;

    @FXML
    private CardImageView mySpellTrap3;

    @FXML
    private CardImageView mySpellTrap1;

    @FXML
    private CardImageView mySpellTrap2;

    @FXML
    private CardImageView mySpellTrap4;

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

    private int attacker = -1;

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
            setButtonActivity(standbyPhaseBut, null, false);
            standbyPhaseBut.setStyle(MY_CURRENT_PHASE_STYLE);
        } else
            setButtonActivity(standbyPhaseBut, null, false);
        if (Phase.DRAW.compareTo(phase) == 0) {
            found = true;
            setButtonActivity(drawPhaseBut, null, false);
            drawPhaseBut.setStyle(MY_CURRENT_PHASE_STYLE);
        } else
            setButtonActivity(drawPhaseBut, this::drawPhase, found);
        if (Phase.MAIN1.compareTo(phase) == 0) {
            found = true;
            setButtonActivity(mainPhase1But, null, false);
            mainPhase1But.setStyle(MY_CURRENT_PHASE_STYLE);
        } else
            setButtonActivity(mainPhase1But, this::mainPhase1, found);
        if (Phase.BATTLE.compareTo(phase) == 0) {
            found = true;
            setButtonActivity(battlePhaseBut, this::battlePhase, false);
            battlePhaseBut.setStyle(MY_CURRENT_PHASE_STYLE);
        } else
            setButtonActivity(battlePhaseBut, this::battlePhase, found);
        if (Phase.MAIN2.compareTo(phase) == 0) {
            found = true;
            setButtonActivity(mainPhase2But, null, false);
            mainPhase2But.setStyle(MY_CURRENT_PHASE_STYLE);
        } else
            setButtonActivity(mainPhase2But, this::mainPhase2, found);
        if (Phase.END.compareTo(phase) == 0) {
            setButtonActivity(endPhaseBut, null, false);
            endPhaseBut.setStyle(MY_CURRENT_PHASE_STYLE);
        } else
            setButtonActivity(endPhaseBut, this::endPhase, found);
    }

    private void setButtonActivity(Button button, EventHandler<ActionEvent> e, boolean active) {
        if (active) {
            button.setOnAction(e);
            button.setCursor(Cursor.HAND);
        } else {
            button.setOnAction(null);
            button.setCursor(Cursor.DEFAULT);
        }
    }


    private void updatePhaseOpponent() {
        standbyPhaseBut.setStyle(OPPONENT_PHASE_STYLE);
        drawPhaseBut.setStyle(OPPONENT_PHASE_STYLE);
        mainPhase1But.setStyle(OPPONENT_PHASE_STYLE);
        battlePhaseBut.setStyle(OPPONENT_PHASE_STYLE);
        mainPhase2But.setStyle(OPPONENT_PHASE_STYLE);
        endPhaseBut.setStyle(OPPONENT_PHASE_STYLE);

        setButtonActivity(standbyPhaseBut, null, false);
        setButtonActivity(drawPhaseBut, null, false);
        setButtonActivity(mainPhase1But, null, false);
        setButtonActivity(battlePhaseBut, null, false);
        setButtonActivity(mainPhase2But, null, false);
        setButtonActivity(endPhaseBut, null, false);

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
        oppGraveyard.setRotate(180);
        initField();
        intiFieldClick();
        setMyGraveyardOnClick();
        setOppGraveyardOnClick();
    }

    private void setOppGraveyardOnClick() {
        oppGraveyard.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                showOppGraveyard();
            }
        });
    }

    private void showOppGraveyard() {
        Stage stage = new Stage();
        AnchorPane root = null;
        GridPane showGraveyard = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/graveyard.fxml"));
            showGraveyard = (GridPane) root.lookup("#showMyGraveyard");
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Card> graveyard = gameController.getGame().getBoard(1 - turn).getGraveyard();
        for (int i = 0; i < graveyard.size(); i++) {
            ImageView imageView = new ImageView();
            imageView.setImage(graveyard.get(i).getImage());
            imageView.setFitWidth(Size.CARD_WIDTH_IN_GRAVEYARD.getValue());
            imageView.setFitHeight(Size.CARD_HEIGHT_IN_GRAVEYARD.getValue());
            showGraveyard.add(imageView, i, 0);
        }

        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    private void setMyGraveyardOnClick() {
        myGraveyard.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                showMyGraveyard();
            }
        });
    }


    public void showMyGraveyard() {
        Stage stage = new Stage();
        AnchorPane root = null;
        GridPane showMyGraveyard = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/graveyard.fxml"));
            showMyGraveyard = (GridPane) root.lookup("#showMyGraveyard");
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Card> graveyard = gameController.getGame().getBoard(turn).getGraveyard();
        for (int i = 0; i < graveyard.size(); i++) {
            ImageView imageView = new ImageView();
            imageView.setFitWidth(Size.CARD_WIDTH_IN_GRAVEYARD.getValue());
            imageView.setFitHeight(Size.CARD_HEIGHT_IN_GRAVEYARD.getValue());
            imageView.setImage(graveyard.get(i).getImage());
            showMyGraveyard.add(imageView, i, 0);
        }

        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    private void intiFieldClick() {
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            myMonsters.get(i).setOnMouseClicked(e -> {
                if (myMonsters.get(finalI).getImage() == null)
                    return;
                switch (gameController.getGame().getPhase()) {
                    case BATTLE:
                        attacker = finalI;
                        break;
                    case MAIN1:
                    case MAIN2:
                        break;
                }
            });
        }

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            oppMonsters.get(i).setOnMouseClicked(e -> {
                if (oppMonsters.get(finalI).getImage() == null)
                    return;
                switch (gameController.getGame().getPhase()) {
                    case BATTLE:
                        if (attacker == -1)
                            return;
                        try {
                            String result = gameController.attack(attacker, finalI);
                            Alert.getInstance().successfulPrint(result);
                            attacker = -1;
                        } catch (Exception exception) {
                            Alert.getInstance().errorPrint(exception.getMessage());
                        }

                        break;
                    case MAIN1:
                    case MAIN2:
                        break;
                }
            });
        }

        opponentHand.setOnMouseClicked(e -> {
            switch (gameController.getGame().getPhase()) {
                case BATTLE:
                    if (attacker == -1)
                        return;
                    try {
                        String result = gameController.attackDirect(attacker);
                        attacker = -1;
                        Alert.getInstance().successfulPrint(result);
                    } catch (Exception exception) {
                        Alert.getInstance().errorPrint(exception.getMessage());
                    }

                    break;
                case MAIN1:
                case MAIN2:
                    break;
            }
        });
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

        for (CardImageView imageView : myMonsters)
            imageView.setGameView(this);
        for (CardImageView imageView : mySpellTraps)
            imageView.setGameView(this);
        for (CardImageView imageView : oppMonsters)
            imageView.setGameView(this);
        for (CardImageView imageView : oppSpellTraps)
            imageView.setGameView(this);
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
        CardImageView imageView = new CardImageView(this, card, true);
        imageView.setFitHeight(Size.CARD_HEIGHT_IN_SHOP.getValue());
        imageView.setFitWidth(Size.CARD_WIDTH_IN_SHOP.getValue());
        imageView.setX(975);
        imageView.setY(425);
        imageView.setFitHeight(92);
        imageView.setFitWidth(92);
        root.getChildren().add(imageView);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), (ActionEvent event) -> {
            if (imageView.getY() < 550){
                imageView.setY(imageView.getY() + 5);
            }
        }));

        timeline.setCycleCount(25);
        timeline.play();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    int number = myHand.getChildren().size();
                    myHand.add(imageView, number, 0);
                });
            }
        }, 5000);

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

    public void moveFromOpponentDeckToHand(Card addedCard) {
        CardImageView imageView = new CardImageView(this, addedCard, false);
        imageView.setFitHeight(Size.CARD_HEIGHT_IN_SHOP.getValue());
        imageView.setFitWidth(Size.CARD_WIDTH_IN_SHOP.getValue());
        int number = opponentHand.getChildren().size();

        opponentHand.add(imageView, number, 0);
    }

    private void activateSpellTrap(Card card, ImageView imageView) {
        try {
            gameController.activateEffect(turn, card, new CardAddress(Place.Hand, Owner.Me));
        } catch (Exception e) {
            Alert.getInstance().errorPrint(e.getMessage());
            e.printStackTrace();
        }
    }

    private void setSpellTrap(Card card, ImageView imageView) {
        try {
            gameController.set(turn, card);
        } catch (Exception e) {
            Alert.getInstance().errorPrint(e.getMessage());
            e.printStackTrace();
        }
    }

    private void summonMonster(Card card, ImageView imageView) {
        try {
            gameController.summon(turn, card);
        } catch (Exception e) {
            Alert.getInstance().errorPrint(e.getMessage());
            e.printStackTrace();
        }
    }

    private void setMonster(Card card, ImageView imageView) {
        try {
            gameController.set(turn, card);
        } catch (Exception e) {
            Alert.getInstance().errorPrint(e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateOpponentMonsterZone() {
        HashMap<Integer, MonsterController> monsterZone = gameController.getGame().getBoard(1 - turn).getMonsterZoneMap();
        for (int i = 0; i < 5; i++) {
            if (!monsterZone.containsKey(i)) {
                CardImageView imageView = oppMonsters.get(i);
                imageView.removeCard();
            } else if (monsterZone.get(i).getPosition() == MonsterPosition.ATTACK) {
                CardImageView imageView = oppMonsters.get(i);
                imageView.addCard(monsterZone.get(i).getMonster(), true);
                imageView.setRotate(180);
            } else if (monsterZone.get(i).getPosition() == MonsterPosition.DEFENCE_UP) {
                CardImageView imageView = oppMonsters.get(i);
                imageView.addCard(monsterZone.get(i).getMonster(), true);
                imageView.setRotate(90);
            } else {
                CardImageView imageView = oppMonsters.get(i);
                imageView.addCard(monsterZone.get(i).getMonster(), false);
                imageView.setRotate(90);
            }
        }

    }

    public void updateOpponentSpellTraps() {
        HashMap<Integer, SpellTrapController> spellTrapZone = gameController.getGame().getBoard(1 - turn).getSpellTrapZoneMap();
        for (int i = 0; i < 5; i++) {
            if (!spellTrapZone.containsKey(i)) {
                CardImageView imageView = oppSpellTraps.get(i);
                imageView.removeCard();
            } else if (spellTrapZone.get(i).getPosition() == SpellTrapPosition.UP) {
                CardImageView imageView = oppSpellTraps.get(i);
                imageView.addCard(spellTrapZone.get(i).getCard(), true);
                imageView.setRotate(180);
            } else {
                CardImageView imageView = oppSpellTraps.get(i);
                imageView.addCard(spellTrapZone.get(i).getCard(), false);
            }
        }
    }

    public void updateMyMonsterZone() {
        HashMap<Integer, MonsterController> monsterZone = gameController.getGame().getBoard(turn).getMonsterZoneMap();
        for (int i = 0; i < 5; i++) {
            if (!monsterZone.containsKey(i)) {
                CardImageView imageView = myMonsters.get(i);
                imageView.removeCard();
            } else if (monsterZone.get(i).getPosition() == MonsterPosition.ATTACK) {
                CardImageView imageView = myMonsters.get(i);
                imageView.addCard(monsterZone.get(i).getMonster(), true);
            } else if (monsterZone.get(i).getPosition() == MonsterPosition.DEFENCE_UP) {
                CardImageView imageView = myMonsters.get(i);
                imageView.addCard(monsterZone.get(i).getMonster(), true);
                imageView.setRotate(270);
            } else {
                CardImageView imageView = myMonsters.get(i);
                imageView.addCard(monsterZone.get(i).getMonster(), false);
                imageView.setRotate(270);
            }
        }

    }

    public void updateMySpellTraps() {
        HashMap<Integer, SpellTrapController> spellTrapZone = gameController.getGame().getBoard(turn).getSpellTrapZoneMap();
        for (int i = 0; i < 5; i++) {
            if (!spellTrapZone.containsKey(i)) {
                CardImageView imageView = mySpellTraps.get(i);
                imageView.removeCard();
            } else if (spellTrapZone.get(i).getPosition() == SpellTrapPosition.UP) {
                CardImageView imageView = mySpellTraps.get(i);
                imageView.addCard(spellTrapZone.get(i).getCard(), true);
            } else {
                CardImageView imageView = mySpellTraps.get(i);
                imageView.addCard(spellTrapZone.get(i).getCard(), false);
            }
        }
    }

    public void updateMyHand() {
        List<Card> hand = gameController.getGame().getBoard(turn).getHand();
        myHand.getChildren().clear();
        for (Card card : hand) {
            ImageView imageView = new fxmlController.CardImageView(this, card, true);
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
    }

    public void updateOpponentHand() {
        List<Card> hand = gameController.getGame().getBoard(1 - turn).getHand();
        while (hand.size() != opponentHand.getChildren().size()) {
            if (opponentHand.getChildren().size() < hand.size()) {
                int number = opponentHand.getChildren().size();
                ImageView imageView = new ImageView();
                imageView.setImage(Card.getUnknownImage());
                imageView.setFitHeight(Size.CARD_HEIGHT_IN_SHOP.getValue());
                imageView.setFitWidth(Size.CARD_WIDTH_IN_SHOP.getValue());
                opponentHand.add(imageView, number, 0);
            } else if (opponentHand.getChildren().size() > hand.size()) {
                opponentHand.getChildren().remove(0);
            }
        }
    }

    public void escape() {
        Stage stage = new Stage();
        VBox root;
        Button yes;
        Button no;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/askSurrender.fxml"));
            yes = (Button) root.lookup("#yes");
            no = (Button) root.lookup("#no");
        } catch (IOException e) {
            Label label = new Label("want to surrender?");
            yes = new Button("yes");
            no = new Button("no");
            HBox hBox = new HBox(yes, no);
            hBox.setSpacing(5);
            hBox.setAlignment(Pos.CENTER);
            root = new VBox(label, hBox);
            root.setSpacing(5);
            root.setAlignment(Pos.CENTER);
        }
        yes.setOnAction(e -> {
            gameController.surrender();
            stage.close();
        });
        no.setOnAction(e -> {
            stage.close();
        });
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }


    public void updateLifePoint() {
        int myLifePoint = gameController.getGame().getLifePoint(turn);
        myLP.setText(String.valueOf(myLifePoint));
        updateProgressBar(myLPProgress, 1.0 * myLifePoint / Game.LIFE_POINT);
        int opponentLifePoint = gameController.getGame().getLifePoint(1 - turn);
        opponentLP.setText(String.valueOf(opponentLifePoint));
        updateProgressBar(opponentLPProgress, 1.0 * opponentLifePoint / Game.LIFE_POINT);
    }

    private void updateProgressBar(ProgressBar progressBar, double value) {
        progressBar.setProgress(value);
        if (value >= 0.8)
            progressBar.setStyle("-fx-accent:green");
        else if (0.4 <= value && value < 0.8)
            progressBar.setStyle("-fx-accent:yellow");
        else
            progressBar.setStyle("-fx-accent:red");
    }

    public void updateMyGraveyard(Card card) {
        myGraveyard.setImage(card.getImage());
    }

    public void updateOppGraveyard(Card card) {
        oppGraveyard.setImage(card.getImage());
    }

    public void updateFieldImage(Spell spell) {
        String path = "file:" + System.getProperty("user.dir") + "/src/main/resources/Assets/Field/" + spell.getName() + ".bmp";
        field.setImage(new Image(path));
    }
}