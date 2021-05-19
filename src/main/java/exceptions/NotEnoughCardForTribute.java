package exceptions;

public class NotEnoughCardForTribute extends RuntimeException {
    public NotEnoughCardForTribute() {
        super("there are not enough cards for tribute");
    }
}
