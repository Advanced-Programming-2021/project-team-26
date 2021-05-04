package controller;

import exceptions.CardNotFoundException;
import exceptions.InvalidInput;
import exceptions.InvalidSelection;
import exceptions.NoCardSelectedException;
import model.Board;
import model.Game;
import model.User;
import model.cards.Card;
import view.Scan;

import java.util.HashMap;

public class GameController {
    private Game game;
    private final int currentRound = 0;
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

    public void select(String address) throws InvalidSelection, CardNotFoundException, InvalidInput {
        HashMap<String, String> input = Scan.getInstance().parseInput(address.split("\\s+"));

        String addressNumber;
        if ((addressNumber = Scan.getInstance().getValue(input, "monster", "m")) != null) {
            int monsterNumber = Integer.parseInt(addressNumber);
            if (monsterNumber > Board.CARD_NUMBER_IN_ROW)
                throw new InvalidSelection();

            if (input.containsKey("opponent") || input.containsKey("o"))
                selectedMonster = game.getOtherBoard().getMonstersZone()[monsterNumber - 1];
            else
                selectedMonster = game.getThisBoard().getMonstersZone()[monsterNumber - 1];

            if (selectedMonster == null)
                throw new CardNotFoundException();

            selectedCard = selectedMonster.getCard();
        } else if ((addressNumber = Scan.getInstance().getValue(input, "spell", "s")) != null) {
            int spellNumber = Integer.parseInt(addressNumber);
            if (spellNumber > Board.CARD_NUMBER_IN_ROW)
                throw new InvalidSelection();

            if (input.containsKey("opponent") || input.containsKey("o"))
                selectedSpellTrap = game.getOtherBoard().getSpellTrapZone()[spellNumber - 1];
            else
                selectedSpellTrap = game.getThisBoard().getSpellTrapZone()[spellNumber - 1];

            if (selectedSpellTrap == null)
                throw new CardNotFoundException();

            this.selectedCard = selectedSpellTrap.getCard();
        } else if (Scan.getInstance().getValue(input, "field", "f") != null) {
            if (input.containsKey("opponent") || input.containsKey("o"))
                selectedCard = game.getOtherBoard().getFieldZone();
            else
                selectedCard = game.getThisBoard().getFieldZone();

            if (selectedCard == null)
                throw new CardNotFoundException();
        } else if ((addressNumber = Scan.getInstance().getValue(input, "hand", "h")) != null) {
            int handNumber = Integer.parseInt(addressNumber);
            if (handNumber > game.getThisBoard().getHand().size())
                throw new InvalidSelection();

            selectedCard = game.getThisBoard().getHand().get(handNumber - 1);
        } else
            throw new InvalidInput();
    }

    public void deselect(String inputString) throws InvalidInput, NoCardSelectedException {
        HashMap<String, String> input = Scan.getInstance().parseInput(inputString.split("\\s+"));
        if (input.containsKey("d")) {
            if (selectedCard == null) {
                throw new NoCardSelectedException();
            }
            selectedCard = null;
            selectedMonster = null;
            selectedSpellTrap = null;
        } else
            throw new InvalidInput();
    }

    public void summon() {

    }

    public void set() {

    }

    public void set(String position) {

    }

    public void flipSummon(){

    }

    public void attack(int number){

    }

    public void attack(){

    }

    public void activateEffect(){

    }

    public void showGraveyard(){

    }

    public void cardShow(){

    }

    public void surrender(){

    }

    public boolean isFinished(){
        return false;
    }

    public void endGame(){

    }

    public void showBoard(){

    }
}
