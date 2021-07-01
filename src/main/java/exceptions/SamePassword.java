package exceptions;

public class SamePassword extends RuntimeException {
    public SamePassword() {
        super("same password");
    }
}
