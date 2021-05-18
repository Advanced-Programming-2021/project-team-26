package view.menus;

import controller.GameController;
import controller.MainMenuController;
import controller.UserController;
import view.ConsumerSp;
import view.Menu;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainMenu extends Menu {
    public  static final MainMenuController MAIN_MENU_CONTROLLER;
    private static final HashMap<Pattern, ConsumerSp<Matcher>> MAP = new HashMap<>();
    static {
        MAIN_MENU_CONTROLLER = new MainMenuController();
        MAP.put(Pattern.compile("^user logout$"), UserController.getInstance()::logout);
        MAP.put(Pattern.compile("^menu show-current$"), i -> {
            return "Main menu";
        });
        MAP.put(Pattern.compile("^menu exit$"), Menu::exitMenu);
        MAP.put(Pattern.compile("^menu enter ([^ ]+)$"),MAIN_MENU_CONTROLLER::enterMenu );
        MAP.put(Pattern.compile("^duel --new --second-player ([^ ]+) --rounds(1|3)$"), MAIN_MENU_CONTROLLER :: createNewGameWithRealPlayer);
        MAP.put(Pattern.compile("^duel --new --ai --rounds(1|3)$"), MAIN_MENU_CONTROLLER :: creatNewGameWithAI );
    }

    public MainMenu(Menu menu) {

    }
    public void execute() {
        run(MAP);
    }
}

