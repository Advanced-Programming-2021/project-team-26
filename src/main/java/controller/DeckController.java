package controller;

import java.util.regex.Matcher;

public class DeckController {
    private static DeckController deckController;

    private DeckController(){
    }

    public static DeckController getInstance(){
        if(deckController==null)
            deckController = new DeckController();
        return deckController;
    }

    public void createDeck(Matcher matcher){

    }

    public void removeDeck(Matcher matcher){

    }

    public void setActive(Matcher matcher){

    }

    public void addCard(Matcher matcher){

    }

    public void removeCard(Matcher matcher){

    }

    public void showAllDeck(Matcher matcher){

    }

    public void showDeck(Matcher matcher){

    }

    public void showCards(Matcher matcher){

    }
}
