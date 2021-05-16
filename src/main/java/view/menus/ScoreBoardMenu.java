package view.menus;

import controller.UserController;
import view.ConsumerSp;
import view.Menu;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScoreBoardMenu extends Menu {
    private static final HashMap<Pattern, ConsumerSp<Matcher>> MAP = new HashMap<>();
    static {
        // TODO: 5/10/21 MAP.put(Pattern.compile("^scoreboard show$"), );
        MAP.put(Pattern.compile("^menu exit$"), Menu::exitMenu);
    }
    public ScoreBoardMenu() {
        super();
    }
    public void execute(){
        run(MAP);
    }
}
