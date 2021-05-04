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
            Database.getInstance().getCurrentUser().setActiveDeck(Deck.getDeckByDeckName(deckName));
        else throw new UnreachableDeckNameException();
    }

    public void addCard(Matcher matcher) throws InvalidInput, DeckNotFoundException, CardNotFoundException,
            InvalidNumberOfACardException, FullMainDeckException, FullSideDeckException {
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
                throw new FullSideDeckException();
        } else {
            if (Deck.getDeckByDeckName(deckName).getMainDeck().size() == 60)
                throw new FullMainDeckException();
        }

        if (!Deck.getDeckByDeckName(deckName).IsNumberOfTheCardInDeckValid(cardName))
            throw new InvalidNumberOfACardException();

        if (input.containsKey("side"))
            Deck.getDeckByDeckName(deckName).addCardToSideDeck(Card.getCard(cardName));
        else
            Deck.getDeckByDeckName(deckName).addCardToMainDeck(Card.getCard(cardName));
    }

    public void removeCard(Matcher matcher) throws InvalidInput, DeckNotFoundException,
            CardNotFoundInSideDeck, CardNotFoundInMainDeck {
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

        if (!Deck.checkDeckNameExistence(deckName))
            throw new DeckNotFoundException();

        if (input.containsKey("side")) {
            if (!Deck.getDeckByDeckName(deckName).doesCardExistInSideDeck(cardName))
                throw new CardNotFoundInSideDeck();
        } else {
            if (!Deck.getDeckByDeckName(deckName).doesCardExistInMainDeck(cardName))
                throw new CardNotFoundInMainDeck();
        }


        if (input.containsKey("side"))
            Deck.getDeckByDeckName(deckName).deleteCardFromSideDeck(Card.getCard(cardName));
        else
            Deck.getDeckByDeckName(deckName).deleteCardFromMainDeck(Card.getCard(cardName));
    }


    public String showDeck(Matcher matcher) throws InvalidInput, DeckNotFoundException {
        String[] rawInput = matcher.group().split("\\s+");
        HashMap<String, String> input = Scan.getInstance().parseInput(rawInput);

        String deckName = null;
        if (input.containsKey("deck-name") || input.containsKey("d-n"))
            deckName = input.get("deck");
        if (deckName == null)
            throw new InvalidInput();

        if (!Deck.checkDeckNameExistence(deckName))
            throw new DeckNotFoundException();

        if (input.containsKey("side"))
            return Deck.getDeckByDeckName(deckName).showDeck("side");
        else
            return Deck.getDeckByDeckName(deckName).showDeck("main");
    }

    public String showCards() {
        return Database.getInstance().getCurrentUser().showAllCards();
    }


    public String showAllDeck() {
        return Database.getInstance().getCurrentUser().showAllDecks();
    }
}
