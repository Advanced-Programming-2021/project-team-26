package model;

import model.cards.Card;

import java.util.ArrayList;

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
    private ArrayList<Deck> allDecks;
    private ArrayList<Card> allCards;
    private Deck activeDeck;

    public User(String username, String password, String nickname) {
        allDecks = new ArrayList<>();
        allCards = new ArrayList<>();
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
        //return 1: firstUser has priority
        if (first.getScore() > second.getScore())
            return 1;
        else if (first.getScore() == second.getScore() &&
                first.getUsername().compareTo(second.getUsername()) < 0)
            return 1;
        return -1;
    }

    public ArrayList<Deck> getAllDecks() {
        return allDecks;
    }

    public void setAllDecks(ArrayList<Deck> allDecks) {
        this.allDecks = allDecks;
    }

    public ArrayList<Card> getAllCards() {
        return allCards;
    }

    public void setAllCards(ArrayList<Card> allCards) {
        this.allCards = allCards;
    }

    public Deck getActiveDeck() {
        return activeDeck;
    }

    public void setActiveDeck(Deck deck) {
        this.activeDeck = deck;
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

    public void decreaseMoney(int amount) {
        this.money -= amount;
    }

    public void addCardToUserCards(Card card) {
        this.allCards.add(card);
    }

    public void addDeckToUserDecks(Deck deck) {
        this.allDecks.add(deck);
    }
}