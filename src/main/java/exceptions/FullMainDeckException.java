package exceptions;

public class FullMainDeckException extends RuntimeException {

    @Override
    public String getMessage() {
        return "main deck is full";
    }
}
