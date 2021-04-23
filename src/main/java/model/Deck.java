package model;

import model.cards.Card;

import java.util.ArrayList;

public class Deck {
    private static ArrayList<Deck> allDecks;

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

    public static void setAllDecks(ArrayList<Deck> allDecks) {
        Deck.allDecks = allDecks;
    }

    public static boolean checkDeckNameExistence(String name) {
        return true;
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

    public void addCardToMainDeck(Card card) {

    }

    public void addCardToSideDeck(Card card) {

    }

    public void deleteCardToMainDeck(Card card) {

    }

    public void deleteCardToSideDeck(Card card) {

    }

    public boolean isDeckValid() {
        //TODO
        return true;
    }

    public String toString() {
        //TODO
        return "";
    }
}
