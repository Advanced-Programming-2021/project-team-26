package exceptions;

import view.Printt;

public class InvalidInput extends RuntimeException {
    public InvalidInput(){
        super("invalid command");
    }
}
