package exceptions;

public class CardNotFoundInSideDeck extends RuntimeException {
    String cardName;

    public CardNotFoundInSideDeck(String cardName) {
        this.cardName = cardName;
    }

    @Override
    public String getMessage() {
        return "card with name " + cardName + " does not exist in side deck";
    }
}
