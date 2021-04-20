package view.menus;

import view.Menu;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MainMenu extends Menu {
    private static final Map<Pattern, Method> PATTERNS;

    static {
        PATTERNS = new HashMap<>();
    }
    public MainMenu(Menu menu) {
        super(menu);
        super.name = "Main";
    }

    public void run(){
    }

}
