package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Request;
import model.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ScoreBoardController {

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

    public static ArrayList<ScoreBoardController> getAndSetDataFromUser() {
        Request request = new Request("ScoreBoardController", "getAndSetDataFromUser");
        Response response = NetworkController.getInstance().sendAndReceive(request);
        Type type = new TypeToken<List<ScoreBoardController>>() {
        }.getType();
        return new Gson().fromJson(response.getData("ScoreBoardControllers"), type);
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

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

}