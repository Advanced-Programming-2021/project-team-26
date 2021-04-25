package view.menus;

import controller.UserController;
import view.Menu;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileMenu extends Menu {
    private static final Map<Pattern, Consumer<Matcher>> MAP = new HashMap<>();
    private static UserController userController = UserController.getInstance();
    static {

        MAP.put(Pattern.compile("^profile change --nickname (.+)"), userController::changeNickname);
        MAP.put(Pattern.compile("^profile change --password --current ([^ ]+) --new (.+)"),userController::changePassword);
            }
    public ProfileMenu(Menu menu) {
        super(menu);
    }

    public void run(){

    }
}
