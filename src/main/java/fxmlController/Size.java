package fxmlController;

public enum Size {
    MAIN_WIDTH(1280),
    MAIN_HEIGHT(720),
    CARD_HEIGHT_IN_DECK(60),
    CARD_WIDTH_IN_DECK(40),
    MAIN_DECK_HEIGHT(400),
    MAIN_DECK_WIDTH(450),
    CARD_HEIGHT_IN_SHOP(120),
    CARD_WIDTH_IN_SHOP(80),
    CARD_HEIGHT_IN_SHOP_INFO(666),
    CARD_WIDTH_IN_SHOP_INFO(444);
    private final int value;

    Size(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}

