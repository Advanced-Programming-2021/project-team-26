package controller;

import model.cards.Card;

import java.util.regex.Matcher;

public class InterchangeController {
    private static InterchangeController instance;

    private InterchangeController() {

    }

    public static InterchangeController getInstance() {
        if (instance == null)
            instance = new InterchangeController();
        return instance;
    }

    public String importCommand(Matcher matcher) {
//        String name = matcher.group(1);
//        Card card = Database.getInstance().readCard(name);
//        if (card == null)
//            return "card not found";
        return "done";
    }

    public String exchangeCommand(Matcher matcher) {
        String name = matcher.group(1);
        Card card = Card.getCard(name);
        if (card == null)
            return "card not found";
        Database.getInstance().writeCard(card);
        return "done";
    }
}
