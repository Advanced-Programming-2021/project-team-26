package view.menus;

import exceptions.ExceptionForPrint;
import view.Menu;
import controller.DeckController;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeckMenu extends Menu {
    private static final Map<Pattern, Consumer<Matcher>> MAP = new HashMap<>();
    private DeckController deckController = DeckController.getInstance();
     {
        MAP.put(Pattern.compile("^deck creat ([^ ]+)$"),deckController::createDeck );
        MAP.put(Pattern.compile("^deck delet ([^ ]+)$"),deckController::removeDeck );
        MAP.put(Pattern.compile("^deck set-active ([^ ]+)$"),deckController::setActive );
        MAP.put(Pattern.compile("^deck add-card --([^ ]+) ([^ ]+) --([^ ]+) ([^ ]+) (--side)?$"), deckController::addCard);
        MAP.put(Pattern.compile("^deck rm-card --([^ ]+) ([^ ]+) --([^ ]+) ([^ ]+) (--side)?$"),deckController::removeCard );
        MAP.put(Pattern.compile("^deck show --all$" ), deckController::showAllDeck);
        MAP.put(Pattern.compile("^deck show --deck-name ([^ ]+) (--side)?$"),deckController::showDeck );
        MAP.put(Pattern.compile("^deck show --cards$"),deckController::showCards);
    }
    public DeckMenu(Menu menu) {
        super(menu);
        super.name = "DeckMenu";
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