package controller;

import exceptions.InvalidInput;
import exceptions.NoCardToSell;
import exceptions.NotEnoughCardException;
import model.User;
import model.cards.Card;
import view.Scan;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class ShopController {
    private static final int INITIAL_NUMBER_OF_CARDS = 20;
    private static Map<String, Integer> allCards; //CardName, CardNumber
    private static Map<String, Boolean> cardsBuyingState;//true -> selling possible

    static {
        setAllCards(new HashMap<>());
        for (String cardName : Card.getAllCards().keySet()) {
            allCards.put(cardName, INITIAL_NUMBER_OF_CARDS);
        }
        setCardsBuyingState(new HashMap<>());
        for (String cardName : Card.getAllCards().keySet()) {
            cardsBuyingState.put(cardName, true);
        }
    }

    public static void disableSelling(String cardName){
        cardsBuyingState.put(cardName, false);
    }

    public static void enableSelling(String cardName){
        cardsBuyingState.put(cardName, true);
    }

    public static Map<String, Boolean> getCardsBuyingState() {
        return cardsBuyingState;
    }

    public static void setCardsBuyingState(Map<String, Boolean> cardsBuyingState) {
        ShopController.cardsBuyingState = cardsBuyingState;
    }

    public static Map<String, Integer> getAllCards() {
        return allCards;
    }

    public static void setAllCards(Map<String, Integer> allCards) {
        ShopController.allCards = allCards;
    }

    public static boolean sellCard(User user, Card card) throws Exception {
        int numberOfCard;
        if (!user.getAllCards().containsKey(card.getName()) || (numberOfCard = user.getAllCards().get(card.getName())) == 0)
            throw new NoCardToSell();
        user.setMoney(user.getMoney() + card.getPrice());
        user.getAllCards().put(card.getName(), --numberOfCard);
        allCards.put(card.getName(), allCards.get(card.getName()) + 1);
        return true;
    }

    public static boolean buyCard(User user, Card card) throws Exception {
        if (allCards.get(card.getName()) == 0) throw new NotEnoughCardException();
        int cardPrice = card.getPrice();
        user.setMoney(user.getMoney() - cardPrice);
        user.addCardToUserCards(card);
        allCards.put(card.getName(), allCards.get(card.getName()) - 1);
        return true;
    }

    public static void decreaseCardAmountInShop(int amount, String cardName) {
        allCards.put(cardName, (allCards.get(cardName) - amount) <= 0 ? 0 : (allCards.get(cardName) - amount));
    }

    public static void increaseCardAmountInShop(int amount, String cardName) {
        allCards.put(cardName, allCards.get(cardName) + amount);
    }

    public static int getNumberOfThisCardInShop(String cardName) {
        return allCards.get(cardName);
    }

    public static int getPrice(String cardName) {
        return Card.getCard(cardName).getPrice();
    }

    public static int getUserBalance(String username) {
        return User.getUserByUsername(username).getMoney();
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