package exceptions;

public class FullMainDeckException extends RuntimeException {
    public FullMainDeckException(){
        super("main deck is full");
    }
}
