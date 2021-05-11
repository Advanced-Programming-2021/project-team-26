package controller;

import exceptions.MonsterNotFoundException;
import model.cards.monster.Monster;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MonsterController {
    private static final HashMap<String, MonsterMakerInterface> monsterMakers;

    static {
        monsterMakers = new HashMap<>();
        monsterMakers.put("Command knight", MonsterController::makeCommandKnight);
        monsterMakers.put("Yomi Ship", MonsterController::makeYomiShip);
        monsterMakers.put("Suijin", MonsterController::makeSuijin);
    }

    private final GameController gameController;
    private final Monster monster;
    private MonsterPosition position;
    private final boolean canMonsterBeAttacked;
    private final boolean canMonsterBeSummoned;

    private boolean changedPosition = false;

    private MonsterController(GameController gameController, Monster monster, MonsterPosition position) {
        this.gameController = gameController;
        this.monster = new Monster(monster);
        this.position = position;
        this.canMonsterBeAttacked = true;
        this.canMonsterBeSummoned = true;
    }

    public static MonsterController getInstance(GameController gameController, Monster monster, MonsterPosition position)
            throws MonsterNotFoundException {
        for (String monsterName : monsterMakers.keySet()) {
            if (monster.getName().equals(monsterName)) {
                return monsterMakers.get(monsterName).make(gameController, monster, position);
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
                    MonsterController[] monstersZone = gameController.getGame().getOtherBoard().getMonstersZone();
                    int theAttackerPower = 0;

                    for (MonsterController monsterController : monstersZone) {
                        if (monsterController.equals(attacker)) {
                            theAttackerPower = attacker.monster.getAttackPower();
                            attacker.monster.setAttackPower(0);
                        }
                    }

                    //attacked default method

                    for (MonsterController monsterController : monstersZone) {
                        if (monsterController.equals(attacker)) {
                            attacker.monster.setAttackPower(theAttackerPower);
                        }
                    }
                }
            }

        };
    }

    public boolean isChangedPosition() {
        return changedPosition;
    }

    public void setChangedPosition(boolean changedPosition) {
        this.changedPosition = changedPosition;
    }

    public void finishTurn() {
        changedPosition = false;
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

    public void flip() {

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

    public interface MonsterMakerInterface {
        MonsterController make(GameController gameController, Monster monster, MonsterPosition position);
    }
}
