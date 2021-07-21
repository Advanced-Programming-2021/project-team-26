package exceptions;

public class NoCardToSell extends RuntimeException{
    public NoCardToSell(){
        super("there is no card with this name to sell");
    }
}
