package view;

import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract public class Menu {
    public static Scanner scanner;
    private HashMap<MenuType, Menu> subMenus;
    protected String name;

    public Menu(Menu menu) {
    }

    private Matcher getCommendMatcher(String input, String regex) {
        return null;
    }
    protected void run(HashMap<Pattern, Consumer<Matcher>> MAP){
        while (true) {
            String input = scanner.nextLine();
            Matcher matcher = null;
            for (Pattern pattern : MAP.keySet()) {
                matcher = pattern.matcher(input);
                if(matcher != null) {
                    try {
                        MAP.get(pattern).accept(matcher);
                    }catch (ExeptionForPrint e){
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }

//    protected void showCurrentMenu(){
//
//    }
}