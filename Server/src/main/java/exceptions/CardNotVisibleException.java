package exceptions;

public class CardNotVisibleException extends RuntimeException {
    public CardNotVisibleException() {
        super("card is not visible");
    }
}
