package controller;

import model.cards.Card;
import model.cards.spell.Spell;

public class SpellController extends SpellTrapController {
    private final Spell spell;

    private SpellController(Spell spell, SpellTrapPosition position) {
        setPosition(position);
        this.spell = new Spell(spell);
    }

    @Override
    public Card getCard() {
        return spell;
    }
}
