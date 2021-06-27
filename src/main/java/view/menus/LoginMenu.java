//package view.menus;
//
//import controller.UserController;
//import view.ConsumerSp;
//import view.Menu;
//
//import java.util.HashMap;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class LoginMenu extends Menu {
//    private static final HashMap<Pattern, ConsumerSp<Matcher>> MAP = new HashMap<>();
//    private static final UserController userController = UserController.getInstance();
//
//    static {
////        MAP.put(Pattern.compile("^\\s*user create (.+)\\s*$"), userController::addNewUser);
//        MAP.put(Pattern.compile("^\\s*user login (.+)\\s*$"), userController::loginUser);
//        MAP.put(Pattern.compile("^\\s*exit\\s*$"), i -> {
//            Menu.exitMenu(null);
//            return null;
//        });
//        MAP.put(Pattern.compile("^\\s*menu show-current\\s*$"), i -> {
//            return "login menu";
//        });
//    }
//
//    public LoginMenu() {
//    }
//
//    public void execute() {
//        System.out.println("WELCOME to our AP project :)");
//        System.out.println("______LOGIN MENU______");
//        run(MAP);
//    }
//}
