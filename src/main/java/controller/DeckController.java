package controller;

import exceptions.*;
import model.Deck;
import model.User;
import model.cards.Card;
import view.Scan;

import java.util.HashMap;
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

    public void addCard(Matcher matcher) throws InvalidInput, DeckNotFoundException, CardNotFoundException,
            FullDeckException, InvalidNumberOfACardException {
        String[] rawInput = matcher.group().split("\\s+");
        HashMap<String, String> input = Scan.getInstance().parseInput(rawInput);

        String cardName = null;
        if (input.containsKey("card") || input.containsKey("c"))
            cardName = input.get("card");
        if (cardName == null)
            throw new InvalidInput();

        String deckName = null;
        if (input.containsKey("deck") || input.containsKey("d"))
            deckName = input.get("deck");
        if (deckName == null)
            throw new InvalidInput();

        if (!Database.getInstance().getCurrentUser().doesUserHaveThisCard(cardName))
            throw new CardNotFoundException();
        if (!Deck.checkDeckNameExistence(deckName))
            throw new DeckNotFoundException();

        if (input.containsKey("side")) {
            if (Deck.getDeckByDeckName(deckName).getSideDeck().size() == 15)
                throw new FullDeckException();
        } else {
            if (Deck.getDeckByDeckName(deckName).getMainDeck().size() == 60)
                throw new FullDeckException();
        }

        if (!Deck.getDeckByDeckName(deckName).IsNumberOfTheCardInDeckValid(cardName))
            throw new InvalidNumberOfACardException();

        if (input.containsKey("side"))
            Deck.getDeckByDeckName(deckName).addCardToSideDeck(Card.getCard(cardName));
        else
            Deck.getDeckByDeckName(deckName).addCardToMainDeck(Card.getCard(cardName));
    }

    private void addCardToMainDeck(HashMap<String, String> input) throws InvalidInput {

    }

    private void addCardToSideDeck(HashMap<String, String> input) {

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
