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

public class LoginMenu extends Menu {
    private static final Map<Pattern, Consumer<Matcher>> MAP = new HashMap<>();
    private static UserController userController = UserController.getInstance();
   static {
        MAP.put(Pattern.compile("^menu show-current$"), Menu::showCurrentMenu);
        MAP.put(Pattern.compile("^user creat --([^ ]+) ([^ ]+) --([^ ]+) ([^ ]+) --([^ ]+) ([^ ]+)$"), userController::addNewUser);
        MAP.put(Pattern.compile("^user login --([^ ]+) ([^ ]+) --([^ ]+) ([^ ]+)$"), userController::loginUser);
        MAP.put(Pattern.compile("^user logout$"), userController::logout);
    }
    public LoginMenu(Menu menu) {
        super(menu);
        super.name = "LoginMenu";
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
