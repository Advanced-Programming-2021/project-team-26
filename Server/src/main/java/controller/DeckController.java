package controller;

import exceptions.*;
import model.Deck;
import model.User;
import model.cards.Card;
import view.Scan;

import java.util.HashMap;
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

    public Deck createDeck(User user, String deckName) throws RepeatedDeckNameException {
        if (!user.checkDeckNameExistence(deckName)) {
            Deck deck = new Deck(deckName, user.getUsername());
            user.addDeckToUserDecks(deck);
            return deck;
        } else throw new RepeatedDeckNameException(deckName);
    }

    public boolean removeDeck(User user, String deckName) throws DeckNameDoesntExistException {
        if (user.checkDeckNameExistence(deckName)) {
            Objects.requireNonNull(User.getUserByUsername(user.getUsername())).getAllDecks().remove(deckName);
            user.getAllDecks().remove(deckName);
            return true;
        } else throw new DeckNameDoesntExistException(deckName);
    }

    public boolean setActive(User user, String deckName) throws DeckNameDoesntExistException {
        if (user.checkDeckNameExistence(deckName)) {
            user.setActiveDeck(user.getDeckByDeckName(deckName));
            return true;
        } else throw new DeckNameDoesntExistException(deckName);
    }

    public boolean addCard(User user, String cardName, String deckName, Boolean side) throws InvalidInput, DeckNameDoesntExistException, CardNotFoundException,
            InvalidNumberOfACardException, FullMainDeckException, FullSideDeckException {
        if (!user.doesUserHaveThisCard(cardName))
            throw new CardNotFoundException();
        if (!user.checkDeckNameExistence(deckName))
            throw new DeckNameDoesntExistException(deckName);

        if (side) {
            if (Objects.requireNonNull(user.getDeckByDeckName(deckName)).getSideDeck().size() == 15)
                throw new FullSideDeckException();
        } else {
            if (Objects.requireNonNull(user.getDeckByDeckName(deckName)).getMainDeck().size() == 60)
                throw new FullMainDeckException();
        }

        if (!Objects.requireNonNull(user.getDeckByDeckName(deckName)).IsNumberOfTheCardInDeckValid(cardName))
            throw new InvalidNumberOfACardException(cardName, deckName);

        HashMap<String, Integer> userCards = user.getAllCards();
        int numberOfTheCardInUserCards = userCards.get(cardName);
        int numberOfTheCardInDeckCards = user.getDeckByDeckName(deckName).getNumberOfCardINDeck(cardName);

        if (numberOfTheCardInDeckCards >= numberOfTheCardInUserCards)
            throw new CardNotFoundException();

        if (side)
            Objects.requireNonNull(user.getDeckByDeckName(deckName)).addCardToSideDeck(Objects.requireNonNull(Card.getCard(cardName)));
        else
            Objects.requireNonNull(user.getDeckByDeckName(deckName)).addCardToMainDeck(Objects.requireNonNull(Card.getCard(cardName)));
        return true;
    }

    public boolean removeCard(User user, String cardName, String deckName, Boolean side) throws InvalidInput, DeckNameDoesntExistException,
            CardNotFoundInSideDeck, CardNotFoundInMainDeck {
        if (!user.checkDeckNameExistence(deckName))
            throw new DeckNameDoesntExistException(deckName);

        if (side) {
            if (!Objects.requireNonNull(user.getDeckByDeckName(deckName)).doesCardExistInSideDeck(cardName))
                throw new CardNotFoundInSideDeck(cardName);
        } else {
            if (!Objects.requireNonNull(user.getDeckByDeckName(deckName)).doesCardExistInMainDeck(cardName))
                throw new CardNotFoundInMainDeck(cardName);
        }

        if (side)
            Objects.requireNonNull(user.getDeckByDeckName(deckName)).deleteCardFromSideDeck(Card.getCard(cardName));
        else
            Objects.requireNonNull(user.getDeckByDeckName(deckName)).deleteCardFromMainDeck(Card.getCard(cardName));

        return true;
    }

    public String showDeck(Matcher matcher) throws InvalidInput, DeckNameDoesntExistException {
        HashMap<String, String> input = Scan.getInstance().parseInput(matcher.group());

        String deckName = null;
        if (input.containsKey("deck-name"))
            deckName = input.get("deck-name");
        else if (input.containsKey("d-n"))
            deckName = input.get("d-n");
        if (deckName == null)
            throw new InvalidInput();

        if (!Database.getInstance().getCurrentUser().checkDeckNameExistence(deckName))
            throw new DeckNameDoesntExistException(deckName);

        if (input.containsKey("side"))
            return Objects.requireNonNull(Database.getInstance().getCurrentUser().getDeckByDeckName(deckName)).showDeck("side");
        else
            return Objects.requireNonNull(Database.getInstance().getCurrentUser().getDeckByDeckName(deckName)).showDeck("main");
    }

    public String showCards(Matcher matcher) {
        return Database.getInstance().getCurrentUser().showAllCards();
    }


    public String showAllDeck(Matcher matcher) {
        return Database.getInstance().getCurrentUser().showAllDecks();
    }
}
