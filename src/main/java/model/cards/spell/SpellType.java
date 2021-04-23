package model.cards.spell;

import model.cards.trap.TrapType;

public enum SpellType {
    NORMAL("Normal"),
    RITUAL("Ritual"),
    CONTINUOUS("Continuous"),
    QUICK_PLAY("Quick-play"),
    FIELD("Field"),
    EQUIP("Equip");

    private final String string;
    SpellType(String string){
        this.string = string;
    }

    public static SpellType stringToSpellType(String string){
        for (SpellType spellType : SpellType.values()){
            if(spellType.string.equals(string))
                return spellType;
        }
        return null;
    }
}
