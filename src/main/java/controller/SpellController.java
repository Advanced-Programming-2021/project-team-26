package controller;

import exceptions.InvalidSelection;
import exceptions.SpellNotFoundException;
import model.cards.SpellTrap;
import model.cards.monster.Monster;
import model.cards.spell.Spell;
import view.Print;
import view.Scan;

import java.util.HashMap;
import java.util.Scanner;

public class SpellController extends SpellTrapController {
    private static final HashMap<String, SpellController.SpellMakerInterface> spellMakers = new HashMap<>();

    static {
        spellMakers.put("Monster Reborn", SpellController::makeMonsterReborn);
    }


    private final Spell spell;

    private SpellController(GameController gameController, Spell spell, SpellTrapPosition position) {
        setGameController(gameController);
        setPosition(position);
        setCanSpellTrapsBeActive(true);
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
            @Override
            public void activate() throws InvalidSelection{
                Print.getInstance().printMessage("Select a monster from your or the rival GRAVEYARD" +
                        "1. select from my GRAVEYARD" +
                        "2. select from rival GRAVEYARD");
                Scanner scanner = Scan.getScanner();
                String input = scanner.nextLine();
                int whichGraveyard = Integer.parseInt(input);

                super.setOurGraveyardAccessible(true);
                setRivalGraveyardAccessible(true);
                input = scanner.nextLine();
                super.select(input);

                if(!(super.selectedCard instanceof Monster)){
                    throw new InvalidSelection();
                }else {
                   Monster selectedMonster = (Monster) selectedCard;
                   if (whichGraveyard == 1){
                       gameController.getGame().getThisBoard().getGraveyard().remove(selectedCard);
                   } else {
                       gameController.getGame().getOtherBoard().getGraveyard().remove(selectedCard);
                   }
                   gameController.getGame().getThisBoard().putMonster(selectedMonster, MonsterPosition.ATTACK);
                }
            }
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
