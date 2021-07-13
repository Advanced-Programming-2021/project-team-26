package exceptions;

public class CardNotFoundInMainDeck extends RuntimeException{
    public CardNotFoundInMainDeck(String cardName){
        super("card with name " + cardName + " does not exist in main deck");
    }
}
