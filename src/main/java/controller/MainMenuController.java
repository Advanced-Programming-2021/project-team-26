package controller;
import exceptions.*;
import view.menus.*;

import java.util.regex.Matcher;

public class MainMenuController {
    public  MainMenuController(){

    }

    public void creatNewGameWithAI(Matcher matcher){

    }

    public void createNewGameWithRealPlayer(Matcher matcher){

    }

    public void enterMenu(Matcher matcher){
        String newMenu = matcher.group(1);
        if (newMenu.contains("Login"))
            new LoginMenu().execute();
        else if (newMenu.contains("Deck"))
            new DeckMenu().execute();
        else if (newMenu.contains("Export"))
            new InterchangeMenu().execute();
        else if (newMenu.contains("shop"))
            new ShopMenu().execute();
        else if (newMenu.contains("Profile"))
            new ProfileMenu().execute();
        else if (newMenu.contains("Score"))
            new ScoreBoardMenu().execute();
    }
}
