package model;

import controller.MonsterController;
import controller.SpellTrapController;
import model.cards.Card;

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
    private Card FieldZone;

    public Board(List<Card> deck) {
        initDeck(deck);
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

    public Card getFieldZone() {
        return FieldZone;
    }

    private void initDeck(List<Card> deck) {
        this.deck = new ArrayList<>(deck);
        Collections.shuffle(this.deck);
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
        FieldZone = null;
    }

    public void addCardToHand() {
        Card addedCard = this.deck.remove(0);
        hand.add(addedCard);
    }
}
