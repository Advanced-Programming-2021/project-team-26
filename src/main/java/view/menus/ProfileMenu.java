package view.menus;

import controller.UserController;
import exceptions.ExceptionForPrint;
import view.Menu;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileMenu extends Menu {
    private static final UserController USER_CONTROLLER = UserController.getInstance();
    private static final Map<Pattern, Consumer<Matcher>> MAP = new HashMap<>();
    static {
        MAP.put(Pattern.compile("^profile change --nickname (.+)"), USER_CONTROLLER::changeNickname);
        MAP.put(Pattern.compile("^profile change --password --current ([^ ]+) --new (.+)"),USER_CONTROLLER::changePassword);
            }
    public ProfileMenu(Menu menu) {
        super(menu);
        super.name = "ProfileMenu";
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
