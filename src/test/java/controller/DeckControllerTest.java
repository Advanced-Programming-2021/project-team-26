package controller;

import exceptions.RepeatedDeckNameException;
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
        String user1 = "user login -u user2 -p password";
        Matcher matcher1 = Pattern.compile(user1).matcher(user1);
        matcher1.find();

        UserController.getInstance().loginUser(matcher1);
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
}
