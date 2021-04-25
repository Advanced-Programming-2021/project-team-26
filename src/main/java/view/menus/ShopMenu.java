package view.menus;
import controller.ShopController;
import model.cards.Card;
import view.ExeptionForPrint;
import view.Menu;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopMenu extends Menu {
    private static ArrayList<Card> shopCards = new ArrayList<>();
    private static ShopController shopController;

    {
        super.MAP.put(Pattern.compile("^shop buy (.+)$"), shopController::buyCard);
        super.MAP.put(Pattern.compile("^shop show --all$"), shopController::showAll);
    }

    public ShopMenu(Menu menu) {
        super(menu);
        super.name = "ShopMenu";
    }

    public void execute(){
        super.run();
    }
}
