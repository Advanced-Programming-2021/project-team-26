package exceptions;

public class InvalidNumberOfACardException extends RuntimeException {
    private String cardName;
    private String deckName;

    public InvalidNumberOfACardException(String cardName, String deckName) {
        this.cardName = cardName;
        this.deckName = deckName;
    }

    @Override
    public String getMessage() {
        return "there are already three cards with name " + cardName + " in deck " + deckName;
    }
}
