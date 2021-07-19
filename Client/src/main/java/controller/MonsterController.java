package controller;

import model.cards.monster.Monster;

public class MonsterController {
    private Monster monster;
    private MonsterPosition position;

    private MonsterController(Monster monster, MonsterPosition position) {
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
