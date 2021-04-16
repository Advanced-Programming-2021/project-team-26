package model;

import java.util.HashMap;

public class Shop {
    private static HashMap<String/*NAME*/, Integer/*PRICE*/> cardsPrice = new HashMap<>();
    private static Shop shop;

    public  Shop(){

    }

    private Shop getInstance() {
        if (shop == null)
            shop = new Shop();
        return shop;
    }

    static {
        //TODO
        //set card names and their price
    }

    public int getPriceByCardName(String cardName) {
        //TODO
        return 0;
    }

    public boolean isExistThisCardName(String cardName) {
        return true;
    }

}
