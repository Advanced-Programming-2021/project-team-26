package model;

import controller.GameController;
import controller.Phase;
import exceptions.NoPlayerAvailable;

public class Game {
    public final static int LIFE_POINT = 8000;
    private final Board[] boards = new Board[2];
    private final User[] users = new User[2];
    private final int[] lifePoints = new int[2];
    private GameController gameController;
    private final int turn = 1;
    private final Phase phase = Phase.END;
    private int winner = -1;

    public Game(GameController gameController, User first, User second, Deck firstDeck, Deck secondDeck) throws NoPlayerAvailable {
        if (first == null || second == null)
            throw new NoPlayerAvailable();
        this.gameController = gameController;
        users[0] = first;
        users[1] = second;
        boards[0] = new Board(gameController, firstDeck,0);
        boards[1] = new Board(gameController, secondDeck,1);
        lifePoints[0] = LIFE_POINT;
        lifePoints[1] = LIFE_POINT;
    }

    public void setFinished(boolean finished) {
    }

    public void setSurrenderPlayer(int surrenderPlayer) {
        winner = 1 - surrenderPlayer;
    }

    public int getTurn() {
        return turn;
    }

    public User getUser(int turn) {
        return users[turn];
    }

    public User getThisUser() {
        return users[turn];
    }

    public User getOtherUser() {
        return users[1 - turn];
    }

    public Board getBoard(int turn) {
        return boards[turn];
    }

    public int getLifePoint(int turn) {
        return lifePoints[turn];
    }

    public void decreaseLifePoint(int turn, int amount) {
        lifePoints[turn] -= amount;
        gameController.getViews()[0].updateLifePoint();
        gameController.getViews()[1].updateLifePoint();
        if (lifePoints[turn] <= 0) {
            winner = 1 - turn;
            gameController.endGame();
        }
    }

    public void decreaseThisLifePoint(int amount) {
        decreaseLifePoint(turn, amount);
    }

    public Phase getPhase() {
        return this.phase;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

}
