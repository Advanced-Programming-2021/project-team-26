package model.cards.spell;

import controller.Database;
import model.cards.SpellTrap;
import model.cards.TrapSpellStatus;

import java.util.Map;

public class Spell extends SpellTrap {
    private static final Map<String, Spell> allSpells;

    static {
        allSpells = Database.getInstance().getAllSpells();
    }

    private final SpellType type;

    //copy constructor
    public Spell(Spell o) {
        super(o);
        this.type = o.type;
    }

    public Spell(String[] fields) {
        super(fields[0],
                fields[3],
                Integer.parseInt(fields[5]),
                TrapSpellStatus.stringToTrapSpellStatus(fields[4]));
        this.type = SpellType.stringToSpellType(fields[2]);
    }

    public static Spell getSpell(String name) {
        if (allSpells.containsKey(name))
            return allSpells.get(name);
        return null;
    }

    public SpellType getType() {
        return type;
    }
}
