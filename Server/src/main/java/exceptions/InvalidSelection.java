package exceptions;

public class InvalidSelection extends RuntimeException {
    public InvalidSelection() {
        super("invalid selection");
    }
}
