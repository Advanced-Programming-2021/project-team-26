package view.menus;

import view.Menu;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DuelMenu extends Menu {
    private static final Map<String , Method> REGEX;

    static {
        REGEX = new HashMap<>();
    }
    public DuelMenu(Menu menu) {
        super(menu);
    }

    public void run(){

    }
}
