package model.cards.trap;

import model.cards.monster.MonsterType;

public enum TrapType {
    NORMAL("Normal"),
    COUNTER("Counter"),
    CONTINUES("Continuous");

    private final String string;
    TrapType(String string){
        this.string = string;
    }

    public static TrapType stringToTrapType(String string){
        for (TrapType trapType : TrapType.values()){
            if(trapType.string.equals(string))
                return trapType;
        }
        return null;
    }
}
