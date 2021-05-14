package controller;

import exceptions.SpellNotFoundException;
import model.cards.SpellTrap;
import model.cards.spell.Spell;

import java.util.HashMap;

public class SpellController extends SpellTrapController {
    private static final HashMap<String, SpellController.SpellMakerInterface> spellMakers = new HashMap<>();

    static {
        spellMakers.put("Monster Reborn", SpellController::makeMonsterReborn);
    }


    private final Spell spell;

    private SpellController(GameController gameController, Spell spell, SpellTrapPosition position) {
        setGameController(gameController);
        setPosition(position);
        this.spell = new Spell(spell);
    }

    public static SpellController getInstance(GameController gameController, Spell spell, SpellTrapPosition position) throws SpellNotFoundException {
        for (String spellName : spellMakers.keySet()) {
            if (spell.getName().equals(spellName)) {
                return spellMakers.get(spellName).make(gameController, spell, position);
            }
        }
        throw new SpellNotFoundException();
    }

    private static SpellController makeMonsterReborn(GameController gameController, Spell spell, SpellTrapPosition position) {
        return new SpellController(gameController, spell, position) {
            //here override methods
        };
    }

    public void field() {

    }

    public boolean canEquip(MonsterController monster) {
        return true;
    }

    public void equip(MonsterController monster) {

    }

    @Override
    public SpellTrap getCard() {
        return this.spell;
    }

    @Override
    public void standBy() {

    }

    public interface SpellMakerInterface {
        SpellController make(GameController gameController, Spell spell, SpellTrapPosition position);
    }
}
