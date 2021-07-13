package exceptions;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException() {
        super("there is no player with this username");
    }
}
