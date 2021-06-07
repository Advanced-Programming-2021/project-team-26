package view.menus;

import controller.GameController;
import view.ConsumerSp;
import view.Menu;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DuelMenu extends Menu {
    private static GameController gameController;
    private final HashMap<Pattern, ConsumerSp<Matcher>> MAP = new HashMap<>();

    {
        MAP.put(Pattern.compile("^\\s*select (.+)\\s*$"), gameController::select);
        MAP.put(Pattern.compile("^\\s*summon\\s*$"), gameController::summon);
        MAP.put(Pattern.compile("^\\s*set\\s*$"), gameController::set);
        MAP.put(Pattern.compile("^\\s*set (.+)\\s*$"), gameController::setPosition);
        MAP.put(Pattern.compile("^\\s*flip-summon\\s*$"), gameController::flipSummon);
        MAP.put(Pattern.compile("^\\s*attack ([1-5])\\s*$"), gameController::attack);
        MAP.put(Pattern.compile("^\\s*attack direct\\s*$"), gameController::attackDirect);
        MAP.put(Pattern.compile("^\\s*activate effect\\s*$"), gameController::activateEffect);
        MAP.put(Pattern.compile("^\\s*show graveyard\\s*$"), gameController::showGraveyard);
        MAP.put(Pattern.compile("^\\s*card show --selected\\s*$"), gameController::showCard);
        MAP.put(Pattern.compile("^\\s*next phase\\s*$"), gameController::nextPhase);
        MAP.put(Pattern.compile("^\\s*surrender\\s*$"), gameController::surrender);
        MAP.put(Pattern.compile("^\\s*menu exit\\s*$"), Menu::exitMenu);
        MAP.put(Pattern.compile("^\\s*menu show-current\\s*$"), i -> {
            return "game menu";
        });
    }

    public DuelMenu(GameController gameController) {
        DuelMenu.gameController = gameController;
    }

    public static void setGameController(GameController gameController) {
        DuelMenu.gameController = gameController;
    }

    public void execute() {
        System.out.println("______DUEL MENU______");
        run(MAP);
    }

}
