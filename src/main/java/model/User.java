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

    private String username;
    private String password;
    private String nickname;
    private int score;
    private int money;
    private HashMap<String, Deck> allDecks;
    private HashMap<String, Card> allCards;
    private String activeDeckName;

    public User(String username, String password, String nickname) {
        allDecks = new HashMap<>();
        allCards = new HashMap<>();
        setUsername(username);
        setPassword(password);
        setNickname(nickname);
        setMoney(0);
        setActiveDeck(null);
        allUsers.put(username, this);
        Database.getInstance().writeUser(this);
    }

    public static HashMap<String, User> getAllUsers() {
        return allUsers;
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

    public HashMap<String, Card> getAllCards() {
        return allCards;
    }

    public void setAllCards(HashMap<String, Card> allCards) {
        this.allCards = allCards;
    }

    public String getActiveDeck() {
        return activeDeckName;
    }

    public void setActiveDeck(String activeDeckName) {
        this.activeDeckName = activeDeckName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public void increaseMoney(int amount) {
        this.money += amount;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
        Database.getInstance().writeUser(this);
    }

    public void addCardToUserCards(Card card) {
        this.allCards.put(card.getName(), card);
    }

    public void addDeckToUserDecks(Deck deck) {
        this.allDecks.put(deck.getName(), deck);
    }

    public void decreaseMoney(int amount) {
        setMoney(this.money - amount);
    }

    public String showAllCards() {
        StringBuilder stringToReturn = new StringBuilder();
        HashMap<String, Card> allCards = getAllCards();
        ArrayList<String> sortedCardNames = new ArrayList<>(allCards.keySet());
        Collections.sort(sortedCardNames);

        for (String name : sortedCardNames) {
            stringToReturn.append(name).append(":").append(allCards.get(name).getDescription()).append("\n");
        }

        return stringToReturn.toString();
    }

    public String showAllDecks() {
        StringBuilder stringToReturn = new StringBuilder();

        stringToReturn.append("Decks").append("\n");
        stringToReturn.append("Active deck:").append("\n");

        if (activeDeckName != null)
            stringToReturn.append(deckToString(activeDeckName));

        stringToReturn.append("Other decks:").append("\n");

        for (String deckName : allDecks.keySet()) {
            if (!deckName.equals(activeDeckName))
                stringToReturn.append(deckToString(deckName));
        }

        return stringToReturn.toString();
    }

    private String deckToString(String name) {
        StringBuilder stringToReturn = new StringBuilder();

        stringToReturn.append(name).append(": ").append("main deck ").
                append(Deck.getDeckByDeckName(name).getMainDeck().size()).append(", ").append("side deck ").
                append(Deck.getDeckByDeckName(name).getSideDeck().size()).append(", ").
                append(Deck.getDeckByDeckName(name).isDeckValid());

        return stringToReturn.toString();
    }
}