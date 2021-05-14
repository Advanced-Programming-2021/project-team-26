package controller;

import exceptions.*;
import model.*;
import model.cards.Card;
import model.cards.SpellTrap;
import model.cards.monster.Monster;
import model.cards.spell.Spell;
import model.cards.spell.SpellType;
import model.cards.trap.Trap;
import view.Print;
import view.Scan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class GameController {
    private final int currentRound = 0;
    private Game game;
    private CardAddress selectedCardAddress = null;
    private Card selectedCard = null;
    private MonsterController selectedMonster = null;
    private SpellTrapController selectedSpellTrap = null;
    private int roundNumber;
    private boolean temporaryTurnChange = false;

    public GameController(String firstPlayer, String secondPayer, int round) throws NoPlayerAvailable {
        this.game = new Game(this, User.getUserByUsername(firstPlayer), User.getUserByUsername(secondPayer));
        this.roundNumber = round;
        game.nextPhase();
    }

    public GameController(String player, int round) {

    }

    public boolean isTemporaryTurnChange() {
        return temporaryTurnChange;
    }

    public void setTemporaryTurnChange(boolean temporaryTurnChange) {
        this.temporaryTurnChange = temporaryTurnChange;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void select(Matcher matcher) throws InvalidSelection, CardNotFoundException, InvalidInput, NoCardSelectedException {
        HashMap<String, String> input = Scan.getInstance().parseInput(matcher.group(1).split("\\s+"));

        String addressNumber;
        if ((addressNumber = Scan.getInstance().getValue(input, "monster", "m")) != null) {
            int monsterNumber = Integer.parseInt(addressNumber);
            if (monsterNumber > Board.CARD_NUMBER_IN_ROW)
                throw new InvalidSelection();

            if (input.containsKey("opponent") || input.containsKey("o")) {
                if (game.getOtherBoard().getMonstersZone()[monsterNumber - 1] != null) {
                    selectedCard = game.getOtherBoard().getMonstersZone()[monsterNumber - 1].getCard();
                    selectedCardAddress = new CardAddress(Place.MonsterZone, Owner.Opponent, monsterNumber - 1);
                }
            } else {
                if (game.getThisBoard().getMonstersZone()[monsterNumber - 1] != null) {
                    selectedMonster = game.getThisBoard().getMonstersZone()[monsterNumber - 1];
                    selectedCard = selectedMonster.getCard();
                    selectedCardAddress = new CardAddress(Place.MonsterZone, Owner.Me, monsterNumber - 1);
                }
            }

            if (selectedCard == null)
                throw new CardNotFoundException();

        } else if ((addressNumber = Scan.getInstance().getValue(input, "spell", "s")) != null) {
            int spellNumber = Integer.parseInt(addressNumber);
            if (spellNumber > Board.CARD_NUMBER_IN_ROW)
                throw new InvalidSelection();

            if (input.containsKey("opponent") || input.containsKey("o")) {
                if (game.getOtherBoard().getSpellTrapZone()[spellNumber - 1] != null) {
                    selectedCard = game.getOtherBoard().getSpellTrapZone()[spellNumber - 1].getCard();
                    selectedCardAddress = new CardAddress(Place.SpellTrapZone, Owner.Opponent, spellNumber - 1);
                }
            } else {
                if (game.getThisBoard().getSpellTrapZone()[spellNumber - 1] != null) {
                    selectedSpellTrap = game.getThisBoard().getSpellTrapZone()[spellNumber - 1];
                    selectedCard = selectedSpellTrap.getCard();
                    selectedCardAddress = new CardAddress(Place.SpellTrapZone, Owner.Me, spellNumber - 1);
                }
            }

            if (selectedCard == null)
                throw new CardNotFoundException();

        } else if (Scan.getInstance().getValue(input, "field", "f") != null) {
            if (input.containsKey("opponent") || input.containsKey("o")) {
                if (game.getOtherBoard().getFieldZone() != null) {
                    selectedSpellTrap = game.getOtherBoard().getFieldZone();
                    selectedCard = selectedSpellTrap.getCard();
                    selectedCardAddress = new CardAddress(Place.Field, Owner.Opponent);
                }
            } else {
                if (game.getThisBoard().getFieldZone() != null) {
                    selectedSpellTrap = game.getThisBoard().getFieldZone();
                    selectedCard = selectedSpellTrap.getCard();
                    selectedCardAddress = new CardAddress(Place.Field, Owner.Me);
                }
            }

            if (selectedCard == null)
                throw new CardNotFoundException();
        } else if ((addressNumber = Scan.getInstance().getValue(input, "hand", "h")) != null) {
            int handNumber = Integer.parseInt(addressNumber);
            if (handNumber > game.getThisBoard().getHand().size())
                throw new InvalidSelection();

            selectedCard = game.getThisBoard().getHand().get(handNumber - 1);
            selectedCardAddress = new CardAddress(Place.Hand, Owner.Me, handNumber - 1);
        } else if (input.containsKey("-d")) {
            if (selectedCard == null) {
                throw new NoCardSelectedException();
            }
            deselect();
        } else
            throw new InvalidInput();
    }

    public void deselect() {
        selectedCardAddress = null;
        selectedCard = null;
        selectedMonster = null;
        selectedSpellTrap = null;
    }

    public void nextPhase() {
        game.nextPhase();
    }

    public void summon(Matcher matcher) throws NoCardSelectedException, CannotSummonException, ActionNotAllowed,
            MonsterNotFoundException, FullMonsterZone, AlreadySummonException, NotEnoughCardForTribute,
            InvalidSelection {
        if (temporaryTurnChange)
            throw new NotYourTurnException();
        if (selectedCard == null)
            throw new NoCardSelectedException();

        if (selectedCardAddress.getOwner() != Owner.Me ||
                selectedCardAddress.getPlace() != Place.Hand ||
                !(selectedCard instanceof Monster))
            throw new CannotSummonException();

        if (game.getPhase() != Phase.MAIN1 && game.getPhase() != Phase.MAIN2)
            throw new ActionNotAllowed();

        if (game.getThisBoard().getMonsterZoneNumber() >= Board.CARD_NUMBER_IN_ROW)
            throw new FullMonsterZone();

        if (game.isSummonOrSetThisTurn())
            throw new AlreadySummonException();

        Monster selectedMonster = (Monster) selectedCard;

        if (selectedMonster.getLevel() > 4 && selectedMonster.getLevel() <= 6) {
            if (game.getThisBoard().getMonsterZoneNumber() < 1)
                throw new NotEnoughCardForTribute();

            Integer monsterAddress = Scan.getInstance().getInteger();
            if (monsterAddress == null)
                return;
            if (game.getThisBoard().getMonstersZone()[monsterAddress - 1] == null)
                throw new InvalidSelection();

            game.getThisBoard().removeMonster(monsterAddress - 1);
        } else if (selectedMonster.getLevel() > 6) {
            if (game.getThisBoard().getMonsterZoneNumber() < 2)
                throw new NotEnoughCardForTribute();

            Integer monsterAddress1 = Scan.getInstance().getInteger();
            Integer monsterAddress2 = Scan.getInstance().getInteger();
            if (monsterAddress1 == null || monsterAddress2 == null)
                return;
            if (game.getThisBoard().getMonstersZone()[monsterAddress1 - 1] == null ||
                    game.getThisBoard().getMonstersZone()[monsterAddress2 - 1] == null)
                throw new InvalidSelection();

            game.getThisBoard().removeMonster(monsterAddress1 - 1);
            game.getThisBoard().removeMonster(monsterAddress2 - 1);
        }
        MonsterController monster = game.getThisBoard().putMonster(selectedMonster, MonsterPosition.ATTACK);
        game.setSummonOrSetThisTurn(true);
        monster.summon();
        deselect();
    }

    public void set(Matcher matcher) throws NoCardSelectedException, CannotSetException {
        if (temporaryTurnChange)
            throw new NotYourTurnException();
        if (selectedCard == null)
            throw new NoCardSelectedException();

        if (selectedCardAddress.getOwner() != Owner.Me ||
                selectedCardAddress.getPlace() != Place.Hand)
            throw new CannotSetException();

        if (game.getPhase() != Phase.MAIN1 && game.getPhase() != Phase.MAIN2)
            throw new ActionNotAllowed();

        if (selectedCard instanceof Monster) {
            setMonster();
        } else if (selectedCard instanceof Spell) {
            setSpell();
        } else if (selectedCard instanceof Trap) {
            setTrap();
        }
        deselect();
    }

    private void setTrap() {
        if (game.getThisBoard().getSpellTrapZoneNumber() >= Board.CARD_NUMBER_IN_ROW)
            throw new FullSpellTrapZone();

        Trap trap = (Trap) selectedCard;
        game.getThisBoard().putSpellTrap(trap, SpellTrapPosition.DOWN);
    }

    private void setSpell() {
        if (game.getThisBoard().getSpellTrapZoneNumber() >= Board.CARD_NUMBER_IN_ROW)
            throw new FullSpellTrapZone();

        Spell spell = (Spell) selectedCard;
        game.getThisBoard().putSpellTrap(spell, SpellTrapPosition.DOWN);
    }

    private void setMonster() {
        if (game.getThisBoard().getMonsterZoneNumber() >= Board.CARD_NUMBER_IN_ROW)
            throw new FullMonsterZone();

        if (game.isSummonOrSetThisTurn())
            throw new AlreadySummonException();

        Monster selectedMonster = (Monster) selectedCard;

        if (selectedMonster.getLevel() > 4 && selectedMonster.getLevel() <= 6) {
            if (game.getThisBoard().getMonsterZoneNumber() < 1)
                throw new NotEnoughCardForTribute();

            Integer monsterAddress = Scan.getInstance().getInteger();
            if (monsterAddress == null)
                return;
            if (game.getThisBoard().getMonstersZone()[monsterAddress - 1] == null)
                throw new InvalidSelection();

            game.getThisBoard().removeMonster(monsterAddress - 1);
        } else if (selectedMonster.getLevel() > 6) {
            if (game.getThisBoard().getMonsterZoneNumber() < 2)
                throw new NotEnoughCardForTribute();

            Integer monsterAddress1 = Scan.getInstance().getInteger();
            Integer monsterAddress2 = Scan.getInstance().getInteger();
            if (monsterAddress1 == null || monsterAddress2 == null)
                return;
            if (game.getThisBoard().getMonstersZone()[monsterAddress1 - 1] == null ||
                    game.getThisBoard().getMonstersZone()[monsterAddress2 - 1] == null)
                throw new InvalidSelection();

            game.getThisBoard().removeMonster(monsterAddress1 - 1);
            game.getThisBoard().removeMonster(monsterAddress2 - 1);
        }
        MonsterController monster = game.getThisBoard().putMonster(selectedMonster, MonsterPosition.DEFENCE_DOWN);
        game.setSummonOrSetThisTurn(true);
        monster.set();
    }

    public void setPosition(Matcher matcher) {
        if (temporaryTurnChange)
            throw new NotYourTurnException();
        if (selectedCard == null)
            throw new NoCardSelectedException();

        if (selectedCardAddress.getOwner() != Owner.Me ||
                selectedCardAddress.getPlace() != Place.MonsterZone ||
                !(selectedCard instanceof Monster))
            throw new CannotChangeException();

        if (game.getPhase() != Phase.MAIN1 && game.getPhase() != Phase.MAIN2)
            throw new ActionNotAllowed();

        MonsterPosition wantedPosition;
        String position = matcher.group(1);
        if (position.equals("attack"))
            wantedPosition = MonsterPosition.ATTACK;
        else if (position.equals("defence"))
            wantedPosition = MonsterPosition.DEFENCE_UP;
        else
            throw new InvalidInput();

        if (wantedPosition == MonsterPosition.ATTACK && selectedMonster.getPosition() != MonsterPosition.DEFENCE_UP)
            throw new CannotChangeException();
        if (wantedPosition == MonsterPosition.DEFENCE_UP && selectedMonster.getPosition() != MonsterPosition.ATTACK)
            throw new CannotChangeException();

        if (selectedMonster.isHasPositionChanged())
            throw new AlreadyChangeException();

        selectedMonster.setPosition(wantedPosition);
        deselect();
    }

    public void flipSummon(Matcher matcher) {
        if (temporaryTurnChange)
            throw new NotYourTurnException();
        if (selectedCard == null)
            throw new NoCardSelectedException();

        if (selectedCardAddress.getOwner() != Owner.Me ||
                selectedCardAddress.getPlace() != Place.MonsterZone ||
                !(selectedCard instanceof Monster))
            throw new CannotChangeException();

        if (game.getPhase() != Phase.MAIN1 && game.getPhase() != Phase.MAIN2)
            throw new ActionNotAllowed();

        if (selectedMonster.getPosition() != MonsterPosition.DEFENCE_DOWN || selectedMonster.isMonsterNew())
            throw new CannotFlipSummon();

        selectedMonster.flip(selectedMonster);
        deselect();
    }

    public void attackDirect(Matcher matcher) {
        if (temporaryTurnChange)
            throw new NotYourTurnException();
        if (selectedCard == null)
            throw new NoCardSelectedException();

        if (selectedCardAddress.getOwner() != Owner.Me ||
                selectedCardAddress.getPlace() != Place.MonsterZone ||
                !(selectedCard instanceof Monster))
            throw new CannotAttackException();

        if (game.getPhase() != Phase.BATTLE)
            throw new ActionNotAllowed();

        if (selectedMonster.isHasAttackedThisTurn())
            throw new AlreadyAttackedException();

        if (game.getOtherBoard().canDirectAttack())
            throw new CannotAttackDirectlyException();

        game.decreaseOtherLifePoint(selectedMonster.getCard().getAttackPower());
        deselect();
    }

    public void attack(Matcher matcher) {
        if (temporaryTurnChange)
            throw new NotYourTurnException();
        if (selectedCard != null)
            throw new NoCardSelectedException();

        if (selectedCardAddress.getOwner() != Owner.Me ||
                selectedCardAddress.getPlace() != Place.MonsterZone ||
                !(selectedCard instanceof Monster) ||
                selectedMonster.getPosition() != MonsterPosition.ATTACK)
            throw new CannotAttackException();

        if (game.getPhase() != Phase.BATTLE)
            throw new ActionNotAllowed();

        if (selectedMonster.isHasAttackedThisTurn())
            throw new AlreadyAttackedException();

        int number = Integer.parseInt(matcher.group(1));
        number--;
        MonsterController toBeAttacked = game.getOtherBoard().getMonstersZone()[number];
        if (toBeAttacked == null || toBeAttacked.canBeAttacked(selectedMonster))
            throw new NoCardToAttackException();

        String message = toBeAttacked.attack(selectedMonster);
        deselect();
    }


    public void activateEffect(Matcher matcher) {
        if (temporaryTurnChange) {
            activateEffectOnOpponentTurn();
            return;
        }
        if (selectedCard == null)
            throw new NoCardSelectedException();

        if (selectedCardAddress.getOwner() != Owner.Me ||
                !(selectedCard instanceof Spell))
            throw new CannotActivateException();

        if (game.getPhase() != Phase.MAIN1 && game.getPhase() != Phase.MAIN2)
            throw new ActionNotAllowed();

        if (selectedCardAddress.getPlace() == Place.SpellTrapZone)
            throw new AlreadyActivatedException();

        Spell spell = (Spell) selectedCard;
        if (game.getThisBoard().getSpellTrapZoneNumber() >= Board.CARD_NUMBER_IN_ROW &&
                spell.getType() != SpellType.FIELD)
            throw new FullSpellTrapZone();

        if (!selectedSpellTrap.conditionMet())
            throw new PreparationFailException();

        if (spell.getType() == SpellType.FIELD) {
            game.getThisBoard().putFiled(spell);
        } else {
            SpellController controller = (SpellController) game.getThisBoard().putSpellTrap(spell, SpellTrapPosition.UP);
            controller.activate();
        }
        deselect();
    }

    private void activateEffectOnOpponentTurn() {
        if (selectedCard == null)
            throw new NoCardSelectedException();

        if (selectedCardAddress.getOwner() != Owner.Me ||
                !(selectedCard instanceof SpellTrap))
            throw new CannotActivateException();

        SpellTrap spellTrap = (SpellTrap) selectedCard;
        if (selectedCardAddress.getPlace() == Place.SpellTrapZone) {
            selectedSpellTrap.activate();
        } else if (selectedCardAddress.getPlace() == Place.Hand) {
            if (spellTrap instanceof Spell && ((Spell) spellTrap).getType() != SpellType.QUICK_PLAY) {
                throw new ActionNotAllowed();
            }
            SpellTrapController controller = game.getThisBoard().putSpellTrap(spellTrap, SpellTrapPosition.UP);
            controller.activate();
            controller.remove();
        }
        deselect();
    }

    public void showGraveyard(Matcher matcher) {
        List<Card> graveyard = game.getThisBoard().getGraveyard();
        if (graveyard.isEmpty()) {
            Print.getInstance().printMessage("graveyard empty");
        } else {
            int i = 1;
            for (Card card : graveyard) {
                Print.getInstance().printMessage(i + ". " + card.getName() + ":" + card.getDescription());
                i++;
            }
        }
        while (true) {
            if (Scan.getInstance().getString().equals("back"))
                break;
        }
    }

    public void showCard(Matcher matcher) {
        String[] rawInput = matcher.group().split("\\s+");
        Map<String, String> input = Scan.getInstance().parseInput(rawInput);
        if (input.containsKey("selected") || input.containsKey("s")) {
            if (selectedCard == null)
                throw new NoCardSelectedException();
            if (selectedCardAddress.getOwner() == Owner.Opponent) {
                if ((selectedCard instanceof Monster) && selectedMonster.getPosition() == MonsterPosition.DEFENCE_DOWN)
                    throw new CardNotVisibleException();
                if ((selectedCard instanceof SpellTrap) && selectedSpellTrap.getPosition() == SpellTrapPosition.DOWN)
                    throw new CardNotVisibleException();
            }
            Print.getInstance().printCard(selectedCard);
        } else {
            String cardName = rawInput[2];
            Card card = Card.getCard(cardName);
            if (card == null)
                throw new CardNotFoundException();
            Print.getInstance().printCard(card);
        }
    }

    public void surrender(Matcher matcher) {

    }

    public boolean isFinished() {
        return false;
    }

    public void endGame() {

    }

    public void showBoard() {

    }

    public boolean conditionsForChangingTurn() {
        return false;
    }

    public void finishTemporaryChangeTurn() {
        if (temporaryTurnChange)
            game.temporaryChangeTurn();
    }

    public void changeTurn() {
        if (!temporaryTurnChange && conditionsForChangingTurn()) {
            game.temporaryChangeTurn();
            Print.getInstance().printMessage("do you want to activate your trap and spell?");
            String answer = Scan.getInstance().getString();
            if (!answer.equals("yse")) {
                game.temporaryChangeTurn();
            }
        }
    }
}
