package view.menus;

import view.Menu;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InterchangeMenu extends Menu {
    private static final HashMap<Pattern, Consumer<Matcher>> MAP = new HashMap<>();

    static {
        // TODO: 5/10/21   MAP.put(Pattern.compile(""), );
        // TODO: 5/10/21   MAP.put(Pattern.compile(""), );
        MAP.put(Pattern.compile("^menu exit$"), Menu::exitMenu);
    }
    public InterchangeMenu() {
        super();
    }

    public void execute() {
        run(MAP);
    }

}
