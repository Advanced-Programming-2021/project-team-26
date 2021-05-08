package view;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract public class Menu {
    protected static final Scanner scanner = new Scanner(System.in);
    protected static String name;
    public Menu() {
    }

    private static Matcher getCommendMatcher(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find())
            return matcher;
        return null;
    }

    protected static void showCurrentMenu(Matcher matcher){
        System.out.println(name);
    }
}