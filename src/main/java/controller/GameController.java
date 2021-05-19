package controller;

import exceptions.*;
import model.*;
import model.cards.Card;
import model.cards.SpellTrap;
import model.cards.monster.Monster;
import model.cards.spell.Spell;
import model.cards.spell.SpellType;
import model.cards.trap.Trap;
import view.Menu;
import view.Print;
import view.Scan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;

public class GameController {
    private final int[] maxLifePoint = new int[]{0, 0};
    private final int[] winningRounds = new int[]{0, 0};
    private final User[] players = new User[2];
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
        this.game = new Game(this, firstPlayer, secondPayer);
        this.roundNumber = round;
        game.nextPhase();
    }

    public GameController(User player, int round) throws NoPlayerAvailable {
        players[0] = player;
        players[1] = new Ai(this);
        this.game = new Game(this, players[0], players[1]);
        this.roundNumber = round;
        game.nextPhase();
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
                if (game.getOtherBoard().getMonstersZone()[monsterNumber - 1] != null) {
                    selectedCard = game.getOtherBoard().getMonstersZone()[monsterNumber - 1].getCard();
                    selectedCardAddress = new CardAddress(Place.MonsterZone, Owner.Opponent, monsterNumber - 1);
                }
            } else {
                if (game.getThisBoard().getMonstersZone()[monsterNumber - 1] != null) {
                    selectedMonster = game.getThisBoard().getMonstersZone()[monsterNumber - 1];
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
                if (game.getOtherBoard().getSpellTrapZone()[spellNumber - 1] != null) {
                    selectedCard = game.getOtherBoard().getSpellTrapZone()[spellNumber - 1].getCard();
                    selectedCardAddress = new CardAddress(Place.SpellTrapZone, Owner.Opponent, spellNumber - 1);
                }
            } else {
                if (game.getThisBoard().getSpellTrapZone()[spellNumber - 1] != null) {
                    selectedSpellTrap = game.getThisBoard().getSpellTrapZone()[spellNumber - 1];
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

    public void nextPhase() {
        game.nextPhase();
    }

    public String summon(Matcher matcher) throws NoCardSelectedException, CannotSummonException, ActionNotAllowed,
            MonsterNotFoundException, FullMonsterZone, AlreadySummonException, NotEnoughCardForTribute,
            InvalidSelection {
        if (temporaryTurnChange)
            throw new NotYourTurnException();
        if (selectedCard == null)
            throw new NoCardSelectedException();

        if (selectedCardAddress.getOwner() != Owner.Me ||
                selectedCardAddress.getPlace() != Place.Hand ||
                !(selectedCard instanceof Monster))
            throw new CannotSummonException();

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

            Integer monsterAddress = Scan.getInstance().getInteger();
            if (monsterAddress == null)
                return null;
            if (monsterAddress >= Board.CARD_NUMBER_IN_ROW ||
                    game.getThisBoard().getMonstersZone()[monsterAddress - 1] == null)
                throw new CardNotFoundInPositionException("there no monsters one this address");

            game.getThisBoard().removeMonster(monsterAddress - 1);
        } else if (selectedMonster.getLevel() > 6) {
            if (game.getThisBoard().getMonsterZoneNumber() < 2)
                throw new NotEnoughCardForTribute();

            Integer monsterAddress1 = Scan.getInstance().getInteger();
            Integer monsterAddress2 = Scan.getInstance().getInteger();
            if (monsterAddress1 == null || monsterAddress2 == null)
                return null;
            if (monsterAddress1 >= Board.CARD_NUMBER_IN_ROW ||
                    monsterAddress2 >= Board.CARD_NUMBER_IN_ROW ||
                    game.getThisBoard().getMonstersZone()[monsterAddress1 - 1] == null ||
                    game.getThisBoard().getMonstersZone()[monsterAddress2 - 1] == null)
                throw new CardNotFoundInPositionException("there no monsters one this address");

            game.getThisBoard().removeMonster(monsterAddress1 - 1);
            game.getThisBoard().removeMonster(monsterAddress2 - 1);
        }
        MonsterController monster = game.getThisBoard().putMonster(selectedMonster, MonsterPosition.ATTACK);
        game.setSummonOrSetThisTurn(true);
        monster.summon();
        deselect();
        return "summoned successfully";
    }

    public String set(Matcher matcher) throws NoCardSelectedException, CannotSetException {
        if (temporaryTurnChange)
            throw new NotYourTurnException();
        if (selectedCard == null)
            throw new NoCardSelectedException();

        if (selectedCardAddress.getOwner() != Owner.Me ||
                selectedCardAddress.getPlace() != Place.Hand)
            throw new CannotSetException();

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
    }

    private void setSpell() {
        if (game.getThisBoard().getSpellTrapZoneNumber() >= Board.CARD_NUMBER_IN_ROW)
            throw new FullSpellTrapZone();

        Spell spell = (Spell) selectedCard;
        game.getThisBoard().putSpellTrap(spell, SpellTrapPosition.DOWN);
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

            Integer monsterAddress = Scan.getInstance().getInteger();
            if (monsterAddress == null)
                return;
            if (monsterAddress >= Board.CARD_NUMBER_IN_ROW ||
                    game.getThisBoard().getMonstersZone()[monsterAddress - 1] == null)
                throw new CardNotFoundInPositionException("there no monsters one this address");

            game.getThisBoard().removeMonster(monsterAddress - 1);
        } else if (selectedMonster.getLevel() > 6) {
            if (game.getThisBoard().getMonsterZoneNumber() < 2)
                throw new NotEnoughCardForTribute();

            Integer monsterAddress1 = Scan.getInstance().getInteger();
            Integer monsterAddress2 = Scan.getInstance().getInteger();
            if (monsterAddress1 == null || monsterAddress2 == null)
                return;
            if (monsterAddress1 >= Board.CARD_NUMBER_IN_ROW ||
                    monsterAddress2 >= Board.CARD_NUMBER_IN_ROW ||
                    game.getThisBoard().getMonstersZone()[monsterAddress1 - 1] == null ||
                    game.getThisBoard().getMonstersZone()[monsterAddress2 - 1] == null)
                throw new CardNotFoundInPositionException("there no monsters one this address");

            game.getThisBoard().removeMonster(monsterAddress1 - 1);
            game.getThisBoard().removeMonster(monsterAddress2 - 1);
        }
        MonsterController monster = game.getThisBoard().putMonster(selectedMonster, MonsterPosition.DEFENCE_DOWN);
        game.setSummonOrSetThisTurn(true);
        monster.set();
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

        selectedMonster.flip();
        deselect();
        return "flip summoned successfully";
    }

    public String attackDirect(Matcher matcher) {
        if (temporaryTurnChange)
            throw new NotYourTurnException();
        if (selectedCard == null)
            throw new NoCardSelectedException();

        if (selectedCardAddress.getOwner() != Owner.Me ||
                selectedCardAddress.getPlace() != Place.MonsterZone ||
                !(selectedCard instanceof Monster))
            throw new CannotAttackException();

        if (game.getPhase() != Phase.BATTLE)
            throw new ActionNotAllowed();

        if (selectedMonster.isHasAttackedThisTurn())
            throw new AlreadyAttackedException();

        if (game.getOtherBoard().canDirectAttack())
            throw new CannotAttackDirectlyException();

        int damage = selectedMonster.getCard().getAttackPower();
        game.decreaseOtherLifePoint(damage);
        deselect();
        return "you opponent receives " + damage + " battle damage";
    }

    public String attack(Matcher matcher) {
        if (temporaryTurnChange)
            throw new NotYourTurnException();
        if (selectedCard != null)
            throw new NoCardSelectedException();

        if (selectedCardAddress.getOwner() != Owner.Me ||
                selectedCardAddress.getPlace() != Place.MonsterZone ||
                !(selectedCard instanceof Monster) ||
                selectedMonster.getPosition() != MonsterPosition.ATTACK)
            throw new CannotAttackException();

        if (game.getPhase() != Phase.BATTLE)
            throw new ActionNotAllowed();

        if (selectedMonster.isHasAttackedThisTurn())
            throw new AlreadyAttackedException();

        int number = Integer.parseInt(matcher.group(1));
        number--;
        MonsterController toBeAttacked = game.getOtherBoard().getMonstersZone()[number];
        if (toBeAttacked == null || toBeAttacked.canBeAttacked(selectedMonster))
            throw new NoCardToAttackException();

        String message = toBeAttacked.attack(selectedMonster);
        deselect();
        return message;
    }


    public String activateEffect(Matcher matcher) {
        if (temporaryTurnChange) {
            activateEffectOnOpponentTurn();
            return null;
        }
        if (selectedCard == null)
            throw new NoCardSelectedException();

        if (selectedCardAddress.getOwner() != Owner.Me ||
                !(selectedCard instanceof Spell))
            throw new CannotActivateException();

        if (game.getPhase() != Phase.MAIN1 && game.getPhase() != Phase.MAIN2)
            throw new ActionNotAllowed();

        if (selectedCardAddress.getPlace() == Place.SpellTrapZone)
            throw new AlreadyActivatedException();

        Spell spell = (Spell) selectedCard;
        if (game.getThisBoard().getSpellTrapZoneNumber() >= Board.CARD_NUMBER_IN_ROW &&
                spell.getType() != SpellType.FIELD)
            throw new FullSpellTrapZone();

        if (!selectedSpellTrap.conditionMet())
            throw new PreparationFailException();

        if (spell.getType() == SpellType.FIELD) {
            game.getThisBoard().putFiled(spell);
        } else {
            SpellController controller = (SpellController) game.getThisBoard().putSpellTrap(spell, SpellTrapPosition.UP);
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

    public String surrender(Matcher matcher) {
        game.setSurrenderPlayer(game.getTurn());
        endGame();
        return null;
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
            try {
                game = new Game(this, players[0], players[1]);
            } catch (NoPlayerAvailable ignored) {

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

        Menu.exitMenu(null);
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

    public void changeTurn() {
        if (!temporaryTurnChange && conditionsForChangingTurn()) {
            game.temporaryChangeTurn();
            Print.getInstance().printMessage("do you want to activate your trap and spell?");
            String answer = Scan.getInstance().getString();
            if (!answer.equals("yse")) {
                game.temporaryChangeTurn();
            }
        }
    }
}
