package exceptions;

public class CannotActivateException extends RuntimeException {
    public CannotActivateException() {
        super("activate effect is only for spell cards.");
    }
}
