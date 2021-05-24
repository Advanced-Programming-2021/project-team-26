package controller;

import exceptions.TrapNotFoundException;
import model.AttackResult;
import model.cards.Card;
import model.cards.SpellTrap;
import model.cards.monster.Monster;
import model.cards.trap.Trap;
import view.Scan;

import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class TrapController extends SpellTrapController {
    private static final HashMap<String, TrapController.TrapMakerInterface> trapMakers = new HashMap<>();

    static {
        trapMakers.put("Magic Cylinder", TrapController::makeMagicCylinder);
        trapMakers.put("Mirror Force", TrapController::makeMirrorForce);
        trapMakers.put("Mind Crush", TrapController::makeMindCrush);
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
            @Override
            public boolean canActiveOnAttacked(MonsterController attacker, MonsterController defender) {
                return true;
            }

            @Override
            public AttackResult onAttacked(MonsterController attacker, MonsterController defender) {
                int damage = attacker.getCard().getAttackPower();
                return new AttackResult(damage, 0, false, false);
            }
        };
    }

    private static TrapController makeMirrorForce(GameController gameController, Trap trap, SpellTrapPosition position) {
        return new TrapController(gameController, trap, position) {
            @Override
            public boolean canActiveOnAttacked(MonsterController attacker, MonsterController defender) {
                return true;
            }

            @Override
            public AttackResult onAttacked(MonsterController attacker, MonsterController defender) {
                MonsterController[] attackerMonsters = gameController.getGame().getThisBoard().getMonstersZone();
                for (MonsterController monster : attackerMonsters) {
                    if (monster.getPosition() == MonsterPosition.ATTACK)
                        //TODO remove monster with effect or just remove it??
                        monster.remove(null);
                }
                return new AttackResult(0, 0, false, false);
            }
        };
    }

    private static TrapController makeMindCrush(GameController gameController, Trap trap, SpellTrapPosition position) {
        return new TrapController(gameController, trap, position) {
            @Override
            public void onActive() {
                String cardName = Scan.getInstance().getString();
                boolean found = false;
                List<Card> hand = gameController.getGame().getOtherBoard().getHand();
                for (Card card : hand) {
                    if (card.getName().equals(cardName)) {
                        found = true;
                        break;
                    }
                }

                if (found) {
                    List<Card> opDeck = gameController.getGame().getOtherBoard().getDeck();
                    opDeck.removeIf(card -> card.getName().equals(cardName));
                } else {
                    List<Card> myDeck = gameController.getGame().getThisBoard().getDeck();
                    int rnd = new Random().nextInt(myDeck.size());
                    myDeck.remove(rnd);
                }
            }
        };
    }

    @Override
    public SpellTrap getCard() {
        return this.trap;
    }

    @Override
    public void standBy() {

    }

    public boolean canActiveOnSummon(Monster summonMonster) {
        return false;
    }

    public void onSummon(Monster summonMonster) {

    }

    public boolean canActiveOnAttacked(MonsterController attacker, MonsterController defender) {
        return false;
    }

    public AttackResult onAttacked(MonsterController attacker, MonsterController defender) {
        return null;
    }

    public void onActive() {

    }

    public interface TrapMakerInterface {
        TrapController make(GameController gameController, Trap trap, SpellTrapPosition position);
    }
}
