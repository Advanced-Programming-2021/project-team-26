package exceptions;

public class InvalidNumberOfACardException extends RuntimeException {
    private String cardName;
    private String deckName;

    public InvalidNumberOfACardException(String cardName, String deckName) {
        this.cardName = cardName;
        this.deckName = deckName;
    }

    public void printMessage() {
        System.out.println("there are already three cards with name " + cardName + " in deck " + deckName);
    }
}
