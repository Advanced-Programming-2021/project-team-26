package model.cards;

import controller.Database;

import java.util.Map;

public class SpellTrap extends Card {
    private static final Map<String, SpellTrap> allSpellTraps;
    static {
        allSpellTraps = Database.getInstance().getAllSpellTraps();
    }
    private final TrapSpellStatus status;

    public SpellTrap(SpellTrap o) {
        super(o);
        this.status = o.status;
    }

    public SpellTrap(String name, String description, int price, TrapSpellStatus status) {
        super(name, description, price);
        this.status = status;
    }
}
