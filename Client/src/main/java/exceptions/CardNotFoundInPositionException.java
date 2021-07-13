package exceptions;

public class CardNotFoundInPositionException extends RuntimeException {
    public CardNotFoundInPositionException() {
        super("no card found in the given position");
    }

    public CardNotFoundInPositionException(String message) {
        super(message);
    }
}
