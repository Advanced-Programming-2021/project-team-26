package model.cards.monster;

import controller.exceptions.MonsterNotFoundException;
import model.cards.Card;

public class Monster extends Card {
    private final int attackPower;
    private final int defencePower;
    private final int level;
    private final MonsterType type;
    private final boolean hasEffect;

    //copy constructor
    public Monster(Monster o){
        super(o);
        this.attackPower = o.attackPower;
        this.defencePower = o.defencePower;
        this.level = o.level;
        this.type = o.type;
        this.hasEffect = o.hasEffect;
    }

    public static Monster getMonster(String name) throws MonsterNotFoundException {
        throw new MonsterNotFoundException();
    }
}
