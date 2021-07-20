package controller;

import model.cards.Card;
import model.cards.trap.Trap;

public class TrapTransfer extends SpellTrapTransfer {
    private final Trap trap;

    private TrapTransfer(Trap trap, SpellTrapPosition position) {
        this.trap = new Trap(trap);
        this.position = position;
    }

    @Override
    public Card getCard() {
        return trap;
    }
}
