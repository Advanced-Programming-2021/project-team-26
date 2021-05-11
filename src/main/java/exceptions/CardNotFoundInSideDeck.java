package exceptions;

public class CardNotFoundInSideDeck extends RuntimeException {
    public CardNotFoundInSideDeck(String cardName) {
        super("card with name " + cardName + " does not exist in side deck");
    }
}
