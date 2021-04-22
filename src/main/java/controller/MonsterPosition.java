package controller;

public enum MonsterPosition {
    ATTACK("OO"),
    DEFENCE_UP("DO"),
    DEFENCE_DOWN("DH");

    private String string;
    MonsterPosition(String string){
        this.string = string;
    }

    public String getString(){
        return string;
    }
}
