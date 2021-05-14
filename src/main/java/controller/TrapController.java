package controller;

import exceptions.TrapNotFoundException;
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
        setCanSpellTrapsBeActive(true);
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

    @Override
    public SpellTrap getCard() {
        return this.trap;
    }

    @Override
    public void standBy() {

    }

    public interface TrapMakerInterface {
        TrapController make(GameController gameController, Trap trap, SpellTrapPosition position);
    }
}
