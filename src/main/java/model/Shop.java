package model;

public class Shop {
    private User user;
    private static final HashMap<String/*NAME*/, Integer/*PRICE*/> cardsPrice = new HashMap<>();
    private static Shop;

    private static Shop getInstance() {
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
