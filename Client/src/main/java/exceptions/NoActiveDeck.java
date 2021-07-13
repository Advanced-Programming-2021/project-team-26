package exceptions;

public class NoActiveDeck extends RuntimeException {
    public NoActiveDeck(String username) {
        super(username + " has no active deck");
    }
}
