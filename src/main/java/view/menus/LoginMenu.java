package view.menus;

import view.Menu;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LoginMenu extends Menu {
    private static  UserController userController = UserController.getInstance();
    {
        super.MAP.put(Pattern.compile("^menu show-current$"), super::showCurrentMenu);
        super.MAP.put(Pattern.compile("^user creat --([^ ]+) ([^ ]+) --([^ ]+) ([^ ]+) --([^ ]+) ([^ ]+)$"), userController::addNewUser);
        super.MAP.put(Pattern.compile("^user login --([^ ]+) ([^ ]+) --([^ ]+) ([^ ]+)$"), userController::loginUser);
        super.MAP.put(Pattern.compile("^user logout$"), userController::logout);
    }
    public LoginMenu(Menu menu) {
        super(menu);
        super.name = "LoginMenu";
    }

    public void run(){

    }

}
