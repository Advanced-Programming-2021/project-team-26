package controller;

import model.User;

public class ShopController {
    private static ShopController shopController;
    public ShopController(){

    }

    private static ShopController getInstance(){
        if (shopController == null)
            shopController = new ShopController();
        else
            return shopController;
    }

    public boolean isHaveEnoughMoney(int price, User user){
        return true;
    }

    public void showAll(){

    }

    public void buyCard(String cardName, User user){

    }


}
