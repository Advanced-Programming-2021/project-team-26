package controller;

import exceptions.CardNotFoundException;
import exceptions.InvalidInput;
import exceptions.NotEnoughMoneyException;
import model.User;
import model.cards.Card;
import view.Scan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class ShopController {
    private static final Map<String, Card> allCards;

    static {
        allCards = Card.getAllCards();
    }

    public static Map<String, Card> getAllCards() {
        return allCards;
    }

    public String buyCard(Matcher matcher) throws CardNotFoundException, NotEnoughMoneyException {
        String cardName = matcher.group(1);
        Card thisCard = Card.getCard(cardName);
        if (thisCard == null)
            throw new CardNotFoundException();
        else {
            int cardPrice = thisCard.getPrice();
            if (cardPrice > Database.getInstance().getCurrentUser().getMoney())
                throw new NotEnoughMoneyException();
            else {
                Database.getInstance().getCurrentUser().setMoney(Database.getInstance().getCurrentUser().getMoney() - cardPrice);
                Database.getInstance().getCurrentUser().addCardToUserCards(thisCard);
            }
        }

        return "You bought card " + cardName + " successfully";
    }

    public String showAll(Matcher matcher) {
        return allCardsToString();
    }

    public boolean checkCardNameExistence(String cardName) {
        for (String key : allCards.keySet()) {
            if (key.equals(cardName)) {
                return true;
            }
        }
        return false;
    }

    public void increaseMoney(Matcher matcher) {
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
    }

    private String allCardsToString() {
        StringBuilder stringToReturn = new StringBuilder();
        Map<String, Card> allCards = getAllCards();
        ArrayList<String> sortedCardNames = new ArrayList<>(getAllCards().keySet());
        Collections.sort(sortedCardNames);

        for (String name : sortedCardNames) {
            stringToReturn.append(name).append(":").append(allCards.get(name).getDescription()).append("\n");
        }

        return stringToReturn.toString();
    }
}
