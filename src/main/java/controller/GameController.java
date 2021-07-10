package controller;

import exceptions.*;
import fxmlController.App;
import fxmlController.GameView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameController {
    private final int[] maxLifePoint = new int[]{0, 0};
    private final int[] winningRounds = new int[]{0, 0};
    private final User[] players = new User[2];
    private final Deck[] decks = new Deck[2];
    private final GameView[] views = new GameView[2];
    private final Stage[] stages = new Stage[2];
    private final Stack<SpellTrapController> chain = new Stack<>();
    private final int roundNumber;
    private Game game;
    private CardAddress selectedCardAddress = null;
    private Card selectedCard = null;
    private MonsterController selectedMonster = null;
    private SpellTrapController selectedSpellTrap = null;
    private boolean temporaryTurnChange = false;
    private int currentRound = 0;

    public GameController(User firstPlayer, User secondPayer, int round) throws NoPlayerAvailable {
        players[0] = firstPlayer;
        players[1] = secondPayer;
        decks[0] = (Deck) players[0].getActiveDeck().clone();
        decks[1] = (Deck) players[1].getActiveDeck().clone();

        views[0] = new GameView(this, 0);
        views[1] = new GameView(this, 1);

        stages[0] = new Stage();
        stages[1] = new Stage();
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
        FXMLLoader firstLoader = new FXMLLoader();
        firstLoader.setControllerFactory(type -> {
            try {
                if (type == GameView.class) {
                    return views[0];
                }
                return type.newInstance();
            } catch (Exception exc) {
                throw new RuntimeException(exc);
            }
        });
        firstLoader.setLocation(GameController.class.getResource("/fxml/" + "game" + ".fxml"));

        FXMLLoader secondLoader = new FXMLLoader();
        secondLoader.setControllerFactory(type -> {
            try {
                if (type == GameView.class)
                    return views[1];

                return type.newInstance();
            } catch (Exception exc) {
                throw new RuntimeException(exc);
            }
        });
        secondLoader.setLocation(GameController.class.getResource("/fxml/" + "game" + ".fxml"));

        try {
            Parent firstRoot = firstLoader.load();
            Parent secondRoot = secondLoader.load();

            Scene[] scenes = new Scene[2];
            scenes[0] = new Scene(firstRoot);
            scenes[1] = new Scene(secondRoot);
            addEscape(scenes);
            stages[0].setScene(scenes[0]);
            stages[1].setScene(scenes[1]);
            App.getStage().close();
            stages[0].show();
            stages[1].show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        getGame().getBoard(0).initBoard();
        getGame().getBoard(1).initBoard();
        game.nextPhase();
    }

    private void addEscape(Scene[] scenes) {
        scenes[0].addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE)
                views[0].escape();
        });
        scenes[1].addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE)
                views[1].escape();
        });
    }

    public boolean isTemporaryTurnChange() {
        return temporaryTurnChange;
    }

    public void setTemporaryTurnChange(boolean temporaryTurnChange) {
        this.temporaryTurnChange = temporaryTurnChange;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String select(Matcher matcher) throws InvalidSelection, CardNotFoundException, InvalidInput, NoCardSelectedException {
        HashMap<String, String> input = Scan.getInstance().parseInput(matcher.group());

        String addressNumber;
        if ((addressNumber = Scan.getInstance().getValue(input, "monster", "m")) != null) {
            int monsterNumber = Integer.parseInt(addressNumber);
            if (monsterNumber > Board.CARD_NUMBER_IN_ROW)
                throw new InvalidSelection();

            if (input.containsKey("opponent") || input.containsKey("o")) {
                if (game.getOtherBoard().getMonsterByIndex(monsterNumber - 1) != null) {
                    selectedMonster = game.getOtherBoard().getMonsterByIndex(monsterNumber - 1);
                    selectedCard = game.getOtherBoard().getMonsterByIndex(monsterNumber - 1).getCard();
                    selectedCardAddress = new CardAddress(Place.MonsterZone, Owner.Opponent, monsterNumber - 1);
                }
            } else {
                if (game.getThisBoard().getMonsterByIndex(monsterNumber - 1) != null) {
                    selectedMonster = game.getThisBoard().getMonsterByIndex(monsterNumber - 1);
                    selectedCard = selectedMonster.getCard();
                    selectedCardAddress = new CardAddress(Place.MonsterZone, Owner.Me, monsterNumber - 1);
                }
            }

            if (selectedCard == null)
                throw new CardNotFoundInPositionException();

        } else if ((addressNumber = Scan.getInstance().getValue(input, "spell", "s")) != null) {
            int spellNumber = Integer.parseInt(addressNumber);
            if (spellNumber > Board.CARD_NUMBER_IN_ROW)
                throw new InvalidSelection();

            if (input.containsKey("opponent") || input.containsKey("o")) {
                if (game.getOtherBoard().getSpellTrapByIndex(spellNumber - 1) != null) {
                    selectedSpellTrap = game.getOtherBoard().getSpellTrapByIndex(spellNumber - 1);
                    selectedCard = game.getOtherBoard().getSpellTrapByIndex(spellNumber - 1).getCard();
                    selectedCardAddress = new CardAddress(Place.SpellTrapZone, Owner.Opponent, spellNumber - 1);
                }
            } else {
                if (game.getThisBoard().getSpellTrapByIndex(spellNumber - 1) != null) {
                    selectedSpellTrap = game.getThisBoard().getSpellTrapByIndex(spellNumber - 1);
                    selectedCard = selectedSpellTrap.getCard();
                    selectedCardAddress = new CardAddress(Place.SpellTrapZone, Owner.Me, spellNumber - 1);
                }
            }

            if (selectedCard == null)
                throw new CardNotFoundInPositionException();

        } else if (Scan.getInstance().getValue(input, "field", "f") != null) {
            if (input.containsKey("opponent") || input.containsKey("o")) {
                if (game.getOtherBoard().getFieldZone() != null) {
                    selectedSpellTrap = game.getOtherBoard().getFieldZone();
                    selectedCard = selectedSpellTrap.getCard();
                    selectedCardAddress = new CardAddress(Place.Field, Owner.Opponent);
                }
            } else {
                if (game.getThisBoard().getFieldZone() != null) {
                    selectedSpellTrap = game.getThisBoard().getFieldZone();
                    selectedCard = selectedSpellTrap.getCard();
                    selectedCardAddress = new CardAddress(Place.Field, Owner.Me);
                }
            }

            if (selectedCard == null)
                throw new CardNotFoundInPositionException();
        } else if ((addressNumber = Scan.getInstance().getValue(input, "hand", "h")) != null) {
            if (input.containsKey("force") || input.containsKey("f")) {
                if (Database.getInstance().isDebuggingMode()) {
                    String cardName = addressNumber;
                    Card card = Card.getCard(cardName);
                    if (card == null)
                        throw new CardNotFoundException();
                    game.getThisBoard().addCardToHand(card);
                    int cardIndex = game.getThisBoard().getHand().size() - 1;
                    selectedCard = card;
                    selectedCardAddress = new CardAddress(Place.Hand, Owner.Me, cardIndex);
                    return "card selected";
                } else {
                    throw new InvalidInput();
                }
            }
            int handNumber = Integer.parseInt(addressNumber);
            if (handNumber > game.getThisBoard().getHand().size())
                throw new InvalidSelection();

            selectedCard = game.getThisBoard().getHand().get(handNumber - 1);
            selectedCardAddress = new CardAddress(Place.Hand, Owner.Me, handNumber - 1);
        } else if (input.containsKey("-d")) {
            if (selectedCard == null) {
                throw new NoCardSelectedException();
            }
            deselect();
            return "card deselected";
        } else
            throw new InvalidInput();
        return "card selected";
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
        selectedCard = card;

        if (temporaryTurnChange)
            throw new NotYourTurnException();
        if (selectedCard == null)
            throw new NoCardSelectedException();

        if (game.getPhase() != Phase.MAIN1 && game.getPhase() != Phase.MAIN2)
            throw new ActionNotAllowed();

        if (game.getThisBoard().getMonsterZoneNumber() >= Board.CARD_NUMBER_IN_ROW)
            throw new FullMonsterZone();

        if (game.isSummonOrSetThisTurn())
            throw new AlreadySummonException();

        Monster selectedMonster = (Monster) selectedCard;

        if (selectedMonster.getLevel() > 4 && selectedMonster.getLevel() <= 6) {
            if (game.getThisBoard().getMonsterZoneNumber() < 1)
                throw new NotEnoughCardForTribute();
            Print.getInstance().printMessage("Enter the address of the monster you want to tribute:");

            Integer monsterAddress = Scan.getInstance().getInteger();
            if (monsterAddress == null)
                return null;
            if (monsterAddress >= Board.CARD_NUMBER_IN_ROW ||
                    game.getThisBoard().getMonsterByIndex(monsterAddress - 1) == null)
                throw new CardNotFoundInPositionException("There no monsters one this address");

            game.getThisBoard().removeMonster(monsterAddress - 1);
        } else if (selectedMonster.getLevel() > 6) {
            if (game.getThisBoard().getMonsterZoneNumber() < 2)
                throw new NotEnoughCardForTribute();

            Print.getInstance().printMessage("Enter the address of the first monster you want to tribute:");
            Integer monsterAddress1 = Scan.getInstance().getInteger();

            Print.getInstance().printMessage("Enter the address of the second monster you want to tribute:");
            Integer monsterAddress2 = Scan.getInstance().getInteger();

            if (monsterAddress1 == null || monsterAddress2 == null)
                return null;
            if (monsterAddress1 >= Board.CARD_NUMBER_IN_ROW ||
                    monsterAddress2 >= Board.CARD_NUMBER_IN_ROW ||
                    game.getThisBoard().getMonsterByIndex(monsterAddress1 - 1) == null ||
                    game.getThisBoard().getMonsterByIndex(monsterAddress2 - 1) == null)
                throw new CardNotFoundInPositionException("there no monsters one this address");

            game.getThisBoard().removeMonster(monsterAddress1 - 1);
            game.getThisBoard().removeMonster(monsterAddress2 - 1);
        }
        MonsterController monster = game.getThisBoard().putMonster(selectedMonster, MonsterPosition.ATTACK);
        game.setSummonOrSetThisTurn(true);
        monster.summon();
        for (MonsterController monsterEffect : game.getThisBoard().getMonstersZone())
            monsterEffect.runMonsterEffectAtSummon();

        if (game.getThisBoard().getFieldZone() != null)
            game.getThisBoard().getFieldZone().runFieldEffectAtSummon();

        if (game.getOtherBoard().getFieldZone() != null)
            game.getOtherBoard().getFieldZone().runFieldEffectAtSummon();

        deselect();
        if (activeOpponentTrapOnSummon(monster, "normal")) {
            return null;
        }

        views[1 - game.getTurn()].showOpponentMonsterZone();
        return "summoned successfully";
    }

    public String set(Card card) throws NoCardSelectedException, CannotSetException {
        selectedCard = card;

        if (temporaryTurnChange)
            throw new NotYourTurnException();
        if (selectedCard == null)
            throw new NoCardSelectedException();


        if (game.getPhase() != Phase.MAIN1 && game.getPhase() != Phase.MAIN2)
            throw new ActionNotAllowed();

        if (selectedCard instanceof Monster) {
            setMonster();
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
        views[1 - game.getTurn()].showOpponentSpellTraps();
    }

    private void setSpell() {
        if (game.getThisBoard().getSpellTrapZoneNumber() >= Board.CARD_NUMBER_IN_ROW)
            throw new FullSpellTrapZone();

        Spell spell = (Spell) selectedCard;
        game.getThisBoard().putSpellTrap(spell, SpellTrapPosition.DOWN);
        views[1 - game.getTurn()].showOpponentSpellTraps();
    }

    private void setMonster() {
        if (game.getThisBoard().getMonsterZoneNumber() >= Board.CARD_NUMBER_IN_ROW)
            throw new FullMonsterZone();

        if (game.isSummonOrSetThisTurn())
            throw new AlreadySummonException();

        Monster selectedMonster = (Monster) selectedCard;

        if (selectedMonster.getLevel() > 4 && selectedMonster.getLevel() <= 6) {
            if (game.getThisBoard().getMonsterZoneNumber() < 1)
                throw new NotEnoughCardForTribute();

            Print.getInstance().printMessage("Enter the address of the monster you want to tribute:");
            Integer monsterAddress = Scan.getInstance().getInteger();
            if (monsterAddress == null)
                return;
            if (monsterAddress >= Board.CARD_NUMBER_IN_ROW ||
                    game.getThisBoard().getMonsterByIndex(monsterAddress - 1) == null)
                throw new CardNotFoundInPositionException("there no monsters one this address");

            game.getThisBoard().removeMonster(monsterAddress - 1);
        } else if (selectedMonster.getLevel() > 6) {
            if (game.getThisBoard().getMonsterZoneNumber() < 2)
                throw new NotEnoughCardForTribute();

            Print.getInstance().printMessage("Enter the address of the first monster you want to tribute:");
            Integer monsterAddress1 = Scan.getInstance().getInteger();

            Print.getInstance().printMessage("Enter the address of the second monster you want to tribute:");
            Integer monsterAddress2 = Scan.getInstance().getInteger();

            if (monsterAddress1 == null || monsterAddress2 == null)
                return;
            if (monsterAddress1 >= Board.CARD_NUMBER_IN_ROW ||
                    monsterAddress2 >= Board.CARD_NUMBER_IN_ROW ||
                    game.getThisBoard().getMonsterByIndex(monsterAddress1 - 1) == null ||
                    game.getThisBoard().getMonsterByIndex(monsterAddress2 - 1) == null)
                throw new CardNotFoundInPositionException("there no monsters one this address");

            game.getThisBoard().removeMonster(monsterAddress1 - 1);
            game.getThisBoard().removeMonster(monsterAddress2 - 1);
        }
        MonsterController monster = game.getThisBoard().putMonster(selectedMonster, MonsterPosition.DEFENCE_DOWN);
        game.setSummonOrSetThisTurn(true);
        monster.set();
        views[1 - game.getTurn()].showOpponentMonsterZone();
    }

    public String setPosition(Matcher matcher) {
        if (temporaryTurnChange)
            throw new NotYourTurnException();
        if (selectedCard == null)
            throw new NoCardSelectedException();

        if (selectedCardAddress.getOwner() != Owner.Me ||
                selectedCardAddress.getPlace() != Place.MonsterZone ||
                !(selectedCard instanceof Monster))
            throw new CannotChangeException();

        if (game.getPhase() != Phase.MAIN1 && game.getPhase() != Phase.MAIN2)
            throw new ActionNotAllowed();

        MonsterPosition wantedPosition;
        String position = matcher.group(1);
        if (position.equals("attack"))
            wantedPosition = MonsterPosition.ATTACK;
        else if (position.equals("defence"))
            wantedPosition = MonsterPosition.DEFENCE_UP;
        else
            throw new InvalidInput();

        if (wantedPosition == MonsterPosition.ATTACK && selectedMonster.getPosition() != MonsterPosition.DEFENCE_UP)
            throw new CannotChangeException();
        if (wantedPosition == MonsterPosition.DEFENCE_UP && selectedMonster.getPosition() != MonsterPosition.ATTACK)
            throw new CannotChangeException();

        if (selectedMonster.isHasPositionChanged())
            throw new AlreadyChangeException();

        selectedMonster.setPosition(wantedPosition);
        deselect();
        return "monster card position changed successfully";
    }

    public String flipSummon(Matcher matcher) {
        if (temporaryTurnChange)
            throw new NotYourTurnException();
        if (selectedCard == null)
            throw new NoCardSelectedException();

        if (selectedCardAddress.getOwner() != Owner.Me ||
                selectedCardAddress.getPlace() != Place.MonsterZone ||
                !(selectedCard instanceof Monster))
            throw new CannotChangeException();

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
            views[i].showMyMonsterZone();
            views[i].showMySpellTraps();
            views[i].showOpponentMonsterZone();
            views[i].showOpponentSpellTraps();
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

        if (attackResult.isRemoveOpCard())
            toBeAttacked.remove(selectedMonster);
        if (attackResult.isRemoveMyCard())
            selectedMonster.remove(toBeAttacked);

        game.decreaseThisLifePoint(attackResult.getMyLPDecrease());
        game.decreaseOtherLifePoint(attackResult.getOpLPDecrease());
        updateViewsGameBoard();
        deselect();

        return attackResult.getMessage();
    }


    public String activateEffect(Card card, CardAddress address) {
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
                    Print.getInstance().printMessage("do you want to activate " + trap.getCard().getName());
                    String answer = Scan.getInstance().getString();
                    if (answer.equals("yes")) {
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
                    Print.getInstance().printMessage("do you want to activate " + trap.getCard().getName());
                    String answer = Scan.getInstance().getString();
                    if (answer.equals("yes")) {
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
            Print.getInstance().printMessage("do you want to activate your trap and spell?");
            String answer = Scan.getInstance().getString();
            if (!answer.equals("yes")) {
                game.temporaryChangeTurn();
            }
        }
    }

    public String increaseLP(Matcher matcher) {
        if (!Database.getInstance().isDebuggingMode())
            throw new InvalidInput();
        HashMap<String, String> input = Scan.getInstance().parseInput(matcher.group());
        String lpString = Scan.getInstance().getValue(input, "LP", "LP");
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
