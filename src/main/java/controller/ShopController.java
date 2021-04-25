package controller;

import model.Shop;
import model.User;

import java.util.regex.Matcher;

public class ShopController {
    private User user;
    private Shop shop;

    public ShopController(User user) {
        setUser(user);
    }

    public void buyCard(Matcher matcher){

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

//    public String getCardName() {
//        return cardName;
//    }

//    public void setCardName(String cardName) {
//        this.cardName = cardName;
//    }

    public boolean isHaveEnoughMoney(int price, User user) {
        return true;
    }

    public void showAll(Matcher matcher) {

    }
}
