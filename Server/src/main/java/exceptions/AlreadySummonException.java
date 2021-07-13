package exceptions;

public class AlreadySummonException extends RuntimeException {
    public AlreadySummonException() {
        super("you already summoned/set on this turn");
    }
}
