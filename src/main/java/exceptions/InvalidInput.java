package exceptions;

public class InvalidInput extends RuntimeException {
    @Override
    public String getMessage(){
        return "invalid command";
    }
}
