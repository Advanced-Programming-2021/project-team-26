package controller;

public enum SpellTrapPosition {
    UP("O"),
    DOWN("H");

    private final String string;
    SpellTrapPosition(String string){
        this.string = string;
    }

    public String getString(){
        return this.string;
    }
}
