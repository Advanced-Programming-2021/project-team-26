package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fxmlController.GameView;
import model.Request;
import model.Response;
import model.cards.Card;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Caller extends Thread {
    private GameController gameController;
    private GameView view;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private boolean end;

    public Caller(GameController gameController, GameView view, Socket socket) throws IOException {
        this.gameController = gameController;
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
            }
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
                ArrayList<Card> selected = view.getCardInput(cards, number, message);
                Response response = new Response(true, "");
                response.addData("selected", selected);
                return response;
            case "updateOpponentMonsterZone":
                view.updateOpponentMonsterZone();
                return new Response(true, "");
            case "updateOpponentHand":
                view.updateOpponentHand();
                return new Response(true, "");
            case "updateMyHand":
                view.updateMyHand();
                return new Response(true, "");
            case "updateMyMonsterZone":
                view.updateMyMonsterZone();
                return new Response(true, "");
            case "updateMySpellTraps":
                view.updateMySpellTraps();
                return new Response(true, "");
        }
        return new Response(false, "method not supported");
    }

    public void end() {
        this.end = true;
        //TODO terminate thread
    }
}
