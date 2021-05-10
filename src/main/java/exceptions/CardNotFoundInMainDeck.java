package exceptions;

public class CardNotFoundInMainDeck extends RuntimeException{
    String cardName;

    public CardNotFoundInMainDeck(String cardName){
        this.cardName = cardName;
    }

    public void printMessage(){
        System.out.println("card with name " + cardName + " does not exist in main deck");
    }
}
