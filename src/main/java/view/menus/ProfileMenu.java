package view.menus;

import controller.UserController;
import view.ConsumerSp;
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
    private static final HashMap<Pattern, ConsumerSp<Matcher>> MAP = new HashMap<>();
    static {
        MAP.put(Pattern.compile("^profile change --nickname (.+)"), USER_CONTROLLER::changeNickname);
        MAP.put(Pattern.compile("^profile change --password --current ([^ ]+) --new (.+)"),USER_CONTROLLER::changePassword);
        MAP.put(Pattern.compile("^menu exit$"), Menu::exitMenu);

    }
    public ProfileMenu() {

    }
    public void execute() {
        run(MAP);
    }
}
