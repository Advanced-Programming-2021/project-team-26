package exceptions;

public class CardNotFoundInMainDeck extends RuntimeException{
    String cardName;

    public CardNotFoundInMainDeck(String cardName){
        this.cardName = cardName;
    }

    @Override
    public String getMessage(){
       return "card with name " + cardName + " does not exist in main deck";
    }
}
