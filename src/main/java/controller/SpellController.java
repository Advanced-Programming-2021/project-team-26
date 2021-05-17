package controller;

import exceptions.InvalidSelection;
import exceptions.SpellNotFoundException;
import model.Owner;
import model.cards.Card;
import model.cards.SpellTrap;
import model.cards.monster.Monster;
import model.cards.monster.MonsterType;
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

    static {
        spellMakers.put("Monster Reborn", SpellController::makeMonsterReborn);
        spellMakers.put("Terraforming", SpellController::makeTerraforming);
        spellMakers.put("Pot of Greed", SpellController::makePotOfGreed);
        spellMakers.put("Raigeki", SpellController::makeRaigeki);
        spellMakers.put("Change of Heart", SpellController::makeChangeOfHeart);
        spellMakers.put("Harpie’s Feather Duster", SpellController::makeHarpiesFeatherDuster);
        spellMakers.put("Dark Hole", SpellController::makeDarkHole);
        spellMakers.put("Supply Squad", SpellController::makeSupplySquad);
        spellMakers.put("Twin Twisters", SpellController::makeTwinTwisters);
        spellMakers.put("Mystical space typhoon", SpellController::makeMysticalSpaceTyphoon);
        spellMakers.put("Yami", SpellController::makeYami);
        spellMakers.put("Forest", SpellController::makeForest);
        spellMakers.put("Closed Forest", SpellController::makeClosedForest);
    }


    private final Spell spell;

    private SpellController(GameController gameController, Spell spell, SpellTrapPosition position) {
        setGameController(gameController);
        setPosition(position);
        setCanSpellTrapsBeActive(true);
        this.spell = new Spell(spell);
        allSpellTrapControllers.add(this);
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

    public static ArrayList<SpellTrapController> getAllSpellControllers() {
        return allSpellTrapControllers;
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
                setRivalMonsterZoneAccessible(true);

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

    private static SpellController makeDarkHole(GameController gameController, Spell spell, SpellTrapPosition position) {
        return new SpellController(gameController, spell, position) {
            @Override
            public void activate() {
                gameController.getGame().getOtherBoard().removeAllMonsters();
                gameController.getGame().getThisBoard().removeAllMonsters();
            }
        };
    }

    private static SpellController makeSupplySquad(GameController gameController, Spell spell, SpellTrapPosition position) {
        return new SpellController(gameController, spell, position) {

        };
    }

    private static SpellController makeTwinTwisters(GameController gameController, Spell spell, SpellTrapPosition position) {
        return new SpellController(gameController, spell, position) {
            @Override
            public void activate() throws InvalidSelection {
                Print.getInstance().printMessage("Select a card from yor HAND to remove");

                setHandAccessible(true);
                Scanner scanner = Scan.getScanner();
                String input = scanner.nextLine();
                select(input);
                Card theSelectedCardFromHand = selectedCard;

                Print.getInstance().printMessage("Do you want to destroy one SpellTrap or two ones?" +
                        "1. one" +
                        "2. two");
                input = scanner.nextLine();

                setHandAccessible(false);
                setRivalSpellTrapAccessible(true);

                if (Integer.parseInt(input) == 1) {
                    Print.getInstance().printMessage("Select a spellTrap from rival spellTrapZone.");
                    input = scanner.nextLine();
                    select(input);
                    removeSpell(selectedCard);
                } else if (Integer.parseInt(input) == 2) {
                    Print.getInstance().printMessage("Select two spellTraps from rival spellTrapZone.");
                    input = scanner.nextLine();
                    select(input);
                    removeSpell(selectedCard);

                    input = scanner.nextLine();
                    select(input);
                    removeSpell(selectedCard);
                }
            }

            private void removeSpell(Card selectedCard) throws InvalidSelection {
                if (!(selectedCard instanceof SpellTrap)) {
                    throw new InvalidSelection();
                } else {
                    gameController.getGame().getOtherBoard().
                            removeSpellTrap(getSpellTrapControllerBySpellTrap((SpellTrap) selectedCard));
                }
            }
        };
    }

    private static SpellController makeMysticalSpaceTyphoon(GameController gameController, Spell spell, SpellTrapPosition position) {
        return new SpellController(gameController, spell, position) {
            @Override
            public void activate() {
                Print.getInstance().printMessage("Select a spellTrap from field to remove");
                setRivalSpellTrapAccessible(true);
                setOurSpellTrapAccessible(true);

                Scanner scanner = Scan.getScanner();
                String input = scanner.nextLine();
                select(input);

                if (selectedCardAddress.getOwner() == Owner.Me) {
                    gameController.getGame().getThisBoard().
                            removeSpellTrap(getSpellTrapControllerBySpellTrap((SpellTrap) selectedCard));
                } else {
                    gameController.getGame().getOtherBoard().
                            removeSpellTrap(getSpellTrapControllerBySpellTrap((SpellTrap) selectedCard));
                }
            }
        };
    }

    private static SpellController makeYami(GameController gameController, Spell spell, SpellTrapPosition position) {
        return new SpellController(gameController, spell, position) {
            @Override
            public void activate() {
                setAttackAndDefenses(gameController.getGame().getThisBoard().getMonstersZone());
                setAttackAndDefenses(gameController.getGame().getOtherBoard().getMonstersZone());
            }

            private void setAttackAndDefenses(MonsterController[] monsterZone) {
                for (MonsterController monsterController : monsterZone) {
                    if (monsterController.getMonster().getType() == MonsterType.SPELL_CASTER ||
                            monsterController.getMonster().getType() == MonsterType.FIEND) {
                        monsterController.getMonster().increaseAttackPower(200);
                        monsterController.getMonster().increaseDefencePower(200);
                    } else if (monsterController.getMonster().getType() == MonsterType.FAIRY) {
                        monsterController.getMonster().decreaseDefencePower(200);
                        monsterController.getMonster().decreaseDefencePower(200);
                    }
                }
            }
        };

    }

    private static SpellController makeForest(GameController gameController, Spell spell, SpellTrapPosition position) {
        return new SpellController(gameController, spell, position) {
            @Override
            public void activate() {
                setAttackAndDefenses(gameController.getGame().getThisBoard().getMonstersZone());
                setAttackAndDefenses(gameController.getGame().getOtherBoard().getMonstersZone());
            }

            private void setAttackAndDefenses(MonsterController[] monsterZone) {
                for (MonsterController monsterController : monsterZone) {
                    if (monsterController.getMonster().getType() == MonsterType.INSECT ||
                            monsterController.getMonster().getType() == MonsterType.BEAST ||
                            monsterController.getMonster().getType() == MonsterType.BEAST_WARRIOR) {
                        monsterController.getMonster().increaseAttackPower(200);
                        monsterController.getMonster().increaseDefencePower(200);
                    }
                }
            }
        };

    }

    private static SpellController makeClosedForest(GameController gameController, Spell spell, SpellTrapPosition position) {
        return new SpellController(gameController, spell, position) {
            @Override
            public void activate() {
                setAttackAndDefenses(gameController.getGame().getThisBoard().getMonstersZone());
            }

            private void setAttackAndDefenses(MonsterController[] monsterZone) {
                int monstersINGraveyard = gameController.getGame().getThisBoard().geNumberOfMonstersINGraveyard();
                for (MonsterController monsterController : monsterZone) {
                    if (monsterController.getMonster().getType() == MonsterType.BEAST_WARRIOR||
                            monsterController.getMonster().getType() == MonsterType.BEAST) {
                        monsterController.getMonster().increaseAttackPower(100 * monstersINGraveyard);
                        monsterController.getMonster().increaseDefencePower(200 * monstersINGraveyard);
                    }
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
