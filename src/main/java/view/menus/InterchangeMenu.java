package view.menus;

import view.ConsumerSp;
import view.Menu;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InterchangeMenu extends Menu {
    private static final HashMap<Pattern, ConsumerSp<Matcher>> MAP = new HashMap<>();

    static {
        MAP.put(Pattern.compile("^import card ([^ ]+)$"), );
        MAP.put(Pattern.compile("^export card ([^ ]+)$"), );
        MAP.put(Pattern.compile("^\\s*menu exit\\s*$"), Menu::exitMenu);
        MAP.put(Pattern.compile("^\\s*menu show-current\\s*$"), i -> {
            return "import/export menu";
        });
    }
    public InterchangeMenu() {
        super();
    }

    public void execute() {
        System.out.println("______IMPORT/EXPORT MENU______");
        run(MAP);
    }

}
