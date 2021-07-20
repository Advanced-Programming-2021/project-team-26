package model;

import com.google.gson.Gson;
import controller.NetworkController;
import controller.Phase;
import exceptions.NoPlayerAvailable;

public class Game {
    public final static int LIFE_POINT = 8000;
    private final Board[] boards = new Board[2];
    private final User[] users = new User[2];

    public Game(User first, User second) throws NoPlayerAvailable {
        if (first == null || second == null)
            throw new NoPlayerAvailable();
        users[0] = first;
        users[1] = second;
        boards[0] = new Board( 0);
        boards[1] = new Board( 1);
    }

    public int getTurn() {
        Request request = new Request("GameController","Game");
        request.addParameter("function","getTurn");
        Response response = NetworkController.getInstance().sendAndReceive(request);
        return Integer.parseInt(response.getData("turn"));
    }

    public User getUser(int turn) {
        return users[turn];
    }

    public Board getBoard(int turn) {
        return boards[turn];
    }

    public int getLifePoint(int turn) {
        Request request = new Request("GameController","Game");
        request.addParameter("function","getLifePoint");
        request.addParameter("turn",turn);
        Response response = NetworkController.getInstance().sendAndReceive(request);
        return Integer.parseInt(response.getData("lp"));
    }

    public Phase getPhase() {
        Request request = new Request("GameController","Game");
        request.addParameter("function","getPhase");
        Response response = NetworkController.getInstance().sendAndReceive(request);
        return new Gson().fromJson(response.getData("phase"),Phase.class);
    }

}
