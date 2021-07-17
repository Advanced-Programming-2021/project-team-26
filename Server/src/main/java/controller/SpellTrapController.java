package controller;

import model.Board;
import model.CardAddress;
import model.cards.Card;
import model.cards.SpellTrap;
import model.cards.spell.Spell;
import model.cards.trap.Trap;

import java.util.ArrayList;

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

    public void deselect() {
        selectedCardAddress = null;
        selectedCard = null;
    }

    public void nextTurn() {
        isSpellTrapNew = false;
    }
}
