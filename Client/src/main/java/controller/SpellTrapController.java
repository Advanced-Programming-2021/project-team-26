package controller;

import model.cards.Card;

public abstract class SpellTrapController {
    protected SpellTrapPosition position;
    public abstract Card getCard();

    public SpellTrapPosition getPosition() {
        return position;
    }

    public void setPosition(SpellTrapPosition position) {
        this.position = position;
    }
}
