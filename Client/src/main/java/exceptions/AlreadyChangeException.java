package exceptions;

public class AlreadyChangeException extends RuntimeException {
    public AlreadyChangeException() {
        super("you already changed this card position in this turn");
    }
}
