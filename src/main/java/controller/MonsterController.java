package controller;

import exceptions.MonsterNotFoundException;
import model.cards.monster.Monster;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MonsterController {
    private static final HashMap<String, MonsterMakerInterface> MONSTER_MAKERS;

    static {
        MONSTER_MAKERS = new HashMap<>();
        MONSTER_MAKERS.put("Command knight", MonsterController::makeCommandKnight);
        MONSTER_MAKERS.put("Yomi Ship", MonsterController::makeYomiShip);
        MONSTER_MAKERS.put("Suijin", MonsterController::makeSuijin);
        MONSTER_MAKERS.put("Man-Eater Bug", MonsterController::makeManEaterBug);
        MONSTER_MAKERS.put("Gate Guardian", MonsterController::makeGateGuardian);
        MONSTER_MAKERS.put("Scanner", MonsterController::makeScanner);
        MONSTER_MAKERS.put("Marshmallon", MonsterController::makeMarshmallon);
        MONSTER_MAKERS.put("The Calculator", MonsterController::makeTheCalculator);
    }

    private final GameController gameController;
    private final Monster monster;
    private MonsterPosition position;
    private boolean hasPositionChanged = false;
    private boolean isMonsterNew = true;
    private boolean hasAttackedThisTurn;

    private MonsterController(GameController gameController, Monster monster, MonsterPosition position) {
        this.gameController = gameController;
        this.monster = new Monster(monster);
        this.position = position;
    }

    public static MonsterController getInstance(GameController gameController, Monster monster, MonsterPosition position)
            throws MonsterNotFoundException {
        for (String monsterName : MONSTER_MAKERS.keySet()) {
            if (monster.getName().equals(monsterName)) {
                return MONSTER_MAKERS.get(monsterName).make(gameController, monster, position);
            }
        }
        throw new MonsterNotFoundException();
    }

    private static MonsterController makeCommandKnight
            (GameController gameController, Monster monster, MonsterPosition position) {
        return new MonsterController(gameController, monster, position) {
            private final Set<MonsterController> underEffectMonsters = new HashSet<>();
            private boolean isEffectActive = position.equals(MonsterPosition.ATTACK);

            @Override
            public void runMonsterEffect() {
                if (isEffectActive) {
                    //increase other monsters attackPower for 400
                    MonsterController[] monstersZone = gameController.getGame().getThisBoard().getMonstersZone();
                    for (MonsterController monsterController : monstersZone) {
                        if (!underEffectMonsters.contains(monsterController)) {
                            monsterController.monster.increaseAttackPower(400);
                            underEffectMonsters.add(monsterController);
                        }
                    }
                }
            }

            //cant be attacked while there are some other monsters in the field
            @Override
            public boolean canBeAttacked(MonsterController attacker) {
                if (isEffectActive) {
                    return gameController.getGame().getThisBoard().getMonstersZone().length < 2;
                }
                return true;
            }

            @Override
            public void endMonsterEffect() {
                for (MonsterController monsterController : underEffectMonsters) {
                    monsterController.monster.decreaseAttackPower(400);
                }
                isEffectActive = false;
            }
        };
    }

    private static MonsterController makeYomiShip
            (GameController gameController, Monster monster, MonsterPosition position) {
        return new MonsterController(gameController, monster, position) {
            @Override
            public void remove(MonsterController attacker) {
                gameController.getGame().getThisBoard().removeMonster(this);
                gameController.getGame().getOtherBoard().removeMonster(attacker);
            }
        };
    }

    private static MonsterController makeSuijin
            (GameController gameController, Monster monster, MonsterPosition position) {
        return new MonsterController(gameController, monster, position) {
            private final boolean wasEffectActiveBefore = false;
            private boolean isEffectActive = false;

            @Override
            public void runMonsterEffect() {
                if (!wasEffectActiveBefore) {
                    isEffectActive = true;
                }
            }

            @Override
            public void attacked(MonsterController attacker) {
                if (isEffectActive) {
                    int theAttackerPower = attacker.monster.getAttackPower();
                    attacker.monster.setAttackPower(0);

                    //attacked default method

                    attacker.monster.setAttackPower(theAttackerPower);
                }
            }

        };
    }

    private static MonsterController makeManEaterBug
            (GameController gameController, Monster monster, MonsterPosition position) {
        return new MonsterController(gameController, monster, position) {
            @Override
            public void flip(MonsterController selectedMonster) {
                // here we should select a monster to remove it
                selectedMonster.setPosition(MonsterPosition.ATTACK);
            }
        };
    }

    private static MonsterController makeGateGuardian
            (GameController gameController, Monster monster, MonsterPosition position) {
        return new MonsterController(gameController, monster, position) {
        };
    }

    private static MonsterController makeScanner
            (GameController gameController, Monster monster1, MonsterPosition position) {
        //select one of rival monsters to be like it
        MonsterController rivalMonsterController = null;
        Monster monster = new Monster(rivalMonsterController.monster);

        return new MonsterController(gameController, monster, position) {

        };
    }

    private static MonsterController makeMarshmallon
            (GameController gameController, Monster monster, MonsterPosition position) {
        return new MonsterController(gameController, monster, position) {
        };
    }

    private static MonsterController makeTheCalculator
            (GameController gameController, Monster monster, MonsterPosition position) {
        return new MonsterController(gameController, monster, position) {
            @Override
            public void runMonsterEffect() {
                MonsterController[] monstersZone = gameController.getGame().getThisBoard().getMonstersZone();
                for (MonsterController monsterController : monstersZone) {
                    if (monsterController.position.equals(MonsterPosition.ATTACK)) {
                        monster.increaseAttackPower(monsterController.monster.getLevel() * 300);
                    }
                }
            }
        };
    }


    public boolean isHasPositionChanged() {
        return hasPositionChanged;
    }

    public void setHasPositionChanged(boolean hasPositionChanged) {
        this.hasPositionChanged = hasPositionChanged;
    }

    public void finishTurn() {
        hasPositionChanged = false;
        isMonsterNew = false;
    }

    public void runMonsterEffect() {

    }

    public void endMonsterEffect() {

    }

    public boolean canBeAttacked(MonsterController attacker) {
        return true;
    }

    public MonsterPosition getPosition() {
        return position;
    }

    public void setPosition(MonsterPosition position) {
        this.position = position;
    }

    public void attacked(MonsterController attacker) {
    }

    public void summon() {

    }

    public void oneTributeSummon(MonsterController tributeMonster) {
        remove(tributeMonster);
        position = MonsterPosition.ATTACK;
    }

    public void twoTributeSummon(MonsterController firstTributeMonster, MonsterController secondTributeMonster) {
        remove(firstTributeMonster);
        remove(secondTributeMonster);
        position = MonsterPosition.ATTACK;
    }

    public void flip(MonsterController selectedMonster) {
        selectedMonster.setPosition(MonsterPosition.ATTACK);
    }

    public void specialSummon() {

    }

    public void activate() {

    }

    public void remove(MonsterController attacker) {
        gameController.getGame().getThisBoard().removeMonster(this);
    }

    public String getName() {
        return monster.getName();
    }

    public Monster getCard() {
        return this.monster;
    }

    public void set() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof MonsterController)) {
            return false;
        }

        MonsterController monsterController = (MonsterController) o;

        return monsterController.monster.getName().equals(this.monster.getName());
    }

    public boolean isMonsterNew() {
        return isMonsterNew;
    }

    public void setMonsterNew(boolean monsterNew) {
        this.isMonsterNew = monsterNew;
    }

    public boolean isHasAttackedThisTurn() {
        return hasAttackedThisTurn;
    }

    public void setHasAttackedThisTurn(boolean hasAttackedThisTurn) {
        this.hasAttackedThisTurn = hasAttackedThisTurn;
    }

    public interface MonsterMakerInterface {
        MonsterController make(GameController gameController, Monster monster, MonsterPosition position);
    }
}
