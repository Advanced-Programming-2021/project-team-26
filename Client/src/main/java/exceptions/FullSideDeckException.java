package exceptions;

public class FullSideDeckException extends RuntimeException {
    public FullSideDeckException() {
        super("side deck is full");
    }
}