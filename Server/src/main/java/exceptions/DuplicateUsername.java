package exceptions;

public class DuplicateUsername extends RuntimeException {
    public DuplicateUsername(String username){
        super("user with username " + username + " already exists");
    }
}