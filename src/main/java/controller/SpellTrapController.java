package controller;

import exceptions.CardNotFoundException;
import exceptions.InvalidInput;
import exceptions.InvalidSelection;
import exceptions.NoCardSelectedException;
import model.*;
import model.cards.Card;
import model.cards.SpellTrap;
import model.cards.spell.Spell;
import model.cards.trap.Trap;
import view.Scan;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class SpellTrapController {

    protected static ArrayList<SpellTrapController> allSpellTrapControllers = new ArrayList<>();
    protected GameController gameController;
    protected SpellTrapPosition position;
    protected boolean canSpellTrapsBeActive = false;
    protected boolean isHandAccessible = false;
    protected boolean isRivalMonsterZoneAccessible = false;
    protected boolean isOurMonsterZoneAccessible = false;
    protected boolean isRivalGraveyardAccessible = false;
    protected boolean isOurGraveyardAccessible = false;
    protected boolean isOurSpellTrapAccessible = false;
    protected boolean isRivalSpellTrapAccessible = false;
    protected Card selectedCard = null;
    protected CardAddress selectedCardAddress = null;
    protected Board thisBoard;
    protected Board otherBoard;
    protected int myTurn;
    protected boolean isSpellTrapNew = true;

    public static SpellTrapController getSpellTrapControllerBySpellTrap(SpellTrap spellTrap) {
        for (SpellTrapController spellTrapController : allSpellTrapControllers) {
            if (spellTrapController.getCard().getName().equals(spellTrap.getName())) {
                return spellTrapController;
            }
        }
        return null;
    }

    public static SpellTrapController getInstance(GameController gameController, SpellTrap spellTrap, SpellTrapPosition position, int myTurn) {
        SpellTrapController instance = null;
        if (spellTrap instanceof Spell)
            instance = SpellController.getInstance(gameController, (Spell) spellTrap, position);
        else if (spellTrap instanceof Trap)
            instance = TrapController.getInstance(gameController, (Trap) spellTrap, position);
        instance.myTurn = myTurn;
        instance.thisBoard = gameController.getGame().getBoard(myTurn);
        instance.otherBoard = gameController.getGame().getBoard(1 - myTurn);
        return instance;
    }

    public boolean isSpellTrapNew() {
        return isSpellTrapNew;
    }

    public void setCanSpellTrapsBeActive(boolean canSpellTrapsBeActive) {
        this.canSpellTrapsBeActive = canSpellTrapsBeActive;
    }

    public void setHandAccessible(boolean handAccessible) {
        isHandAccessible = handAccessible;
    }

    public void setRivalMonsterZoneAccessible(boolean rivalMonsterZoneAccessible) {
        isRivalMonsterZoneAccessible = rivalMonsterZoneAccessible;
    }

    public void setOurMonsterZoneAccessible(boolean ourMonsterZoneAccessible) {
        isOurMonsterZoneAccessible = ourMonsterZoneAccessible;
    }

    public void setRivalGraveyardAccessible(boolean rivalGraveyardAccessible) {
        isRivalGraveyardAccessible = rivalGraveyardAccessible;
    }

    public void setOurGraveyardAccessible(boolean ourGraveyardAccessible) {
        isOurGraveyardAccessible = ourGraveyardAccessible;
    }

    public void setSelectedCard(Card selectedCard) {
        this.selectedCard = selectedCard;
    }

    public void setSelectedCardAddress(CardAddress selectedCardAddress) {
        this.selectedCardAddress = selectedCardAddress;
    }

    public SpellTrapPosition getPosition() {
        return position;
    }

    protected void setPosition(SpellTrapPosition position) {
        this.position = position;
    }

    public GameController getGameController() {
        return gameController;
    }

    protected void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setOurSpellTrapAccessible(boolean ourSpellTrapAccessible) {
        isOurSpellTrapAccessible = ourSpellTrapAccessible;
    }

    public void setRivalSpellTrapAccessible(boolean rivalSpellTrapAccessible) {
        isRivalSpellTrapAccessible = rivalSpellTrapAccessible;
    }

    public void remove() {

    }

    public void activate() {

    }

    public abstract SpellTrap getCard();

    public abstract void standBy();

    public boolean conditionMet() {
        return true;
    }

    public void select(String selectCommand) throws InvalidSelection, CardNotFoundException, InvalidInput, NoCardSelectedException {
        HashMap<String, String> input = Scan.getInstance().parseInput(selectCommand);
        Game game = gameController.getGame();
        String addressNumber;


        if ((addressNumber = Scan.getInstance().getValue(input, "monster", "m")) != null) {
            int monsterNumber = Integer.parseInt(addressNumber);
            if (monsterNumber > Board.CARD_NUMBER_IN_ROW)
                throw new InvalidSelection();

            if (input.containsKey("opponent") || input.containsKey("o") && isRivalMonsterZoneAccessible) {
                if (game.getOtherBoard().getMonsterByIndex(monsterNumber - 1) != null) {
                    selectedCard = game.getOtherBoard().getMonsterByIndex(monsterNumber - 1).getMonster();
                    selectedCardAddress = new CardAddress(Place.MonsterZone, Owner.Opponent, monsterNumber - 1);
                }
            } else if (isOurMonsterZoneAccessible) {
                if (game.getThisBoard().getMonsterByIndex(monsterNumber - 1) != null) {
                    selectedCard = game.getThisBoard().getMonsterByIndex(monsterNumber - 1).getMonster();
                    selectedCardAddress = new CardAddress(Place.MonsterZone, Owner.Me, monsterNumber - 1);
                }
            } else throw new InvalidSelection();

            if (selectedCard == null)
                throw new CardNotFoundException();

        }
        if ((addressNumber = Scan.getInstance().getValue(input, "spellTrap", "s")) != null) {
            int spellTrap = Integer.parseInt(addressNumber);
            if (spellTrap > Board.CARD_NUMBER_IN_ROW)
                throw new InvalidSelection();

            if (input.containsKey("opponent") || input.containsKey("o") && isRivalSpellTrapAccessible) {
                if (game.getOtherBoard().getSpellTrapByIndex(spellTrap - 1) != null) {
                    selectedCard = game.getOtherBoard().getSpellTrapByIndex(spellTrap - 1).getCard();
                    selectedCardAddress = new CardAddress(Place.SpellTrapZone, Owner.Opponent, spellTrap - 1);
                }
            } else if (isOurSpellTrapAccessible) {
                if (game.getThisBoard().getSpellTrapByIndex(spellTrap - 1) != null) {
                    selectedCard = game.getThisBoard().getSpellTrapByIndex(spellTrap - 1).getCard();
                    selectedCardAddress = new CardAddress(Place.SpellTrapZone, Owner.Me, spellTrap - 1);
                }
            } else throw new InvalidSelection();

            if (selectedCard == null)
                throw new CardNotFoundException();

        } else if ((addressNumber = Scan.getInstance().getValue(input, "hand", "h")) != null && isHandAccessible) {
            int handNumber = Integer.parseInt(addressNumber);
            if (handNumber > game.getThisBoard().getHand().size())
                throw new InvalidSelection();

            selectedCard = game.getThisBoard().getHand().get(handNumber - 1);
            selectedCardAddress = new CardAddress(Place.Hand, Owner.Me, handNumber - 1);

            if (this.selectedCard == null)
                throw new CardNotFoundException();

        } else if ((addressNumber = Scan.getInstance().getValue(input, "graveyard", "g")) != null) {
            int graveyardNumber = Integer.parseInt(addressNumber);

            if (input.containsKey("opponent") || input.containsKey("o") && isRivalGraveyardAccessible) {
                if (graveyardNumber > game.getOtherBoard().getGraveyard().size())
                    throw new InvalidSelection();
                else {
                    this.selectedCard = game.getOtherBoard().getGraveyard().get(graveyardNumber - 1);
                    selectedCardAddress = new CardAddress(Place.Graveyard, Owner.Opponent, graveyardNumber - 1);
                }
            } else if (isOurGraveyardAccessible) {
                if (graveyardNumber > game.getThisBoard().getGraveyard().size())
                    throw new InvalidSelection();
                else {
                    this.selectedCard = game.getThisBoard().getGraveyard().get(graveyardNumber - 1);
                    selectedCardAddress = new CardAddress(Place.Graveyard, Owner.Me, graveyardNumber - 1);
                }
            } else throw new InvalidSelection();

            if (selectedCard == null)
                throw new CardNotFoundException();

        } else if (input.containsKey("-d")) {
            if (selectedCard == null) {
                throw new NoCardSelectedException();
            }
            deselect();
        } else
            throw new InvalidInput();

        if (selectedCard == null)
            throw new CardNotFoundException();
    }

    public void deselect() {
        selectedCardAddress = null;
        selectedCard = null;
    }

    public void nextTurn() {
        isSpellTrapNew = false;
    }
}
