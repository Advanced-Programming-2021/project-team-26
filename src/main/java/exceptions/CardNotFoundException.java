package exceptions;

public class CardNotFoundException extends RuntimeException {

    public CardNotFoundException() {
        super("there is no card with this name");
    }

    public CardNotFoundException(String cardName) {
        super("there is no card with name *" + cardName + "*");
    }
}
