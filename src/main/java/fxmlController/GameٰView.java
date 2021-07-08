package fxmlController;

import controller.GameController;

public class GameٰView {

    private final GameController controller;
    private final int turn;


    public GameٰView(GameController controller, int turn) {
        this.controller = controller;
        this.turn = turn;
    }
}
