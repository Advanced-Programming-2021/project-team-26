package view.menus;
import view.Menu;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import controller.GameController;

public class DuelMenu extends Menu {
    private static final HashMap<Pattern, Consumer<Matcher>> MAP = new HashMap<>();
    public DuelMenu(Menu menu) {
        name = "DuelMenu";
    }

    private void execute() {
        run(MAP);
    }

}
