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

import java.util.*;

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
        spellMakers.put("Twin Twisters", SpellController::makeTwinTwisters);
        spellMakers.put("Mystical space typhoon", SpellController::makeMysticalSpaceTyphoon);
        spellMakers.put("Yami", SpellController::makeYami);
        spellMakers.put("Forest", SpellController::makeForest);
        spellMakers.put("Closed Forest", SpellController::makeClosedForest);
        spellMakers.put("UMIIRUKA", SpellController::makeUMIIRUKA);
        spellMakers.put("Sword of Dark Destruction", SpellController::makeSwordOfDarkDestruction);
        spellMakers.put("Black Pendant", SpellController::makeBlackPendant);
        spellMakers.put("United We Stand", SpellController::makeUnitedWeStand);
        spellMakers.put("Magnum Shield", SpellController::makeMagnumShield);
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
        return new SpellController(gameController, spell, position);
    }

    public static ArrayList<SpellTrapController> getAllSpellControllers() {
        return allSpellTrapControllers;
    }

    private static SpellController makeMonsterReborn(GameController gameController, Spell spell, SpellTrapPosition position) {
        return new SpellController(gameController, spell, position) {
            @Override
            public void activate() throws InvalidSelection {
                Boolean answer = gameController.getViews()[gameController.getGame().getTurn()].ask(
                        "Do you want to select from your GRAVEYARD?");

                if (answer == null)
                    answer = true;

                ArrayList<Card> options = new ArrayList<>();
                for (Card card : gameController.getGame().getThisBoard().getGraveyard()) {
                    if (card instanceof Monster)
                        options.add(card);
                }

                for (Card card : gameController.getGame().getOtherBoard().getGraveyard()) {
                    if (card instanceof Monster)
                        options.add(card);
                }

                ArrayList<Card> selected = gameController.getViews()[gameController.getGame().getTurn()].getCardInput(
                        options,
                        1,
                        "Select a monster from your or the rival GRAVEYARD"
                );

                if (selected.size() != 1)
                    return;
                Monster selectedMonster = (Monster) selected.get(0);
                if (answer) {
                    gameController.getGame().getThisBoard().getGraveyard().remove(selectedCard);
                } else {
                    gameController.getGame().getOtherBoard().getGraveyard().remove(selectedCard);
                }
                gameController.getGame().getThisBoard().putMonster(selectedMonster, MonsterPosition.ATTACK);

            }
        };
    }


    private static SpellController makeTerraforming(GameController gameController, Spell spell, SpellTrapPosition position) {
        return new SpellController(gameController, spell, position) {
            @Override
            public void activate() throws InvalidSelection {
                List<Card> deck = gameController.getGame().getThisBoard().getDeck();
                ArrayList<Card> fieldSpells = new ArrayList<>();

                for (Card card : deck) {
                    if (card instanceof Spell) {
                        Spell spell = (Spell) card;
                        if (spell.getType().equals(SpellType.FIELD) && !fieldSpells.contains(spell)) {
                            fieldSpells.add(spell);
                        }
                    }
                }

                gameController.getGame().getThisBoard().shuffleDeck();

                ArrayList<Card> selected = gameController.getViews()[gameController.getGame().getTurn()].getCardInput(
                        fieldSpells,
                        1,
                        "Select one Field Spell"
                );


                for (Card card : deck) {
                    if (card.getName().equals(selected.get(0).getName())) {
                        deck.remove(card);
                        break;
                    }
                }
                gameController.getGame().getThisBoard().addCardToHand(selected.get(0));

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
            Monster selectedMonster;

            @Override
            public void activate() throws InvalidSelection {
                ArrayList<Card> options = new ArrayList<>();
                for (MonsterController monsterController : gameController.getGame().getOtherBoard().getMonstersZone())
                    options.add(monsterController.getMonster());

                ArrayList<Card> selected = gameController.getViews()[gameController.getGame().getTurn()].getCardInput(
                        options,
                        1,
                        "select a Monster from rival monsterZone"
                );

                if (selected.size() == 1) {
                    selectedMonster = (Monster) selected.get(0);
                    gameController.getGame().getOtherBoard().removeMonsterWithoutAddingToGraveyard(selectedMonster);
                    gameController.getGame().getThisBoard().putMonster(selectedMonster, MonsterPosition.ATTACK);
                }

            }

            @Override
            public void endActivation() {
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

    private static SpellController makeTwinTwisters(GameController gameController, Spell spell, SpellTrapPosition position) {
        return new SpellController(gameController, spell, position) {
            @Override
            public void activate() throws InvalidSelection {

                ArrayList<Card> options = new ArrayList<>();
                for (Card card : gameController.getGame().getThisBoard().getHand())
                    options.add(card);

                ArrayList<Card> selected = gameController.getViews()[gameController.getGame().getTurn()].getCardInput(
                        options,
                        1,
                        "Select a card from yor HAND to remove"
                );

                Card theSelectedCardFromHand = selected.get(0);

                Boolean answer = gameController.getViews()[gameController.getGame().getTurn()].ask("Do you want to destroy one SpellTrap or two ones?(yes if one,otherwise no)");
                if (answer == null)
                    answer = true;

                setHandAccessible(false);
                setRivalSpellTrapAccessible(true);

                if (answer) {
                    options.clear();
                    for (SpellTrapController controller : gameController.getGame().getOtherBoard().getSpellTrapZone())
                        options.add(controller.getCard());

                    selected = gameController.getViews()[gameController.getGame().getTurn()].getCardInput(
                            options,
                            1,
                            "Select a spellTrap from rival spellTrapZone."
                    );
                    if (selected.size() == 1)
                        removeSpell(selected.get(0));
                } else {
                    options.clear();
                    for (SpellTrapController controller : gameController.getGame().getOtherBoard().getSpellTrapZone())
                        options.add(controller.getCard());

                    selected = gameController.getViews()[gameController.getGame().getTurn()].getCardInput(
                            options,
                            2,
                            "Select two spellTraps from rival spellTrapZone."
                    );
                    for (Card card : selected)
                        removeSpell(card);

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
                ArrayList<Card> options = new ArrayList<>();
                for (SpellTrapController controller : gameController.getGame().getOtherBoard().getSpellTrapZone())
                    options.add(controller.getCard());

                ArrayList<Card> selected = gameController.getViews()[gameController.getGame().getTurn()].getCardInput(
                        options,
                        1,
                        "Select a spellTrap from game to remove"
                );

                if (selected.size() != 1)
                    return;

                gameController.getGame().getOtherBoard().
                        removeSpellTrap(getSpellTrapControllerBySpellTrap((SpellTrap) selected.get(0)));
            }
        };
    }

    private static SpellController makeYami(GameController gameController, Spell spell, SpellTrapPosition position) {
        return new SpellController(gameController, spell, position) {

            private final Set<MonsterController> underEffectMonsters = new HashSet<>();

            @Override
            public void runFieldEffectAtSummon() {
                Collection<MonsterController> monstersZone = gameController.getGame().getThisBoard().getMonstersZone();
                for (MonsterController monsterController : monstersZone) {
                    if (!underEffectMonsters.contains(monsterController)) {
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

                monstersZone = gameController.getGame().getOtherBoard().getMonstersZone();
                for (MonsterController monsterController : monstersZone) {
                    if (!underEffectMonsters.contains(monsterController)) {
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
            }
        };

    }

    private static SpellController makeForest(GameController gameController, Spell spell, SpellTrapPosition position) {
        return new SpellController(gameController, spell, position) {

            private final Set<MonsterController> underEffectMonsters = new HashSet<>();

            @Override
            public void runFieldEffectAtSummon() {
                Collection<MonsterController> monstersZone = gameController.getGame().getThisBoard().getMonstersZone();
                for (MonsterController monsterController : monstersZone) {
                    if (!underEffectMonsters.contains(monsterController)) {
                        if (monsterController.getMonster().getType() == MonsterType.INSECT ||
                                monsterController.getMonster().getType() == MonsterType.BEAST ||
                                monsterController.getMonster().getType() == MonsterType.BEAST_WARRIOR) {
                            monsterController.getMonster().increaseAttackPower(200);
                            monsterController.getMonster().increaseDefencePower(200);
                        }
                    }
                }

                monstersZone = gameController.getGame().getOtherBoard().getMonstersZone();
                for (MonsterController monsterController : monstersZone) {
                    if (!underEffectMonsters.contains(monsterController)) {
                        if (monsterController.getMonster().getType() == MonsterType.INSECT ||
                                monsterController.getMonster().getType() == MonsterType.BEAST ||
                                monsterController.getMonster().getType() == MonsterType.BEAST_WARRIOR) {
                            monsterController.getMonster().increaseAttackPower(200);
                            monsterController.getMonster().increaseDefencePower(200);
                        }
                    }
                }
            }
        };
    }

    private static SpellController makeClosedForest(GameController gameController, Spell spell, SpellTrapPosition position) {
        return new SpellController(gameController, spell, position) {

            private final Set<MonsterController> underEffectMonsters = new HashSet<>();

            @Override
            public void runFieldEffectAtSummon() {
                int monstersINGraveyard = gameController.getGame().getThisBoard().getNumberOfMonstersINGraveyard();

                Collection<MonsterController> monstersZone = gameController.getGame().getThisBoard().getMonstersZone();
                for (MonsterController monsterController : monstersZone) {
                    if (!underEffectMonsters.contains(monsterController)) {
                        if (monsterController.getMonster().getType() == MonsterType.BEAST_WARRIOR ||
                                monsterController.getMonster().getType() == MonsterType.BEAST) {
                            monsterController.getMonster().increaseAttackPower(100 * monstersINGraveyard);
                            monsterController.getMonster().increaseDefencePower(200 * monstersINGraveyard);
                        }
                    }
                }

                monstersZone = gameController.getGame().getOtherBoard().getMonstersZone();
                for (MonsterController monsterController : monstersZone) {
                    if (!underEffectMonsters.contains(monsterController)) {
                        if (monsterController.getMonster().getType() == MonsterType.BEAST_WARRIOR ||
                                monsterController.getMonster().getType() == MonsterType.BEAST) {
                            monsterController.getMonster().increaseAttackPower(100 * monstersINGraveyard);
                            monsterController.getMonster().increaseDefencePower(200 * monstersINGraveyard);
                        }
                    }
                }
            }
        };

    }

    private static SpellController makeUMIIRUKA(GameController gameController, Spell spell, SpellTrapPosition position) {
        return new SpellController(gameController, spell, position) {

            private final Set<MonsterController> underEffectMonsters = new HashSet<>();

            @Override
            public void runFieldEffectAtSummon() {
                Collection<MonsterController> monstersZone = gameController.getGame().getThisBoard().getMonstersZone();
                for (MonsterController monsterController : monstersZone) {
                    if (!underEffectMonsters.contains(monsterController)) {
                        if (monsterController.getMonster().getType() == MonsterType.AQUA) {
                            monsterController.getMonster().increaseAttackPower(500);
                            monsterController.getMonster().decreaseDefencePower(400);
                        }
                    }
                }

                monstersZone = gameController.getGame().getOtherBoard().getMonstersZone();
                for (MonsterController monsterController : monstersZone) {
                    if (!underEffectMonsters.contains(monsterController)) {
                        if (monsterController.getMonster().getType() == MonsterType.AQUA) {
                            monsterController.getMonster().increaseAttackPower(500);
                            monsterController.getMonster().decreaseDefencePower(400);
                        }
                    }
                }
            }
        };
    }


    private static SpellController makeSwordOfDarkDestruction(GameController gameController, Spell spell, SpellTrapPosition position) {
        return new SpellController(gameController, spell, position) {
            @Override
            public void activate(MonsterController equippedMonster) {
                if (equippedMonster.getMonster().getType() == MonsterType.FIEND ||
                        equippedMonster.getMonster().getType() == MonsterType.SPELL_CASTER) {
                    equippedMonster.getMonster().increaseAttackPower(400);
                    equippedMonster.getMonster().decreaseDefencePower(200);
                }
            }
        };
    }

    private static SpellController makeBlackPendant(GameController gameController, Spell spell, SpellTrapPosition position) {
        return new SpellController(gameController, spell, position) {
            @Override
            public void activate(MonsterController equippedMonster) {
                equippedMonster.getMonster().increaseAttackPower(500);
            }
        };
    }

    private static SpellController makeUnitedWeStand(GameController gameController, Spell spell, SpellTrapPosition position) {
        return new SpellController(gameController, spell, position) {
            @Override
            public void activate(MonsterController equippedMonster) {
                if (equippedMonster.getMonsterAddress().getOwner() == Owner.Me &&
                        equippedMonster.getPosition() == MonsterPosition.ATTACK) {
                    equippedMonster.getMonster().increaseAttackPower(800);
                    equippedMonster.getMonster().increaseDefencePower(800);
                }
            }
        };
    }

    private static SpellController makeMagnumShield(GameController gameController, Spell spell, SpellTrapPosition position) {
        return new SpellController(gameController, spell, position) {
            @Override
            public void activate(MonsterController equippedMonster) {
                if (equippedMonster.getPosition() == MonsterPosition.ATTACK) {
                    int defencePower = equippedMonster.getMonster().getDefencePower();
                    equippedMonster.getMonster().increaseAttackPower(defencePower);
                } else if (equippedMonster.getPosition() == MonsterPosition.DEFENCE_DOWN ||
                        equippedMonster.getPosition() == MonsterPosition.DEFENCE_UP) {
                    int attackPower = equippedMonster.getMonster().getAttackPower();
                    equippedMonster.getMonster().increaseDefencePower(attackPower);
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

    public void activate(MonsterController equippedMonster) {

    }

    @Override
    public SpellTrap getCard() {
        return this.spell;
    }

    @Override
    public void standBy() {

    }

    public void runFieldEffectAtSummon() {

    }

    public interface SpellMakerInterface {
        SpellController make(GameController gameController, Spell spell, SpellTrapPosition position);
    }
}
