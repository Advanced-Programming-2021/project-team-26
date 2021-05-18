package view.menus;
import view.ConsumerSp;
import view.Menu;
import controller.DeckController;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeckMenu extends Menu {
    private static final HashMap<Pattern, ConsumerSp<Matcher>> MAP = new HashMap<>();
    private DeckController deckController = DeckController.getInstance();
     {
        MAP.put(Pattern.compile("^deck create ([^ ]+)$"),deckController::createDeck );
        MAP.put(Pattern.compile("^deck delete ([^ ]+)$"),deckController::removeDeck );
        MAP.put(Pattern.compile("^deck set-active ([^ ]+)$"),deckController::setActive );
        MAP.put(Pattern.compile("^deck add-card --([^ ]+) ([\\w\\s]+) --([^ ]+) ([\\w\\s]+)( --side)?$"), deckController::addCard);
        MAP.put(Pattern.compile("^deck rm-card --([^ ]+) (.+) --([^ ]+) (.+)( --side)?$"),deckController::removeCard );
        MAP.put(Pattern.compile("^deck show --all$" ), deckController::showAllDeck);
        MAP.put(Pattern.compile("^deck show --deck-name ([^ ]+) (--side)?$"),deckController::showDeck );
        MAP.put(Pattern.compile("^deck show --cards$"),deckController::showCards);
        MAP.put(Pattern.compile("^menu exit$"), Menu::exitMenu);
     }

    public DeckMenu() {}

    public void execute() {
        run(MAP);
    }

}