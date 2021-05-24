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
        MAP.put(Pattern.compile("^\\s*deck create ([^ ]+)\\s*$"),deckController::createDeck );
        MAP.put(Pattern.compile("^\\s*deck delete ([^ ]+)\\s*$"),deckController::removeDeck );
        MAP.put(Pattern.compile("^\\s*deck set-active ([^ ]+)\\s*$"),deckController::setActive );
        MAP.put(Pattern.compile("^\\s*deck add-card (.+)\\s*$"), deckController::addCard);
        MAP.put(Pattern.compile("^\\s*deck rm-card (.+)\\s*$"),deckController::removeCard );
        MAP.put(Pattern.compile("^\\s*deck show --all\\s*$" ), deckController::showAllDeck);
        MAP.put(Pattern.compile("^\\s*deck show --deck-name ([^ ]+) (--side)?\\s*$"),deckController::showDeck );
        MAP.put(Pattern.compile("^\\s*deck show --cards\\s*$"),deckController::showCards);
        MAP.put(Pattern.compile("^\\s*menu exit\\s*$"), Menu::exitMenu);
         MAP.put(Pattern.compile("^\\s*menu show-current\\s*$"), i -> {
             return "deck menu";
         });
     }

    public DeckMenu() {}

    public void execute() {
        System.out.println("______DECK MENU______");
         run(MAP);
    }

}