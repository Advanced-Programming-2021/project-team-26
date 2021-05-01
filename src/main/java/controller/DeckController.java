package controller;

import exceptions.UnreachableDeckNameException;
import model.Deck;
import model.User;

import java.util.regex.Matcher;

public class DeckController {
    private static DeckController deckController;

    private DeckController() {
    }

    public static DeckController getInstance() {
        if (deckController == null)
            deckController = new DeckController();
        return deckController;
    }

    public void createDeck(Matcher matcher) throws UnreachableDeckNameException {
        String deckName = matcher.group(1);
        if (!Deck.checkDeckNameExistence(deckName))
            new Deck(deckName, Database.getInstance().getCurrentUser().getUsername());
        else throw new UnreachableDeckNameException();
    }

    public void removeDeck(Matcher matcher) throws UnreachableDeckNameException {
        String deckName = matcher.group(1);
        if (!Deck.checkDeckNameExistence(deckName)) {
            User.getUserByUsername(Database.getInstance().getCurrentUser().getUsername()).getAllDecks().remove(deckName);
            Deck.getAllDecks().removeIf(deck -> deck.getName().equals(deckName));
        } else throw new UnreachableDeckNameException();
    }

    public void setActive(Matcher matcher) throws UnreachableDeckNameException {
        String deckName = matcher.group(1);
        if (!Deck.checkDeckNameExistence(deckName))
        Database.getInstance().getCurrentUser().setActiveDeck(deckName);
        else throw new UnreachableDeckNameException();
    }

    public void addCard(Matcher matcher) {

    }

    public void removeCard(Matcher matcher) {

    }


    public void showDeck(Matcher matcher) {

    }

    public void showCards() {

    }


    public void showAllDeck() {

    }
}
