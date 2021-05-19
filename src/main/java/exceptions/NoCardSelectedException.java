package exceptions;

public class NoCardSelectedException extends RuntimeException {
    public NoCardSelectedException() {
        super("no card is selected yet");
    }
}
