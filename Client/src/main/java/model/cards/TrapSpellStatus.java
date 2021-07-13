package model.cards;

public enum TrapSpellStatus {
    LIMITED("Limited"),
    UNLIMITED("Unlimited");

    private final String string;

    TrapSpellStatus(String string) {
        this.string = string;
    }

    public static TrapSpellStatus stringToTrapSpellStatus(String string) {
        for (TrapSpellStatus trapSpellStatus : TrapSpellStatus.values()) {
            if (trapSpellStatus.string.equals(string))
                return trapSpellStatus;
        }
        return null;
    }
}
