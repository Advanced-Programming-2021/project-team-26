package model;

import controller.Database;
import model.cards.Card;

import java.util.HashMap;

public class Shop {
    private static final HashMap<String, Card> allCards;
    private static Shop shop;

    static {
        allCards = new HashMap<>();
    }

    private Shop() {
        allCards.putAll(Database.getInstance().getAllCards());
    }

    public static Shop getInstance() {
        if (shop == null)
            shop = new Shop();
        return shop;
    }

    public static HashMap<String, Card> getAllCards() {
        return allCards;
    }

    public int getPriceByCardName(String cardName) {
        for (String key : allCards.keySet()) {
            if (key.equals(cardName)) {
                return allCards.get(key).getPrice();
            }
        }
        return 0;
    }

    public boolean checkCardNameExistence(String cardName) {
        for (String key : allCards.keySet()) {
            if (key.equals(cardName)) {
                return true;
            }
        }
        return false;
    }
}
