package exceptions;

public class CannotAttackException extends RuntimeException {
    public CannotAttackException() {
        super("you can’t attack with this card");
    }
}
