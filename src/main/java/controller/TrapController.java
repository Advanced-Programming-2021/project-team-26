package controller;

import controller.exceptions.TrapNotFoundException;
import model.cards.trap.Trap;

import java.util.HashMap;

enum TrapPosition {
    UP,
    DOWN
}

public class TrapController {
    private static HashMap<String, TrapController.TrapMakerInterface> trapMakers = new HashMap<>();

    static {
        trapMakers.put("Magic Cylinder", TrapController::makeMagicCylinder);
    }

    private final GameController gameController;
    private final Trap trap;
    private final TrapPosition position;

    private TrapController(GameController gameController, Trap trap, TrapPosition position) {
        this.gameController = gameController;
        this.trap = new Trap(trap);
        this.position = position;
    }

    public static TrapController getInstance(GameController gameController, Trap trap, TrapPosition position) throws TrapNotFoundException {
        for (String trapName : trapMakers.keySet()) {
            if (trap.getName().equals(trapName)) {
                return trapMakers.get(trapName).make(gameController, trap, position);
            }
        }
        throw new TrapNotFoundException();
    }

    private static TrapController makeMagicCylinder(GameController gameController, Trap trap, TrapPosition position) {
        return new TrapController(gameController, trap, position) {
            //here override methods
        };
    }

    public void activate() {

    }

    public void remove() {

    }

    public interface TrapMakerInterface {
        TrapController make(GameController gameController, Trap trap, TrapPosition position);
    }
}
