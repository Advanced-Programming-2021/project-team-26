package model;

import controller.Database;
import model.cards.Card;
import model.cards.SpellTrap;
import model.cards.monster.Monster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Deck {
    private static final ArrayList<Deck> allDecks;

    static {
        allDecks = new ArrayList<>();
    }

    private String name;
    private String deckOwnerUsername;
    private ArrayList<Card> mainDeck;
    private ArrayList<Card> sideDeck;
    private ArrayList<Card> allCards;

    public Deck(String name, String deckOwnerUsername) {
        setName(name);
        setDeckOwner(deckOwnerUsername);
        setMainDeck(new ArrayList<>());
        setSideDeck(new ArrayList<>());
        setAllCards(new ArrayList<>());
        allDecks.add(this);
    }

    public static ArrayList<Deck> getAllDecks() {
        return allDecks;
    }

    public static boolean checkDeckNameExistence(String name) {
        for (Deck deck : allDecks) {
            if (deck.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static Deck getDeckByDeckName(String name) {
        for (Deck deck : allDecks) {
            if (deck.getName().equals(name))
                return deck;
        }
        return null;
    }

    public ArrayList<Card> getAllCards() {
        return allCards;
    }

    public void setAllCards(ArrayList<Card> allCards) {
        this.allCards = allCards;
    }

    public void setDeckOwner(String deckOwnerUsername) {
        this.deckOwnerUsername = deckOwnerUsername;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Card> getMainDeck() {
        return mainDeck;
    }

    public void setMainDeck(ArrayList<Card> mainDeck) {
        this.mainDeck = mainDeck;
    }

    public ArrayList<Card> getSideDeck() {
        return sideDeck;
    }

    public void setSideDeck(ArrayList<Card> sideDeck) {
        this.sideDeck = sideDeck;
    }

    public void addCardToMainDeck(Card card) {
        mainDeck.add(card);
        allCards.add(card);
    }

    public void addCardToSideDeck(Card card) {
        sideDeck.add(card);
        allCards.add(card);
    }

    public boolean IsNumberOfTheCardInDeckValid(String cardName) {
        int count = 0;

        for (Card card : allCards) {
            if (card.getName().equals(cardName))
                count++;
        }
        return count < 3;
    }

    public void deleteCardFromMainDeck(Card card) {
        mainDeck.removeIf(theCard -> theCard.getName().equals(card.getName()));
    }

    public void deleteCardFromSideDeck(Card card) {
        sideDeck.removeIf(theCard -> theCard.getName().equals(card.getName()));
    }

    public boolean isDeckValid() {
        return mainDeck.size() >= 40;
    }

    @Override
    public String toString() {
        StringBuilder stringToReturn = new StringBuilder();

        HashMap<String, Monster> monsters = Database.getInstance().getAllMonsters();
        HashMap<String, SpellTrap> spellTraps = Database.getInstance().getAllSpellTraps();

        stringToReturn.append("Deck: ").append(getName()).append("\n");
        stringToReturn.append("Main deck:").append("\n");

        stringToReturn.append("Monsters:").append("\n");
        ArrayList<String> sortedMonsters = new ArrayList<>(monsters.keySet());
        Collections.sort(sortedMonsters);
        for (String name : sortedMonsters) {
            stringToReturn.append(name).append(": ").append(monsters.get(name).getDescription()).append("\n");
        }

        stringToReturn.append("Spell and Traps:").append("\n");
        ArrayList<String> sortedSpellTraps = new ArrayList<>(spellTraps.keySet());
        Collections.sort(sortedSpellTraps);
        for (String name : sortedSpellTraps) {
            stringToReturn.append(name).append(": ").append(spellTraps.get(name).getDescription()).append("\n");
        }
        return stringToReturn.toString();
    }

    public String allCardsToString() {
        StringBuilder stringToReturn = new StringBuilder();
        HashMap<String, Card> allCards = User.getUserByUsername(deckOwnerUsername).getAllCards();
        ArrayList<String> sortedCardNames = new ArrayList<>(allCards.keySet());
        Collections.sort(sortedCardNames);

        for (String name : sortedCardNames) {
            stringToReturn.append(name).append(":").append(allCards.get(name).getDescription()).append("\n");
        }

        return stringToReturn.toString();
    }
}
