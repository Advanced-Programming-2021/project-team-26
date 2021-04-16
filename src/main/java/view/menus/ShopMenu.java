package view.menus;
import model.cards.Card;
import view.Menu;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class ShopMenu extends Menu {
    private static final Map<String , Method> REGEX;

    static {
        REGEX = new HashMap<>();
    }

    public ShopMenu(Menu menu) {
        super(menu);
    }

    public void run(){

    }
}
