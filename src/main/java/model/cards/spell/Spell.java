package model.cards.spell;

import controller.exceptions.SpellNotFoundException;
import model.cards.Card;

public class Spell extends Card {
    private final SpellType type;

    //copy constructor
    public Spell(Spell o){
        super(o);
        this.type = o.type;
    }

    public static Spell getSpell(String name) throws SpellNotFoundException {
        throw new SpellNotFoundException();
    }
}
