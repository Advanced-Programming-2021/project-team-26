package exceptions;

public class WrongPassword extends RuntimeException {
    public WrongPassword() {
        super("wrong password");
    }
}
