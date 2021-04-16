package controller;

public class DeckController {
    private static DeckController deckController;

    private DeckController(){
    }

    public static DeckController getInstance(){
        if(deckController==null)
            deckController = new DeckController();
        return deckController;
    }

    public void createDeck(String name){

    }

    public void removeDeck(String name){

    }

    public void setActive(String name){

    }

    public void addCard(String cardName,String deckName,boolean side){

    }

    public void removeCard(String cardName,String deckName,boolean side){

    }

    public void showAllDeck(){

    }

    public void showDeck(String name,boolean side){

    }

    public void showCards(){

    }
}
