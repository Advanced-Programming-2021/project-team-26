package view;

import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;

abstract public class Menu {
    public static Scanner scanner;
    private HashMap<MenuType, Menu> subMenus;

    public Menu(Menu menu) {
    }

    private Matcher getCommendMatcher(String input, String regex) {
        return null;
    }

    abstract protected void run();
}