package controller;

import Utilities.Alert;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fxmlController.CardImageView;
import fxmlController.Size;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.*;
import model.cards.Card;
import model.cards.spell.Spell;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GameView implements Initializable {

    public static final String OPPONENT_PHASE_STYLE = "-fx-background-color: #e01313;";
    public static final String OPPONENT_CURRENT_PHASE_STYLE = "-fx-background-color: #e01313;-fx-border-color: #570000; -fx-border-width: 5";
    private final ArrayList<CardImageView> myMonsters = new ArrayList<>();
    private final ArrayList<CardImageView> mySpellTraps = new ArrayList<>();
    private final ArrayList<CardImageView> oppMonsters = new ArrayList<>();
    private final ArrayList<CardImageView> oppSpellTraps = new ArrayList<>();
    private final GameController gameController;
    private final int turn;
    private final Handler handler;
    public Button drawPhaseBut;
    public Button standbyPhaseBut;
    public Button mainPhase1But;
    public Button battlePhaseBut;
    public Button mainPhase2But;
    public Button endPhaseBut;
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
    private Caller caller;

    public GameView(Handler handler, GameController controller, int turn) {
        this.handler = handler;
        this.gameController = controller;
        this.turn = turn;
    }

    public void updatePhase() {
        caller.sendAndReceive(new Request("view", "updatePhase"));
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
                        try {
                            gameController.setPosition(turn, finalI);
                        } catch (Exception exception) {
                            Alert.getInstance().errorPrint(exception.getMessage());
                        }
                        break;
                }
            });
        }

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            mySpellTraps.get(i).setOnMouseClicked(e -> {
                if (mySpellTraps.get(finalI).getImage() == null)
                    return;
                try {
                    //gameController.activateEffect(turn, mySpellTraps.get(finalI).card, new CardAddress(Place.SpellTrapZone, Owner.Me));
                } catch (Exception exception) {
                    Alert.getInstance().errorPrint(exception.getMessage());
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

    public void attackAnimation(int attackerMonster, int defenderMonster, Owner owner) {
        Request request = new Request("view", "attackAnimation");
        request.addParameter("attackerMonster", attackerMonster);
        request.addParameter("defenderMonster", defenderMonster);
        request.addParameter("owner", owner);

        caller.sendAndReceive(request);
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
            imageView.setGameView(null);
        for (CardImageView imageView : mySpellTraps)
            imageView.setGameView(null);
        for (CardImageView imageView : oppMonsters)
            imageView.setGameView(null);
        for (CardImageView imageView : oppSpellTraps)
            imageView.setGameView(null);
    }


    public void moveFromDeckToHand(Card card) {
        Request request = new Request("view", "moveFromDeckToHand");
        request.addParameter("card", card.getName());
        caller.sendAndReceive(request);
    }

    public void moveFromOpponentDeckToHand(Card addedCard) {
        Request request = new Request("view", "moveFromOpponentDeckToHand");
        request.addParameter("card", addedCard.getName());
        caller.sendAndReceive(request);
    }

    public void updateOpponentMonsterZone() {
        caller.sendAndReceive(new Request("view", "updateOpponentMonsterZone"));
    }

    public void updateOpponentSpellTraps() {
        caller.sendAndReceive(new Request("view", "updateOpponentSpellTraps"));
    }

    public void updateMyMonsterZone() {
        caller.sendAndReceive(new Request("view", "updateMyMonsterZone"));
    }

    public void updateMySpellTraps() {
        caller.sendAndReceive(new Request("view", "updateMySpellTraps"));
    }

    public void updateMyHand() {
        caller.sendAndReceive(new Request("view", "updateMyHand"));
    }

    public void updateOpponentHand() {
        caller.sendAndReceive(new Request("view", "updateOpponentHand"));
    }


    public void updateLifePoint() {
        caller.sendAndReceive(new Request("view", "updateLifePoint"));
    }

    public void updateMyGraveyard(Card card) {
        Request request = new Request("view", "updateMyGraveyard");
        request.addParameter("card", card.getName());
        caller.sendAndReceive(request);
    }

    public void updateOppGraveyard(Card card) {
        Request request = new Request("view", "updateOppGraveyard");
        request.addParameter("card", card.getName());
        caller.sendAndReceive(request);
    }

    public void updateFieldImage(Spell spell) {
        Request request = new Request("view", "updateFieldImage");
        request.addParameter("spell", spell.getName());
        caller.sendAndReceive(request);
    }

    public ArrayList<Card> getCardInput(ArrayList<Card> cards, int number, String message) {
        Request request = new Request("view", "getCardInput");
        request.addParameter("cards", cards);
        request.addParameter("number", number);
        request.addParameter("message", message);

        Response response = caller.sendAndReceive(request);
        if (!response.isSuccess())
            return new ArrayList<>();
        else {
            Type cardListType = new TypeToken<ArrayList<Card>>() {
            }.getType();
            ArrayList<Card> tmp = new Gson().fromJson(response.getData("selected"), cardListType);
            ArrayList<Card> selected = new ArrayList<>();
            for(Card card:tmp)
                selected.add(Card.getCard(card.getName()));
            return selected;
        }
    }

    public Boolean ask(String message) {
        Request request = new Request("view", "getCardInput");
        request.addParameter("message",message);

        Response response = caller.sendAndReceive(request);
        if(!response.isSuccess())
            return false;
        else {
            return new Gson().fromJson(response.getData("result"),Boolean.class);
        }
    }


    public String getString(String message) {
        Request request = new Request("view","getString");
        request.addParameter("message",message);
        Response response = caller.sendAndReceive(request);
        if(response.isSuccess())
            return response.getData("result");
        else
            return "";
    }

    public Response handle(Request request) {
        switch (request.getMethodToCall()) {
            case "summon":
                try {
                    String cardName = request.getParameter("card");
                    Card card = Card.getCard(cardName);
                    if (card == null)
                        return new Response(false, "card not found");
                    gameController.summon(turn, card);
                    return new Response(true, "summoned");
                } catch (Exception e) {
                    return new Response(false, e.getMessage());
                }
            case "set":
                try {
                    String cardName = request.getParameter("card");
                    Card card = Card.getCard(cardName);
                    if (card == null)
                        return new Response(false, "card not found");
                    gameController.set(turn, card);
                    return new Response(true, "set successfully");
                } catch (Exception e) {
                    return new Response(false, e.getMessage());
                }

            case "setPosition":
                try {
                    int index = Integer.parseInt(request.getParameter("index"));

                    gameController.setPosition(turn, index);
                    return new Response(true, "position set successfully");
                } catch (Exception e) {
                    return new Response(false, e.getMessage());
                }

            case "attackDirect":
                try {
                    int attacker = Integer.parseInt(request.getParameter("attacker"));

                    String message = gameController.attackDirect(attacker);
                    return new Response(true, message);
                } catch (Exception e) {
                    return new Response(false, e.getMessage());
                }

            case "attack":
                try {
                    int attacker = Integer.parseInt(request.getParameter("attacker"));
                    int number = Integer.parseInt(request.getParameter("number"));

                    String message = gameController.attack(attacker, number);
                    return new Response(true, message);
                } catch (Exception e) {
                    return new Response(false, e.getMessage());
                }
            case "activateEffect":
                try {
                    Card card = Card.getCard(request.getParameter("card"));
                    CardAddress address = new Gson().fromJson(request.getParameter("address"), CardAddress.class);

                    gameController.activateEffect(turn, card, address);
                    return new Response(true, "done");
                } catch (Exception e) {
                    return new Response(false, e.getMessage());
                }
            case "nextPhase":
                Phase phase = new Gson().fromJson(request.getParameter("phase"),Phase.class);
                gameController.nextPhase(turn,phase);
                return new Response(true,"");
            case "Board":
                int wantedTurn = Integer.parseInt(request.getParameter("turn"));
                Board board = gameController.getGame().getBoard(wantedTurn);
                return board.handle(request);
            case "Game":
                return gameController.getGame().handle(request);
            case "surrender":
                gameController.surrender(turn);
                return new Response(true, "");
            case "addCardToHand":
                String cardName = request.getParameter("cardName");
                gameController.addCardToHand(turn, cardName);
                return new Response(true, "");
            case "increaseLP":
                int lp = Integer.parseInt(request.getParameter("lp"));
                gameController.increaseLP(turn, lp);
                return new Response(true, "");
            case "setWinner":
                String nickname = request.getParameter("nickname");
                gameController.setWinner(nickname);
                return new Response(true,"done");
        }
        return new Response(false, "method not supported");
    }

    public void close() {
        handler.setView(null);
    }

    public void setCaller(Caller caller) {
        this.caller = caller;
    }
}