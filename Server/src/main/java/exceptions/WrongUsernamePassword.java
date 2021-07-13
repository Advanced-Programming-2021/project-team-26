package exceptions;

public class WrongUsernamePassword extends RuntimeException {
    public WrongUsernamePassword() {
        super("Username and password didn't !match");
    }
}
