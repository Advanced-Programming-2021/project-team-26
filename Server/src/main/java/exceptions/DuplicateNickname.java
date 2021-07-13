package exceptions;

public class DuplicateNickname extends RuntimeException {
    public DuplicateNickname(String nickname) {
        super("user with nickname " + nickname + " already exists");
    }
}
