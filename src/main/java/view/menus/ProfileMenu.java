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
    private static final UserController USER_CONTROLLER = UserController.getInstance();
     {
        super.MAP.put(Pattern.compile("^profile change --nickname (.+)"), USER_CONTROLLER::changeNickname);
        super.MAP.put(Pattern.compile("^profile change --password --current ([^ ]+) --new (.+)"),USER_CONTROLLER::changePassword);
            }
    public ProfileMenu(Menu menu) {
        super(menu);
        super.name = "ProfileMenu";
    }

    public void run(){
        super.run();
    }
}
