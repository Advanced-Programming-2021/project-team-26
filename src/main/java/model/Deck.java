package model;

import model.cards.Card;

import java.util.ArrayList;

public class Deck {
    private static ArrayList<Deck> allDecks = new ArrayList<>();
    private String name;
    private ArrayList<Card> mainDeck;
    private ArrayList<Card> sideDeck;

    public Deck(String name) {
        this.name = name;
        setMainDeck(new ArrayList<>());
        setSideDeck(new ArrayList<>());
        allDecks.add(this);
    }

    public static ArrayList<Deck> getAllDecks() {
        return allDecks;
    }

    public static void deleteDeck(String name) {

    }

    public static boolean checkDeckNameExist(String name) {
        return true;
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

    public boolean isValidDeck() {
        //TODO
        return true;
    }

    public String toString() {
        //TODO
        return "";
    }
}
