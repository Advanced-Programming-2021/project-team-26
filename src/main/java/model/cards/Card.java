package model.cards;

import controller.Database;

import java.util.Map;

public class Card {
    private static final Map<String, Card> allCards;

    static {
        allCards = Database.getInstance().getAllCards();
    }

    protected String name;
    protected String Description;
    protected int price;

    //copy constructor
    public Card(Card o) {
        setName(o.name);
        setDescription(o.Description);
        setPrice(o.price);
    }

    public Card(String name, String description, int price) {
        setName(name);
        setDescription(description);
        setPrice(price);
    }

    public static boolean checkCardNameExistence(String cardName) {
        return allCards.containsKey(cardName);
    }

    public static Card getCard(String name) {
        if (allCards.containsKey(name))
            return allCards.get(name);
        return null;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
