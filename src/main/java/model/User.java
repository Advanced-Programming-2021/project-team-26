package model;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private String nickname;
    private int Score;
    private int money;
    private ArrayList<Deck> allDecks;
    private Deck activeDeck;

    public User(String username, String password, String nickname, int score) {
        setUsername(username);
        setPassword(password);
        setNickname(nickname);
        setScore(score);
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
}
