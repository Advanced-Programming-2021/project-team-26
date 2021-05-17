package model;

import controller.*;
import exceptions.FullMonsterZone;
import exceptions.FullSpellTrapZone;
import exceptions.MonsterNotFoundException;
import exceptions.SpellTrapNotFoundException;
import model.cards.Card;
import model.cards.SpellTrap;
import model.cards.monster.Monster;
import model.cards.spell.Spell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Board {
    public final static int CARD_NUMBER_IN_ROW = 5;
    public final static int INITIAL_CARDS = 5;
    private List<Card> deck;
    private MonsterController[] monstersZone;
    private SpellTrapController[] spellTrapZone;
    private List<Card> graveyard;
    private List<Card> hand;
    private SpellController fieldZone;
    private GameController gameController;

    public Board(GameController gameController, Deck deck) {
        initDeck(deck);
        setGameController(gameController);
        initMonstersZone();
        initSpellTrapZone();
        initGraveyard();
        initHand();
        initFieldZone();
    }


    public List<Card> getDeck() {
        return deck;
    }

    public MonsterController[] getMonstersZone() {
        return monstersZone;
    }

    public SpellTrapController[] getSpellTrapZone() {
        return spellTrapZone;
    }

    public List<Card> getGraveyard() {
        return graveyard;
    }

    public List<Card> getHand() {
        return hand;
    }

    public SpellController getFieldZone() {
        return fieldZone;
    }

    private void initDeck(Deck deck) {
        this.deck = new ArrayList<>(deck.getMainDeck());
        Collections.shuffle(this.deck);
    }

    private void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    private void initMonstersZone() {
        this.monstersZone = new MonsterController[CARD_NUMBER_IN_ROW];
    }

    private void initSpellTrapZone() {
        this.spellTrapZone = new SpellTrapController[CARD_NUMBER_IN_ROW];
    }

    private void initGraveyard() {
        this.graveyard = new ArrayList<>();
    }

    private void initHand() {
        hand = new ArrayList<>();
        for (int i = 0; i < INITIAL_CARDS; i++)
            addCardToHand();
    }

    private void initFieldZone() {
        fieldZone = null;
    }

    private Board getOtherBoard() {
        if (gameController.getGame().getOtherBoard() != this)
            return gameController.getGame().getOtherBoard();
        else
            return gameController.getGame().getThisBoard();
    }

    public Card addCardToHand() {
        if (this.deck.size() == 0)
            return null;
        Card addedCard = this.deck.remove(0);
        hand.add(addedCard);
        return addedCard;
    }

    public void addCardToHand(Card card) {
        hand.add(card);
    }

    public MonsterController putMonster(Monster monster, MonsterPosition position) throws MonsterNotFoundException, FullMonsterZone {
        if (!hand.contains(monster)) {
            throw new MonsterNotFoundException();
        }
        int lastEmpty = 0;
        while (lastEmpty < monstersZone.length && monstersZone[lastEmpty] != null) {
            lastEmpty++;
        }
        if (lastEmpty >= monstersZone.length) {
            throw new FullMonsterZone();
        }
        hand.remove(monster);
        monstersZone[lastEmpty] = MonsterController.getInstance(gameController, monster, position);
        return monstersZone[lastEmpty];
    }

    public void shuffleDeck() {
        Collections.shuffle(this.deck);
    }

    public SpellTrapController putSpellTrap(SpellTrap spellTrap, SpellTrapPosition position) throws SpellTrapNotFoundException, FullSpellTrapZone {
        if (!hand.contains(spellTrap)) {
            throw new SpellTrapNotFoundException();
        }
        int lastEmpty = 0;
        while (lastEmpty < spellTrapZone.length && spellTrapZone[lastEmpty] != null) {
            lastEmpty++;
        }
        if (lastEmpty >= spellTrapZone.length) {
            throw new FullSpellTrapZone();
        }
        hand.remove(spellTrap);
        spellTrapZone[lastEmpty] = SpellTrapController.getInstance(gameController, spellTrap, position);
        return spellTrapZone[lastEmpty];
    }

    public void removeMonster(int index) {
        if (monstersZone[index] == null)
            return;
        graveyard.add(monstersZone[index].getCard());
        monstersZone[index] = null;
    }

    public void removeMonster(MonsterController monster) {
        for (int i = 0; i < CARD_NUMBER_IN_ROW; i++) {
            if (monstersZone[i] == monster) {
                removeMonster(i);
                return;
            }
        }
    }

    public void removeAllMonsters(){
        for (int i = 0; i < CARD_NUMBER_IN_ROW; i++) {
                removeMonster(i);
        }
    }

    public void removeSpellTrap(int index) {
        if (spellTrapZone[index] == null)
            return;
        graveyard.add(spellTrapZone[index].getCard());
        spellTrapZone[index] = null;
    }

    public void standByPhase() {
        for (SpellTrapController spellTrap : this.spellTrapZone)
            spellTrap.standBy();
    }

    public int getMonsterZoneNumber() {
        int number = 0;
        for (int i = 0; i < CARD_NUMBER_IN_ROW; i++)
            if (monstersZone[i] != null)
                number++;

        return number;
    }

    public boolean canDirectAttack() {
        return true;
    }

    public int getSpellTrapZoneNumber() {
        int number = 0;
        for (int i = 0; i < CARD_NUMBER_IN_ROW; i++)
            if (spellTrapZone[i] != null)
                number++;

        return number;
    }

    public void putFiled(Spell spell) {
        Board otherBoard = getOtherBoard();
        otherBoard.fieldZone = null;
        this.fieldZone = SpellController.getInstance(gameController, spell, SpellTrapPosition.UP);
    }
}
