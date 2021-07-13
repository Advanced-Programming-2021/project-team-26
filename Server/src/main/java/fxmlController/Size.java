package fxmlController;

public enum Size {
    MAIN_WIDTH(1070),
    MAIN_HEIGHT(600),
    CARD_HEIGHT_IN_DECK(45),
    CARD_WIDTH_IN_DECK(30),
    CARD_HEIGHT_IN_SHOP(90),
    CARD_WIDTH_IN_SHOP(60),
    CARD_HEIGHT_IN_SHOP_INFO(580),
    CARD_WIDTH_IN_SHOP_INFO(400),
    CARD_SIZE_IN_GAME(92),
    CARD_WIDTH_IN_GRAVEYARD(100),
    CARD_HEIGHT_IN_GRAVEYARD(150);

    private final int value;

    Size(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}

