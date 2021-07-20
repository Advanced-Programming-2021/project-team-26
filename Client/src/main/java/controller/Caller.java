package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fxmlController.GameView;
import javafx.application.Platform;
import model.Owner;
import model.Request;
import model.Response;
import model.cards.Card;
import model.cards.spell.Spell;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Caller extends Thread {
    private final GameController gameController;
    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final GameView view;
    private boolean end;

    public Caller(GameController gameController, GameView view, Socket socket) throws IOException {
        this.gameController = gameController;
        this.view = view;
        this.socket = socket;
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        while (!end) {
            try {
                String input = dataInputStream.readUTF();
                Request request = new Gson().fromJson(input, Request.class);
                Response response = process(request);
                if (response != null) {
                    dataOutputStream.writeUTF(response.toJSON());
                    dataOutputStream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void sendResponse(Response response) {
        try {
            if (response != null) {
                dataOutputStream.writeUTF(response.toJSON());
                dataOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Response process(Request request) {
        switch (request.getController()) {
            case "view":
                return handleView(request);
        }
        return new Response(false, "controller not found");
    }

    private Response handleView(Request request) {
        switch (request.getMethodToCall()) {
            case "getCardInput":
                Gson gson = new Gson();
                ArrayList<Card> cards = gson.fromJson(request.getParameter("cards"),
                        new TypeToken<ArrayList<Card>>() {
                        }.getType());
                int number = gson.fromJson(request.getParameter("number"), int.class);
                String message = request.getParameter("message");
                Platform.runLater(() -> {
                    ArrayList<Card> selected = view.getCardInput(cards, number, message);
                    Response response = new Response(true, "");
                    response.addData("selected", selected);
                    sendResponse(response);
                });
                return null;
            case "updateOpponentMonsterZone":
                Platform.runLater(() -> view.updateOpponentMonsterZone());
                return new Response(true, "");
            case "updateOpponentHand":
                Platform.runLater(() -> view.updateOpponentHand());
                return new Response(true, "");
            case "updateMyHand":
                Platform.runLater(() -> view.updateMyHand());
                return new Response(true, "");
            case "updateMyMonsterZone":
                Platform.runLater(() -> view.updateMyMonsterZone());
                return new Response(true, "");
            case "updateMySpellTraps":
                Platform.runLater(() -> view.updateMySpellTraps());
                return new Response(true, "");
            case "updateOpponentSpellTraps":
                Platform.runLater(() -> view.updateOpponentSpellTraps());
                return new Response(true, "");
            case "updateLifePoint":
                Platform.runLater(() -> view.updateLifePoint());
                return new Response(true, "");
            case "updateMyGraveyard":
                Card card = Card.getCard(request.getParameter("card"));
                Platform.runLater(() -> view.updateMyGraveyard(card));
                return new Response(true, "");
            case "updateOppGraveyard":
                Card card1 = Card.getCard(request.getParameter("card"));
                Platform.runLater(() -> view.updateOppGraveyard(card1));
                return new Response(true, "");
            case "updateFieldImage":
                Spell spell = (Spell) Card.getCard(request.getParameter("spell"));
                Platform.runLater(() -> view.updateFieldImage(spell));
                return new Response(true, "");
            case "moveFromDeckToHand":
                Card card2 = Card.getCard(request.getParameter("card"));
                Platform.runLater(() -> view.moveFromDeckToHand(card2));
                return new Response(true, "");
            case "moveFromOpponentDeckToHand":
                Card card3 = Card.getCard(request.getParameter("card"));
                Platform.runLater(() -> view.moveFromOpponentDeckToHand(card3));
                return new Response(true, "");
            case "attackAnimation":
                int attackerMonster = Integer.parseInt(request.getParameter("attackerMonster"));
                int defenderMonster = Integer.parseInt(request.getParameter("defenderMonster"));
                Owner owner = Owner.valueOf(request.getParameter("owner"));
                Platform.runLater(() -> view.attackAnimation(attackerMonster, defenderMonster, owner));
                return new Response(true, "");
            case "updatePhase":
                Platform.runLater(() -> view.updatePhase());
                return new Response(true, "");
            case "endGame":
                Platform.runLater(() -> gameController.endGame(request.getParameter("message")));
                return new Response(true, "");
        }
        return new Response(false, "method not supported");
    }

    public void end() {
        this.end = true;
        //TODO terminate thread
    }
}