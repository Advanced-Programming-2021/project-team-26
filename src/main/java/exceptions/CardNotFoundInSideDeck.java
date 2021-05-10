package exceptions;

public class CardNotFoundInSideDeck extends RuntimeException{
    String cardName;

    public CardNotFoundInSideDeck(String cardName){
        this.cardName = cardName;
    }

    public void printMessage(){
        System.out.println("card with name " + cardName + " does not exist in side deck");
    }
}
