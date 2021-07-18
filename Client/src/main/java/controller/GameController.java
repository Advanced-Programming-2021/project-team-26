package controller;

import com.google.gson.Gson;
import exceptions.*;
import fxmlController.App;
import fxmlController.GameView;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.*;
import model.cards.Card;
import model.cards.SpellTrap;
import model.cards.monster.Monster;
import model.cards.spell.Spell;
import model.cards.spell.SpellType;
import view.Print;
import view.Scan;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameController {
    private final int[] maxLifePoint = new int[]{0, 0};
    private final int[] winningRounds = new int[]{0, 0};
    private final User[] players = new User[2];
    private final Deck[] decks = new Deck[2];
    private final Stack<SpellTrapController> chain = new Stack<>();
    private final int roundNumber;
    private GameView view;
    private Stage stage;
    private Caller caller;
    private Game game;
    private CardAddress selectedCardAddress = null;
    private Card selectedCard = null;
    private MonsterController selectedMonster = null;
    private SpellTrapController selectedSpellTrap = null;
    private boolean temporaryTurnChange = false;
    private int myTurn;
    private int currentRound = 0;

    public GameController(User firstPlayer, User secondPayer, int round, int myTurn) throws NoPlayerAvailable {
        this.myTurn = myTurn;
        players[0] = firstPlayer;
        players[1] = secondPayer;
        decks[0] = (Deck) players[0].getActiveDeck().clone();
        decks[1] = (Deck) players[1].getActiveDeck().clone();

        view = new GameView(this, myTurn);

        stage = new Stage();
        this.game = new Game(this, players[0], players[1], decks[0], decks[1]);
        this.roundNumber = round;
    }

    public GameController(User player, int round) throws NoPlayerAvailable {
        players[0] = player;
        players[1] = new Ai(this, 1);
        decks[0] = (Deck) players[0].getActiveDeck().clone();
        decks[1] = (Deck) players[1].getActiveDeck().clone();
        this.game = new Game(this, players[0], players[1], decks[0], decks[1]);
        ((Ai) players[1]).init();
        this.roundNumber = round;
        game.nextPhase();
    }

    public GameView[] getViews() {
        return views;
    }

    public void run() {
        initCaller();
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(type -> {
            try {
                if (type == GameView.class) {
                    return view;
                }
                return type.newInstance();
            } catch (Exception exc) {
                throw new RuntimeException(exc);
            }
        });
        loader.setLocation(GameController.class.getResource("/fxml/" + "game" + ".fxml"));

        try {
            Parent root = loader.load();

            Scene scene = new Scene(root);
            addEscape(scene);
            addDebugMode(scene);
            stage.setScene(scene);
            stage.setResizable(false);
            App.getStage().close();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getGame().getBoard(0).initBoard();
        getGame().getBoard(1).initBoard();
        game.nextPhase();
    }

    private void initCaller() {
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            String port = String.valueOf(serverSocket.getLocalPort());
            Request request = new Request("", "");
            request.addParameter("port", port);
            NetworkController.getInstance().sendRequest(request);
            Socket socket = serverSocket.accept();
            caller = new Caller(this, view, socket);
            caller.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addDebugMode(Scene scene) {
        KeyCombination key = new KeyCodeCombination(KeyCode.C,
                KeyCombination.CONTROL_DOWN,
                KeyCodeCombination.SHIFT_DOWN);

        scene.getAccelerators().put(key, () -> {
            new Thread(() -> {
                System.out.println("debugging mode 0");
                String input = new Scanner(System.in).nextLine();
                handleDebug(input, myTurn);
            }).start();
        });
    }

    private void handleDebug(String input, int turn) {
        if (!Database.getInstance().isDebuggingMode())
            return;
        Matcher matcher;
        if ((matcher = Pattern.compile("select --hand (.+) --force").matcher(input)).find()) {
            String cardName = matcher.group(1);
            Card card = Card.getCard(cardName);
            if (card == null)
                return;
            Platform.runLater(() -> game.getBoard(turn).addCardToHand(card));
        } else if ((matcher = Pattern.compile("increase --LP (-?\\d+)").matcher(input)).find()) {
            Matcher finalMatcher = matcher;
            Platform.runLater(() -> increaseLP(finalMatcher));
        } else if ((matcher = Pattern.compile("duel set-winner (.+)").matcher(input)).find()) {
            Matcher finalMatcher1 = matcher;
            Platform.runLater(() -> setWinner(finalMatcher1));
        }
    }

    private void addEscape(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE)
                view.escape();
        });
    }

    public boolean isTemporaryTurnChange() {
        return temporaryTurnChange;
    }

    public void setTemporaryTurnChange(boolean temporaryTurnChange) {
        this.temporaryTurnChange = temporaryTurnChange;
    }

    public Game getGame() {
        Request request = new Request("GameController", "getGame");
        Response response = NetworkController.getInstance().sendAndReceive(request);
        this.game = new Gson().fromJson(response.getData("game"), Game.class);
        this.game.setGameController(this);
        return this.game;
    }

    public void deselect() {
        selectedCardAddress = null;
        selectedCard = null;
        selectedMonster = null;
        selectedSpellTrap = null;
    }

    public void nextPhase(Phase phase) {
        int myTurn = game.getTurn();
        while (myTurn == game.getTurn() && game.getPhase().compareTo(phase) < 0)
            game.nextPhase();
    }

    public String summon(Card card) throws NoCardSelectedException, CannotSummonException, ActionNotAllowed,
            MonsterNotFoundException, FullMonsterZone, AlreadySummonException, NotEnoughCardForTribute,
            InvalidSelection {

        Request request = new Request("GameController", "summon");
        request.addParameter("card", card.getName());
        Response response = NetworkController.getInstance().sendAndReceive(request);
        if (response.isSuccess())
            return "summoned successfully";
        else
            throw new RuntimeException(response.getMessage());
    }

    public String set(Card card) throws NoCardSelectedException, CannotSetException {
        Request request = new Request("GameController", "set");
        request.addParameter("card", card.getName());

        Response response = NetworkController.getInstance().sendAndReceive(request);
        if (response.isSuccess())
            return "set successfully";
        else
            throw new RuntimeException(response.getMessage());
    }

    public String setPosition(int index) {
        Request request = new Request("GameController", "setPosition");
        request.addParameter("index", String.valueOf(index));

        Response response = NetworkController.getInstance().sendAndReceive(request);
        if (response.isSuccess())
            return "position set successfully";
        else
            throw new RuntimeException(response.getMessage());
    }

    public String flipSummon(int turn, int index) {
        if (turn != game.getTurn())
            return null;
        selectedMonster = game.getThisBoard().getMonsterByIndex(index);
        if (temporaryTurnChange)
            throw new NotYourTurnException();

        if (game.getPhase() != Phase.MAIN1 && game.getPhase() != Phase.MAIN2)
            throw new ActionNotAllowed();

        if (selectedMonster.getPosition() != MonsterPosition.DEFENCE_DOWN || selectedMonster.isMonsterNew())
            throw new CannotFlipSummon();

        activeOpponentTrapOnSummon(selectedMonster, "flip");
        selectedMonster.flip();
        deselect();
        return "flip summoned successfully";
    }

    public String attackDirect(int attacker) {
        Request request = new Request("GameController", "attackDirect");
        request.addParameter("index", String.valueOf(attacker));

        Response response = NetworkController.getInstance().sendAndReceive(request);
        if (response.isSuccess())
            return "direct attack was successful";
        else
            throw new RuntimeException(response.getMessage());
    }

    private void updateViewsGameBoard() {
        view.updateMyMonsterZone();
        view.updateMySpellTraps();
        view.updateOpponentMonsterZone();
        view.updateOpponentSpellTraps();
    }

    public String attack(int attacker, int number) {
        Request request = new Request("GameController", "attack");
        request.addParameter("index", String.valueOf(attacker));
        request.addParameter("number", String.valueOf(number));

        Response response = NetworkController.getInstance().sendAndReceive(request);
        if (response.isSuccess())
            return "attack was successful";
        else
            throw new RuntimeException(response.getMessage());
    }


    public String activateEffect(Card card, CardAddress address) {
        Request request = new Request("GameController", "activateEffect");
        request.addParameter("card", card);
        request.addParameter("address", address);

        Response response = NetworkController.getInstance().sendAndReceive(request);

        if (response.isSuccess())
            return "spell activated";
        else
            throw new RuntimeException(response.getMessage());
    }

    private void activateEffectOnOpponentTurn() {
        if (selectedCard == null)
            throw new NoCardSelectedException();

        if (selectedCardAddress.getOwner() != Owner.Me ||
                !(selectedCard instanceof SpellTrap))
            throw new CannotActivateException();

        SpellTrap spellTrap = (SpellTrap) selectedCard;
        if (selectedCardAddress.getPlace() == Place.SpellTrapZone) {
            chain.push(selectedSpellTrap);
            while (!chain.isEmpty()) {
                SpellTrapController current = chain.pop();
                current.activate();
            }
        } else if (selectedCardAddress.getPlace() == Place.Hand) {
            if (spellTrap instanceof Spell && ((Spell) spellTrap).getType() != SpellType.QUICK_PLAY) {
                throw new ActionNotAllowed();
            }
            SpellTrapController controller = game.getThisBoard().putSpellTrap(spellTrap, SpellTrapPosition.UP);
            chain.push(controller);
            while (!chain.isEmpty()) {
                SpellTrapController current = chain.pop();
                current.activate();
            }
            controller.remove();
        }
        deselect();
    }

    public void surrender() {
        game.setSurrenderPlayer(game.getTurn());
        endGame();
    }

    public void endGame() {
        int winner = game.getWinner();
        int[] scores = new int[2];
        scores[winner] = 1000;
        scores[1 - winner] = 0;
        Print.getInstance().printMessage(game.getUser(winner).getUsername() + " won the game" +
                " and the score is: " + scores[0] + "-" + scores[1]);
        winningRounds[winner]++;
        maxLifePoint[winner] = Math.max(maxLifePoint[winner], game.getLifePoint(winner));
        if (winningRounds[0] > roundNumber / 2 || winningRounds[1] > roundNumber / 2) {
            endMatch();
        } else {
            currentRound++;
            changeDeck(0);
            changeDeck(1);
            try {
                game = new Game(this, players[0], players[1], decks[0], decks[1]);
            } catch (NoPlayerAvailable ignored) {

            }
        }
    }

    private void changeDeck(int turn) {
        //TODO make it graphic
        if (players[turn] instanceof Ai)
            return;
        Print print = Print.getInstance();
        print.printMessage(decks[turn].showDeck("main"));
        print.printMessage(decks[turn].showDeck("side"));

        print.printMessage("\"<card name> side\" : put <card name> from main to side");
        print.printMessage("\"<card name> main\" : put <card name> from side to main");
        print.printMessage("\"show\" : show deck");
        print.printMessage("\"end\" : finish arranging deck");

        int mainDeckChange = 0;
        String command;
        Pattern sidePattern = Pattern.compile("[\\w ]* side");
        Pattern mainPattern = Pattern.compile("[\\w ]* main");
        while (true) {
            command = Scan.getInstance().getString();
            Matcher matcher;
            if ((matcher = sidePattern.matcher(command)).find()) {
                String name = matcher.group(1);
                if (!decks[turn].getMainDeck().contains(name)) {
                    print.printMessage("no such card");
                    continue;
                }
                decks[turn].getMainDeck().remove(name);
                decks[turn].getSideDeck().add(name);
                mainDeckChange--;
            } else if ((matcher = mainPattern.matcher(command)).find()) {
                String name = matcher.group(1);
                if (!decks[turn].getSideDeck().contains(name)) {
                    print.printMessage("no such card");
                    continue;
                }
                decks[turn].getSideDeck().remove(name);
                decks[turn].getMainDeck().add(name);
                mainDeckChange++;
            } else if (command.equals("show")) {
                print.printMessage(decks[turn].showDeck("main"));
                print.printMessage(decks[turn].showDeck("side"));
            } else if (command.equals("end")) {
                if (mainDeckChange != 0) {
                    print.printMessage("main deck card number shouldn't change");
                } else
                    break;
            }
        }
    }

    public void endMatch() {
        int winner;
        if (winningRounds[0] > roundNumber / 2)
            winner = 0;
        else
            winner = 1;
        int looser = 1 - winner;
        int winnerScore = 1000 * roundNumber;
        int looserScore = 0;

        int winnerPrize = roundNumber * (1000 + maxLifePoint[winner]);
        int looserPrize = roundNumber * 100;

        players[winner].increaseScore(winnerScore);
        players[looser].increaseScore(looserScore);
        players[winner].increaseMoney(winnerPrize);
        players[looser].increaseMoney(looserPrize);

        int[] scores = new int[2];
        scores[winner] = winnerScore;
        scores[looser] = looserScore;
        Print.getInstance().printMessage(players[winner].getUsername() + " won the whole match" +
                " with score: " + scores[0] + "-" + scores[1]);

        closeGame();
    }

    private void closeGame() {
        caller.end();
        stage.close();
        App.getStage().show();
    }

    public void showBoard() {

    }

    public boolean conditionsForChangingTurn() {
        return false;
    }

    public void finishTemporaryChangeTurn() {
        if (temporaryTurnChange)
            game.temporaryChangeTurn();
    }

    public AttackResult activeOpponentTrapOnAttack(MonsterController attacker, MonsterController defender) {
        AttackResult result = null;
        boolean found = false;
        for (SpellTrapController controller : game.getOtherBoard().getSpellTrapZone()) {
            if (controller instanceof TrapController) {
                TrapController trap = (TrapController) controller;
                if (trap.canActiveOnAttacked(attacker, defender)) {
                    if (!found) {
                        found = true;
                        game.temporaryChangeTurn();
                    }
                    Boolean answer = views[game.getTurn()].ask("do you want to activate " + trap.getCard().getName());
                    if (answer != null && answer) {
                        result = trap.onAttacked(attacker, defender);
                        trap.remove();
                    }
                }
            }
        }
        if (found) {
            game.temporaryChangeTurn();
        }
        return result;
    }

    public boolean activeOpponentTrapOnSummon(MonsterController summoned, String type) {
        boolean result = false;
        boolean found = false;
        for (SpellTrapController controller : game.getOtherBoard().getSpellTrapZone()) {
            if (controller instanceof TrapController) {
                TrapController trap = (TrapController) controller;
                if (trap.canActiveOnSummon(summoned, type)) {
                    if (!found) {
                        found = true;
                        game.temporaryChangeTurn();
                    }
                    Boolean answer = views[game.getTurn()].ask("do you want to activate " + trap.getCard().getName());
                    if (answer != null && answer) {
                        result |= trap.onSummon(summoned, type);
                        trap.remove();
                    }
                }
            }
        }
        if (found) {
            game.temporaryChangeTurn();
        }
        return result;
    }

    public void changeTurn() {
        if (!temporaryTurnChange && conditionsForChangingTurn()) {
            game.temporaryChangeTurn();
            Boolean answer = views[game.getTurn()].ask("do you want to activate your trap and spell?");
            if (answer != null && !answer) {
                game.temporaryChangeTurn();
            }
        }
    }

    public String increaseLP(Matcher matcher) {
        if (!Database.getInstance().isDebuggingMode())
            throw new InvalidInput();

        String lpString = matcher.group(1);
        if (lpString == null)
            throw new InvalidInput();
        int lp = Integer.parseInt(lpString);
        game.decreaseThisLifePoint(-lp);
        return "LifePoint added";
    }

    public String setWinner(Matcher matcher) {
        if (!Database.getInstance().isDebuggingMode())
            throw new InvalidInput();
        String nickname = matcher.group(1);
        if (game.getThisUser().getNickname().equals(nickname)) {
            game.setFinished(true);
            game.setWinner(game.getTurn());
            endGame();
            return null;
        } else if (game.getOtherUser().getNickname().equals(nickname)) {
            game.setFinished(true);
            game.setWinner(1 - game.getTurn());
            endGame();
            return null;
        } else
            throw new InvalidInput();
    }
}
