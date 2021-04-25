package view.menus;

import view.Menu;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class DuelMenu extends Menu {
    private static final Map<Pattern, Method> PATTERNS;

    static {
        PATTERNS = new HashMap<>();
    }
    public DuelMenu(Menu menu) {
        super(menu);
        super.name = "DuelMenu";
    }

    public void run(){

    }
}
