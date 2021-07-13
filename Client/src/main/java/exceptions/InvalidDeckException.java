package exceptions;

public class InvalidDeckException extends RuntimeException {
    public InvalidDeckException(String username) {
        super(username + "’s deck is invalid");
    }
}
