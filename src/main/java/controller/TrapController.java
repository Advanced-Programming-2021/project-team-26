package controller;

import controller.exceptions.TrapNotFoundException;
import model.cards.SpellTrap;
import model.cards.trap.Trap;

import java.util.HashMap;


public class TrapController extends SpellTrapController {
    private static final HashMap<String, TrapController.TrapMakerInterface> trapMakers = new HashMap<>();

    static {
        trapMakers.put("Magic Cylinder", TrapController::makeMagicCylinder);
    }

    private final Trap trap;

    private TrapController(GameController gameController, Trap trap, SpellTrapPosition position) {
        this.gameController = gameController;
        this.trap = new Trap(trap);
        this.position = position;
    }

    public static TrapController getInstance(GameController gameController, Trap trap, SpellTrapPosition position) throws TrapNotFoundException {
        for (String trapName : trapMakers.keySet()) {
            if (trap.getName().equals(trapName)) {
                return trapMakers.get(trapName).make(gameController, trap, position);
            }
        }
        throw new TrapNotFoundException();
    }

    private static TrapController makeMagicCylinder(GameController gameController, Trap trap, SpellTrapPosition position) {
        return new TrapController(gameController, trap, position) {
            //here override methods
        };
    }

    public void activate() {

    }

    public void remove() {

    }

    @Override
    public SpellTrap getCard() {
        return this.trap;
    }

    public interface TrapMakerInterface {
        TrapController make(GameController gameController, Trap trap, SpellTrapPosition position);
    }
}
