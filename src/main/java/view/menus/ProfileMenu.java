package view.menus;

import view.Menu;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ProfileMenu extends Menu {
    private static final Map<Pattern, Method> PATTERNS;

    static {
        PATTERNS = new HashMap<>();
    }
    public ProfileMenu(Menu menu) {
        super(menu);
    }

    public void run(){

    }
}
