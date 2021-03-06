package controller;

import Utilities.Alert;
import exceptions.TrapNotFoundException;
import model.AttackResult;
import model.cards.Card;
import model.cards.SpellTrap;
import model.cards.trap.Trap;

import java.util.*;

public class TrapController extends SpellTrapController {
    private static final HashMap<String, TrapController.TrapMakerInterface> trapMakers = new HashMap<>();

    static {
        trapMakers.put("Magic Cylinder", TrapController::makeMagicCylinder);
        trapMakers.put("Mirror Force", TrapController::makeMirrorForce);
        trapMakers.put("Mind Crush", TrapController::makeMindCrush);
        trapMakers.put("Trap Hole", TrapController::makeTrapHole);
        trapMakers.put("Torrential Tribute", TrapController::makeTorrentialTribute);
        trapMakers.put("Time Seal", TrapController::makeTimeSeal);
        trapMakers.put("Negate Attack", TrapController::makeNegateAttack);
        trapMakers.put("Solemn Warning", TrapController::makeSolemnWarning);
        trapMakers.put("Magic Jammer", TrapController::makeMagicJammer);
        trapMakers.put("Call of the Haunted", TrapController::makeCallOfTheHaunted);
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
        return new TrapController(gameController, trap, position);
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
                Collection<MonsterController> attackerMonsters = otherBoard.getMonstersZone();
                for (MonsterController monster : attackerMonsters) {
                    if (monster.getPosition() == MonsterPosition.ATTACK)
                        otherBoard.removeMonster(monster);
                }
                return new AttackResult(0, 0, false, false);
            }
        };
    }

    private static TrapController makeMindCrush(GameController gameController, Trap trap, SpellTrapPosition position) {
        return new TrapController(gameController, trap, position) {
            @Override
            public void onActive() {
                String cardName = gameController.getViews()[myTurn].getString("enter card name to remove:");
                boolean found = false;
                List<Card> hand = otherBoard.getDeck();
                for (Card card : hand) {
                    if (card.getName().equals(cardName)) {
                        found = true;
                        break;
                    }
                }

                if (found) {
                    List<Card> opDeck = otherBoard.getDeck();
                    opDeck.removeIf(card -> card.getName().equals(cardName));
                    Alert.getInstance().successfulPrint("all " + cardName + " are removed from opponent deck");
                } else {
                    List<Card> myDeck = thisBoard.getDeck();
                    int rnd = new Random().nextInt(myDeck.size());
                    Card removed = myDeck.remove(rnd);
                    Alert.getInstance().successfulPrint(removed.getName() + " is removed from your deck");
                }
            }
        };
    }

    private static TrapController makeTrapHole(GameController gameController, Trap trap, SpellTrapPosition position) {
        return new TrapController(gameController, trap, position) {
            @Override
            public boolean canActiveOnSummon(MonsterController summonMonster, String type) {
                return (type.equals("normal") || type.equals("flip")) &&
                        (summonMonster.getMonster().getAttackPower() >= 1000);
            }

            @Override
            public boolean onSummon(MonsterController summonMonster, String type) {
                otherBoard.removeMonster(summonMonster);
                return true;
            }
        };
    }

    private static TrapController makeTorrentialTribute(GameController gameController, Trap trap, SpellTrapPosition position) {
        return new TrapController(gameController, trap, position) {
            @Override
            public boolean canActiveOnSummon(MonsterController summonMonster, String type) {
                return true;
            }

            @Override
            public boolean onSummon(MonsterController summonMonster, String type) {
                for (MonsterController monster : thisBoard.getMonstersZone()) {
                    thisBoard.removeMonster(monster);
                }
                for (MonsterController monster : otherBoard.getMonstersZone()) {
                    otherBoard.removeMonster(monster);
                }
                Alert.getInstance().successfulPrint("all monsters removed");
                return true;
            }
        };
    }

    private static TrapController makeTimeSeal(GameController gameController, Trap trap, SpellTrapPosition position) {
        return new TrapController(gameController, trap, position) {

        };
    }

    private static TrapController makeNegateAttack(GameController gameController, Trap trap, SpellTrapPosition position) {
        return new TrapController(gameController, trap, position) {
            @Override
            public boolean canActiveOnAttacked(MonsterController attacker, MonsterController defender) {
                return true;
            }

            @Override
            public AttackResult onAttacked(MonsterController attacker, MonsterController defender) {
                AttackResult result = new AttackResult(0, 0, false, false);
                result.setMessage("attack is negated");
                gameController.getGame().nextPhase();
                return result;
            }
        };
    }

    private static TrapController makeSolemnWarning(GameController gameController, Trap trap, SpellTrapPosition position) {
        return new TrapController(gameController, trap, position) {
            @Override
            public boolean canActiveOnSummon(MonsterController summonMonster, String type) {
                return type.equals("normal") || type.equals("special");
            }

            @Override
            public boolean onSummon(MonsterController summonMonster, String type) {
                gameController.getGame().decreaseLifePoint(myTurn, 2000);
                otherBoard.removeMonster(summonMonster);
                Alert.getInstance().successfulPrint("your monster removed");
                return true;
            }
        };
    }

    private static TrapController makeMagicJammer(GameController gameController, Trap trap, SpellTrapPosition position) {
        return new TrapController(gameController, trap, position) {

        };
    }

    private static TrapController makeCallOfTheHaunted(GameController gameController, Trap trap, SpellTrapPosition position) {
        return new TrapController(gameController, trap, position) {

        };
    }

    @Override
    public SpellTrap getCard() {
        return this.trap;
    }

    @Override
    public void standBy() {

    }

    public boolean canActiveOnSummon(MonsterController summonMonster, String type) {
        return false;
    }

    public boolean onSummon(MonsterController summonMonster, String type) {
        return false;
    }

    public boolean canActiveOnAttacked(MonsterController attacker, MonsterController defender) {
        return false;
    }

    public AttackResult onAttacked(MonsterController attacker, MonsterController defender) {
        return null;
    }

    public void onActive() {

    }

    public void remove() {
        thisBoard.removeSpellTrap(this);
    }

    public interface TrapMakerInterface {
        TrapController make(GameController gameController, Trap trap, SpellTrapPosition position);
    }
}
