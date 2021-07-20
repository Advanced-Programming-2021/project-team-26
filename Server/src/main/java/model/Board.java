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

import java.util.*;

public class Board {
    public final static int CARD_NUMBER_IN_ROW = 5;
    public final static int INITIAL_CARDS = 5;
    private List<Card> deck;
    private HashMap<Integer, MonsterController> monstersZone;
    private HashMap<Integer, SpellTrapController> spellTrapZone;
    private List<Card> graveyard;
    private List<Card> hand;
    private SpellController fieldZone;
    private GameController gameController;
    private int myTurn = -1;

    public Board(GameController gameController, Deck deck) {
        initDeck(deck);
        this.gameController = gameController;
    }

    public void initBoard() {
        initMonstersZone();
        initSpellTrapZone();
        initGraveyard();
        initHand();
        initFieldZone();
    }

    public int getMonsterZoneLastEmpty() {
        return monstersZone.size();
    }

    public int getSpellTrapZoneLastEmpty() {
        return spellTrapZone.size();
    }

    public HashMap<Integer, MonsterController> getMonsterZoneMap() {
        return monstersZone;
    }

    public HashMap<Integer, SpellTrapController> getSpellTrapZoneMap() {
        return spellTrapZone;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public Collection<MonsterController> getMonstersZone() {
        return monstersZone.values();
    }

    public MonsterController getMonsterByIndex(int index) {
        if (monstersZone.containsKey(index))
            return monstersZone.get(index);
        return null;
    }

    public Collection<SpellTrapController> getSpellTrapZone() {
        return spellTrapZone.values();
    }

    public SpellTrapController getSpellTrapByIndex(int index) {
        if (spellTrapZone.containsKey(index))
            return spellTrapZone.get(index);
        return null;
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
        ArrayList<String> mainDeck = deck.getMainDeck();
        this.deck = new ArrayList<>();

        for (String cardName : mainDeck) {
            this.deck.add(Card.getCard(cardName));
        }

        Collections.shuffle(this.deck);
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        if(monstersZone!=null) {
            for (MonsterController monsterController : getMonstersZone())
                if (monsterController != null)
                    monsterController.setGameController(gameController);
        }
        if(spellTrapZone!=null) {
            for (SpellTrapController spellTrapController : getSpellTrapZone())
                if (spellTrapController != null)
                    spellTrapController.setGameController(gameController);
        }
        if(fieldZone!=null)
            fieldZone.setGameController(gameController);
    }

    private void initMonstersZone() {
        this.monstersZone = new HashMap<>();
    }

    private void initSpellTrapZone() {
        this.spellTrapZone = new HashMap<>();
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

    private int getMyTurn() {
        if (myTurn != -1)
            return myTurn;
        if (gameController.getGame().getBoard(0) == this)
            myTurn = 0;
        else
            myTurn = 1;
        return myTurn;
    }

    public Card addCardToHand() {
        if (this.deck.size() == 0)
            return null;
        Card addedCard = this.deck.remove(0);
        hand.add(addedCard);
        gameController.getViews()[getMyTurn()].moveFromDeckToHand(addedCard);
        gameController.getViews()[1 - getMyTurn()].moveFromOpponentDeckToHand(addedCard);
        return addedCard;
    }

    public void addCardToHand(Card card) {
        hand.add(card);
        gameController.getViews()[getMyTurn()].moveFromDeckToHand(card);
        gameController.getViews()[1 - getMyTurn()].moveFromOpponentDeckToHand(card);
    }

    public MonsterController putMonster(Monster monster, MonsterPosition position) throws MonsterNotFoundException, FullMonsterZone {
        if (!hand.contains(monster)) {
            throw new MonsterNotFoundException();
        }
        int lastEmpty = 0;
        while (lastEmpty < CARD_NUMBER_IN_ROW && monstersZone.containsKey(lastEmpty)) {
            lastEmpty++;
        }
        if (lastEmpty >= CARD_NUMBER_IN_ROW) {
            throw new FullMonsterZone();
        }
        hand.remove(monster);
        CardAddress monsterAddress = new CardAddress(Place.MonsterZone, Owner.Me, lastEmpty);
        monstersZone.put(lastEmpty, MonsterController.getInstance(gameController, monster, position, monsterAddress));
        return monstersZone.get(lastEmpty);
    }

    public void shuffleDeck() {
        Collections.shuffle(this.deck);
    }

    public SpellTrapController putSpellTrap(SpellTrap spellTrap, SpellTrapPosition position) throws SpellTrapNotFoundException, FullSpellTrapZone {
        if (!hand.contains(spellTrap)) {
            throw new SpellTrapNotFoundException();
        }
        int lastEmpty = 0;
        while (lastEmpty < CARD_NUMBER_IN_ROW && spellTrapZone.containsKey(lastEmpty)) {
            lastEmpty++;
        }
        if (lastEmpty >= CARD_NUMBER_IN_ROW) {
            throw new FullSpellTrapZone();
        }
        hand.remove(spellTrap);
        spellTrapZone.put(lastEmpty, SpellTrapController.getInstance(gameController, spellTrap, position, getMyTurn()));
        return spellTrapZone.get(lastEmpty);
    }

    public void removeMonster(int index) {
        if (!monstersZone.containsKey(index))
            return;
        graveyard.add(monstersZone.get(index).getCard());
        gameController.getViews()[getMyTurn()].updateMyGraveyard(monstersZone.get(index).getCard());
        gameController.getViews()[1 - getMyTurn()].updateOppGraveyard(monstersZone.get(index).getCard());
        monstersZone.remove(index);
    }

    public void removeMonsterWithoutAddingToGraveyard(Monster monster) {
        for (Integer index : monstersZone.keySet()) {
            if (monstersZone.get(index).getMonster().equals(monster)) {
                monstersZone.remove(index);
                return;
            }
        }
    }

    public void removeMonster(MonsterController monster) {
        for (Integer index : monstersZone.keySet()) {
            if (monstersZone.get(index) == monster) {
                removeMonster(index);
                return;
            }
        }
    }

    public int getNumberOfMonstersINGraveyard() {
        int count = 0;
        for (Card card : graveyard) {
            if (card instanceof Monster)
                count++;
        }

        return count;
    }

    public void removeAllMonsters() {
        monstersZone.clear();
    }

    public void removeSpellTrap(int index) {
        if (!spellTrapZone.containsKey(index))
            return;
        graveyard.add(spellTrapZone.get(index).getCard());
        gameController.getViews()[getMyTurn()].updateMyGraveyard(spellTrapZone.get(index).getCard());
        gameController.getViews()[1 - getMyTurn()].updateOppGraveyard(spellTrapZone.get(index).getCard());
        spellTrapZone.remove(index);
    }

    public void removeSpellTrap(SpellTrapController spellTrap) {
        for (Integer index : spellTrapZone.keySet()) {
            if (spellTrapZone.get(index) == spellTrap) {
                removeSpellTrap(index);
                return;
            }
        }
    }

    public void removeAllSpellTraps() {
        spellTrapZone.clear();
    }

    public void standByPhase() {
        for (SpellTrapController spellTrap : this.spellTrapZone.values()) {
            spellTrap.standBy();
        }
    }

    public int getMonsterZoneNumber() {
        return monstersZone.size();
    }

    public boolean canDirectAttack() {
        return true;
    }

    public int getSpellTrapZoneNumber() {
        return spellTrapZone.size();
    }

    public SpellController putFiled(Spell spell) {
        Board otherBoard = getOtherBoard();
        otherBoard.fieldZone = null;
        this.fieldZone = SpellController.getInstance(gameController, spell, SpellTrapPosition.UP);
        hand.remove(spell);
        gameController.getViews()[1 - gameController.getGame().getTurn()].updateFieldImage(spell);
        gameController.getViews()[gameController.getGame().getTurn()].updateFieldImage(spell);
        return this.fieldZone;
    }

    public Response handle(Request request) {
        switch (request.getParameter("function")){
            case "getMonsterZoneMap":
                HashMap<Integer,MonsterTransfer> map = new HashMap<>();
                monstersZone.forEach((i,monster) -> {
                    map.put(i,new MonsterTransfer(monster.getMonster(),monster.getPosition()));
                });
                Response response = new Response(true,"");
                response.addData("MonsterZoneMap",map);
                return response;
        }
        return new Response(false,"method not found");
    }
}