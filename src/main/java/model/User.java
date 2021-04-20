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
    private int Score;
    private int money;
    private ArrayList<Deck> allDecks;
    private ArrayList<Card> allCards;
    private Deck activeDeck;

    public User(String username, String password, String nickname) {
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

    public static User getUserByUsername(String username){
        for(User user: allUsers){
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
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
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    public static int compareTo(User first,User second){
        return 0;
    }
}