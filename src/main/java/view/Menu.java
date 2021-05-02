package view;

import exceptions.ExceptionForPrint;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract public class Menu {
    public static Scanner scanner;
    protected Map<Pattern, Consumer<Matcher>> MAP = new HashMap<>();
    private HashMap<MenuType, Menu> subMenus;
    protected String name;

    public Menu(Menu menu) {
    }

    private Matcher getCommendMatcher(String input, String regex) {
        return null;
    }
    protected void run(){
        while (true) {
            String input = scanner.nextLine();
            Matcher matcher = null;
            for (Pattern pattern : MAP.keySet()) {
                matcher = pattern.matcher(input);
                if(matcher != null) {
                    try {
                        MAP.get(pattern).accept(matcher);
                    }catch (ExceptionForPrint e){
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }

    protected void showCurrentMenu(Matcher matcher){
        System.out.println(this.name);
    }
}