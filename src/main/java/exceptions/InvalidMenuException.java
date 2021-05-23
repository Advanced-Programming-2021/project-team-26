package exceptions;

public class InvalidMenuException extends RuntimeException {
    public InvalidMenuException(){
        super("this Menu is not a valid Menu name");
    }
}
