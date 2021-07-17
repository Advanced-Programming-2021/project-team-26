package controller;

import model.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
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

    public boolean buyCard(Card card) {
        int cardPrice = card.getPrice();
        Database.getInstance().getCurrentUser().setMoney(Database.getInstance().getCurrentUser().getMoney() - cardPrice);
        Database.getInstance().getCurrentUser().addCardToUserCards(card);
        return true;
    }

    public int getNumberOfThisCardInUserCards(String cardName) {
        if (Database.getInstance().getCurrentUser().getAllCards().containsKey(cardName))
            return Database.getInstance().getCurrentUser().getAllCards().get(cardName);
        return 0;
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

    public int getPrice(Card card) {
        return card.getPrice();
    }

    public int getUserBalance() {
        return Database.getInstance().getCurrentUser().getMoney();
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
