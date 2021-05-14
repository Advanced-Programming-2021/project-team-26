package controller;

import model.cards.SpellTrap;
import model.cards.spell.Spell;
import model.cards.trap.Trap;

public abstract class SpellTrapController {

    protected GameController gameController;
    protected SpellTrapPosition position;

    public static SpellTrapController getInstance(GameController gameController, SpellTrap spellTrap, SpellTrapPosition position) {
        if (spellTrap instanceof Spell)
            return SpellController.getInstance(gameController, spellTrap, position);
        else if (spellTrap instanceof Trap)
            return TrapController.getInstance(gameController, spellTrap, position);
        return null;
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

    public void remove() {

    }

    public void activate() {

    }

    public abstract SpellTrap getCard();

    public abstract void standBy();

    public boolean conditionMet() {
        return true;
    }
}
