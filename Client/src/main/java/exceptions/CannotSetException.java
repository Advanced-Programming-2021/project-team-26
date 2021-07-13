package exceptions;

public class CannotSetException extends RuntimeException {
    public CannotSetException() {
        super("you can’t set this card");
    }
}
