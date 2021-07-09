package fxmlController;

public enum Layout {
    MY_DECK_X(975),
    MY_DECK_Y(425);

    private final int value;

    Layout(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}
