package model.cards.trap;

import controller.exceptions.TrapNotFoundException;
import model.cards.SpellTrap;

public class Trap extends SpellTrap {
    private final TrapType type;

    //copy constructor
    public Trap(Trap o) {
        super(o);
        this.type = o.type;
    }

    public static Trap getTrap(String name) throws TrapNotFoundException {
        throw new TrapNotFoundException();
    }
}
