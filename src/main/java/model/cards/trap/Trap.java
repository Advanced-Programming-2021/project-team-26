package model.cards.trap;

import controller.Database;
import controller.exceptions.TrapNotFoundException;
import model.cards.SpellTrap;
import model.cards.TrapSpellStatus;

import java.util.Map;

public class Trap extends SpellTrap {
    private static final Map<String, Trap> allTraps;

    static {
        allTraps = Database.getInstance().getAllTraps();
    }

    private final TrapType type;

    //copy constructor
    public Trap(Trap o) {
        super(o);
        this.type = o.type;
    }

    public Trap(String[] fields) {
        super(fields[0],
                fields[3],
                Integer.parseInt(fields[5]),
                TrapSpellStatus.stringToTrapSpellStatus(fields[4]));
        this.type = TrapType.stringToTrapType(fields[2]);
    }

    public static Trap getTrap(String name) throws TrapNotFoundException {
        throw new TrapNotFoundException();
    }
}
