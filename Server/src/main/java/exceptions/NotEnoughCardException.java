package exceptions;

public class NotEnoughCardException extends RuntimeException{
    public NotEnoughCardException(){
        super("There are not anymore of this card in Shop");
    }
}
