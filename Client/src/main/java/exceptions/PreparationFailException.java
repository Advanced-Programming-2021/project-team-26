package exceptions;

public class PreparationFailException extends RuntimeException {
    public PreparationFailException() {
        super("preparations of this spell are not done yet");
    }
}
