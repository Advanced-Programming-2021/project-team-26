package controller;

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
        MONSTER_MAKERS.put("Crab Turtle", MonsterController::makeCrabTurtle);
        MONSTER_MAKERS.put("Skull Gaurdian", MonsterController::makeSkullGaurdian);
        MONSTER_MAKERS.put("Man-Eater Bug", MonsterController::makeManEaterBug);
        MONSTER_MAKERS.put("Gate Guardian", MonsterController::makeGateGuardian);
        MONSTER_MAKERS.put("Scanner", MonsterController::makeScanner);
        MONSTER_MAKERS.put("Marshmallon", MonsterController::makeMarshmallon);
        MONSTER_MAKERS.put("Beast King Barbaros", MonsterController::makeBeastKingBarbaros);
        MONSTER_MAKERS.put("Texchanger", MonsterController::makeTexchanger);
        MONSTER_MAKERS.put("The Calculator", MonsterController::makeTheCalculator);
        MONSTER_MAKERS.put("Mirage Dragon", MonsterController::makeMirageDragon);
        MONSTER_MAKERS.put("Hearld of Creation", MonsterController::makeHearldOfCreation);
        MONSTER_MAKERS.put("Exploder Dragon", MonsterController::makeExploderDragon);
        MONSTER_MAKERS.put("Terratiger, the Empowered Warrior\n", MonsterController::makeTerratiger);
        MONSTER_MAKERS.put("The Tricky", MonsterController::makeTheTricky);
    }

    private final GameController gameController;
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
        setMonsterAddress(monsterAddress);
        setPosition(position);
        setMonsterNew(true);
        setHasPositionChanged(false);
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
            private boolean isEffectActive = position.equals(MonsterPosition.ATTACK);

            @Override
            public void runMonsterEffect() {
                if (isEffectActive) {
                    //increase other monsters attackPower for 400
                    MonsterController[] monstersZone = gameController.getGame().getThisBoard().getMonstersZone();
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
                    return gameController.getGame().getThisBoard().getMonstersZone().length < 2;
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
                gameController.getGame().getThisBoard().removeMonster(this);
                gameController.getGame().getOtherBoard().removeMonster(attacker);
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
            public String attack(MonsterController attacker) {
                if (!wasEffectActiveBefore) {
                    // TODO ask the user weather to active or not
                    isEffectActive = true;
                }

                if (isEffectActive) {
                    int theAttackerPower = attacker.monster.getAttackPower();
                    attacker.monster.setAttackPower(0);
                    String toReturn = defaultAttack(attacker);
                    attacker.monster.setAttackPower(theAttackerPower);
                    isEffectActive = false;
                    return toReturn;
                }
                return defaultAttack(attacker);
            }
        };
    }

    private static MonsterController makeCrabTurtle
            (GameController gameController, Monster monster,
             MonsterPosition position, CardAddress monsterAddress) {
        return new MonsterController(gameController, monster, position, monsterAddress) {

        };
    }

    private static MonsterController makeSkullGaurdian
            (GameController gameController, Monster monster,
             MonsterPosition position, CardAddress monsterAddress) {
        return new MonsterController(gameController, monster, position, monsterAddress) {

        };
    }

    private static MonsterController makeManEaterBug
            (GameController gameController, Monster monster,
             MonsterPosition position, CardAddress monsterAddress) {
        return new MonsterController(gameController, monster, position, monsterAddress) {
            @Override
            public void flip() {
                setRivalMonsterZoneAccessible(true);
                Print.getInstance().printMessage("Do you want to activate the card effect?" +
                        "1. yes" +
                        "2. no");
                Scanner scanner = Scan.getScanner();
                String input = scanner.nextLine();

                if (Integer.parseInt(input) == 1) {
                    Print.getInstance().printMessage("Select one of rival Monsters to remove from his Monster Zone");
                    input = scanner.nextLine();
                    select(input);
                    MonsterController[] monstersZone = gameController.getGame().getOtherBoard().getMonstersZone();
                    for (MonsterController monster : monstersZone) {
                        if (monster.getMonster().getName().equals(getSelectedCard().getName())) {
                            MonsterController selectedMonsterController = MonsterController.getMonsterControllerByMonster((Monster) getSelectedCard());
                            gameController.getGame().getOtherBoard().removeMonster(selectedMonsterController);
                        }
                    }
                }

                this.setPosition(MonsterPosition.ATTACK);
            }
        };
    }

    private static MonsterController makeGateGuardian
            (GameController gameController, Monster monster,
             MonsterPosition position, CardAddress monsterAddress) {
        return new MonsterController(gameController, monster, position, monsterAddress) {
            //TODO
        };
    }

    private static MonsterController makeScanner
            (GameController gameController, Monster monster,
             MonsterPosition position, CardAddress monsterAddress) {
        return new MonsterController(gameController, monster, position, monsterAddress) {
            @Override
            public void runMonsterEffectAtTheBeginning() throws InvalidSelection {
                if (!isHasActivateEffectThisTurn()) {
                    Print.getInstance().printMessage("Do you want to activate the card effect?" +
                            "1. yes" +
                            "2. no");
                    Scanner scanner = Scan.getScanner();
                    String input = scanner.nextLine();

                    if (Integer.parseInt(input) == 1) {
                        Print.getInstance().printMessage("Select a monster from rival GRAVEYARD");
                        input = scanner.nextLine();
                        setRivalGraveyardAccessible(true);
                        select(input);
                        if (!(getSelectedCard() instanceof Monster)) {
                            throw new InvalidSelection();
                        } else {
                            Monster selectedMonster = (Monster) getSelectedCard();
                            setMonster(new Monster(selectedMonster));
                        }
                        setRivalGraveyardAccessible(false);
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

    private static MonsterController makeTexchanger
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
                MonsterController[] monstersZone = gameController.getGame().getThisBoard().getMonstersZone();
                for (MonsterController monsterController : monstersZone) {
                    if (monsterController == null) continue;
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
                    //TODO rival cannot active his spellTraps
                }
            }
        };
    }

    private static MonsterController makeHearldOfCreation
            (GameController gameController, Monster monster,
             MonsterPosition position, CardAddress monsterAddress) {
        return new MonsterController(gameController, monster, position, monsterAddress) {
            @Override
            public void runMonsterEffectAtTheBeginning() throws InvalidSelection {
                if (!isHasActivateEffectThisTurn()) {
                    Print.getInstance().printMessage("Do you want to activate the card effect?" +
                            "1. yes" +
                            "2. no");
                    Scanner scanner = Scan.getScanner();
                    String input = scanner.nextLine();

                    if (Integer.parseInt(input) == 1) {
                        Print.getInstance().printMessage("Select a Card from your HAND to remove");
                        input = scanner.nextLine();
                        setHandAccessible(true);
                        select(input);
                        gameController.getGame().getThisBoard().getHand().remove(getSelectedCard());

                        setHandAccessible(false);
                        setOurGraveyardAccessible(true);

                        Print.getInstance().printMessage("Select a Monster from your GRAVEYARD with level 7 or more to put it in your HAND");
                        input = scanner.nextLine();
                        select(input);
                        if (!(getSelectedCard() instanceof Monster)) {
                            throw new InvalidSelection();
                        } else {
                            Monster selectedMonster = (Monster) getSelectedCard();
                            if (selectedMonster.getLevel() < 7)
                                throw new InvalidSelection();
                            else {
                                gameController.getGame().getThisBoard().getGraveyard().remove(getSelectedCard());
                                gameController.getGame().getThisBoard().addCardToHand(getSelectedCard());
                            }
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
            public void remove(MonsterController attacker) {
                gameController.getGame().getThisBoard().removeMonster(this);
                gameController.getGame().getOtherBoard().removeMonster(attacker);
            }
        };
    }

    private static MonsterController makeTerratiger
            (GameController gameController, Monster monster,
             MonsterPosition position, CardAddress monsterAddress) {
        return new MonsterController(gameController, monster, position, monsterAddress) {
        };
    }

    private static MonsterController makeTheTricky
            (GameController gameController, Monster monster,
             MonsterPosition position, CardAddress monsterAddress) {
        return new MonsterController(gameController, monster, position, monsterAddress) {
            @Override
            public void summon() {
                Print.getInstance().printMessage("Do you want to summon this card specially with remove a card from your HAND?" +
                        "1. yes" +
                        "2. no");

                Scanner scanner = Scan.getScanner();
                String input = scanner.nextLine();

                if (Integer.parseInt(input) == 1) {
                    Print.getInstance().printMessage("Select a card from your HAND to remove");

                    input = scanner.nextLine();
                    setHandAccessible(true);
                    select(input);

                    gameController.getGame().getThisBoard().getHand().remove(getSelectedCard());
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

    public void finishTurn() {
        hasPositionChanged = false;
        isMonsterNew = false;
    }

    public void runMonsterEffect() {

    }

    public void runMonsterEffectAtTheBeginning() {

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
    }

    public void remove(MonsterController attacker) {
        gameController.getGame().getThisBoard().removeMonster(this);
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

    public String attack(MonsterController attacker) {
        return defaultAttack(attacker);
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
                if (game.getOtherBoard().getMonstersZone()[monsterNumber - 1] != null) {
                    selectedCard = game.getOtherBoard().getMonstersZone()[monsterNumber - 1].getMonster();
                    selectedCardAddress = new CardAddress(Place.MonsterZone, Owner.Opponent, monsterNumber - 1);
                }
            } else if (isOurMonsterZoneAccessible) {
                if (game.getThisBoard().getMonstersZone()[monsterNumber - 1] != null) {
                    selectedCard = game.getThisBoard().getMonstersZone()[monsterNumber - 1].getMonster();
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
