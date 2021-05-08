package view.menus;
import controller.ShopController;
import exceptions.ExceptionForPrint;
import model.cards.Card;
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
    private static final Map<Pattern, Consumer<Matcher>> MAP = new HashMap<>();
    static{
        MAP.put(Pattern.compile("^shop buy (.+)$"), shopController::buyCard);
        MAP.put(Pattern.compile("^shop show --all$"), shopController::showAll);
    }

    public ShopMenu(Menu menu) {
        super(menu);
        super.name = "ShopMenu";
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
