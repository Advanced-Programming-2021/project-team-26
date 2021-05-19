package exceptions;

public class CannotSummonException extends RuntimeException {
    public CannotSummonException() {
        super("you can’t summon this card");
    }
}
