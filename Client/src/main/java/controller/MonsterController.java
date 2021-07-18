package controller;

import Utilities.Alert;
import exceptions.CardNotFoundException;
import exceptions.InvalidInput;
import exceptions.InvalidSelection;
import exceptions.NoCardSelectedException;
import model.*;
import model.cards.Card;
import model.cards.monster.Monster;
import view.Print;
import view.Scan;

import java.util.*;

public class MonsterController {
    private static final HashMap<String, MonsterMakerInterface> MONSTER_MAKERS;
    private static final ArrayList<MonsterController> allMonsterControllers;

    static {
        MONSTER_MAKERS = new HashMap<>();
        allMonsterControllers = new ArrayList<>();

        //Effective Monsters
        MONSTER_MAKERS.put("Command knight", MonsterController::makeCommandKnight);
        MONSTER_MAKERS.put("Yomi Ship", MonsterController::makeYomiShip);
        MONSTER_MAKERS.put("Suijin", MonsterController::makeSuijin);
        MONSTER_MAKERS.put("Man-Eater Bug", MonsterController::makeManEaterBug);
        MONSTER_MAKERS.put("Scanner", MonsterController::makeScanner);
        MONSTER_MAKERS.put("Marshmallon", MonsterController::makeMarshmallon);
        MONSTER_MAKERS.put("Beast King Barbaros", MonsterController::makeBeastKingBarbaros);
        MONSTER_MAKERS.put("The Calculator", MonsterController::makeTheCalculator);
        MONSTER_MAKERS.put("Mirage Dragon", MonsterController::makeMirageDragon);
        MONSTER_MAKERS.put("Hearld of Creation", MonsterController::makeHearldOfCreation);
        MONSTER_MAKERS.put("Exploder Dragon", MonsterController::makeExploderDragon);
        MONSTER_MAKERS.put("The Tricky", MonsterController::makeTheTricky);
    }

    private final GameController gameController;
    protected Board thisBoard;
    protected Board otherBoard;
    private Monster monster;
    private MonsterPosition position;
    private CardAddress monsterAddress;
    private boolean hasPositionChanged;
    private boolean isMonsterNew;
    private boolean hasAttackedThisTurn;
    private boolean hasActivateEffectThisTurn;
    private boolean isHandAccessible;
    private boolean isRivalMonsterZoneAccessible;
    private boolean isOurMonsterZoneAccessible;
    private boolean isRivalGraveyardAccessible;
    private boolean isOurGraveyardAccessible;
    private Card selectedCard;
    private CardAddress selectedCardAddress;


    private MonsterController(GameController gameController, Monster monster,
                              MonsterPosition position, CardAddress monsterAddress) {
        this.gameController = gameController;
        this.monster = new Monster(monster);
        this.thisBoard = gameController.getGame().getThisBoard();
        this.otherBoard = gameController.getGame().getOtherBoard();
        setMonsterAddress(monsterAddress);
        setPosition(position);
        setMonsterNew(true);
        setHasPositionChanged(true);
        setHasAttackedThisTurn(false);
        setHasActivateEffectThisTurn(false);
        setHandAccessible(false);
        setRivalGraveyardAccessible(false);
        setRivalMonsterZoneAccessible(false);
        setOurGraveyardAccessible(false);
        setOurMonsterZoneAccessible(false);
        setSelectedCard(null);
        setSelectedCardAddress(null);
        allMonsterControllers.add(this);
    }

    public static ArrayList<MonsterController> getAllMonsterControllers() {
        return allMonsterControllers;
    }

    public static MonsterController getMonsterControllerByMonster(Monster monster) {
        for (MonsterController monsterController : allMonsterControllers) {
            if (monsterController.monster == monster) {
                return monsterController;
            }
        }
        return null;
    }

    public static MonsterController getInstance(GameController gameController, Monster monster,
                                                MonsterPosition position, CardAddress monsterAddress) {
        for (String monsterName : MONSTER_MAKERS.keySet()) {
            if (monster.getName().equals(monsterName)) {
                return MONSTER_MAKERS.get(monsterName).make(gameController, monster, position, monsterAddress);
            }
        }
        return new MonsterController(gameController, monster, position, monsterAddress);
    }

    private static MonsterController makeCommandKnight(GameController gameController, Monster monster,
                                                       MonsterPosition position, CardAddress monsterAddress) {
        return new MonsterController(gameController, monster, position, monsterAddress) {
            private final Set<MonsterController> underEffectMonsters = new HashSet<>();
            private boolean isEffectActive = position == MonsterPosition.ATTACK;

            @Override
            public void runMonsterEffectAtSummon() {
                if (isEffectActive) {
                    //increase other monsters attackPower for 400
                    Collection<MonsterController> monstersZone = gameController.getGame().getThisBoard().getMonstersZone();
                    for (MonsterController monsterController : monstersZone) {
                        if (!underEffectMonsters.contains(monsterController)) {
                            monsterController.monster.increaseAttackPower(400);
                            underEffectMonsters.add(monsterController);
                        }
                    }
                }
            }

            //cant be attacked while there are some other monsters in the field
            @Override
            public boolean canBeAttacked(MonsterController attacker) {
                if (isEffectActive) {
                    return gameController.getGame().getOtherBoard().getMonsterZoneNumber() < 2;
                }
                return true;
            }

            @Override
            public void endMonsterEffect() {
                for (MonsterController monsterController : underEffectMonsters) {
                    monsterController.monster.decreaseAttackPower(400);
                }
                isEffectActive = false;
            }
        };
    }

    private static MonsterController makeYomiShip
            (GameController gameController, Monster monster,
             MonsterPosition position, CardAddress monsterAddress) {
        return new MonsterController(gameController, monster, position, monsterAddress) {
            @Override
            public void remove(MonsterController attacker) {
                this.thisBoard.removeMonster(this);
                this.otherBoard.removeMonster(attacker);
                Alert.getInstance().successfulPrint("You and your opponent cards are destroyed according to Yomi Ship's effect");
            }
        };
    }

    private static MonsterController makeSuijin
            (GameController gameController, Monster monster,
             MonsterPosition position, CardAddress monsterAddress) {
        return new MonsterController(gameController, monster, position, monsterAddress) {
            private final boolean wasEffectActiveBefore = false;
            private boolean isEffectActive = false;

            @Override
            public void runMonsterEffect() {
                if (!wasEffectActiveBefore) {
                    isEffectActive = true;
                }
            }

            @Override
            public AttackResult attack(MonsterController attacker) {
                if (isEffectActive) {
                    int theAttackerPower = attacker.monster.getAttackPower();
                    attacker.monster.setAttackPower(0);
                    AttackResult toReturn = new AttackResult(attacker, this);
                    attacker.monster.setAttackPower(theAttackerPower);
                    isEffectActive = false;
                    return toReturn;
                }
                return null;
            }

        };
    }

    private static MonsterController makeManEaterBug
            (GameController gameController, Monster monster,
             MonsterPosition position, CardAddress monsterAddress) {
        return new MonsterController(gameController, monster, position, monsterAddress) {
            @Override
            public void flip() {
                setRivalMonsterZoneAccessible(true);
                Boolean answer = gameController.getViews()[gameController.getGame().getTurn()].ask(
                        "Do you want to activate the card effect?");
                Print.getInstance().printMessage("Do you want to activate the card effect?" +
                        "1. yes" +
                        "2. no");
                if (answer == null)
                    answer = false;
                if (answer) {
                    ArrayList<Card> options = new ArrayList<>();
                    for (MonsterController monsterController : gameController.getGame().getOtherBoard().getMonstersZone())
                        options.add(monsterController.getMonster());
                    Print.getInstance().printMessage("Select one of rival Monsters to remove from his Monster Zone");

                    ArrayList<Card> selected = gameController.getViews()[gameController.getGame().getTurn()].getCardInput(
                            options,
                            1,
                            "Select one of rival Monsters to remove from his Monster Zone"
                    );

                    if (selected.size() != 1)
                        return;

                    Collection<MonsterController> monstersZone = gameController.getGame().getOtherBoard().getMonstersZone();
                    for (MonsterController monster : monstersZone) {
                        if (monster.getMonster() == selected.get(1)) {
                            MonsterController selectedMonsterController = MonsterController.getMonsterControllerByMonster((Monster) getSelectedCard());
                            gameController.getGame().getOtherBoard().removeMonster(selectedMonsterController);
                            return;
                        }
                    }
                }

                this.setPosition(MonsterPosition.ATTACK);
                this.setHasPositionChanged(true);
            }
        };
    }

    private static MonsterController makeScanner
            (GameController gameController, Monster monster,
             MonsterPosition position, CardAddress monsterAddress) {
        return new MonsterController(gameController, monster, position, monsterAddress) {
            @Override
            public void runMonsterEffectOnEachTurn() throws InvalidSelection {
                if (!isHasActivateEffectThisTurn()) {
                    Print.getInstance().printMessage("You have Scanner in your Monster" +
                            " Zone and you should select a card to clone it");

                    Print.getInstance().printMessage("Select a monster from rival GRAVEYARD");
                    ArrayList<Card> options = new ArrayList<>();
                    for (Card card : gameController.getGame().getOtherBoard().getGraveyard()) {
                        if (card instanceof Monster)
                            options.add(card);
                    }

                    ArrayList<Card> selected = gameController.getViews()[gameController.getGame().getTurn()].getCardInput(
                            options, 1, "Select a monster from rival GRAVEYARD"
                    );

                    if (selected.size() != 1) {
                        throw new InvalidSelection();
                    } else {
                        Monster selectedMonster = (Monster) selected.get(0);
                        setMonster(new Monster(selectedMonster));
                    }
                }
                setHasActivateEffectThisTurn(true);
            }

            @Override
            public void endMonsterEffect() {
                setMonster(Monster.getMonster("Scanner"));
            }
        };
    }

    private static MonsterController makeMarshmallon
            (GameController gameController, Monster monster,
             MonsterPosition position, CardAddress monsterAddress) {
        return new MonsterController(gameController, monster, position, monsterAddress) {
            @Override
            public void remove(MonsterController attacker) {
                if (position.equals(MonsterPosition.DEFENCE_DOWN)) {
                    gameController.getGame().decreaseOtherLifePoint(1000);
                }
            }
        };
    }

    private static MonsterController makeBeastKingBarbaros
            (GameController gameController, Monster monster,
             MonsterPosition position, CardAddress monsterAddress) {
        return new MonsterController(gameController, monster, position, monsterAddress) {

        };
    }

    private static MonsterController makeTheCalculator
            (GameController gameController, Monster monster,
             MonsterPosition position, CardAddress monsterAddress) {
        return new MonsterController(gameController, monster, position, monsterAddress) {
            {
                Collection<MonsterController> monstersZone = gameController.getGame().getThisBoard().getMonstersZone();
                for (MonsterController monsterController : monstersZone) {
                    if (monsterController.position.equals(MonsterPosition.ATTACK)) {
                        monster.increaseAttackPower(monsterController.monster.getLevel() * 300);
                    }
                }
            }
        };
    }

    private static MonsterController makeMirageDragon
            (GameController gameController, Monster monster,
             MonsterPosition position, CardAddress monsterAddress) {
        return new MonsterController(gameController, monster, position, monsterAddress) {
            final boolean isEffectActive = position.equals(MonsterPosition.ATTACK);

            @Override
            public void runMonsterEffect() {
                if (isEffectActive) {
                    //rival cannot active his spellTraps
                }
            }
        };
    }

    private static MonsterController makeHearldOfCreation
            (GameController gameController, Monster monster,
             MonsterPosition position, CardAddress monsterAddress) {
        return new MonsterController(gameController, monster, position, monsterAddress) {
            @Override
            public void runMonsterEffectOnEachTurn() throws InvalidSelection {
                if (!isHasActivateEffectThisTurn()) {
                    Print.getInstance().printMessage("Do you want to activate the card effect?\n" +
                            "1. yes\n" +
                            "2. no");

                    Boolean answer = gameController.getViews()[gameController.getGame().getTurn()].ask("Do you want to activate the card effect?");

                    if (answer != null && answer) {
                        Print.getInstance().printMessage("Select a Card from your HAND to remove");
                        ArrayList<Card> options = new ArrayList<>();
                        for (Card card : gameController.getGame().getThisBoard().getHand()) {
                            options.add(card);
                        }

                        ArrayList<Card> selected = gameController.getViews()[gameController.getGame().getTurn()].getCardInput(
                                options,
                                1,
                                "Select a Card from your HAND to remove"
                        );

                        if (selected.size() == 1)
                            gameController.getGame().getThisBoard().getHand().remove(selected.get(0));

                        setHandAccessible(false);
                        setOurGraveyardAccessible(true);

                        Print.getInstance().printMessage("Select a Monster from your GRAVEYARD with level 7 or more to put it in your HAND");

                        options.clear();
                        for (Card card : gameController.getGame().getThisBoard().getGraveyard()) {
                            if (card instanceof Monster && ((Monster) card).getLevel() >= 7)
                                options.add(card);
                        }

                        selected = gameController.getViews()[gameController.getGame().getTurn()].getCardInput(
                                options,
                                1,
                                "Select a Monster from your GRAVEYARD with level 7 or more to put it in your HAND"
                        );

                        if (selected.size() == 1) {
                            gameController.getGame().getThisBoard().getGraveyard().remove(selected.get(0));
                            gameController.getGame().getThisBoard().addCardToHand(selected.get(0));
                        }

                        setOurGraveyardAccessible(false);
                        setHasActivateEffectThisTurn(true);
                    }
                }
            }
        };
    }

    private static MonsterController makeExploderDragon
            (GameController gameController, Monster monster,
             MonsterPosition position, CardAddress monsterAddress) {
        return new MonsterController(gameController, monster, position, monsterAddress) {
            @Override
            public AttackResult attack(MonsterController attacker) {
                AttackResult result = new AttackResult(attacker, this);

                if (result.isRemoveMyCard()) {
                    result.setMyLPDecrease(0);
                    result.setOpLPDecrease(0);
                    result.setRemoveOpCard(true);
                }

                return result;
            }
        };
    }

    private static MonsterController makeTheTricky
            (GameController gameController, Monster monster,
             MonsterPosition position, CardAddress monsterAddress) {
        return new MonsterController(gameController, monster, position, monsterAddress) {
            @Override
            public void summon() {
                Boolean ask = gameController.getViews()[gameController.getGame().getTurn()]
                        .ask("Do you want to summon this card specially with remove a card from your HAND?");

                if (ask == null)
                    throw new InvalidSelection();

                if (ask) {
                    Print.getInstance().printMessage("Select a card from your HAND to remove");
                    ArrayList<Card> options = new ArrayList<>();
                    options.addAll(gameController.getGame().getThisBoard().getHand());

                    ArrayList<Card> selects = gameController.getViews()[gameController.getGame().getTurn()]
                            .getCardInput(options, 1, "Select a card from your HAND to remove");

                    gameController.getGame().getThisBoard().getHand().remove(selects.get(0));
                }
            }
        };
    }

    public CardAddress getMonsterAddress() {
        return monsterAddress;
    }

    public void setMonsterAddress(CardAddress monsterAddress) {
        this.monsterAddress = monsterAddress;
    }

    public boolean isHasActivateEffectThisTurn() {
        return hasActivateEffectThisTurn;
    }

    public void setHasActivateEffectThisTurn(boolean hasActivateEffectThisTurn) {
        this.hasActivateEffectThisTurn = hasActivateEffectThisTurn;
    }

    public void setSelectedCardAddress(CardAddress selectedCardAddress) {
        this.selectedCardAddress = selectedCardAddress;
    }

    public Card getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(Card selectedCard) {
        this.selectedCard = selectedCard;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public void setHandAccessible(boolean handAccessible) {
        isHandAccessible = handAccessible;
    }

    public void setRivalMonsterZoneAccessible(boolean rivalMonsterZoneAccessible) {
        isRivalMonsterZoneAccessible = rivalMonsterZoneAccessible;
    }

    public void setOurMonsterZoneAccessible(boolean ourMonsterZoneAccessible) {
        isOurMonsterZoneAccessible = ourMonsterZoneAccessible;
    }

    public void setRivalGraveyardAccessible(boolean rivalGraveyardAccessible) {
        isRivalGraveyardAccessible = rivalGraveyardAccessible;
    }

    public void setOurGraveyardAccessible(boolean ourGraveyardAccessible) {
        isOurGraveyardAccessible = ourGraveyardAccessible;
    }

    public boolean isHasPositionChanged() {
        return hasPositionChanged;
    }

    public void setHasPositionChanged(boolean hasPositionChanged) {
        this.hasPositionChanged = hasPositionChanged;
    }

    public void runMonsterEffect() {

    }

    public void runMonsterEffectAtSummon() {

    }

    public void runMonsterEffectOnEachTurn() {

    }

    public void endMonsterEffect() {

    }

    public boolean canBeAttacked(MonsterController attacker) {
        return true;
    }

    public MonsterPosition getPosition() {
        return position;
    }

    public void setPosition(MonsterPosition position) {
        this.position = position;
    }

    public void summon() {

    }

    public void flip() {
        this.setPosition(MonsterPosition.ATTACK);
        setHasPositionChanged(true);
    }

    public void remove(MonsterController attacker) {
        endMonsterEffect();
        thisBoard.removeMonster(this);
    }

    public String getName() {
        return monster.getName();
    }

    public Monster getCard() {
        return this.monster;
    }

    public void set() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof MonsterController)) {
            return false;
        }

        MonsterController monsterController = (MonsterController) o;

        return monsterController.monster.getName().equals(this.monster.getName());
    }

    public boolean isMonsterNew() {
        return isMonsterNew;
    }

    public void setMonsterNew(boolean monsterNew) {
        this.isMonsterNew = monsterNew;
    }

    public boolean isHasAttackedThisTurn() {
        return hasAttackedThisTurn;
    }

    public void setHasAttackedThisTurn(boolean hasAttackedThisTurn) {
        this.hasAttackedThisTurn = hasAttackedThisTurn;
    }

    public AttackResult attack(MonsterController attacker) {
        return null;
    }

    public void nextTurn() {
        setHasActivateEffectThisTurn(false);
        isMonsterNew = false;
    }

    public String defaultAttack(MonsterController attacker) {
        int damage;
        MonsterController defender = this;
        Game game = gameController.getGame();
        switch (defender.getPosition()) {
            case ATTACK:
                damage = attacker.getCard().getAttackPower() - defender.getCard().getAttackPower();
                if (damage > 0) {
                    defender.remove(attacker);
                    game.decreaseOtherLifePoint(damage);
                    return "your opponent’s monster is destroyed and your opponent receives " + damage + " battle damage";
                } else if (damage == 0) {
                    game.getThisBoard().removeMonster(attacker);
                    defender.remove(attacker);
                    return "both you and your opponent monster cards are destroyed and no one receives damage";
                } else {
                    damage = -damage;
                    game.getThisBoard().removeMonster(attacker);
                    game.decreaseThisLifePoint(damage);
                    return "your monster is destroyed and you receive " + damage + " battle damage";
                }
            case DEFENCE_UP:
                damage = attacker.getCard().getAttackPower() - defender.getCard().getDefencePower();
                if (damage > 0) {
                    defender.remove(attacker);
                    return "the defense position monster is destroyed";
                } else if (damage == 0) {
                    return "no card is destroyed";
                } else {
                    damage = -damage;
                    game.decreaseThisLifePoint(damage);
                    return "no card is destroyed and you received " + damage + " battle damage";
                }
            case DEFENCE_DOWN:
                String cardNameMessage = "opponent’s monster card was <monster card >name and ";
                damage = attacker.getCard().getAttackPower() - defender.getCard().getDefencePower();
                if (damage > 0) {
                    defender.remove(attacker);
                    return cardNameMessage + "the defense position monster is destroyed";
                } else if (damage == 0) {
                    return cardNameMessage + "no card is destroyed";
                } else {
                    damage = -damage;
                    game.decreaseThisLifePoint(damage);
                    return cardNameMessage + "no card is destroyed and you received " + damage + " battle damage";
                }
        }
        return null;
    }

    public void select(String selectCommand) throws InvalidSelection, CardNotFoundException, InvalidInput, NoCardSelectedException {
        HashMap<String, String> input = Scan.getInstance().parseInput(selectCommand);
        Game game = gameController.getGame();
        String addressNumber;


        if ((addressNumber = Scan.getInstance().getValue(input, "monster", "m")) != null) {
            int monsterNumber = Integer.parseInt(addressNumber);
            if (monsterNumber > Board.CARD_NUMBER_IN_ROW)
                throw new InvalidSelection();

            if (input.containsKey("opponent") || input.containsKey("o") && isRivalMonsterZoneAccessible) {
                if (game.getOtherBoard().getMonsterByIndex(monsterNumber - 1) != null) {
                    selectedCard = game.getOtherBoard().getMonsterByIndex(monsterNumber - 1).getMonster();
                    selectedCardAddress = new CardAddress(Place.MonsterZone, Owner.Opponent, monsterNumber - 1);
                }
            } else if (isOurMonsterZoneAccessible) {
                if (game.getThisBoard().getMonsterByIndex(monsterNumber - 1) != null) {
                    selectedCard = game.getThisBoard().getMonsterByIndex(monsterNumber - 1).getMonster();
                    selectedCardAddress = new CardAddress(Place.MonsterZone, Owner.Me, monsterNumber - 1);
                }
            } else throw new InvalidSelection();

            if (selectedCard == null)
                throw new CardNotFoundException();

        } else if ((addressNumber = Scan.getInstance().getValue(input, "hand", "h")) != null && isHandAccessible) {
            int handNumber = Integer.parseInt(addressNumber);
            if (handNumber > game.getThisBoard().getHand().size())
                throw new InvalidSelection();

            selectedCard = game.getThisBoard().getHand().get(handNumber - 1);
            selectedCardAddress = new CardAddress(Place.Hand, Owner.Me, handNumber - 1);

            if (this.selectedCard == null)
                throw new CardNotFoundException();

        } else if ((addressNumber = Scan.getInstance().getValue(input, "graveyard", "g")) != null) {
            int graveyardNumber = Integer.parseInt(addressNumber);

            if (input.containsKey("opponent") || input.containsKey("o") && isRivalGraveyardAccessible) {
                if (graveyardNumber > game.getOtherBoard().getGraveyard().size())
                    throw new InvalidSelection();
                else {
                    this.selectedCard = game.getOtherBoard().getGraveyard().get(graveyardNumber - 1);
                    selectedCardAddress = new CardAddress(Place.Graveyard, Owner.Opponent, graveyardNumber - 1);
                }
            } else if (isOurGraveyardAccessible) {
                if (graveyardNumber > game.getThisBoard().getGraveyard().size())
                    throw new InvalidSelection();
                else {
                    this.selectedCard = game.getThisBoard().getGraveyard().get(graveyardNumber - 1);
                    selectedCardAddress = new CardAddress(Place.Graveyard, Owner.Me, graveyardNumber - 1);
                }
            } else throw new InvalidSelection();

            if (selectedCard == null)
                throw new CardNotFoundException();

        } else if (input.containsKey("-d")) {
            if (selectedCard == null) {
                throw new NoCardSelectedException();
            }
            deselect();
        } else
            throw new InvalidInput();

        if (selectedCard == null)
            throw new CardNotFoundException();
    }

    public void deselect() {
        selectedCardAddress = null;
        selectedCard = null;
    }

    public interface MonsterMakerInterface {
        MonsterController make(GameController gameController, Monster monster,
                               MonsterPosition position, CardAddress monsterAddress);
    }
}
