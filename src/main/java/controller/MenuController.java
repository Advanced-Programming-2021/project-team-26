package controller;

import view.Menu;

public class MenuController {
    private static MenuController menuController;
    private Menu currentMenu;

    private MenuController(){

    }

    public static MenuController getInstance(){
        if (menuController == null)
            menuController = new MenuController();
        return menuController;
    }

    private void goToThisMenu(Menu theMenu){

    }
}
