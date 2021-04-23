package model;

import model.cards.Card;

import java.util.ArrayList;
import java.util.List;

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
    private List<Deck> allDecks;
    private List<Card> allCards;
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
        return 0;
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

    public void setActiveDeck(Deck deck) {
        this.activeDeck = deck;
    }
}