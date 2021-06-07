package exceptions;

public class PlayWithYourself extends RuntimeException {
    public PlayWithYourself() {
        super("you cannot play with yourself");
    }
}
