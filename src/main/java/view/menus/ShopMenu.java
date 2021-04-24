package view.menus;
import controller.ShopController;
import model.cards.Card;
import view.Menu;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopMenu extends Menu {
    private static ShopController shopController;

    public ShopMenu(Menu menu) {
        super(menu);
    }

    public void run(Scanner scanner){
        String input = scanner.nextLine();
        Matcher matcher = null;
        if ((matcher = Pattern.compile("^shop buy (.+)$").matcher(input)).matches())
            new ShopController().buyCard(matcher.group(1));
        else if (input.mat)
    }

}
