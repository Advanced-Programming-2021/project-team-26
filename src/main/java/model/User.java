package model;

import controller.Database;
import model.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class User {
    private static final HashMap<String, User> allUsers;

    static {
        allUsers = Database.getInstance().getAllUsers();
    }

    protected String username;
    protected int score;
    protected HashMap<String, Deck> allDecks;
    protected HashMap<String, Integer> allCards;
    protected Deck activeDeck;
    protected int money;
    protected String password;
    protected String nickname;

    public User(String username, String password, String nickname) {
        allDecks = new HashMap<>();
        allCards = new HashMap<>();
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.money = 10000000;
        this.activeDeck = null;
        allUsers.put(username, this);
        Database.getInstance().writeUser(this);
    }

    protected User() {
        allDecks = new HashMap<>();
        allCards = new HashMap<>();
        this.username = "";
        this.password = "";
        this.nickname = "";
    }

    public static HashMap<String, User> getAllUsers() {
        return allUsers;
    }

    public boolean checkDeckNameExistence(String name) {
        return allDecks.containsKey(name);
    }

    public Deck getDeckByDeckName(String name) {
        if (allDecks.containsKey(name)) return allDecks.get(name);
        else return null;
    }

    public static String getPasswordByUsername(String username) {
        if (getUserByUsername(username) == null)
            return null;
        return getUserByUsername(username).getPassword();
    }

    public static User getUserByUsername(String username) {
        if (allUsers.containsKey(username))
            return allUsers.get(username);
        return null;
    }

    public static int compareTo(User first, User second) {
        //return -1: firstUser has priority
        if (first.getScore() > second.getScore())
            return -1;
        else if (first.getScore() == second.getScore() &&
                first.getUsername().compareTo(second.getUsername()) < 0)
            return 1;
        return 0;
    }

    public static void removeUser(String username) {
        allUsers.remove(username);
        Database.getInstance().removeUser(username);
    }

    public static boolean checkUsernameExistence(String username) {
        return allUsers.containsKey(username);
    }

    public static boolean checkNicknameExistence(String nickname) {
        for (User user : allUsers.values()) {
            if (user.getNickname().equals(nickname))
                return true;
        }
        return false;
    }

    public boolean doesUserHaveThisCard(String cardName) {
        return allCards.containsKey(cardName);
    }

    public HashMap<String, Deck> getAllDecks() {
        return allDecks;
    }

    public void setAllDecks(HashMap<String, Deck> allDecks) {
        this.allDecks = allDecks;
    }

    public HashMap<String, Integer>  getAllCards() {
        return allCards;
    }

    public void setAllCards(HashMap<String, Integer>  allCards) {
        this.allCards = allCards;
        Database.getInstance().writeUser(this);
    }

    public Deck getActiveDeck() {
        return activeDeck;
    }

    public void setActiveDeck(Deck activeDeckName) {
        this.activeDeck = activeDeckName;
        Database.getInstance().writeUser(this);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        Database.getInstance().writeUser(this);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        Database.getInstance().writeUser(this);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        Database.getInstance().writeUser(this);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        Database.getInstance().writeUser(this);
    }

    public void increaseScore(int amount) {
        setScore(this.score + amount);
    }

    public void increaseMoney(int amount) {
        setMoney(this.money + amount);
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
        Database.getInstance().writeUser(this);
    }

    public void addCardToUserCards(Card card) {
        if (allCards.containsKey(card.getName())){
           int numberOfCards = allCards.get(card.getName());
           allCards.put(card.getName(), ++numberOfCards);
        } else {
            this.allCards.put(card.getName(), 1);
        }
        Database.getInstance().writeUser(this);
    }

    public void addDeckToUserDecks(Deck deck) {
        this.allDecks.put(deck.getName(), deck);
        Database.getInstance().writeUser(this);
    }

    public void decreaseMoney(int amount) {
        setMoney(this.money - amount);
    }

    public String showAllCards() {
        StringBuilder stringToReturn = new StringBuilder();
        HashMap<String, Integer>  allCards = getAllCards();
        ArrayList<String> sortedCardNames = new ArrayList<>(allCards.keySet());
        Collections.sort(sortedCardNames);

        for (String name : sortedCardNames) {
            stringToReturn.append(name).append(":").append(Card.getCard(name).getDescription()).append("\n");
        }

        return stringToReturn.toString();
    }

    public String showAllDecks() {
        StringBuilder stringToReturn = new StringBuilder();

        stringToReturn.append("Decks").append("\n");
        stringToReturn.append("Active deck:").append("\n");

        if (activeDeck != null)
            stringToReturn.append(deckToString(activeDeck.getName()));

        stringToReturn.append("\nOther decks:").append("\n");

        for (String deckName : allDecks.keySet()) {
            stringToReturn.append(deckToString(deckName));
        }

        return stringToReturn.toString();
    }

    private String deckToString(String name) {
        return name + ": " + "main deck " +
                allDecks.get(name).getMainDeck().size() + ", " + "side deck " +
                allDecks.get(name).getSideDeck().size() + ", " +
                ( allDecks.get(name).isDeckValid() ? "valid" : "invalid");
    }
}