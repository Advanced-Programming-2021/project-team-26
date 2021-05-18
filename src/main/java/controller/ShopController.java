package controller;

import exceptions.CardNotFoundException;
import exceptions.NotEnoughMoneyException;
import model.User;
import model.cards.Card;
import view.Print;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class ShopController {
    private User userInShop ;
    private static Map<String, Card> allCards;
    static {
        allCards = Card.getAllCards();
    }

    public ShopController() {
        setUserInShop(Database.getInstance().getCurrentUser());
    }

    public User getUserInShop() {
        return userInShop;
    }

    public void setUserInShop(User userInShop) {
        this.userInShop = userInShop;
    }

    public String buyCard(Matcher matcher) throws CardNotFoundException, NotEnoughMoneyException {
        String cardName = matcher.group(1);
        Card thisCard = Card.getCard(cardName);
        if (thisCard == null)
            throw new CardNotFoundException();
        else {
            int cardPrice = thisCard.getPrice();
            if (cardPrice > userInShop.getMoney())
                throw new NotEnoughMoneyException();
            else {
                userInShop.setMoney(userInShop.getMoney() - cardPrice);
                userInShop.addCardToUserCards(thisCard);
            }
        }

        return "You bought card " + cardName + " successfully";
    }

    public String showAll(Matcher matcher) {
        return allCardsToString();
    }

    public static Map<String, Card> getAllCards() {
        return allCards;
    }


    public boolean checkCardNameExistence(String cardName) {
        for (String key : allCards.keySet()) {
            if (key.equals(cardName)) {
                return true;
            }
        }
        return false;
    }

    private String allCardsToString(){
        StringBuilder stringToReturn = new StringBuilder();
        Map<String, Card> allCards = getAllCards();
        ArrayList<String> sortedCardNames = new ArrayList<>(getAllCards().keySet());
        Collections.sort(sortedCardNames);

        for (String name : sortedCardNames){
            stringToReturn.append(name).append(":").append(allCards.get(name).getDescription()).append("\n");
        }

        return stringToReturn.toString();
    }
}
