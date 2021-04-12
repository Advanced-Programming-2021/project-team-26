package model.cards.monster;

public enum MonsterType {
    WARRIOR("Warrior"),
    BEAST_WARRIOR("Beast-Warrior"),
    FIEND("Fiend"),
    AQUA("Aqua"),
    BEAST("Beast"),
    PYRO("Pyro"),
    SPELL_CASTER("Spellcaster"),
    THUNDER("Thunder"),
    DRAGON("Dragon"),
    MACHINE("machine"),
    ROCK("Rock"),
    INSECT("Insect"),
    CYBERSE("Cyberse"),
    FAIRY("Fairy"),
    SEA_SERPENT("Sea Serpent");

    private final String string;

    MonsterType(String string){
        this.string = string;
    }

    public String getString(){
        return this.string;
    }
}
