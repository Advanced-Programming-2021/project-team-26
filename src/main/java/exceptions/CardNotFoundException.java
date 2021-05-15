package exceptions;

public class CardNotFoundException extends RuntimeException {
    private String cardName;

    public CardNotFoundException() {
        super("there is no card with this name");
    }

    public CardNotFoundException(String cardName) {
        this.cardName = cardName;
    }
}
