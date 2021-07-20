package model;

import controller.SpellTrapPosition;
import model.cards.Card;
import model.cards.SpellTrap;

public class SpellTrapTransfer {
    protected SpellTrapPosition position;
    protected SpellTrap card;

    public SpellTrapTransfer(SpellTrap card, SpellTrapPosition position) {
        setCard(card);
        setPosition(position);
    }

    public Card getCard() {
        return this.card;
    }

    private void setCard(SpellTrap card) {
        this.card = card;
    }

    public SpellTrapPosition getPosition() {
        return position;
    }

    public void setPosition(SpellTrapPosition position) {
        this.position = position;
    }
}
