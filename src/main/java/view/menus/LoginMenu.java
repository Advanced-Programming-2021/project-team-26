package view.menus;

import view.Menu;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LoginMenu extends Menu {
  //  private static final Map<Pattern, Method> PATTERNS;

     {
      //  PATTERNS = new HashMap<>();
       // PATTERNS.put("user login --([^-]+)--(.+)")
    }
    public LoginMenu(Menu menu) {
        super(menu);
        super.name = "LoginMenu";
    }

    public void run(){

    }

}
