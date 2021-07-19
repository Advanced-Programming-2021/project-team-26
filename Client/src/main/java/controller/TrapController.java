package controller;

import model.cards.Card;
import model.cards.trap.Trap;

public class TrapController extends SpellTrapController {
    private final Trap trap;

    private TrapController(Trap trap, SpellTrapPosition position) {
        this.trap = new Trap(trap);
        this.position = position;
    }

    @Override
    public Card getCard() {
        return trap;
    }
}
