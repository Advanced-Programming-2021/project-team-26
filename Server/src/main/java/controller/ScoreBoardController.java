package controller;

import model.User;

import java.util.ArrayList;
import java.util.Comparator;

public class ScoreBoardController {
    private static final ArrayList<ScoreBoardController> scoreBoardControllers;

    static {
        scoreBoardControllers = new ArrayList<>();
    }

    private int rank;
    private String name;
    private int score;
    private boolean isOnline;

    public ScoreBoardController(int rank, String name, int score, boolean isOnline) {
        setRank(rank);
        setName(name);
        setScore(score);
        setOnline(isOnline);
    }

    public static ArrayList<ScoreBoardController> getScoreBoardControllers() {
        return scoreBoardControllers;
    }

    public static ArrayList<ScoreBoardController> getAndSetDataFromUser() {
        scoreBoardControllers.clear();
        ArrayList<User> users = sortUsersBasedScore();
        int counter = 1;
        for (User user : users) {
            scoreBoardControllers.add(new ScoreBoardController(counter, user.getUsername(), user.getScore(), Database.getInstance().isUserLoggedIn()));
            counter++;
        }
        return scoreBoardControllers;
    }

    public static ArrayList<User> sortUsersBasedScore() {
        ArrayList<User> users = new ArrayList<>(User.getAllUsers().values());
        users.sort(Comparator.comparing(User::getScore).reversed().thenComparing(User::getNickname));
        return users;
    }

    private void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

