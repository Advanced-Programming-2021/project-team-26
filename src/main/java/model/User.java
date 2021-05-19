package model;

import controller.Database;
import model.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class User {
    private static final HashMap<String, User> allUsers;

    static {
        allUsers = Database.getInstance().getAllUsers();
    }

    protected String username;
    protected int score;
    protected HashMap<String, Deck> allDecks;
    protected HashMap<String, List<Integer>> allCards;
    protected Deck activeDeckName;
    protected int money;
    protected String password;
    protected String nickname;

    public User(String username, String password, String nickname) {
        allDecks = new HashMap<>();
        allCards = new HashMap<>();
        setUsername(username);
        setPassword(password);
        setNickname(nickname);
        setMoney(10000000);
        setActiveDeck(null);
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

    public HashMap<String, List<Integer>>  getAllCards() {
        return allCards;
    }

    public void setAllCards(HashMap<String, List<Integer>>  allCards) {
        this.allCards = allCards;
    }

    public Deck getActiveDeck() {
        return activeDeckName;
    }

    public void setActiveDeck(Deck activeDeckName) {
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

    public void increaseScore(int amount) {
        setScore(this.score + amount);
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
        if (allCards.containsKey(card.getName())){
            allCards.get(card.getName()).add(1);
        } else {
            List<Integer> numberOfCards = new ArrayList<>();
            numberOfCards.add(1);
            this.allCards.put(card.getName(), numberOfCards);
        }
    }

    public void addDeckToUserDecks(Deck deck) {
        this.allDecks.put(deck.getName(), deck);
    }

    public void decreaseMoney(int amount) {
        setMoney(this.money - amount);
    }

    public String showAllCards() {
        StringBuilder stringToReturn = new StringBuilder();
        HashMap<String, List<Integer>>  allCards = getAllCards();
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

        if (activeDeckName != null)
            stringToReturn.append(deckToString(activeDeckName.getName()));

        stringToReturn.append("Other decks:").append("\n");

        for (String deckName : allDecks.keySet()) {
            stringToReturn.append(deckToString(deckName));
        }

        return stringToReturn.toString();
    }

    private String deckToString(String name) {
        return name + ": " + "main deck " +
                Deck.getDeckByDeckName(name).getMainDeck().size() + ", " + "side deck " +
                Deck.getDeckByDeckName(name).getSideDeck().size() + ", " +
                (Deck.getDeckByDeckName(name).isDeckValid() ? "valid" : "invalid");
    }
}