package view.menus;

import controller.UserController;
import exceptions.ExceptionForPrint;
import view.Menu;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainMenu extends Menu {
    private static final Map<Pattern, Consumer<Matcher>> MAP = new HashMap<>();

    public MainMenu(Menu menu) {
        super(menu);
        super.name = "Main";
    }
    private void run(){
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

}
