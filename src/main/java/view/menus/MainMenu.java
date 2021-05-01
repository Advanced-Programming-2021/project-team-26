package view.menus;

import controller.UserController;
import view.Menu;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainMenu extends Menu {

    public MainMenu(Menu menu) {
        super(menu);
        super.name = "Main";
    }

    public void execute(){
        super.run();
    }

}
