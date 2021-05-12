package controller;

import exceptions.*;
import model.*;
import model.cards.Card;
import model.cards.monster.Monster;
import view.Scan;

import java.util.HashMap;
import java.util.regex.Matcher;

public class GameController {
    private final int currentRound = 0;
    private Game game;
    private CardAddress selectedCardAddress = null;
    private Card selectedCard = null;
    private MonsterController selectedMonster = null;
    private SpellTrapController selectedSpellTrap = null;
    private int roundNumber;


    public GameController(String firstPlayer, String secondPayer, int round) throws NoPlayerAvailable {
        this.game = new Game(this, User.getUserByUsername(firstPlayer), User.getUserByUsername(secondPayer));
        this.roundNumber = round;
        game.nextPhase();
    }

    public GameController(String player, int round) {

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
                selectedCard = game.getOtherBoard().getMonstersZone()[monsterNumber - 1].getCard();
                selectedCardAddress = new CardAddress(Place.MonsterZone, Owner.Opponent, monsterNumber - 1);
            } else {
                selectedMonster = game.getThisBoard().getMonstersZone()[monsterNumber - 1];
                selectedCard = selectedMonster.getCard();
                selectedCardAddress = new CardAddress(Place.MonsterZone, Owner.Me, monsterNumber - 1);
            }

            if (selectedCard == null)
                throw new CardNotFoundException();

        } else if ((addressNumber = Scan.getInstance().getValue(input, "spell", "s")) != null) {
            int spellNumber = Integer.parseInt(addressNumber);
            if (spellNumber > Board.CARD_NUMBER_IN_ROW)
                throw new InvalidSelection();

            if (input.containsKey("opponent") || input.containsKey("o")) {
                selectedCard = game.getOtherBoard().getSpellTrapZone()[spellNumber - 1].getCard();
                selectedCardAddress = new CardAddress(Place.SpellTrapZone, Owner.Opponent, spellNumber - 1);
            } else {
                selectedSpellTrap = game.getThisBoard().getSpellTrapZone()[spellNumber - 1];
                selectedCard = selectedSpellTrap.getCard();
                selectedCardAddress = new CardAddress(Place.SpellTrapZone, Owner.Me, spellNumber - 1);
            }

            if (selectedCard == null)
                throw new CardNotFoundException();

        } else if (Scan.getInstance().getValue(input, "field", "f") != null) {
            if (input.containsKey("opponent") || input.containsKey("o")) {
                selectedCard = game.getOtherBoard().getFieldZone();
                selectedCardAddress = new CardAddress(Place.Field, Owner.Opponent);
            } else {
                selectedCard = game.getThisBoard().getFieldZone();
                selectedCardAddress = new CardAddress(Place.Field, Owner.Me);
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
        if (selectedCard == null)
            throw new NoCardSelectedException();

        if (selectedCardAddress.getOwner() != Owner.Me ||
                selectedCardAddress.getPlace() != Place.Hand ||
                !(selectedCard instanceof Monster))
            throw new CannotSetException();

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
        MonsterController monster = game.getThisBoard().putMonster(selectedMonster, MonsterPosition.DEFENCE_DOWN);
        game.setSummonOrSetThisTurn(true);
        monster.set();
        deselect();
    }

    public void setPosition(Matcher matcher) {
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

        if (selectedMonster.isChangedPosition())
            throw new AlreadyChangeException();

        selectedMonster.setPosition(wantedPosition);
    }

    public void flipSummon(Matcher matcher) {
        if (selectedCard == null)
            throw new NoCardSelectedException();

        if (selectedCardAddress.getOwner() != Owner.Me ||
                selectedCardAddress.getPlace() != Place.MonsterZone ||
                !(selectedCard instanceof Monster))
            throw new CannotChangeException();

        if (game.getPhase() != Phase.MAIN1 && game.getPhase() != Phase.MAIN2)
            throw new ActionNotAllowed();

        if (selectedMonster.getPosition() != MonsterPosition.DEFENCE_DOWN || selectedMonster.isNewMonster())
            throw new CannotFlipSummon();

        selectedMonster.flip(selectedMonster);
    }

    public void attackDirect(Matcher matcher) {
        if (selectedCard == null)
            throw new NoCardSelectedException();

        if (selectedCardAddress.getOwner() != Owner.Me ||
                selectedCardAddress.getPlace() != Place.MonsterZone ||
                !(selectedCard instanceof Monster))
            throw new CannotAttackException();

        if (game.getPhase() != Phase.BATTLE)
            throw new ActionNotAllowed();

        if (selectedMonster.isAttackThisTurn())
            throw new AlreadyAttackedException();

        if (game.getOtherBoard().canDirectAttack())
            throw new CannotAttackDirectlyException();

        game.decreaseOtherLifePoint(selectedMonster.getCard().getAttackPower());
    }

    public void attack(Matcher matcher) {
        if (selectedCard != null)
            throw new NoCardSelectedException();

        if (selectedCardAddress.getOwner() != Owner.Me ||
                selectedCardAddress.getPlace() != Place.MonsterZone ||
                !(selectedCard instanceof Monster) ||
                selectedMonster.getPosition() != MonsterPosition.ATTACK)
            throw new CannotAttackException();

        if (game.getPhase() != Phase.BATTLE)
            throw new ActionNotAllowed();

        if (selectedMonster.isAttackThisTurn())
            throw new AlreadyAttackedException();

        int number = Integer.parseInt(matcher.group(1));
        number--;
        MonsterController toBeAttacked = game.getOtherBoard().getMonstersZone()[number];
        if (toBeAttacked == null || toBeAttacked.canBeAttacked(selectedMonster))
            throw new NoCardToAttackException();

        String message = attack(selectedMonster, toBeAttacked);
    }

    public String attack(MonsterController attacker, MonsterController defender) {
        int damage;
        switch (defender.getPosition()) {
            case ATTACK:
                damage = attacker.getCard().getAttackPower() - defender.getCard().getAttackPower();
                if (damage > 0) {
                    defender.remove(attacker);
                    game.decreaseOtherLifePoint(damage);
                    return "your opponent’s monster is destroyed and your opponent receives " + damage + "battle damage";
                } else if (damage == 0) {
                    game.getThisBoard().removeMonster(attacker);
                    defender.remove(attacker);
                    return "both you and your opponent monster cards are destroyed and no one receives damage";
                } else {
                    damage = -damage;
                    game.getThisBoard().removeMonster(attacker);
                    game.decreaseThisLifePoint(damage);
                    return "your monster is destroyed and you receives " + damage + "battle damage";
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

    public void activateEffect(Matcher matcher) {

    }

    public void showGraveyard(Matcher matcher) {

    }

    public void showSelectedCard(Matcher matcher) {

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
}
