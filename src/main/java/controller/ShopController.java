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


    public boolean isExistThisCardName(String name){
        return true;
    }

    public boolean isHaveEnoughMoney(User user){
        return true;
    }

    public void showAll(){

    }

    public void buyCard(String cardName, User user){

    }


}
