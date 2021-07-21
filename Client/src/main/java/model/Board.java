package model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controller.MonsterTransfer;
import controller.NetworkController;
import controller.SpellTrapTransfer;
import model.cards.Card;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private final int myTurn;

    public Board(int myTurn) {
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
        List<Card> tmp = new Gson().fromJson(response.getData("Deck"), listType);
        List<Card> deck = new ArrayList<>();
        for(Card card:tmp)
            deck.add(Card.getCard(card.getName()));
        return deck;
    }

    public List<Card> getGraveyard() {
        Request request = new Request("GameController", "Board");
        request.addParameter("turn", myTurn);
        request.addParameter("function", "getGraveyard");
        Response response = NetworkController.getInstance().sendAndReceive(request);
        Type listType = new TypeToken<List<Card>>() {
        }.getType();
        List<Card> tmp = new Gson().fromJson(response.getData("Graveyard"), listType);
        List<Card> graveyard = new ArrayList<>();
        for(Card card:tmp)
            graveyard.add(Card.getCard(card.getName()));
        return graveyard;
    }

    public List<Card> getHand() {
        Request request = new Request("GameController", "Board");
        request.addParameter("turn", myTurn);
        request.addParameter("function", "getHand");
        Response response = NetworkController.getInstance().sendAndReceive(request);
        Type listType = new TypeToken<List<Card>>() {
        }.getType();
        List<Card> tmp = new Gson().fromJson(response.getData("Hand"), listType);
        List<Card> hand = new ArrayList<>();
        for(Card card:tmp)
            hand.add(Card.getCard(card.getName()));
        return hand;
    }
}