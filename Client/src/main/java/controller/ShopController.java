package controller;

import exceptions.InvalidInput;
import model.Request;
import model.Response;
import model.User;
import model.cards.Card;
import view.Scan;

import java.util.HashMap;
import java.util.regex.Matcher;

public class ShopController {
    public boolean sellCard(Card card) throws Exception {
        Request request = new Request("ShopController", "sellCard");
        request.addParameter("card", card.getName());
        Response response = NetworkController.getInstance().sendAndReceive(request);

        if (response.isSuccess()) return true;
        throw new RuntimeException(response.getMessage());
    }

    public boolean buyCard(Card card) throws Exception {
        Request request = new Request("ShopController", "buyCard");
        request.addParameter("card", card.getName());
        Response response = NetworkController.getInstance().sendAndReceive(request);

        if (response.isSuccess()) return true;
        throw new RuntimeException(response.getMessage());
    }

    public int getNumberOfThisCardInShop(String cardName) {
        Request request = new Request("ShopController", "getNumberOfThisCardInShop");
        request.addParameter("cardName", cardName);
        Response response = NetworkController.getInstance().sendAndReceive(request);

        return Integer.parseInt(response.getData("NumberOfThisCardInShop"));
    }

    public int getPrice(String cardName) {
        Request request = new Request("ShopController", "getPrice");
        request.addParameter("cardName", cardName);
        Response response = NetworkController.getInstance().sendAndReceive(request);

        return Integer.parseInt(response.getData("Price"));
    }

    public int getUserBalance() {
        Request request = new Request("ShopController", "getUserBalance");
        request.addParameter("username", Database.getInstance().getCurrentUser().getUsername());
        Response response = NetworkController.getInstance().sendAndReceive(request);

        return Integer.parseInt(response.getData("UserBalance"));
    }

    public String increaseMoney(Matcher matcher) {
        HashMap<String, String> input = Scan.getInstance().parseInput(matcher.group());
        String moneyString = Scan.getInstance().getValue(input, "money", "m");
        if (moneyString == null)
            throw new InvalidInput();

        try {
            int money = Integer.parseInt(moneyString);
            if (Database.getInstance().isDebuggingMode())
                Database.getInstance().getCurrentUser().increaseMoney(money);
            else
                throw new InvalidInput();
        } catch (NumberFormatException e) {
            throw new InvalidInput();
        }
        return "successfully increased";
    }
}
