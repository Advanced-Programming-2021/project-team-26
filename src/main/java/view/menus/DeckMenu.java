package view.menus;

import view.Menu;
import controller.DeckController;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class DeckMenu extends Menu {
    private DeckController deckController = DeckController.getInstance();
     {
        super.MAP.put(Pattern.compile("^deck creat ([^ ]+)$"),deckController::createDeck );
        super.MAP.put(Pattern.compile("^deck delet ([^ ]+)$"),deckController::removeDeck );
        super.MAP.put(Pattern.compile("^deck set-active ([^ ]+)$"),deckController::setActive );
        super.MAP.put(Pattern.compile("^deck add-card --([^ ]+) ([^ ]+) --([^ ]+) ([^ ]+) (--side)?$"), deckController::addCard);
        super.MAP.put(Pattern.compile("^deck rm-card --([^ ]+) ([^ ]+) --([^ ]+) ([^ ]+) (--side)?$"),deckController::removeCard );
        super.MAP.put(Pattern.compile("^deck show --all$" ), deckController::showAllDeck);
        super.MAP.put(Pattern.compile("^deck show --deck-name ([^ ]+) (--side)?$"),deckController::showDeck );
        super.MAP.put(Pattern.compile("^deck show --cards$"),deckController::showCards);
    }
    public DeckMenu(Menu menu) {
        super(menu);
        super.name = "DeckMenu";
    }

    public void run(){
        super.run();
    }

}