package controller;

import Utilities.Alert;
import exceptions.*;
import fxmlController.App;
import fxmlController.GameView;
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
import view.Print;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameController {
    private final int[] maxLifePoint = new int[]{0, 0};
    private final int[] winningRounds = new int[]{0, 0};
    private final User[] players = new User[2];
    private final Stack<SpellTrapTransfer> chain = new Stack<>();
    private final int roundNumber;
    private final GameView view;
    private final Stage stage;
    private final Game game;
    private final int myTurn;
    private Caller caller;

    public GameController(User firstPlayer, User secondPayer, int round, int myTurn) throws NoPlayerAvailable {
        this.myTurn = myTurn;
        players[0] = firstPlayer;
        players[1] = secondPayer;
        Deck[] decks = new Deck[2];
        decks[0] = (Deck) players[0].getActiveDeck().clone();
        decks[1] = (Deck) players[1].getActiveDeck().clone();

        view = new GameView(this, myTurn);

        stage = new Stage();
        this.game = new Game(players[0], players[1]);
        this.roundNumber = round;
    }

    public GameView[] getViews() {
        return null;
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
        caller.start();
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
                handleDebug(input);
            }).start();
        });
    }

    private void handleDebug(String input) {
        if (!Database.getInstance().isDebuggingMode())
            return;
        Matcher matcher;
        if ((matcher = Pattern.compile("select --hand (.+) --force").matcher(input)).find()) {
            String cardName = matcher.group(1);
            addCardToHand(cardName);
        } else if ((matcher = Pattern.compile("increase --LP (-?\\d+)").matcher(input)).find()) {
            increaseLP(matcher);
        } else if ((matcher = Pattern.compile("duel set-winner (.+)").matcher(input)).find()) {
            setWinner(matcher);
        }
    }

    private void addCardToHand(String cardName) {
        Request request = new Request("GameController","addCardToHand");
        request.addParameter("cardName",cardName);
        NetworkController.getInstance().sendAndReceive(request);
    }

    private void addEscape(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE)
                view.escape();
        });
    }

    public Game getGame() {
        return this.game;
    }


    public void nextPhase(Phase phase) {
        Request request = new Request("GameController","nextPhase");
        request.addParameter("phase",phase);
        NetworkController.getInstance().sendAndReceive(request);
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

    public String attackDirect(int attacker) {
        Request request = new Request("GameController", "attackDirect");
        request.addParameter("attacker", String.valueOf(attacker));

        Response response = NetworkController.getInstance().sendAndReceive(request);
        if (response.isSuccess())
            return response.getMessage();
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
        request.addParameter("attacker", String.valueOf(attacker));
        request.addParameter("number", String.valueOf(number));

        Response response = NetworkController.getInstance().sendAndReceive(request);
        if (response.isSuccess())
            return response.getMessage();
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


    public void surrender() {
        Request request = new Request("GameController","surrender");
        request.addParameter("turn",myTurn);
        NetworkController.getInstance().sendAndReceive(request);
    }

    public void endGame(String message) {
        Alert.getInstance().successfulPrint(message);
    }

    private void changeDeck(int turn) {
        //TODO make it graphic
//        if (players[turn] instanceof Ai)
//            return;
//        Print print = Print.getInstance();
//        print.printMessage(decks[turn].showDeck("main"));
//        print.printMessage(decks[turn].showDeck("side"));
//
//        print.printMessage("\"<card name> side\" : put <card name> from main to side");
//        print.printMessage("\"<card name> main\" : put <card name> from side to main");
//        print.printMessage("\"show\" : show deck");
//        print.printMessage("\"end\" : finish arranging deck");
//
//        int mainDeckChange = 0;
//        String command;
//        Pattern sidePattern = Pattern.compile("[\\w ]* side");
//        Pattern mainPattern = Pattern.compile("[\\w ]* main");
//        while (true) {
//            command = Scan.getInstance().getString();
//            Matcher matcher;
//            if ((matcher = sidePattern.matcher(command)).find()) {
//                String name = matcher.group(1);
//                if (!decks[turn].getMainDeck().contains(name)) {
//                    print.printMessage("no such card");
//                    continue;
//                }
//                decks[turn].getMainDeck().remove(name);
//                decks[turn].getSideDeck().add(name);
//                mainDeckChange--;
//            } else if ((matcher = mainPattern.matcher(command)).find()) {
//                String name = matcher.group(1);
//                if (!decks[turn].getSideDeck().contains(name)) {
//                    print.printMessage("no such card");
//                    continue;
//                }
//                decks[turn].getSideDeck().remove(name);
//                decks[turn].getMainDeck().add(name);
//                mainDeckChange++;
//            } else if (command.equals("show")) {
//                print.printMessage(decks[turn].showDeck("main"));
//                print.printMessage(decks[turn].showDeck("side"));
//            } else if (command.equals("end")) {
//                if (mainDeckChange != 0) {
//                    print.printMessage("main deck card number shouldn't change");
//                } else
//                    break;
//            }
//        }
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

    public void increaseLP(Matcher matcher) {
        if (!Database.getInstance().isDebuggingMode())
            throw new InvalidInput();

        String lpString = matcher.group(1);
        if (lpString == null)
            throw new InvalidInput();
        int lp = Integer.parseInt(lpString);
        Request request = new Request("GameController","increaseLP");
        request.addParameter("lp",lp);
        NetworkController.getInstance().sendAndReceive(request);
    }

    public void setWinner(Matcher matcher) {
        if (!Database.getInstance().isDebuggingMode())
            throw new InvalidInput();
        String nickname = matcher.group(1);
        Request request = new Request("GameController","setWinner");
        request.addParameter("nickname",nickname);
        NetworkController.getInstance().sendAndReceive(request);
    }
}
