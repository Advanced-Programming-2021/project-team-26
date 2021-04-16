package view.menus;

import view.Menu;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DeckMenu extends Menu {
    private static final Map<String , Method> REGEX;

    static {
        REGEX = new HashMap<>();
    }
    public DeckMenu(Menu menu) {
        super(menu);
    }

    public void run(){

    }

}