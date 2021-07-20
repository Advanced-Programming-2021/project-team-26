package model;

import controller.MonsterPosition;
import model.cards.monster.Monster;

public class MonsterTransfer {
    private Monster monster;
    private MonsterPosition position;

    public MonsterTransfer(Monster monster, MonsterPosition position) {
        setMonster(new Monster(monster));
        setPosition(position);
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public MonsterPosition getPosition() {
        return position;
    }

    public void setPosition(MonsterPosition position) {
        this.position = position;
    }
}
