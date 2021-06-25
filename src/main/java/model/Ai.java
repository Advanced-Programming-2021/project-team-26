package model;

import controller.*;
import model.cards.Card;
import model.cards.monster.Monster;
import model.cards.spell.Spell;
import model.cards.trap.Trap;

import java.util.ArrayList;
import java.util.Random;

public class Ai extends User {
    private final GameController gameController;
    private final int myTurn;
    private final int monsterNumberInDeck = 35;
    private final int spellNumberInDeck = 10;
    private Board myBoard;
    private Board opponentBoard;
    private final String[] selectedSpells = {"Pot of Greed", "Raigeki", "Harpie's Feather Duster",
            "Dark Hole", "Yami", "Forest", "Closed Forest", "UMIIRUKA", "Sword of Dark Destruction"};

    public Ai(GameController gameController, int myTurn) {
        this.username = "AI";
        this.nickname = "AI";
        this.money = Integer.MAX_VALUE;
        this.gameController = gameController;
        this.myTurn = myTurn;
        createDeck();
    }

    public void init() {
        this.myBoard = gameController.getGame().getBoard(myTurn);
        this.opponentBoard = gameController.getGame().getBoard(1 - myTurn);
    }

    private void createDeck() {
        Deck deck = new Deck("aiDeck", null);
        for (Card card : getMainCards()) {
            deck.addCardToMainDeck(card);
        }
        for (Card card : getSideCards()) {
            deck.addCardToSideDeck(card);
        }
        allDecks.put(deck.getName(), deck);
        activeDeckName = deck.getName();
    }

    private ArrayList<Card> getSideCards() {
        return new ArrayList<>();
    }

    private ArrayList<Card> getMainCards() {
        ArrayList<Card> allMonster = new ArrayList<>(Monster.getAllMonsters().values());
        ArrayList<Card> selectedCard = new ArrayList<>();
        while (selectedCard.size() < monsterNumberInDeck) {
            int random = new Random().nextInt(allMonster.size());
            Monster randomCard = (Monster) allMonster.get(random);
            if (randomCard.getLevel() > 4) continue;
            if (countCards(selectedCard, randomCard) < 3) {
                selectedCard.add(randomCard);
            }
        }
        while (selectedCard.size() < monsterNumberInDeck + spellNumberInDeck) {
            int random = new Random().nextInt(selectedSpells.length);
            Card randomCard = Card.getCard(selectedSpells[random]);
            if (randomCard == null)
                continue;
            if (countCards(selectedCard, randomCard) < 3) {
                selectedCard.add(randomCard);
            }
        }
        return selectedCard;
    }

    private int countCards(ArrayList<Card> list, Card targetCard) {
        int count = 0;
        for (Card card : list) {
            if (card.equals(targetCard))
                count++;
        }
        return count;
    }


    public void mainPhase1() {
        ArrayList<Monster> monsters = new ArrayList<>();
        ArrayList<Spell> spells = new ArrayList<>();
        ArrayList<Trap> traps = new ArrayList<>();
        for (Card card : myBoard.getHand()) {
            if (card instanceof Monster)
                monsters.add((Monster) card);
            else if (card instanceof Spell)
                spells.add((Spell) card);
            else if (card instanceof Trap)
                traps.add((Trap) card);
        }

        Monster bestMonster = chooseBestMonster(monsters);
        if (bestMonster != null) {
            MonsterPosition bestPosition = monsterBestPosition(bestMonster);
            myBoard.putMonster(bestMonster, bestPosition);
        }
        activateBestSpells(spells);
        activateBestTraps(traps);
    }

    private void activateBestSpells(ArrayList<Spell> spells) {
        if (!spells.isEmpty()) {
            for (Spell spell : spells) {
                if (shouldActivateSpell(spell)) {
                    SpellTrapPosition bestPosition = spellBestPosition(spell);
                    SpellTrapController controller = myBoard.putSpellTrap(spell, bestPosition);
                    if (bestPosition == SpellTrapPosition.UP)
                        controller.activate();
                }
            }
        }
    }

    private SpellTrapPosition trapBestPosition(Trap trap) {
        if (new Random().nextInt(5) == 0)
            return SpellTrapPosition.DOWN;
        else
            return SpellTrapPosition.UP;
    }

    private boolean shouldActivateTrap(Trap trap) {
        return new Random().nextInt(3) != 0;
    }

    private SpellTrapPosition spellBestPosition(Spell spell) {
        if (new Random().nextInt(5) == 0)
            return SpellTrapPosition.DOWN;
        else
            return SpellTrapPosition.UP;
    }

    private boolean shouldActivateSpell(Spell spell) {
        return new Random().nextInt(3) != 0;
    }

    private MonsterPosition monsterBestPosition(Monster monster) {
        if (monster.getAttackPower() > monster.getDefencePower())
            return MonsterPosition.ATTACK;
        else
            return MonsterPosition.DEFENCE_DOWN;
    }

    private Monster chooseBestMonster(ArrayList<Monster> monsters) {
        int maxPower = Integer.MIN_VALUE;
        Monster selectedMonster = null;

        for (Monster monster : monsters) {
            if (monster.getAttackPower() > maxPower) {
                maxPower = monster.getAttackPower();
                selectedMonster = monster;
            }
            if (monster.getDefencePower() > maxPower) {
                maxPower = monster.getDefencePower();
                selectedMonster = monster;
            }
        }
        return selectedMonster;
    }

    public void mainPhase2() {
        ArrayList<Spell> spells = new ArrayList<>();
        ArrayList<Trap> traps = new ArrayList<>();
        for (Card card : myBoard.getHand()) {
            if (card instanceof Spell)
                spells.add((Spell) card);
            else if (card instanceof Trap)
                traps.add((Trap) card);
        }
        activateBestSpells(spells);
        activateBestTraps(traps);
    }

    private void activateBestTraps(ArrayList<Trap> traps) {
        if (!traps.isEmpty()) {
            for (Trap trap : traps) {
                if (shouldActivateTrap(trap)) {
                    SpellTrapPosition bestPosition = trapBestPosition(trap);
                    SpellTrapController controller = myBoard.putSpellTrap(trap, bestPosition);
                    if (bestPosition == SpellTrapPosition.UP)
                        controller.activate();
                }
            }
        }
    }

    public void battlePhase() {
        for (MonsterController monster : myBoard.getMonstersZone()) {
            if (monster == null || monster.getPosition() != MonsterPosition.ATTACK)
                continue;
            MonsterController bestTarget = bestTarget(monster);
            if (bestTarget == null) {
                if (opponentBoard.getMonsterZoneNumber() == 0)
                    directAttack(monster);
            } else
                bestTarget.attack(monster);
        }
    }

    private void directAttack(MonsterController monster) {
        int damage = monster.getCard().getAttackPower();
        gameController.getGame().decreaseOtherLifePoint(damage);
    }

    private MonsterController bestTarget(MonsterController monster) {
        int bestDistance = 0;
        MonsterController best = null;
        for (MonsterController opponentMonster : opponentBoard.getMonstersZone()) {
            if (opponentMonster == null)
                continue;
            int distance;
            if (opponentMonster.getPosition() == MonsterPosition.ATTACK)
                distance = monster.getMonster().getAttackPower() - opponentMonster.getMonster().getAttackPower();
            else
                distance = monster.getMonster().getAttackPower() - opponentMonster.getMonster().getDefencePower();

            if (distance > bestDistance) {
                bestDistance = distance;
                best = opponentMonster;
            }
        }
        return best;
    }
}
