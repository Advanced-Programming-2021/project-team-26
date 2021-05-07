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
    private final MonsterPosition position;

    private MonsterController(GameController gameController, Monster monster, MonsterPosition position) {
        this.gameController = gameController;
        this.monster = new Monster(monster);
        this.position = position;
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

            @Override
            public void runMonsterEffect() {
                if (position.equals(MonsterPosition.ATTACK)) {
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
                if (position.equals(MonsterPosition.ATTACK)) {
                    return gameController.getGame().getThisBoard().getMonstersZone().length < 2;
                }
                return true;
            }

            @Override
            public void endMonsterEffect() {
                for (MonsterController monsterController : underEffectMonsters) {
                    monsterController.monster.decreaseAttackPower(400);
                }
            }
        };
    }

    private static MonsterController makeYomiShip
            (GameController gameController, Monster monster, MonsterPosition position) {
        return new MonsterController(gameController, monster, position) {
            @Override
            public void remove(MonsterController attacker) {
                MonsterController[] rivalMonsterZone = gameController.getGame().getOtherBoard().getMonstersZone();
                for(MonsterController rivalMonster : rivalMonsterZone){
                    if (rivalMonster.monster.getName().equals(attacker.monster.getName())){
                        rivalMonster = null;
                    }
                }

                MonsterController[] playerMonsterZone = gameController.getGame().getThisBoard().getMonstersZone();
                for(MonsterController playerMonster : playerMonsterZone){
                    if (playerMonster.monster.getName().equals(attacker.monster.getName())){
                        playerMonster = null;
                    }
                }
            }
        };
    }

    private static MonsterController makeSuijin
            (GameController gameController, Monster monster, MonsterPosition position) {
        return new MonsterController(gameController, monster, position) {

        };
    }

    public void runMonsterEffect() {

    }

    public void endMonsterEffect() {

    }

    public MonsterPosition getPosition() {
        return position;
    }

    public boolean canBeAttacked(MonsterController attacker) {
        return true;
    }

    public void attacked(MonsterController attacker) {
    }

    public boolean canBeSummoned() {
        return true;
    }

    public void summon() {

    }

    public void flip() {

    }

    public void specialSummon() {

    }

    public void activate() {

    }

    public void remove(MonsterController attacker) {

    }

    public String getName() {
        return monster.getName();
    }

    public Monster getCard() {
        return this.monster;
    }

    public interface MonsterMakerInterface {
        MonsterController make(GameController gameController, Monster monster, MonsterPosition position);
    }
}
