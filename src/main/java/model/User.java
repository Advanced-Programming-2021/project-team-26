package model;

import model.cards.Card;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private static ArrayList<User> allUsers;

    static {
        allUsers = new ArrayList<>();
    }

    private String username;
    private String password;
    private String nickname;
    private int score;
    private int money;
    private HashMap<String, Deck> allDecks;
    private HashMap<String, Card> allCards;
    private Deck activeDeck;

    public User(String username, String password, String nickname) {
        allDecks = new HashMap<>();
        allCards = new HashMap<>();
        setMoney(0);
        setUsername(username);
        setPassword(password);
        setNickname(nickname);
        allUsers.add(this);
    }

    public static ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public static void setAllUsers(ArrayList<User> allUsers) {
        User.allUsers = allUsers;
    }

    public static String getPasswordByUsername(String username) {
        if (getUserByUsername(username) == null)
            return null;
        return getUserByUsername(username).getPassword();
    }

    public static User getUserByUsername(String username) {
        for (User user : allUsers) {
            if (user.getUsername().equals(username))
                return user;
        }
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

    public Deck getActiveDeck() {
        return activeDeck;
    }

    public void setActiveDeck(Deck activeDeck) {
        this.activeDeck = activeDeck;
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
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void increaseMoney(int amount) {
        this.money += amount;
    }

    public int getMoney() {
        return money;
    }

    public void decreaseMoney(int amount) {
        this.money -= amount;
    }

    public void addCardToUserCards(Card card) {
        this.allCards.put(card.getName(), card);
    }

    public void addDeckToUserDecks(Deck deck) {
        this.allDecks.put(deck.getName(), deck);
    }
}