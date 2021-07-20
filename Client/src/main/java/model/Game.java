package model;

import controller.Phase;
import exceptions.NoPlayerAvailable;

public class Game {
    public final static int LIFE_POINT = 8000;
    private final Board[] boards = new Board[2];
    private final User[] users = new User[2];
    private final int[] lifePoints = new int[2];
    private final int turn = 1;
    private final Phase phase = Phase.END;

    public Game(User first, User second, Deck firstDeck, Deck secondDeck) throws NoPlayerAvailable {
        if (first == null || second == null)
            throw new NoPlayerAvailable();
        users[0] = first;
        users[1] = second;
        boards[0] = new Board(firstDeck, 0);
        boards[1] = new Board(secondDeck, 1);
        lifePoints[0] = LIFE_POINT;
        lifePoints[1] = LIFE_POINT;
    }

    public int getTurn() {
        //TODO
        return turn;
    }

    public User getUser(int turn) {
        return users[turn];
    }

    public Board getBoard(int turn) {
        return boards[turn];
    }

    public int getLifePoint(int turn) {
        return lifePoints[turn];
    }

    public Phase getPhase() {
        return this.phase;
    }

}
