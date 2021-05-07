package controller;

import exceptions.MonsterNotFoundException;
import model.cards.monster.Monster;

import java.util.HashMap;


public class MonsterController {
    private static final HashMap<String, MonsterMakerInterface> monsterMakers;

    static {
        monsterMakers = new HashMap<>();
        monsterMakers.put("Command knight", MonsterController::makeCommandKnight);
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
            public void runMonsterEffect() {
                if (position.equals(MonsterPosition.ATTACK)) {

                    //increase other monsters attackPower for 400
                    MonsterController[] monstersZone = gameController.getGame().getThisBoard().getMonstersZone();
                    for (MonsterController monsterController : monstersZone) {
                        monsterController.monster.increaseAttackPower(400);
                    }
                }
            }

            //cant be attacked while there are some other monsters in the field
            @Override
            public boolean canBeAttacked() {
                if (position.equals(MonsterPosition.ATTACK)) {
                    return gameController.getGame().getThisBoard().getMonstersZone().length < 2;
                }
                return true;
            }
        };
    }

    public void runMonsterEffect() {

    }

    public MonsterPosition getPosition() {
        return position;
    }

    public boolean canBeAttacked() {
        return true;
    }

    public void attacked(MonsterController monster) {

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

    public void remove() {

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
