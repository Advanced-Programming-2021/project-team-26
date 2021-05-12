package controller;

import exceptions.CardNotFoundException;
import exceptions.DeckNameDoesntExistException;
import exceptions.InvalidNumberOfACardException;
import exceptions.RepeatedDeckNameException;
import model.Deck;
import model.User;
import model.cards.Card;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.fail;

public class DeckControllerTest {
    @BeforeAll
    public static void init() {
        String user2 = "user login -u user2 -p password";
        Matcher matcher1 = Pattern.compile(user2).matcher(user2);
        matcher1.find();

        UserController.getInstance().loginUser(matcher1);

        String deck1 = "deck create deck2";
        String deckRegex = "deck create (deck2)";
        Matcher matcher2 = Pattern.compile(deckRegex).matcher(deck1);
        matcher2.find();

        DeckController.getInstance().createDeck(matcher2);
    }

    @Test
    @DisplayName("create new deck")
    public void createDeck() {
        String deck1 = "deck create deck1";
        String deckRegex = "deck create (deck1)";
        Matcher matcher1 = Pattern.compile(deckRegex).matcher(deck1);

        matcher1.find();
        try {
            DeckController.getInstance().createDeck(matcher1);
        } catch (Exception e) {
            fail();
        }

        Assertions.assertThrows(RepeatedDeckNameException.class, () ->
        {
            DeckController.getInstance().createDeck(matcher1);
        });
    }

    @Test
    @DisplayName("remove deck")
    public void remove() {
        String deck3 = "deck delete deck3";
        String deck3Regex = "deck delete (deck3)";
        Matcher matcher3 = Pattern.compile(deck3Regex).matcher(deck3);
        matcher3.find();

        Assertions.assertThrows(DeckNameDoesntExistException.class, () ->
        {
            DeckController.getInstance().removeDeck(matcher3);
        });

        String deck2 = "deck delete deck2";
        String deck2Regex = "deck delete (deck2)";
        Matcher matcher2 = Pattern.compile(deck2Regex).matcher(deck2);
        matcher2.find();

        try {
            DeckController.getInstance().removeDeck(matcher2);
        } catch (Exception e) {
            fail();
        }

        Assertions.assertNull(Deck.getDeckByDeckName("deck2"));
        Assertions.assertTrue(User.getUserByUsername("user2").getAllDecks().size() == 0);
    }

    @Test
    @DisplayName("add card to deck")
    public void addCard() {
        String cardToMain = "deck add-card --card Suijin --deck deck2";
        String invalidCardToMain = "deck add-card --card Alaki --deck deck2";
        String cardToSide = "deck add-card --card Suijin --deck deck2 --side";

        String cardToMainRegex = "deck add-card --card Suijin --deck deck2";
        String cardToSideRegex = "deck add-card --card Suijin --deck deck2 --side";
        String invalidCardToMainRegex = "deck add-card --card Alaki --deck deck2";

        Matcher matcher = Pattern.compile(cardToMainRegex).matcher(cardToMain);
        matcher.find();

        Matcher matcher1 = Pattern.compile(cardToSideRegex).matcher(cardToSide);
        matcher1.find();

        Matcher matcher2 = Pattern.compile(invalidCardToMainRegex).matcher(invalidCardToMain);
        matcher2.find();

        User.getUserByUsername("user2").getAllCards().put("Suijin", Card.getCard("Suijin"));

        try {
            DeckController.getInstance().addCard(matcher);
        } catch (Exception e) {
            fail();
        }

        Assertions.assertTrue(Deck.getDeckByDeckName("deck2").getMainDeck().size() == 1);

        try {
            DeckController.getInstance().addCard(matcher1);
        } catch (Exception e) {
            fail();
        }

        Assertions.assertTrue(Deck.getDeckByDeckName("deck2").getSideDeck().size() == 1);

        Assertions.assertThrows(CardNotFoundException.class, () ->
        {
            DeckController.getInstance().addCard(matcher2);
        });

        DeckController.getInstance().addCard(matcher);
        Assertions.assertTrue(Deck.getDeckByDeckName("deck2").getMainDeck().size() == 2);

        Assertions.assertThrows(InvalidNumberOfACardException.class, () ->
        {
            DeckController.getInstance().addCard(matcher);
        });
    }
}