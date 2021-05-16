package view;

import view.menus.*;

import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import view.ConsumerSp;

abstract public class Menu  {
    protected static final Scanner scanner = Scan.getScanner();
    private static boolean mustBeStay;
    public Menu() {
        mustBeStay = true;
    }

    protected  static String exitMenu(Matcher matcher){
        mustBeStay = false;
        return null;
    }
    protected String showCurrentMenu(String currentMenu){
        return currentMenu;
    }
    protected void run(HashMap <Pattern, ConsumerSp<Matcher>>MAP){
        while (mustBeStay) {
            boolean isMatched = false;
            String input = scanner.nextLine();
            Matcher matcher = null;
            for (Pattern pattern : MAP.keySet()) {
                matcher = pattern.matcher(input);
                if(matcher.find()) {
                    isMatched = true;
                    try {
                        String msg = MAP.get(pattern).accept(matcher);
                        if( msg != null){
                            System.out.println(msg);
                        }
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
