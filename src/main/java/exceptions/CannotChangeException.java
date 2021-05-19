package exceptions;

public class
CannotChangeException extends RuntimeException {
    public CannotChangeException() {
        super("you can’t change this card position");
    }
}
