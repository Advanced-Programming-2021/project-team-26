package view.menus;

import view.Menu;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MainMenu extends Menu {
    private static final Map<String , Method> REGEX;

    static {
        REGEX = new HashMap<>();
    }
    public MainMenu(Menu menu) {
        super(menu);
        super.name = "Main";
    }

    public void run(){
    }

}
