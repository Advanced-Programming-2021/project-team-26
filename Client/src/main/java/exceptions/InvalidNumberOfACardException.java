package exceptions;

public class InvalidNumberOfACardException extends RuntimeException {
    public InvalidNumberOfACardException(String cardName, String deckName) {
       super("there are already three cards with name " + cardName + " in deck " + deckName);
    }
}
