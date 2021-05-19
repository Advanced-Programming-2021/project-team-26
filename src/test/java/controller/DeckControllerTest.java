package controller;

import exceptions.*;
import model.Deck;
import model.User;
import model.cards.Card;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static controller.DatabaseTest.deleteDir;
import static org.junit.jupiter.api.Assertions.fail;

public class DeckControllerTest {

    @BeforeAll
    public static void init() {
        String databasePath = System.getProperty("user.dir") + File.separator + "database";
        File file = new File(databasePath);
        deleteDir(file);

        String user = "user create --username user2 --nickname nick2 -p password";
        Matcher matcher = Pattern.compile(user).matcher(user);
        matcher.find();

        UserController.getInstance().addNewUser(matcher);

        String user2 = "user login -u user2 -p password";
        Matcher matcher1 = Pattern.compile(user2).matcher(user2);
        matcher1.find();

        UserController.getInstance().loginUser(matcher1);

        String deck1 = "deck create deck2";
        String deckRegex = "deck create (deck2)";
        Matcher matcher2 = Pattern.compile(deckRegex).matcher(deck1);
        matcher2.find();

        DeckController.getInstance().createDeck(matcher2);

        Deck.getDeckByDeckName("deck2").addCardToSideDeck(Card.getCard("Scanner"));
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

        ArrayList<Integer> numberOfCards = new ArrayList<>();
        numberOfCards.add(1);

        User.getUserByUsername("user2").getAllCards().put("Suijin", numberOfCards);

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

        Assertions.assertTrue(Deck.getDeckByDeckName("deck2").getSideDeck().size() == 2);

        Assertions.assertThrows(CardNotFoundException.class, () ->
        {
            DeckController.getInstance().addCard(matcher2);
        });

        Assertions.assertTrue(Deck.getDeckByDeckName("deck2").getAllCards().size() == 3);
        Assertions.assertTrue(Deck.getDeckByDeckName("deck2").getMainDeck().size() == 1);
        DeckController.getInstance().addCard(matcher);
        Assertions.assertTrue(Deck.getDeckByDeckName("deck2").getAllCards().size() == 4);
        Assertions.assertThrows(InvalidNumberOfACardException.class, () ->
        {
            DeckController.getInstance().addCard(matcher);
        });
    }

    @Test
    @DisplayName("remove card from deck")
    public void removeCard() {
        String deck1 = "deck create deck2";
        String deckRegex = "deck create (deck2)";
        Matcher matcher2 = Pattern.compile(deckRegex).matcher(deck1);
        matcher2.find();

        DeckController.getInstance().createDeck(matcher2);
        Deck.getDeckByDeckName("deck2").addCardToSideDeck(Card.getCard("Scanner"));

        String invalidInput = "deck remove-card --card --deck deck2";
        String invalidInputRegex = "deck remove-card --card --deck deck2";
        Matcher matcher = Pattern.compile(invalidInputRegex).matcher(invalidInput);
        matcher.find();

        Assertions.assertThrows(InvalidInput.class, () ->
        {
            DeckController.getInstance().removeCard(matcher);
        });

        String deck2 = "deck remove-card --card Marshmallon --deck deck2";
        String deck2Regex = "deck remove-card --card Marshmallon --deck deck2";
        Matcher matcher1 = Pattern.compile(deck2Regex).matcher(deck2);
        matcher1.find();

        Assertions.assertThrows(CardNotFoundInMainDeck.class, () ->
        {
            DeckController.getInstance().removeCard(matcher1);
        });

        String deck21 = "deck remove-card --card Suijin --deck deck2 --side";
        String deck21Regex = "deck remove-card --card Suijin --deck deck2 --side";
        Matcher matcher5 = Pattern.compile(deck21Regex).matcher(deck21);
        matcher5.find();

        Assertions.assertThrows(CardNotFoundInSideDeck.class, () ->
        {
            DeckController.getInstance().removeCard(matcher5);
        });

        String deck3 = "deck remove-card --card Scanner --deck deck4 --side";
        String deck3Regex = "deck remove-card --card Scanner --deck deck4 --side";
        Matcher matcher3 = Pattern.compile(deck3Regex).matcher(deck3);
        matcher3.find();

        Assertions.assertThrows(DeckNameDoesntExistException.class, () ->
        {
            DeckController.getInstance().removeCard(matcher3);
        });

        String deck4 = "deck remove-card --card Scanner --deck deck2 --side";
        String deck4Regex = "deck remove-card --card Scanner --deck deck2 --side";
        Matcher matcher4 = Pattern.compile(deck4Regex).matcher(deck4);
        matcher4.find();

        try {
            DeckController.getInstance().removeCard(matcher4);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @DisplayName("show a deck")
    public void showDeck() {
        String deck1 = "deck create deck3";
        String deckRegex3 = "deck create (deck3)";
        Matcher matcher3 = Pattern.compile(deckRegex3).matcher(deck1);
        matcher3.find();

        DeckController.getInstance().createDeck(matcher3);
        Deck.getDeckByDeckName("deck3").addCardToMainDeck(Card.getCard("Command Knight"));
        Deck.getDeckByDeckName("deck3").addCardToMainDeck(Card.getCard("Trap Hole"));
        Deck.getDeckByDeckName("deck3").addCardToMainDeck(Card.getCard("Command Knight"));
        Deck.getDeckByDeckName("deck3").addCardToMainDeck(Card.getCard("Black Pendant"));
        Deck.getDeckByDeckName("deck3").addCardToSideDeck(Card.getCard("Black Pendant"));
        Deck.getDeckByDeckName("deck3").addCardToMainDeck(Card.getCard("Black Pendant"));
        Deck.getDeckByDeckName("deck3").addCardToSideDeck(Card.getCard("Trap Hole"));
        Deck.getDeckByDeckName("deck3").addCardToSideDeck(Card.getCard("Trap Hole"));

        String deck = "deck show --deck-name deck3";
        String deckRegex = "deck show --deck-name deck3";
        Matcher matcher = Pattern.compile(deckRegex).matcher(deck);
        matcher.find();

        String deck2 = "deck show --deck-name deck3 --side";
        String deckRegex2 = "deck show --deck-name deck3 --side";
        Matcher matcher2 = Pattern.compile(deckRegex2).matcher(deck2);
        matcher2.find();

        Assertions.assertTrue(DeckController.getInstance().showDeck(matcher).equals("Deck: deck3\n" +
                "Main deck:\n" +
                "Monsters:\n" +
                "Command Knight: All Warrior-Type monsters you control gain 400 ATK. If you control another monster, monsters your opponent controls cannot target this card for an attack.\n" +
                "Spell and Traps:\n" +
                "Black Pendant: The equipped monster gains 500 ATK. When this card is sent from the field to the Graveyard: Inflict 500 damage to your opponent.\n" +
                "Trap Hole: When your opponent Normal or Flip Summons 1 monster with 1000 or more ATK: Target that monster; destroy that target.\n"));

        Assertions.assertTrue(DeckController.getInstance().showDeck(matcher2).equals("Deck: deck3\n" +
                "Side deck:\n" +
                "Monsters:\n" +
                "Spell and Traps:\n" +
                "Black Pendant: The equipped monster gains 500 ATK. When this card is sent from the field to the Graveyard: Inflict 500 damage to your opponent.\n" +
                "Trap Hole: When your opponent Normal or Flip Summons 1 monster with 1000 or more ATK: Target that monster; destroy that target.\n"));
    }

    @Test
    @DisplayName("show all cards of a user")
    public void showAllCards() {
        User.getUserByUsername("user2").addCardToUserCards(Card.getCard("Command Knight"));
        User.getUserByUsername("user2").addCardToUserCards(Card.getCard("Trap Hole"));
        User.getUserByUsername("user2").addCardToUserCards(Card.getCard("Black Pendant"));
        User.getUserByUsername("user2").addCardToUserCards(Card.getCard("Scanner"));

        String deck2 = "deck show --deck-name deck2 --side";
        String deckRegex2 = "deck show --deck-name deck2 --side";
        Matcher matcher2 = Pattern.compile(deckRegex2).matcher(deck2);
        matcher2.find();

        Assertions.assertTrue(DeckController.getInstance().showCards(matcher2).equals("Black Pendant:The equipped monster gains 500 ATK. When this card is sent from the field to the Graveyard: Inflict 500 damage to your opponent.\n" +
                "Command Knight:All Warrior-Type monsters you control gain 400 ATK. If you control another monster, monsters your opponent controls cannot target this card for an attack.\n" +
                "Scanner:Once per turn, you can select 1 of your opponent's monsters that is removed from play. Until the End Phase, this card's name is treated as the selected monster's name, and this card has the same Attribute, Level, ATK, and DEF as the selected monster. If this card is removed from the field while this effect is applied, remove it from play.\n" +
                "Suijin:During damage calculation in your opponent's turn, if this card is being attacked: You can target the attacking monster; make that target's ATK 0 during damage calculation only (this is a Quick Effect). This effect can only be used once while this card is face-up on the field.\n" +
                "Trap Hole:When your opponent Normal or Flip Summons 1 monster with 1000 or more ATK: Target that monster; destroy that target.\n"));
    }

    @Test
    @DisplayName("show all decks of a user")
    public void showAllDecks() {

    }
}