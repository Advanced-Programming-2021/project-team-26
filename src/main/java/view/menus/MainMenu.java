package view.menus;

import controller.GameController;
import controller.MainMenuController;
import controller.UserController;
import view.Menu;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainMenu extends Menu {
    public  static final MainMenuController MAIN_MENU_CONTROLLER;
    private static final HashMap<Pattern, Consumer<Matcher>> MAP = new HashMap<>();
    static {
        MAIN_MENU_CONTROLLER = new MainMenuController();
        MAP.put(Pattern.compile("^user logout$"), UserController.getInstance()::logout);
        MAP.put(Pattern.compile("^menu show-current$"), Menu::showCurrentMenu);
        MAP.put(Pattern.compile("^menu exit$"), Menu::exitMenu);
        MAP.put(Pattern.compile("^menu enter ([^ ]+)$"),MainMenu::enterMenu );
        MAP.put(Pattern.compile("^duel --new --second-player ([^ ]+) --rounds(1|3)$"), MAIN_MENU_CONTROLLER :: createNewGameWithRealPlayer);
        MAP.put(Pattern.compile("^duel --new --ai --rounds(1|3)$"), MAIN_MENU_CONTROLLER :: creatNewGameWithAI );
    }

    private static void enterMenu(Matcher matcher){
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

    public MainMenu(Menu menu) {
        name = "Main";
    }
    public void execute() {
        run(MAP);
    }
}

