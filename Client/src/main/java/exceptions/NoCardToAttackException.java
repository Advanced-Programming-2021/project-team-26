package exceptions;

public class NoCardToAttackException extends RuntimeException {
    public NoCardToAttackException() {
        super("there is no card to attack here");
    }
}
