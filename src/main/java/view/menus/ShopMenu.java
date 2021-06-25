package view.menus;

import controller.ShopController;
import model.cards.Card;
import view.ConsumerSp;
import view.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopMenu extends Menu {
    private static final HashMap<Pattern, ConsumerSp<Matcher>> MAP = new HashMap<>();
    private static final ArrayList<Card> shopCards = new ArrayList<>();
    private static final ShopController shopController;

    static {
        shopController = new ShopController();
        MAP.put(Pattern.compile("^\\s*shop buy (.+)\\s*$"), shopController::buyCard);
        MAP.put(Pattern.compile("^\\s*shop show --all\\s*$"), shopController::showAll);
        MAP.put(Pattern.compile("^\\s*increase --money[0-9]+\\s*$"), shopController::increaseMoney);
        MAP.put(Pattern.compile("^\\s*menu exit\\s*$"), Menu::exitMenu);
        MAP.put(Pattern.compile("^\\s*menu show-current\\s*$"), i -> {
            return "shop menu";
        });
    }

    public void execute() {
        System.out.println("______SHOP MENU______");
        run(MAP);
    }
}
