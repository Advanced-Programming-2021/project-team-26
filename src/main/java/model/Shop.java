package model;

import model.cards.Card;

import java.util.ArrayList;
import java.util.HashMap;

public class Shop {
    private static HashMap<String/*NAME*/, Integer/*PRICE*/> cardsPrice = new HashMap<>();
    private static ArrayList<Card> allCards;
    private static Shop shop;

    static {
        //TODO
        //set card names and their price
    }

    public Shop() {

    }

    private Shop getInstance() {
        if (shop == null)
            shop = new Shop();
        return shop;
    }

    public int getPriceByCardName(String cardName) {
        //TODO
        return 0;
    }

    public boolean isExistThisCardName(String cardName) {
        return true;
    }

}
