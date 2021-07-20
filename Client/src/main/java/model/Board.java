package model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controller.MonsterTransfer;
import controller.NetworkController;
import controller.SpellTrapTransfer;
import model.cards.Card;

import java.lang.reflect.Type;
import java.util.*;

public class Board {
    private List<Card> deck;
    private int myTurn;

    public Board(Deck deck, int myTurn) {
        initDeck(deck);
        this.myTurn = myTurn;
    }

    public HashMap<Integer, MonsterTransfer> getMonsterZoneMap() {
        Request request = new Request("GameController", "Board");
        request.addParameter("turn", myTurn);
        request.addParameter("function", "getMonsterZoneMap");
        Response response = NetworkController.getInstance().sendAndReceive(request);
        Type hashMapType = new TypeToken<Map<Integer, MonsterTransfer>>() {
        }.getType();
        return new Gson().fromJson(response.getData("MonsterZoneMap"), hashMapType);
    }

    public HashMap<Integer, SpellTrapTransfer> getSpellTrapZoneMap() {
        Request request = new Request("GameController", "Board");
        request.addParameter("turn", myTurn);
        request.addParameter("function", "getSpellTrapZoneMap");
        Response response = NetworkController.getInstance().sendAndReceive(request);
        Type hashMapType = new TypeToken<Map<Integer, SpellTrapTransfer>>() {
        }.getType();
        return new Gson().fromJson(response.getData("SpellTrapZoneMap"), hashMapType);
    }

    public List<Card> getDeck() {
        Request request = new Request("GameController", "Board");
        request.addParameter("turn", myTurn);
        request.addParameter("function", "getDeck");
        Response response = NetworkController.getInstance().sendAndReceive(request);
        Type listType = new TypeToken<List<Card>>() {
        }.getType();
        return new Gson().fromJson(response.getData("Deck"), listType);
    }

    public List<Card> getGraveyard() {
        Request request = new Request("GameController", "Board");
        request.addParameter("turn", myTurn);
        request.addParameter("function", "getGraveyard");
        Response response = NetworkController.getInstance().sendAndReceive(request);
        Type listType = new TypeToken<List<Card>>() {
        }.getType();
        return new Gson().fromJson(response.getData("Graveyard"), listType);
    }

    public List<Card> getHand() {
        Request request = new Request("GameController", "Board");
        request.addParameter("turn", myTurn);
        request.addParameter("function", "getHand");
        Response response = NetworkController.getInstance().sendAndReceive(request);
        Type listType = new TypeToken<List<Card>>() {
        }.getType();
        return new Gson().fromJson(response.getData("Hand"), listType);
    }

    private void initDeck(Deck deck) {
        ArrayList<String> mainDeck = deck.getMainDeck();
        this.deck = new ArrayList<>();

        for (String cardName : mainDeck) {
            this.deck.add(Card.getCard(cardName));
        }

        Collections.shuffle(this.deck);
    }
}