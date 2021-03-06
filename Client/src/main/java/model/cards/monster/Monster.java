package model.cards.monster;

import controller.Database;
import model.cards.Card;

import java.util.Map;

public class Monster extends Card {
    private static final Map<String, Monster> allMonsters;

    static {
        allMonsters = Database.getInstance().getAllMonsters();
    }

    private final int level;
    private final MonsterType type;
    private final CardType cardType;
    private final String attribute;
    private int attackPower;
    private int defencePower;

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

    public static Map<String, Monster> getAllMonsters() {
        return allMonsters;
    }

    public static Monster getMonster(String name) {
        if (allMonsters.containsKey(name))
            return allMonsters.get(name);
        return null;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public void increaseAttackPower(int amount) {
        this.attackPower += amount;
    }

    public void decreaseAttackPower(int amount) {
        this.attackPower -= amount;
    }

    public int getDefencePower() {
        return defencePower;
    }

    public void increaseDefencePower(int amount) {
        this.defencePower += amount;
    }

    public void decreaseDefencePower(int amount) {
        this.defencePower -= amount;
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

    public boolean equals(Object o) {
        if (o instanceof Monster)
            return name.equals(((Monster) o).name);
        return false;
    }
}
