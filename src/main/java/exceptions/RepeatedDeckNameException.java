package exceptions;

public class RepeatedDeckNameException extends RuntimeException{
    public RepeatedDeckNameException(String deckName){
        super("deck with name " + deckName + " already exists");
    }
}
