package controller;

import exceptions.InvalidSelection;
import exceptions.SpellNotFoundException;
import model.cards.Card;
import model.cards.SpellTrap;
import model.cards.monster.Monster;
import model.cards.spell.Spell;
import model.cards.spell.SpellType;
import view.Print;
import view.Scan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class SpellController extends SpellTrapController {
    private static final HashMap<String, SpellController.SpellMakerInterface> spellMakers = new HashMap<>();
    public static ArrayList<SpellController> allSpellControllers = new ArrayList<>();

    static {
        spellMakers.put("Monster Reborn", SpellController::makeMonsterReborn);
        spellMakers.put("Terraforming", SpellController::makeTerraforming);
        spellMakers.put("Pot of Greed", SpellController::makePotOfGreed);
        spellMakers.put("Raigeki", SpellController::makeRaigeki);
        spellMakers.put("Change of Heart", SpellController::makeChangeOfHeart);
        spellMakers.put("Harpie’s Feather Duster", SpellController::makeHarpiesFeatherDuster);
    }


    private final Spell spell;

    private SpellController(GameController gameController, Spell spell, SpellTrapPosition position) {
        setGameController(gameController);
        setPosition(position);
        setCanSpellTrapsBeActive(true);
        this.spell = new Spell(spell);
        allSpellControllers.add(this);
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
            public void activate() throws InvalidSelection {
                Print.getInstance().printMessage("Select a monster from your or the rival GRAVEYARD" +
                        "1. select from my GRAVEYARD" +
                        "2. select from rival GRAVEYARD");
                Scanner scanner = Scan.getScanner();
                String input = scanner.nextLine();
                int whichGraveyard = Integer.parseInt(input);

                setOurGraveyardAccessible(true);
                setRivalGraveyardAccessible(true);
                input = scanner.nextLine();
                select(input);

                if (!(super.selectedCard instanceof Monster)) {
                    throw new InvalidSelection();
                } else {
                    Monster selectedMonster = (Monster) selectedCard;
                    if (whichGraveyard == 1) {
                        gameController.getGame().getThisBoard().getGraveyard().remove(selectedCard);
                    } else if (whichGraveyard == 2) {
                        gameController.getGame().getOtherBoard().getGraveyard().remove(selectedCard);
                    }
                    gameController.getGame().getThisBoard().putMonster(selectedMonster, MonsterPosition.ATTACK);
                }
            }
        };
    }


    private static SpellController makeTerraforming(GameController gameController, Spell spell, SpellTrapPosition position) {
        return new SpellController(gameController, spell, position) {
            @Override
            public void activate() throws InvalidSelection {
                List<Card> deck = gameController.getGame().getThisBoard().getDeck();
                ArrayList<Spell> fieldSpells = new ArrayList<>();

                for (Card card : deck) {
                    if (card instanceof Spell) {
                        Spell spell = (Spell) card;
                        if (spell.getType().equals(SpellType.FIELD)) {
                            fieldSpells.add(spell);
                        }
                    }
                }

                Print.getInstance().printMessage(showCards(fieldSpells));
                gameController.getGame().getThisBoard().shuffleDeck();
                Print.getInstance().printMessage("\n");
                Print.getInstance().printMessage("select number of the spell you want:");

                Scanner scanner = Scan.getScanner();
                String input = scanner.nextLine();

                int select = Integer.parseInt(input);
                if (select < 0 || select > fieldSpells.size())
                    throw new InvalidSelection();
                else {
                    for (Card card : deck) {
                        if (card.getName().equals(fieldSpells.get(select - 1).getName())) {
                            deck.remove(card);
                            break;
                        }
                    }
                    gameController.getGame().getThisBoard().addCardToHand(fieldSpells.get(select - 1));
                }
            }

            private String showCards(ArrayList<Spell> fieldSpells) {
                StringBuilder stringToReturn = new StringBuilder();

                int count = 1;
                for (Spell spell : fieldSpells) {
                    stringToReturn.append(count).append(". ").append(spell.getName()).append(":").
                            append(spell.getDescription()).append("\n");
                    count++;
                }
                return stringToReturn.toString();
            }
        };
    }

    public static ArrayList<SpellController> getAllSpellControllers() {
        return allSpellControllers;
    }

    private static SpellController makePotOfGreed(GameController gameController, Spell spell, SpellTrapPosition position) {
        return new SpellController(gameController, spell, position) {
            @Override
            public void activate() {
                gameController.getGame().getThisBoard().addCardToHand();
                gameController.getGame().getThisBoard().addCardToHand();
            }
        };
    }

    private static SpellController makeRaigeki(GameController gameController, Spell spell, SpellTrapPosition position) {
        return new SpellController(gameController, spell, position) {
            @Override
            public void activate() {
                gameController.getGame().getOtherBoard().removeAllMonsters();
            }
        };
    }

    private static SpellController makeChangeOfHeart(GameController gameController, Spell spell, SpellTrapPosition position) {
        return new SpellController(gameController, spell, position) {
            @Override
            public void activate() throws InvalidSelection {
                Print.getInstance().printMessage("select a Monster from rival monsterZone:");

                Scanner scanner = Scan.getScanner();
                String input = scanner.nextLine();
                select(input);

                if (!(selectedCard instanceof Monster)) {
                    throw new InvalidSelection();
                } else {
                    Monster selectedMonster = (Monster) selectedCard;
                    gameController.getGame().getOtherBoard().removeMonsterWithoutAddingToGraveyard(selectedMonster);
                    gameController.getGame().getThisBoard().putMonster(selectedMonster, MonsterPosition.ATTACK);
                }
            }

            @Override
            public void endActivation() {
                Monster selectedMonster = (Monster) selectedCard;
                gameController.getGame().getThisBoard().removeMonsterWithoutAddingToGraveyard(selectedMonster);
                gameController.getGame().getOtherBoard().putMonster(selectedMonster, MonsterPosition.ATTACK);
            }
        };
    }

    private static SpellController makeHarpiesFeatherDuster(GameController gameController, Spell spell, SpellTrapPosition position) {
        return new SpellController(gameController, spell, position) {
            @Override
            public void activate() {
                gameController.getGame().getOtherBoard().removeAllSpellTraps();
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

    public void endActivation() {

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
