package view.menus;

import controller.ScoreBoardController;
import controller.UserController;
import view.ConsumerSp;
import view.Menu;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScoreBoardMenu extends Menu {
    private static final HashMap<Pattern, ConsumerSp<Matcher>> MAP = new HashMap<>();
    private static ScoreBoardController scoreBoardController ;
    static {
        scoreBoardController = new ScoreBoardController();
        MAP.put(Pattern.compile("^scoreboard show$"), scoreBoardController::showScoreBoard);
        MAP.put(Pattern.compile("^menu exit$"), Menu::exitMenu);
        MAP.put(Pattern.compile("^menu show-current$"), i -> {
            return "scoreboard menu";
        });
    }
    public ScoreBoardMenu() {
        super();
    }
    public void execute(){
        run(MAP);
    }
}
