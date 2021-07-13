package exceptions;

public class AlreadyActivatedException extends RuntimeException {
    public AlreadyActivatedException() {
        super("you have already activated this card");
    }
}
