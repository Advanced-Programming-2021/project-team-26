package controller;

public class SpellTrapController {

    protected GameController gameController;
    protected SpellTrapPosition position;

    protected void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public SpellTrapPosition getPosition() {
        return position;
    }

    public void setPosition(SpellTrapPosition position) {
        this.position = position;
    }

    public GameController getGameController(){
        return gameController;
    }
}
