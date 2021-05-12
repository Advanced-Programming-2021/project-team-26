package controller;

import exceptions.DeckNameDoesntExistException;
import exceptions.RepeatedDeckNameException;
import model.Deck;
import model.User;
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
}