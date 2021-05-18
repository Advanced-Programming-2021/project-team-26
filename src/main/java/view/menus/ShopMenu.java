package view.menus;

import controller.ShopController;
import model.cards.Card;
import view.ConsumerSp;
import view.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopMenu extends Menu {
    private static ArrayList<Card> shopCards = new ArrayList<>();
    private static ShopController shopController;
    private static final HashMap<Pattern, ConsumerSp<Matcher>> MAP = new HashMap<>();

    static {
        shopController = new ShopController();
        MAP.put(Pattern.compile("^shop buy (.+)$"), shopController::buyCard);
        MAP.put(Pattern.compile("^shop show --all$"), shopController::showAll);
        MAP.put(Pattern.compile("^menu exit$"), Menu::exitMenu);
    }

    public ShopMenu() {

    }

    public void execute() {
        run(MAP);
    }
}
