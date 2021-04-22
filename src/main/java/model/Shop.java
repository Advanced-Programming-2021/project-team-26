package model;

import model.cards.Card;

import java.util.ArrayList;
import java.util.HashMap;

public class Shop {
    private static ArrayList<Card> allCards;
    private static Shop shop;

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
