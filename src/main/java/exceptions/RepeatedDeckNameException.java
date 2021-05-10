package exceptions;

public class RepeatedDeckNameException extends RuntimeException{
    private String deckName;

    public RepeatedDeckNameException(String deckName){
        this.deckName = deckName;
    }

    public void printMessage(){
        System.out.println("deck with name " + deckName + " already exists");
    }
}
