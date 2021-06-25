package model;

import controller.Database;
import model.cards.Card;
import model.cards.SpellTrap;
import model.cards.monster.Monster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class Deck {

    private String name;
    private String deckOwnerUsername;
    private ArrayList<String> mainDeck;
    private ArrayList<String> sideDeck;
    private ArrayList<String> allCards;

    public Deck(String name, String deckOwnerUsername) {
        setName(name);
        setDeckOwner(deckOwnerUsername);
        setMainDeck(new ArrayList<>());
        setSideDeck(new ArrayList<>());
        setAllCards(new ArrayList<>());
    }

    public int getNumberOfCardINDeck(String cardName) {
        int count = 0;

        for (String cardName1 : allCards) {
            if (cardName1.equals(cardName))
                count++;
        }

        return count;
    }

    public boolean doesCardExistInMainDeck(String cardName) {
        for (String cardName1 : mainDeck) {
            if (cardName1.equals(cardName))
                return true;
        }
        return false;
    }

    public boolean doesCardExistInSideDeck(String cardName) {
        for (String cardName1 : sideDeck) {
            if (cardName1.equals(cardName))
                return true;
        }
        return false;
    }


    public void setDeckOwner(String deckOwnerUsername) {
        this.deckOwnerUsername = deckOwnerUsername;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeckOwnerUsername() {
        return deckOwnerUsername;
    }

    public void setDeckOwnerUsername(String deckOwnerUsername) {
        this.deckOwnerUsername = deckOwnerUsername;
    }

    public ArrayList<String> getMainDeck() {
        return mainDeck;
    }

    public void setMainDeck(ArrayList<String> mainDeck) {
        this.mainDeck = mainDeck;
    }

    public ArrayList<String> getSideDeck() {
        return sideDeck;
    }

    public void setSideDeck(ArrayList<String> sideDeck) {
        this.sideDeck = sideDeck;
    }

    public ArrayList<String> getAllCards() {
        return allCards;
    }

    public void setAllCards(ArrayList<String> allCards) {
        this.allCards = allCards;
    }

    public void addCardToMainDeck(Card card) {
        mainDeck.add(card.getName());
        allCards.add(card.getName());
        if (deckOwnerUsername != null)
            Database.getInstance().writeUser(Objects.requireNonNull(User.getUserByUsername(deckOwnerUsername)));
    }

    public void addCardToSideDeck(Card card) {
        sideDeck.add(card.getName());
        allCards.add(card.getName());
        if (deckOwnerUsername != null)
            Database.getInstance().writeUser(User.getUserByUsername(deckOwnerUsername));
    }

    public boolean IsNumberOfTheCardInDeckValid(String cardName) {
        int count = 0;

        for (String cardName1 : allCards) {
            if (cardName1.equals(cardName))
                count++;
        }
        return count < 3;
    }

    public void deleteCardFromMainDeck(Card card) {
        for (String cardName1 : mainDeck) {
            if (cardName1.equals(card.getName())) {
                mainDeck.remove(cardName1);
                break;
            }
        }

        for (String cardName1 : allCards) {
            if (cardName1.equals(card.getName())) {
                allCards.remove(cardName1);
                break;
            }
        }
    }

    public void deleteCardFromSideDeck(Card card) {
        for (String cardName1 : sideDeck) {
            if (cardName1.equals(card.getName())) {
                sideDeck.remove(cardName1);
                break;
            }
        }

        for (String cardName1 : allCards) {
            if (cardName1.equals(card.getName())) {
                allCards.remove(cardName1);
                break;
            }
        }
    }

    public boolean isDeckValid() {
        return mainDeck.size() >= 40;
    }

    public String showDeck(String deckType) {
        StringBuilder stringToReturn = new StringBuilder();

        HashMap<String, Monster> monsters = new HashMap<>();
        HashMap<String, SpellTrap> spellTraps = new HashMap<>();

        if (deckType.equals("main")) {
            for (String cardName : mainDeck) {
                if (Card.getCard(cardName) instanceof Monster)
                    monsters.put(cardName, (Monster) Card.getCard(cardName));
            }

            for (String cardName : mainDeck) {
                if (Card.getCard(cardName) instanceof SpellTrap)
                    spellTraps.put(cardName, (SpellTrap) Card.getCard(cardName));
            }
        } else {
            for (String cardName : sideDeck) {
                if (Card.getCard(cardName) instanceof Monster)
                    monsters.put(cardName, (Monster) Card.getCard(cardName));
            }

            for (String cardName : sideDeck) {
                if (Card.getCard(cardName) instanceof SpellTrap)
                    spellTraps.put(cardName, (SpellTrap) Card.getCard(cardName));
            }
        }

        stringToReturn.append("Deck: ").append(getName()).append("\n");

        if (deckType.equals("main"))
            stringToReturn.append("Main deck:").append("\n");
        else
            stringToReturn.append("Side deck:").append("\n");

        stringToReturn.append("Monsters:").append("\n");
        ArrayList<String> sortedMonsters = new ArrayList<>(monsters.keySet());
        Collections.sort(sortedMonsters);
        for (String name : sortedMonsters) {
            stringToReturn.append(name).append(": ").append(monsters.get(name).getDescription()).append("\n");
        }

        stringToReturn.append("Spell and Traps:").append("\n");
        ArrayList<String> sortedSpellTraps = new ArrayList<>(spellTraps.keySet());
        Collections.sort(sortedSpellTraps);
        for (String name : sortedSpellTraps) {
            stringToReturn.append(name).append(": ").append(spellTraps.get(name).getDescription()).append("\n");
        }
        return stringToReturn.toString();
    }

    public Object clone() {
        Deck cloned = new Deck(this.name, this.deckOwnerUsername);
        cloned.name = this.name;
        cloned.deckOwnerUsername = this.deckOwnerUsername;
        cloned.mainDeck = (ArrayList<String>) this.mainDeck.clone();
        cloned.sideDeck = (ArrayList<String>) this.sideDeck.clone();
        cloned.allCards = (ArrayList<String>) this.allCards.clone();
        return cloned;
    }
}
