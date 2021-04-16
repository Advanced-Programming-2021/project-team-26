package model;

public class Shop {
    private static HashMap<String/*NAME*/, Integer/*PRICE*/> cardsPrice = new HashMap<>();
    private static Shop;

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

    }

    public boolean isExistThisCardName(String cardName) {
        return true;
    }

}
