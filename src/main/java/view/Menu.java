package view;

import view.menus.*;

import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract public class Menu {
    protected static final Scanner scanner = Scan.getScanner();
    protected static String name;
    private static boolean mustBeStay;
    public Menu() {
        mustBeStay = true;
    }

    protected  static void exitMenu(Matcher matcher){
        mustBeStay = false;
    }
    protected static void showCurrentMenu(Matcher matcher){
        System.out.println(name);
    }

    protected void run(HashMap <Pattern, Consumer<Matcher>>MAP){
        while (mustBeStay) {
            boolean isMatched = false;
            String input = scanner.nextLine();
            Matcher matcher = null;
            for (Pattern pattern : MAP.keySet()) {
                matcher = pattern.matcher(input);
                if(matcher.find()) {
                    isMatched = true;
                    try {
                        MAP.get(pattern).accept(matcher);
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }finally {
                        break;
                    }
                }
            }
            if (!isMatched)
                System.out.println("invalid command");
        }
        mustBeStay = true;
    }

}
