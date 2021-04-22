package model.cards.spell;

import controller.exceptions.SpellNotFoundException;
import model.cards.SpellTrap;

public class Spell extends SpellTrap {
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
