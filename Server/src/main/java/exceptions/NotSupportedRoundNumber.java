package exceptions;

public class NotSupportedRoundNumber extends RuntimeException {
    public NotSupportedRoundNumber() {
        super("number of rounds is not supported");
    }
}
