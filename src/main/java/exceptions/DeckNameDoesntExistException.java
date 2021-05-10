package exceptions;

public class DeckNameDoesntExistException extends RuntimeException{
    private String deckName;

    public DeckNameDoesntExistException(String deckName){
        this.deckName = deckName;
    }

    public void printMessage(){
        System.out.println("deck with name " + deckName +" does not exist");
    }
}
