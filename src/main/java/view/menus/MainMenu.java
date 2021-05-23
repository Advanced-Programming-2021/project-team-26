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
        MAP.put(Pattern.compile("^\\s*user logout\\s*$"), UserController.getInstance()::logout);
        MAP.put(Pattern.compile("^\\s*menu show-current\\s*$"), i -> {
            return "Main menu";
        });
        MAP.put(Pattern.compile("^\\s*menu exit\\s*$"), Menu::exitMenu);
        MAP.put(Pattern.compile("^\\s*menu enter ([^ ]+)\\s*$"),MAIN_MENU_CONTROLLER::enterMenu );
        MAP.put(Pattern.compile("^\\s*duel --new --second-player ([^ ]+) --rounds(1|3)\\s*$"), MAIN_MENU_CONTROLLER :: createNewGameWithRealPlayer);
        MAP.put(Pattern.compile("^\\s*duel --new --ai --rounds(1|3)\\s*$"), MAIN_MENU_CONTROLLER :: creatNewGameWithAI );
    }

    public void execute() {
        System.out.println("______MAIN MENU______");
        run(MAP);
    }
}

