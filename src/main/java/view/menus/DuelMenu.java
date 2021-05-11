package view.menus;
import view.Menu;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import controller.GameController;

public class DuelMenu extends Menu {
    private static final HashMap<Pattern, Consumer<Matcher>> MAP = new HashMap<>();
    private static GameController gameController;
    static {
        MAP.put(Pattern.compile("^select (.+)$"), gameController :: select );
        MAP.put(Pattern.compile("^summon$"), gameController :: summon );
        MAP.put(Pattern.compile("^set$"), gameController :: set);
        MAP.put(Pattern.compile("^set --position (attack|defense)$"), gameController :: setPosition );
        MAP.put(Pattern.compile("^flip-summon$"), gameController :: flipSummon);
        MAP.put(Pattern.compile("^attack (1-5)$"), gameController :: attack );
        MAP.put(Pattern.compile("^attack direct$"), gameController :: attackDirect);
        MAP.put(Pattern.compile("^activate effect$"), gameController :: activateEffect );
        MAP.put(Pattern.compile("^show graveyard$"), gameController :: showGraveyard);
        MAP.put(Pattern.compile("^card show --selected$"), gameController :: showSelectedCard );
        MAP.put(Pattern.compile("^surrender$"), gameController :: surrender);
        MAP.put(Pattern.compile("^menu exit$"), Menu::exitMenu);
        //MAP.put(Pattern.compile("^back$"), );

    }

    public DuelMenu(GameController gameController) {
        this.gameController = gameController;
        name = "DuelMenu";
    }

    public void execute() {
        run(MAP);
    }

}
