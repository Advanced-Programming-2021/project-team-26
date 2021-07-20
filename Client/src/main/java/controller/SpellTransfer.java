package controller;

import model.cards.Card;
import model.cards.spell.Spell;

public class SpellTransfer extends SpellTrapTransfer {
    private final Spell spell;

    private SpellTransfer(Spell spell, SpellTrapPosition position) {
        setPosition(position);
        this.spell = new Spell(spell);
    }

    @Override
    public Card getCard() {
        return spell;
    }
}
