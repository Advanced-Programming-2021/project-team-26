package exceptions;

public class NotYourTurnException extends RuntimeException {
    public NotYourTurnException() {
        super("it’s not your turn to play this kind of moves");
    }
}
