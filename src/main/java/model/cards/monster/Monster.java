package model.cards.monster;

import controller.Database;
import model.cards.Card;

import java.util.Map;

public class Monster extends Card {
    private static final Map<String, Monster> allMonsters;

    static {
        allMonsters = Database.getInstance().getAllMonsters();
    }

    private int attackPower;
    private int defencePower;
    private int level;
    private MonsterType type;
    private CardType cardType;
    private String attribute;

    //copy constructor
    public Monster(Monster o) {
        super(o);
        this.attackPower = o.attackPower;
        this.defencePower = o.defencePower;
        this.level = o.level;
        this.type = o.type;
        this.cardType = o.cardType;
        this.attribute = o.attribute;
    }

    public Monster(String[] fields) {
        super(fields[0], fields[7], Integer.parseInt(fields[8]));
        this.level = Integer.parseInt(fields[1]);
        this.attribute = fields[2];
        this.type = MonsterType.stringToMonsterType(fields[3]);
        this.cardType = CardType.stringToCardType(fields[4]);
        this.attackPower = Integer.parseInt(fields[5]);
        this.defencePower = Integer.parseInt(fields[6]);
    }

    public static Monster getMonster(String name) {
        if (allMonsters.containsKey(name))
            return allMonsters.get(name);
        return null;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void increaseAttackPower(int amount){
        this.attackPower += amount;
    }

    public int getDefencePower() {
        return defencePower;
    }

    public int getLevel() {
        return level;
    }

    public MonsterType getType() {
        return type;
    }

    public CardType getCardType() {
        return cardType;
    }
}
