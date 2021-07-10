package exceptions;

public class CardNotFoundException extends RuntimeException {

    public CardNotFoundException(String cardName) {
        super("there is no card with name *" + cardName + "*");
    }
}
