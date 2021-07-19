package model;

import controller.GameController;
import controller.MonsterController;
import controller.SpellController;
import controller.SpellTrapController;
import model.cards.Card;
import model.cards.monster.Monster;

import java.util.*;

public class Board {
    public final static int CARD_NUMBER_IN_ROW = 5;
    private List<Card> deck;
    private final GameController gameController;
    private int myTurn = -1;

    public Board(GameController gameController, Deck deck) {
        initDeck(deck);
        this.gameController = gameController;
    }

    public HashMap<Integer, MonsterController> getMonsterZoneMap() {
        //TODO request to server
        return null;
    }

    public HashMap<Integer, SpellTrapController> getSpellTrapZoneMap() {
        //TODO request to server
        return null;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public Collection<MonsterController> getMonstersZone() {
        //TODO request to server
        return null;
    }

    public MonsterController getMonsterByIndex(int index) {
        //TODO request to server
        return null;
    }

    public Collection<SpellTrapController> getSpellTrapZone() {
        //TODO request to server
        return null;
    }

    public SpellTrapController getSpellTrapByIndex(int index) {
        //TODO request to server
        return null;
    }

    public List<Card> getGraveyard() {
        //TODO request to server
        return null;
    }

    public List<Card> getHand() {
        //TODO request to server
        return null;
    }

    public SpellController getFieldZone() {
        //TODO request to server
        return null;
    }

    private void initDeck(Deck deck) {
        ArrayList<String> mainDeck = deck.getMainDeck();
        this.deck = new ArrayList<>();

        for (String cardName : mainDeck) {
            this.deck.add(Card.getCard(cardName));
        }

        Collections.shuffle(this.deck);
    }

    private int getMyTurn() {
        if (myTurn != -1)
            return myTurn;
        if (gameController.getGame().getBoard(0) == this)
            myTurn = 0;
        else
            myTurn = 1;
        return myTurn;
    }

    public void shuffleDeck() {
        Collections.shuffle(this.deck);
    }

    public void removeMonsterWithoutAddingToGraveyard(Monster monster) {
        //TODO request to server

    }

    public void removeMonster(MonsterController monster) {
        //TODO request to server
    }

    public int getNumberOfMonstersINGraveyard() {
        //TODO request to server
        return 0;
    }

    public void removeAllMonsters() {
        //TODO request to server
    }

    public void removeSpellTrap(SpellTrapController spellTrap) {
        //TODO request to server

    }

    public void removeAllSpellTraps() {
        //TODO request to server

    }

    public int getMonsterZoneNumber() {
        //TODO request to server
        return 0;
    }
}