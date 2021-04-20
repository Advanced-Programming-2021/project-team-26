package model;

import model.cards.Card;

import java.util.ArrayList;
import java.util.HashMap;

public class Shop {
    private static HashMap<String/*NAME*/, Integer/*PRICE*/> cardsPrice = new HashMap<>();
    private static ArrayList<Card> allCards;
    private static Shop shop;

    static {
        for (Card card : allCards){
            cardsPrice.put(card.getName(), card.getPrice());
        }
    }

    private Shop() {

    }

    public static Shop getInstance() {
        if (shop == null)
            shop = new Shop();
        return shop;
    }

    public int getPriceByCardName(String cardName) {
        for (Card card  : allCards){
            if (card.getName().equals(cardName))
                return card.getPrice();
        }
        return 0;
    }

    public boolean checkCardNameExistence(String cardName) {
        for (Card card  : allCards){
            if (card.getName().equals(cardName))
                return true;
        }
        return false;
    }

}
