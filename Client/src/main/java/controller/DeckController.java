package controller;

import com.google.gson.Gson;
import exceptions.*;
import model.Deck;
import model.Request;
import model.Response;
import model.cards.Card;
import view.Scan;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;

public class DeckController {
    private static DeckController deckController;
    private final Gson gson;
    private HashMap<String, String> parameters = new HashMap<>();

    private DeckController() {
        gson = new Gson();
    }

    public static DeckController getInstance() {
        if (deckController == null)
            deckController = new DeckController();
        return deckController;
    }

    public boolean createDeck(String deckName) throws RepeatedDeckNameException {
        parameters.clear();
        parameters.put("deckName", deckName);
        Request request = new Request("DeckController", "createDeck", parameters);
        Response response = NetworkController.getInstance().sendAndReceive(request);

        if (response.isSuccess()) {
            Database.getInstance().getCurrentUser().addDeckToUserDecks(gson.fromJson(response.getData(deckName), Deck.class));
            return true;
        }
        throw new RuntimeException(response.getMessage());
    }

    public boolean removeDeck(String deckName) throws DeckNameDoesntExistException {
        parameters.clear();
        parameters.put("deckName", deckName);
        Request request = new Request("DeckController", "removeDeck", parameters);
        Response response = NetworkController.getInstance().sendAndReceive(request);

        if (response.isSuccess()) {
            Database.getInstance().getCurrentUser().getAllDecks().remove(deckName);
            return true;
        }

        throw new RuntimeException(response.getMessage());
    }

    public boolean setActive(String deckName) throws DeckNameDoesntExistException {
        parameters.clear();
        parameters.put("deckName", deckName);
        Request request = new Request("DeckController", "setActive", parameters);
        Response response = NetworkController.getInstance().sendAndReceive(request);

        if (response.isSuccess()) {
            Database.getInstance().getCurrentUser().setActiveDeck(Database.getInstance().getCurrentUser().getDeckByDeckName(deckName));
            return true;
        }

        throw new RuntimeException(response.getMessage());
    }

    public boolean addCard(String cardName, String deckName, Boolean side) {
        parameters.clear();
        parameters.put("cardName", cardName);
        parameters.put("deckName", deckName);
        parameters.put("side", side.toString());

        Request request = new Request("DeckController", "addCard", parameters);
        Response response = NetworkController.getInstance().sendAndReceive(request);

        if (response.isSuccess()) {
            if (side)
                Objects.requireNonNull(Database.getInstance().getCurrentUser().getDeckByDeckName(deckName)).addCardToSideDeck(Card.getCard(cardName));
            else
                Objects.requireNonNull(Database.getInstance().getCurrentUser().getDeckByDeckName(deckName)).addCardToMainDeck(Card.getCard(cardName));
            return true;
        }

        throw new RuntimeException(response.getMessage());
    }

    public boolean removeCard(String cardName, String deckName, Boolean side){
        parameters.clear();
        parameters.put("cardName", cardName);
        parameters.put("deckName", deckName);
        parameters.put("side", side.toString());

        Request request = new Request("DeckController", "removeCard", parameters);
        Response response = NetworkController.getInstance().sendAndReceive(request);

        if (response.isSuccess()) {
            if (side)
            Objects.requireNonNull(Database.getInstance().getCurrentUser().getDeckByDeckName(deckName)).deleteCardFromSideDeck(Card.getCard(cardName));
        else
            Objects.requireNonNull(Database.getInstance().getCurrentUser().getDeckByDeckName(deckName)).deleteCardFromMainDeck(Card.getCard(cardName));
            return true;
        }

        throw new RuntimeException(response.getMessage());
    }
//
//    public String showDeck(Matcher matcher) throws InvalidInput, DeckNameDoesntExistException {
//        HashMap<String, String> input = Scan.getInstance().parseInput(matcher.group());
//
//        String deckName = null;
//        if (input.containsKey("deck-name"))
//            deckName = input.get("deck-name");
//        else if (input.containsKey("d-n"))
//            deckName = input.get("d-n");
//        if (deckName == null)
//            throw new InvalidInput();
//
//        if (!Database.getInstance().getCurrentUser().checkDeckNameExistence(deckName))
//            throw new DeckNameDoesntExistException(deckName);
//
//        if (input.containsKey("side"))
//            return Objects.requireNonNull(Database.getInstance().getCurrentUser().getDeckByDeckName(deckName)).showDeck("side");
//        else
//            return Objects.requireNonNull(Database.getInstance().getCurrentUser().getDeckByDeckName(deckName)).showDeck("main");
//    }
//
//    public String showCards(Matcher matcher) {
//        return Database.getInstance().getCurrentUser().showAllCards();
//    }
//
//
//    public String showAllDeck(Matcher matcher) {
//        return Database.getInstance().getCurrentUser().showAllDecks();
//    }
}