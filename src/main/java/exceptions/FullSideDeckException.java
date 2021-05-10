package exceptions;

public class FullSideDeckException extends RuntimeException{
    @Override
    public String getMessage() {
        return "side deck is full";
    }
}
