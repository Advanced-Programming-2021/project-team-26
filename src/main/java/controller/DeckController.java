package controller;

import exceptions.*;
import model.Deck;
import model.User;
import model.cards.Card;
import view.Scan;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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

    public String createDeck(Matcher matcher) throws RepeatedDeckNameException {
        String deckName = matcher.group(1);
        if (!Deck.checkDeckNameExistence(deckName)) {
            new Deck(deckName, Database.getInstance().getCurrentUser().getUsername());
            Database.getInstance().getCurrentUser().addDeckToUserDecks(Deck.getDeckByDeckName(deckName));
            return "deck created successfully!";
        } else throw new RepeatedDeckNameException(deckName);
    }

    public String removeDeck(Matcher matcher) throws DeckNameDoesntExistException {
        String deckName = matcher.group(1);
        if (Deck.checkDeckNameExistence(deckName)) {
            Objects.requireNonNull(User.getUserByUsername(Database.getInstance().getCurrentUser().getUsername())).getAllDecks().remove(deckName);
            Deck.getAllDecks().removeIf(deck -> deck.getName().equals(deckName));
            return "deck deleted successfully!";
        } else throw new DeckNameDoesntExistException(deckName);
    }

    public String setActive(Matcher matcher) throws DeckNameDoesntExistException {
        String deckName = matcher.group(1);
        if (Deck.checkDeckNameExistence(deckName)) {
            Database.getInstance().getCurrentUser().setActiveDeck(Deck.getDeckByDeckName(deckName));
            return "deck activated successfully!";
        } else throw new DeckNameDoesntExistException(deckName);
    }

    public String addCard(Matcher matcher) throws InvalidInput, DeckNameDoesntExistException, CardNotFoundException,
            InvalidNumberOfACardException, FullMainDeckException, FullSideDeckException {
        String[] rawInput = matcher.group().split("\\s+");
        HashMap<String, String> input = Scan.getInstance().parseInput(rawInput);

        String cardName = null;
        if (input.containsKey("card"))
            cardName = input.get("card");
        else if (input.containsKey("c"))
            cardName = input.get("c");
        if (cardName == null)
            throw new InvalidInput();

        String deckName = null;
        if (input.containsKey("deck"))
            deckName = input.get("deck");
        else if (input.containsKey("d"))
            deckName = input.get("d");
        if (deckName == null)
            throw new InvalidInput();

        if (!Database.getInstance().getCurrentUser().doesUserHaveThisCard(cardName))
            throw new CardNotFoundException();
        if (!Deck.checkDeckNameExistence(deckName))
            throw new DeckNameDoesntExistException(deckName);

        if (input.containsKey("side")) {
            if (Objects.requireNonNull(Deck.getDeckByDeckName(deckName)).getSideDeck().size() == 15)
                throw new FullSideDeckException();
        } else {
            if (Objects.requireNonNull(Deck.getDeckByDeckName(deckName)).getMainDeck().size() == 60)
                throw new FullMainDeckException();
        }

        if (!Objects.requireNonNull(Deck.getDeckByDeckName(deckName)).IsNumberOfTheCardInDeckValid(cardName))
            throw new InvalidNumberOfACardException(cardName, deckName);

        HashMap<String, List<Integer>> userCards = Database.getInstance().getCurrentUser().getAllCards();
        int numberOfTheCardInUserCards = userCards.get(cardName).size();
        int numberOfTheCardInDeckCards = Deck.getDeckByDeckName(deckName).getNumberOfCardINDeck(cardName);

        if (numberOfTheCardInDeckCards >= numberOfTheCardInUserCards)
            throw new CardNotFoundException();

        if (input.containsKey("side"))
            Objects.requireNonNull(Deck.getDeckByDeckName(deckName)).addCardToSideDeck(Card.getCard(cardName));
        else
            Objects.requireNonNull(Deck.getDeckByDeckName(deckName)).addCardToMainDeck(Card.getCard(cardName));
        return "card added to deck successfully!";
    }

    public String removeCard(Matcher matcher) throws InvalidInput, DeckNameDoesntExistException,
            CardNotFoundInSideDeck, CardNotFoundInMainDeck {
        String[] rawInput = matcher.group().split("\\s+");
        HashMap<String, String> input = Scan.getInstance().parseInput(rawInput);

        String cardName = null;
        if (input.containsKey("card"))
            cardName = input.get("card");
        else if (input.containsKey("c"))
            cardName = input.get("c");
        if (cardName == null)
            throw new InvalidInput();

        String deckName = null;
        if (input.containsKey("deck"))
            deckName = input.get("deck");
        else if (input.containsKey("d"))
            deckName = input.get("d");
        if (deckName == null)
            throw new InvalidInput();

        if (!Deck.checkDeckNameExistence(deckName))
            throw new DeckNameDoesntExistException(deckName);

        if (input.containsKey("side")) {
            if (!Objects.requireNonNull(Deck.getDeckByDeckName(deckName)).doesCardExistInSideDeck(cardName))
                throw new CardNotFoundInSideDeck(cardName);
        } else {
            if (!Objects.requireNonNull(Deck.getDeckByDeckName(deckName)).doesCardExistInMainDeck(cardName))
                throw new CardNotFoundInMainDeck(cardName);
        }


        if (input.containsKey("side"))
            Objects.requireNonNull(Deck.getDeckByDeckName(deckName)).deleteCardFromSideDeck(Card.getCard(cardName));
        else
            Objects.requireNonNull(Deck.getDeckByDeckName(deckName)).deleteCardFromMainDeck(Card.getCard(cardName));

        return "card removed form deck successfully";
    }

    public String showDeck(Matcher matcher) throws InvalidInput, DeckNameDoesntExistException {
        String[] rawInput = matcher.group().split("\\s+");
        HashMap<String, String> input = Scan.getInstance().parseInput(rawInput);

        String deckName = null;
        if (input.containsKey("deck-name"))
            deckName = input.get("deck-name");
        else if (input.containsKey("d-n"))
            deckName = input.get("d-n");
        if (deckName == null)
            throw new InvalidInput();

        if (!Deck.checkDeckNameExistence(deckName))
            throw new DeckNameDoesntExistException(deckName);

        if (input.containsKey("side"))
            return Objects.requireNonNull(Deck.getDeckByDeckName(deckName)).showDeck("side");
        else
            return Objects.requireNonNull(Deck.getDeckByDeckName(deckName)).showDeck("main");
    }

    public String showCards(Matcher matcher) {
        return Database.getInstance().getCurrentUser().showAllCards();
    }


    public String showAllDeck(Matcher matcher) {
        return Database.getInstance().getCurrentUser().showAllDecks();
    }
}
