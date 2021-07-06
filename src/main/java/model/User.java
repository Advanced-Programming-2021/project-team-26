package model;

import controller.Database;
import model.cards.Card;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class User {
    private static final HashMap<String, User> allUsers;
    private static final String defaultProfile = "src/main/resources/characters/Chara001.dds.png";

    static {
        allUsers = Database.getInstance().getAllUsers();
    }

    protected String username;
    protected int score;
    protected HashMap<String, Deck> allDecks;
    protected HashMap<String, Integer> allCards;
    protected String activeDeckName;
    protected int money;
    protected String password;
    protected String nickname;
    private String profileImagePath;

    public User(String username, String password, String nickname) {
        allDecks = new HashMap<>();
        allCards = new HashMap<>();
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.money = 100000;
        addDefaultDeck();
        this.profileImagePath = defaultProfile;
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

    private void addDefaultDeck() {
        Deck defaultDeck = (Deck) Database.getInstance().readDefaultDeck().clone();
        defaultDeck.setDeckOwner(this.username);
        this.allDecks.put(defaultDeck.getName(), defaultDeck);
        this.activeDeckName = defaultDeck.getName();
        for (String cardName : defaultDeck.getMainDeck()) {
            if (allCards.containsKey(cardName)) {
                int numberOfCards = allCards.get(cardName);
                allCards.put(cardName, ++numberOfCards);
            } else {
                allCards.put(cardName, 1);
            }
        }
        for (String cardName : defaultDeck.getSideDeck()) {
            if (allCards.containsKey(cardName)) {
                int numberOfCards = allCards.get(cardName);
                allCards.put(cardName, ++numberOfCards);
            } else {
                allCards.put(cardName, 1);
            }
        }
    }

    public boolean checkDeckNameExistence(String name) {
        return allDecks.containsKey(name);
    }

    public Deck getDeckByDeckName(String name) {
        return allDecks.getOrDefault(name, null);
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

    public HashMap<String, Integer> getAllCards() {
        return allCards;
    }

    public void setAllCards(HashMap<String, Integer> allCards) {
        this.allCards = allCards;
        Database.getInstance().writeUser(this);
    }

    public Deck getActiveDeck() {
        return allDecks.get(activeDeckName);
    }

    public void setActiveDeck(Deck activeDeckName) {
        this.activeDeckName = activeDeckName.getName();
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
        if (allCards.containsKey(card.getName())) {
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
        HashMap<String, Integer> allCards = getAllCards();
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
            stringToReturn.append(deckToString(activeDeckName));

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
                (allDecks.get(name).isDeckValid() ? "valid" : "invalid");
    }

    public String getProfileImagePath() {
        return "file:" + System.getProperty("user.dir") + File.separator + profileImagePath;
    }

    public boolean setProfileImage(File file) {
        String path = Database.getInstance().writeProfile(file, username);
        if (path == null)
            return false;
        profileImagePath = path;
        Database.getInstance().writeUser(this);
        return true;
    }
}