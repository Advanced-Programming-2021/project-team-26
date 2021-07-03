package fxmlController;

public enum Size {
    MAIN_WIDTH(1280),
    MAIN_HEIGHT(720),
    CARD_HEIGHT(100),
    CARD_WIDTH(30),
    MAIN_DECK_HEIGHT(400),
    MAIN_DECK_WIDTH(450);
    private final int value;

    Size(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}

