//package view.menus;
//
//import controller.ScoreBoardController;
//import controller.UserController;
//import view.ConsumerSp;
//import view.Menu;
//
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Consumer;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class ScoreBoardMenu extends Menu {
//    private static final HashMap<Pattern, ConsumerSp<Matcher>> MAP = new HashMap<>();
//    private static ScoreBoardController scoreBoardController ;
//    static {
//        scoreBoardController = new ScoreBoardController();
//        MAP.put(Pattern.compile("^\\s*scoreboard show\\s*$"), scoreBoardController::showScoreBoard);
//        MAP.put(Pattern.compile("^\\s*menu exit\\s*$"), Menu::exitMenu);
//        MAP.put(Pattern.compile("^\\s*menu show-current\\s*$"), i -> {
//            return "scoreboard menu";
//        });
//    }
//    public ScoreBoardMenu() {
//        super();
//    }
//    public void execute(){
//        System.out.println("______SCORE BOARD MENU______");
//        run(MAP);
//    }
//}
