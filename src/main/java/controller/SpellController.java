package controller;

import controller.exceptions.SpellNotFoundException;
import model.cards.spell.Spell;

import java.util.HashMap;

enum SpellPosition {
    UP,
    DOWN
}

public class SpellController {
    private static HashMap<String, SpellController.SpellMakerInterface> spellMakers = new HashMap<>();

    static {
        spellMakers.put("Monster Reborn", SpellController::makeMonsterReborn);
    }

    private final GameController gameController;
    private final Spell spell;
    private final SpellPosition position;

    private SpellController(GameController gameController, Spell spell, SpellPosition position) {
        this.gameController = gameController;
        this.spell = new Spell(spell);
        this.position = position;
    }

    public static SpellController getInstance(GameController gameController, Spell spell, SpellPosition position) throws SpellNotFoundException {
        for (String spellName : spellMakers.keySet()) {
            if (spell.getName().equals(spellName)) {
                return spellMakers.get(spellName).make(gameController, spell, position);
            }
        }
        throw new SpellNotFoundException();
    }

    private static SpellController makeMonsterReborn(GameController gameController, Spell spell, SpellPosition position) {
        return new SpellController(gameController, spell, position) {
            //here override methods
        };
    }

    public void activate() {

    }

    public void field() {

    }

    public boolean canEquip(MonsterController monster) {
        return true;
    }

    public void equip(MonsterController monster) {

    }

    public void remove() {

    }

    public interface SpellMakerInterface {
        SpellController make(GameController gameController, Spell spell, SpellPosition position);
    }
}
