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
import model.cards.trap.Trap;
import view.Print;
import view.Scan;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
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

    public String set(int turn, Card card) throws NoCardSelectedException, CannotSetException {
        if (turn != game.getTurn())
            return null;
        selectedCard = card;

        if (temporaryTurnChange)
            throw new NotYourTurnException();
        if (selectedCard == null)
            throw new NoCardSelectedException();


        if (game.getPhase() != Phase.MAIN1 && game.getPhase() != Phase.MAIN2)
            throw new ActionNotAllowed();

        if (selectedCard instanceof Monster) {
            setMonster(turn);
        } else if (selectedCard instanceof Spell) {
            setSpell();
        } else if (selectedCard instanceof Trap) {
            setTrap();
        }
        deselect();
        return "set successfully";
    }

    private void setTrap() {
        if (game.getThisBoard().getSpellTrapZoneNumber() >= Board.CARD_NUMBER_IN_ROW)
            throw new FullSpellTrapZone();

        Trap trap = (Trap) selectedCard;
        game.getThisBoard().putSpellTrap(trap, SpellTrapPosition.DOWN);
        views[1 - game.getTurn()].updateOpponentSpellTraps();
        views[1 - game.getTurn()].updateOpponentHand();
        views[game.getTurn()].updateMySpellTraps();
        views[game.getTurn()].updateMyHand();
    }

    private void setSpell() {
        if (game.getThisBoard().getSpellTrapZoneNumber() >= Board.CARD_NUMBER_IN_ROW)
            throw new FullSpellTrapZone();

        Spell spell = (Spell) selectedCard;
        game.getThisBoard().putSpellTrap(spell, SpellTrapPosition.DOWN);
        views[1 - game.getTurn()].updateOpponentSpellTraps();
        views[1 - game.getTurn()].updateOpponentHand();
        views[game.getTurn()].updateMySpellTraps();
        views[game.getTurn()].updateMyHand();
    }

    private void setMonster(int turn) {
        if (game.getThisBoard().getMonsterZoneNumber() >= Board.CARD_NUMBER_IN_ROW)
            throw new FullMonsterZone();

        if (game.isSummonOrSetThisTurn())
            throw new AlreadySummonException();

        Monster selectedMonster = (Monster) selectedCard;

        if (selectedMonster.getLevel() > 4 && selectedMonster.getLevel() <= 6) {
            if (game.getThisBoard().getMonsterZoneNumber() < 1)
                throw new NotEnoughCardForTribute();

            ArrayList<Card> options = new ArrayList<>();
            for (MonsterController monsterController : game.getThisBoard().getMonstersZone())
                options.add(monsterController.getMonster());
            ArrayList<Card> selected = views[turn].getCardInput(options, 1, "Select the monster you want to tribute:");

            if (selected.size() != 1)
                return;

            for (MonsterController monsterController : game.getThisBoard().getMonstersZone()) {
                if (monsterController.getMonster() == selected.get(0)) {
                    game.getThisBoard().removeMonster(monsterController);
                    break;
                }
            }
        } else if (selectedMonster.getLevel() > 6) {
            if (game.getThisBoard().getMonsterZoneNumber() < 2)
                throw new NotEnoughCardForTribute();

            ArrayList<Card> options = new ArrayList<>();
            for (MonsterController monsterController : game.getThisBoard().getMonstersZone())
                options.add(monsterController.getMonster());
            ArrayList<Card> selected = views[turn].getCardInput(options, 1, "Select the monsters you want to tribute");

            if (selected.size() != 2)
                return;

            for (MonsterController monsterController : game.getThisBoard().getMonstersZone()) {
                if (monsterController.getMonster() == selected.get(0)) {
                    game.getThisBoard().removeMonster(monsterController);
                    break;
                }
            }
            for (MonsterController monsterController : game.getThisBoard().getMonstersZone()) {
                if (monsterController.getMonster() == selected.get(1)) {
                    game.getThisBoard().removeMonster(monsterController);
                    break;
                }
            }
        }
        MonsterController monster = game.getThisBoard().putMonster(selectedMonster, MonsterPosition.DEFENCE_DOWN);
        game.setSummonOrSetThisTurn(true);
        monster.set();
        views[1 - game.getTurn()].updateOpponentMonsterZone();
        views[1 - game.getTurn()].updateOpponentHand();
        views[game.getTurn()].updateMyMonsterZone();
        views[game.getTurn()].updateMyHand();
    }

    public String setPosition(int turn, int index) {
        if (turn != game.getTurn())
            return null;
        selectedMonster = game.getThisBoard().getMonsterByIndex(index);
        if (temporaryTurnChange)
            throw new NotYourTurnException();

        if (game.getPhase() != Phase.MAIN1 && game.getPhase() != Phase.MAIN2)
            throw new ActionNotAllowed();

        MonsterPosition wantedPosition;
        if (selectedMonster.getPosition() == MonsterPosition.DEFENCE_DOWN) {
            return flipSummon(turn, index);
        }
        if (selectedMonster.getPosition() == MonsterPosition.ATTACK)
            wantedPosition = MonsterPosition.DEFENCE_UP;
        else
            wantedPosition = MonsterPosition.ATTACK;

        if (wantedPosition == MonsterPosition.ATTACK && selectedMonster.getPosition() != MonsterPosition.DEFENCE_UP)
            throw new CannotChangeException();
        if (wantedPosition == MonsterPosition.DEFENCE_UP && selectedMonster.getPosition() != MonsterPosition.ATTACK)
            throw new CannotChangeException();

        if (selectedMonster.isHasPositionChanged())
            throw new AlreadyChangeException();

        selectedMonster.setPosition(wantedPosition);
        selectedMonster.setHasPositionChanged(true);
        deselect();
        views[turn].updateMyMonsterZone();
        views[1 - turn].updateOpponentMonsterZone();
        return "monster card position changed successfully";
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
        selectedCard = game.getThisBoard().getMonsterByIndex(attacker).getCard();
        selectedMonster = game.getThisBoard().getMonsterByIndex(attacker);
        if (temporaryTurnChange)
            throw new NotYourTurnException();
        if (selectedCard == null)
            throw new NoCardSelectedException();

        if (game.getPhase() != Phase.BATTLE)
            throw new ActionNotAllowed();

        if (selectedMonster.isHasAttackedThisTurn())
            throw new AlreadyAttackedException();

        if (!game.getOtherBoard().canDirectAttack())
            throw new CannotAttackDirectlyException();

        AttackResult result = activeOpponentTrapOnAttack(selectedMonster, null);

        if (result == null) {
            result = new AttackResult(0, selectedMonster.getCard().getAttackPower(), false, false);
        }

        if (result.isRemoveMyCard())
            selectedMonster.remove(null);

        game.decreaseThisLifePoint(result.getMyLPDecrease());
        game.decreaseOtherLifePoint(result.getOpLPDecrease());
        updateViewsGameBoard();
        deselect();
        return result.getMessage();
    }

    private void updateViewsGameBoard() {
        for (int i = 0; i < 2; i++) {
            views[i].updateMyMonsterZone();
            views[i].updateMySpellTraps();
            views[i].updateOpponentMonsterZone();
            views[i].updateOpponentSpellTraps();
        }
    }

    public String attack(int attacker, int number) {
        selectedCard = game.getThisBoard().getMonsterByIndex(attacker).getCard();
        selectedMonster = game.getThisBoard().getMonsterByIndex(attacker);
        if (temporaryTurnChange)
            throw new NotYourTurnException();
        if (selectedCard == null)
            throw new NoCardSelectedException();

        if (selectedMonster.getPosition() != MonsterPosition.ATTACK)
            throw new CannotAttackException();

        if (game.getPhase() != Phase.BATTLE)
            throw new ActionNotAllowed();

        if (selectedMonster.isHasAttackedThisTurn())
            throw new AlreadyAttackedException();

        MonsterController toBeAttacked = game.getOtherBoard().getMonsterByIndex(number);
        if (toBeAttacked == null || !toBeAttacked.canBeAttacked(selectedMonster))
            throw new NoCardToAttackException();

        AttackResult attackResult = toBeAttacked.attack(selectedMonster);
        AttackResult trapEffect = activeOpponentTrapOnAttack(selectedMonster, toBeAttacked);
        if (trapEffect != null)
            attackResult = trapEffect;
        if (attackResult == null)
            attackResult = new AttackResult(selectedMonster, toBeAttacked);

        getViews()[game.getTurn()].attackAnimation(attacker, number, Owner.Me);
        getViews()[1 - game.getTurn()].attackAnimation(attacker, number, Owner.Opponent);

        Timer timer = new Timer();
        AttackResult finalAttackResult = attackResult;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (finalAttackResult.isRemoveOpCard())
                        toBeAttacked.remove(selectedMonster);
                    if (finalAttackResult.isRemoveMyCard())
                        selectedMonster.remove(toBeAttacked);

                    updateViewsGameBoard();
                });
            }
        }, 1440);

        game.decreaseThisLifePoint(attackResult.getMyLPDecrease());
        game.decreaseOtherLifePoint(attackResult.getOpLPDecrease());
        deselect();

        return attackResult.getMessage();
    }


    public String activateEffect(int turn, Card card, CardAddress address) {
        if (turn != game.getTurn())
            return null;
        selectedCard = card;
        selectedCardAddress = address;
        if (temporaryTurnChange) {
            activateEffectOnOpponentTurn();
            return null;
        }
        if (selectedCard == null)
            throw new NoCardSelectedException();

        if (game.getPhase() != Phase.MAIN1 && game.getPhase() != Phase.MAIN2)
            throw new ActionNotAllowed();

        if (selectedCardAddress.getPlace() == Place.SpellTrapZone &&
                selectedSpellTrap.getPosition() == SpellTrapPosition.UP)
            throw new AlreadyActivatedException();

        Spell spell = (Spell) selectedCard;

        if (spell.getType() == SpellType.FIELD) {
            SpellController spellController = game.getThisBoard().putFiled(spell);
            spellController.runFieldEffectAtSummon();
        } else {
            SpellTrapController controller;
            if (selectedCardAddress.getPlace() == Place.Hand) {
                if (game.getThisBoard().getSpellTrapZoneNumber() >= Board.CARD_NUMBER_IN_ROW)
                    throw new FullSpellTrapZone();
                controller = game.getThisBoard().putSpellTrap(spell, SpellTrapPosition.UP);
            } else {
                controller = selectedSpellTrap;
                if (controller.isSpellTrapNew())
                    throw new CannotActivateException();
            }

            chain.push(controller);
            if (conditionsForChangingTurn()) {
                changeTurn();
            } else {
                while (!chain.isEmpty()) {
                    SpellTrapController current = chain.pop();
                    current.activate();
                }
            }
        }
        deselect();
        views[1 - game.getTurn()].updateOpponentHand();
        views[game.getTurn()].updateMyHand();
        return "spell activated";
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

    public String showGraveyard(Matcher matcher) {
        List<Card> graveyard = game.getThisBoard().getGraveyard();
        if (graveyard.isEmpty()) {
            Print.getInstance().printMessage("graveyard empty");
        } else {
            int i = 1;
            for (Card card : graveyard) {
                Print.getInstance().printMessage(i + ". " + card.getName() + ":" + card.getDescription());
                i++;
            }
        }
        while (true) {
            if (Scan.getInstance().getString().equals("back"))
                break;
        }
        return null;
    }

    public String showCard(Matcher matcher) {
        String[] rawInput = matcher.group().split("\\s+");
        Map<String, String> input = Scan.getInstance().parseInput(matcher.group());
        if (input.containsKey("selected") || input.containsKey("s")) {
            if (selectedCard == null)
                throw new NoCardSelectedException();
            if (selectedCardAddress.getOwner() == Owner.Opponent) {
                if ((selectedCard instanceof Monster) && selectedMonster.getPosition() == MonsterPosition.DEFENCE_DOWN)
                    throw new CardNotVisibleException();
                if ((selectedCard instanceof SpellTrap) && selectedSpellTrap.getPosition() == SpellTrapPosition.DOWN)
                    throw new CardNotVisibleException();
            }
            Print.getInstance().printCard(selectedCard);
        } else {
            String cardName = rawInput[2];
            Card card = Card.getCard(cardName);
            if (card == null)
                throw new CardNotFoundInPositionException();
            Print.getInstance().printCard(card);
        }
        return null;
    }

    public void surrender() {
        game.setSurrenderPlayer(game.getTurn());
        endGame();
    }

    public boolean isFinished() {
        return false;
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
        stages[0].close();
        stages[1].close();
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
