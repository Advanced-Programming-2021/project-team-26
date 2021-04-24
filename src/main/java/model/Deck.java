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
    private User deckOwner;
    private ArrayList<Card> mainDeck;
    private ArrayList<Card> sideDeck;


    public Deck(String name, User deckOwner) {
        setName(name);
        setDeckOwner(deckOwner);
        setMainDeck(new ArrayList<>());
        setSideDeck(new ArrayList<>());
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

    public User getDeckOwner() {
        return deckOwner;
    }

    public void setDeckOwner(User deckOwner) {
        this.deckOwner = deckOwner;
    }

    public void deleteDeck(String name) {
        getDeckOwner().getAllDecks().removeIf(deck -> deck.getName().equals(name));
        allDecks.removeIf(deck -> deck.getName().equals(name));
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

    public int addCardToMainDeck(Card card) {
        if (!doesUserHaveThisCard(card))
            return -2; //user doesnt have this card
        else if (mainDeck.size() == 60)
            return -1; //deck is full
        else {
            mainDeck.add(card);
            return 1; //method has been run successfully
        }
    }

    public int addCardToSideDeck(Card card) {
        if (!doesUserHaveThisCard(card))
            return -2; //user doesnt have this card
        else if (sideDeck.size() == 15)
            return -1; //deck is full
        else {
            sideDeck.add(card);
            return 1; //method has been run successfully
        }
    }

    public int deleteCardFromMainDeck(Card card) {
        if (!doesUserHaveThisCard(card))
            return -1; //user doesnt have this card
        else {
            mainDeck.removeIf(theCard -> theCard.getName().equals(card.getName()));
            return 1; //method has been run successfully
        }
    }

    public int deleteCardFromSideDeck(Card card) {
        if (!doesUserHaveThisCard(card))
            return -1; //user doesnt have this card
        else {
            sideDeck.removeIf(theCard -> theCard.getName().equals(card.getName()));
            return 1; //method has been run successfully
        }
    }

    public boolean doesUserHaveThisCard(Card card) {
        ArrayList<Card> allCards = getDeckOwner().getAllCards();
        for (Card theCard : allCards) {
            if (theCard.getName().equals(card.getName()))
                return true;
        }
        return false;
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
        for (String key : sortedMonsters) {
            stringToReturn.append(key).append(": ").append(monsters.get(key).getDescription());
        }

        stringToReturn.append("Spell and Traps:").append("\n");
        ArrayList<String> sortedSpellTraps = new ArrayList<>(spellTraps.keySet());
        Collections.sort(sortedSpellTraps);
        for (String key : sortedSpellTraps) {
            stringToReturn.append(key).append(": ").append(spellTraps.get(key).getDescription());
        }
        return stringToReturn.toString();
    }
}
