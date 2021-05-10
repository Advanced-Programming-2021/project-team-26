package controller;

import model.Shop;
import model.User;
import model.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;

public class ShopController {
    private User userInShop = Database.getInstance().getCurrentUser();


    public ShopController(User user) {
        setUserInShop(Database.getInstance().getCurrentUser());
    }

    public User getUserInShop() {
        return userInShop;
    }

    public void setUserInShop(User userInShop) {
        this.userInShop = userInShop;
    }

    public void buyCard(Matcher matcher){

    }

    public boolean isHaveEnoughMoney(int price, User user) {
        return true;
    }

    public void showAll(Matcher matcher) {

    }

    private static final HashMap<String, Card> allCards;
  //  private static Shop shop;

    static {
        allCards = new HashMap<>();
    }

    private User currentUserInShop;

//    private Shop(User currentUserInShop) {
//        setCurrentUserInShop(currentUserInShop);
//        allCards.putAll(Database.getInstance().getAllCards());
//    }

//    public static Shop getInstance(User currentUserInShop) {
//        if (shop == null)
//            shop = new Shop(currentUserInShop);
//        return shop;
//    }

    public static HashMap<String, Card> getAllCards() {
        return allCards;
    }

    public User getCurrentUserInShop() {
        return currentUserInShop;
    }

    public void setCurrentUserInShop(User currentUserInShop) {
        this.currentUserInShop = currentUserInShop;
    }

    public int getPriceByCardName(String cardName) {
        for (String key : allCards.keySet()) {
            if (key.equals(cardName)) {
                return allCards.get(key).getPrice();
            }
        }
        return 0;
    }

    public boolean checkCardNameExistence(String cardName) {
        for (String key : allCards.keySet()) {
            if (key.equals(cardName)) {
                return true;
            }
        }
        return false;
    }

    public int addCardToUsersCards(Card card){
        if (getCurrentUserInShop().getMoney() < card.getPrice())
            return 0; // user didnt have enough money
        else {
            getCurrentUserInShop().getAllCards().put(card.getName(), card);
            return 1; //card added to users cards
        }
    }

    public String allCardsToString(){
        StringBuilder stringToReturn = new StringBuilder();
        HashMap<String, Card> allCards = getAllCards();
        ArrayList<String> sortedCardNames = new ArrayList<>(getAllCards().keySet());
        Collections.sort(sortedCardNames);

        for (String name : sortedCardNames){
            stringToReturn.append(name).append(":").append(allCards.get(name).getDescription()).append("\n");
        }

        return stringToReturn.toString();
    }
}
