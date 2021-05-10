package view.menus;

import controller.UserController;
import view.Menu;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginMenu extends Menu {
    private static final HashMap<Pattern, Consumer<Matcher>> MAP = new HashMap<>();
    private static UserController userController = UserController.getInstance();
   static {
        MAP.put(Pattern.compile("^menu show-current$"), Menu::showCurrentMenu);
        MAP.put(Pattern.compile("^user creat --([^ ]+) ([^ ]+) --([^ ]+) ([^ ]+) --([^ ]+) ([^ ]+)$"), userController::addNewUser);
        MAP.put(Pattern.compile("^user login --([^ ]+) ([^ ]+) --([^ ]+) ([^ ]+)$"), userController::loginUser);
        MAP.put(Pattern.compile("^user logout$"), userController::logout);
    }
    public LoginMenu() {
       name = "LoginMenu";
    }

    private void execute() {
        run(MAP);
    }
}
