package exceptions;

public class InvalidInput extends RuntimeException {
    public InvalidInput(){
        super("invalid command");
    }
}
