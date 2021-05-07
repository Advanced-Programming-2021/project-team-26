package controller;

import exceptions.*;
import model.*;
import model.cards.Card;
import model.cards.monster.Monster;
import view.Scan;

import java.util.HashMap;

public class GameController {
    private final int currentRound = 0;
    private Game game;
    private CardAddress selectedCardAddress = null;
    private Card selectedCard = null;
    private MonsterController selectedMonster = null;
    private SpellTrapController selectedSpellTrap = null;
    private int roundNumber;


    public GameController(String firstPlayer, String secondPayer, int round) {
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

    public void select(String address) throws InvalidSelection, CardNotFoundException, InvalidInput, NoCardSelectedException {
        HashMap<String, String> input = Scan.getInstance().parseInput(address.split("\\s+"));

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

    public void summon() throws NoCardSelectedException, CannotSummonException, ActionNotAllowed,
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
    }

    public void set() {

    }

    public void set(String position) {

    }

    public void flipSummon() {

    }

    public void attack(int number) {

    }

    public void attack() {

    }

    public void activateEffect() {

    }

    public void showGraveyard() {

    }

    public void cardShow() {

    }

    public void surrender() {

    }

    public boolean isFinished() {
        return false;
    }

    public void endGame() {

    }

    public void showBoard() {

    }
}
