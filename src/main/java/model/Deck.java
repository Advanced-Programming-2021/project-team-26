package model;

import controller.Database;
import exceptions.CardNotFoundException;
import exceptions.FullDeckException;
import model.cards.Card;
import model.cards.SpellTrap;
import model.cards.monster.Monster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Deck {
    private static ArrayList<Deck> allDecks;

    static {
        allDecks = new ArrayList<>();
    }

    private String name;
    private String deckOwnerUsername;
    private ArrayList<Card> mainDeck;
    private ArrayList<Card> sideDeck;


    public Deck(String name, User deckOwner) {
        setName(name);
        setDeckOwner(deckOwnerUsername);
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

    public void setDeckOwner(String deckOwnerUsername) {
        this.deckOwnerUsername = deckOwnerUsername;
    }

    public void deleteDeck(String name) {
        User.getUserByUsername(deckOwnerUsername).getAllDecks().remove(name);
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

    public void addCardToMainDeck(Card card) throws CardNotFoundException, FullDeckException {
        if (!doesUserHaveThisCard(card))
            throw new CardNotFoundException();
        else if (mainDeck.size() == 60)
            throw new FullDeckException();
        else
            mainDeck.add(card);
    }

    public void addCardToSideDeck(Card card) throws CardNotFoundException, FullDeckException {
        if (!doesUserHaveThisCard(card))
            throw new CardNotFoundException();
        else if (sideDeck.size() == 15)
            throw new FullDeckException();
        else
            sideDeck.add(card);
    }

    public void deleteCardFromMainDeck(Card card) throws CardNotFoundException {
        if (!doesUserHaveThisCard(card))
            throw new CardNotFoundException();
        else
            mainDeck.removeIf(theCard -> theCard.getName().equals(card.getName()));

    }

    public void deleteCardFromSideDeck(Card card) throws CardNotFoundException {
        if (!doesUserHaveThisCard(card))
            throw new CardNotFoundException();
        else
            sideDeck.removeIf(theCard -> theCard.getName().equals(card.getName()));
    }

    public boolean doesUserHaveThisCard(Card card) {
        for (String name : User.getUserByUsername(deckOwnerUsername).getAllCards().keySet()) {
            if (card.getName().equals(name))
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
