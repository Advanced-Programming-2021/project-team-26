package controller;

import model.User;

public class ShopController {
    private User user = new User();
    private String cardName;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public ShopController(User user, String cardName){
        setCardName(cardName);
        setUser(user);
    }

    public boolean isHaveEnoughMoney(int price, User user){
        return true;
    }

    public void showAll(){

    }

    public void buyCard(String cardName, User user){

    }
}
