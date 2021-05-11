package exceptions;

public class CardNotFoundException extends RuntimeException {
    private String cardName;

    public CardNotFoundException() {

    }

    public CardNotFoundException(String cardName) {
        this.cardName = cardName;
    }
}
