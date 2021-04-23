package model;

public class Game {
    private Board[] boards = new Board[2];
    private User[] users = new User[2];
    private int[] lifePoints = new int[2];
    private int turn = 0;

    public int getTurn() {
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
}
