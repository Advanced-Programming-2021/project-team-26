package controller;

import com.google.gson.Gson;
import model.Deck;
import model.Request;
import model.Response;
import model.User;
import model.cards.Card;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class Handler extends Thread {
    private final Socket socket;
    User user;
    String token;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    WaitingGame waitingGame;
    GameView view;
    int round = -1;
    private boolean getInput = true;

    public Handler(Socket socket) throws IOException {
        this.socket = socket;
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    public Socket getSocket() {
        return socket;
    }

    public Request getRequest() {
        try {
            String input = dataInputStream.readUTF();
            Logger.log("input:" + input);
            return new Gson().fromJson(input, Request.class);
        } catch (IOException e) {
            return null;
        }
    }

    public void sendResponse(Response response) {
        try {
            dataOutputStream.writeUTF(response.toJSON());
            dataOutputStream.flush();
            Logger.log("output:" + response.toJSON());
        } catch (IOException ignored) {
        }
    }

    public void run() {
        while (true) {
            if (!isGetInput())
                continue;
            try {
                String input = dataInputStream.readUTF();
                Request request = new Gson().fromJson(input, Request.class);
                token = request.getToken();
                user = Database.getInstance().getLoggedInUser(token);
                Response response = process(request);
                if (response != null) {
                    dataOutputStream.writeUTF(response.toJSON());
                    dataOutputStream.flush();
                    Logger.log(input + " -> " + response.toJSON());
                }
            } catch (IOException e) {
                Logger.log("Client disconnected");
                cleanup();
                break;
            }
        }

    }

    private void cleanup() {
        Database.getInstance().removeLoggedInUser(token);
        Database.getInstance().removeWaitingGame(waitingGame);
        user = null;
        token = null;
        waitingGame = null;
        view = null;
        round = -1;
        getInput = true;
    }

    private Response process(Request request) {
        switch (request.getController()) {
            case "UserController":
                return handleUserCommands(request);
            case "DeckController":
                return handleDeckCommands(request);
            case "MainMenuController":
                return handleMainMenuCommands(request);
            case "ShopController":
                return handleShopCommands(request);
            case "ScoreBoardController":
                return handleScoreBoardCommands(request);
            case "GameController":
                if (view == null)
                    break;
                return view.handle(request);
        }
        return new Response(false, "controller not found");
    }

    private Response handleScoreBoardCommands(Request request) {
        if (user == null) {
            return new Response(false, "invalid token");
        }

        if ("getAndSetDataFromUser".equals(request.getMethodToCall())) {
            Response response = new Response(true, "");
            response.addData("ScoreBoardControllers", ScoreBoardController.getAndSetDataFromUser());
            return response;
        }

        return new Response(false, "method not found");
    }

    private Response handleShopCommands(Request request) {
        if (user == null) {
            return new Response(false, "invalid token");
        }

        switch (request.getMethodToCall()) {
            case "buyCard":
                try {
                    Card card = Card.getCard(request.getParameter("card"));
                    boolean success = ShopController.buyCard(user, card);
                    return new Response(success, "");
                } catch (Exception e) {
                    return new Response(false, e.getMessage());
                }
            case "sellCard":
                try {
                    Card card = Card.getCard(request.getParameter("card"));
                    boolean success = ShopController.sellCard(user, card);
                    return new Response(success, "");
                } catch (Exception e) {
                    return new Response(false, e.getMessage());
                }
            case "getNumberOfThisCardInShop":
                String cardName = request.getParameter("cardName");
                Response response = new Response(true, "");
                response.addData("NumberOfThisCardInShop", ShopController.getNumberOfThisCardInShop(cardName));
                return response;
            case "getPrice":
                String cardName1 = request.getParameter("cardName");
                Response response1 = new Response(true, "");
                response1.addData("Price", ShopController.getPrice(cardName1));
                return response1;
            case "getUserBalance":
                String username = request.getParameter("username");
                Response response2 = new Response(true, "");
                response2.addData("UserBalance", ShopController.getUserBalance(username));
                return response2;
        }

        return new Response(false, "method not found");
    }

    private Response handleMainMenuCommands(Request request) {
        if (user == null) {
            return new Response(false, "invalid token");
        }

        switch (request.getMethodToCall()) {
            case "newGame":
                try {
                    int round = Integer.parseInt(request.getParameter("round"));
                    waitingGame = MainMenuController.getInstance().createNewGameWithRealPlayer(this, round);
                    this.round = round;
                    Response response = new Response(true, "game created");
                    return response;
                } catch (Exception e) {
                    return new Response(false, e.getMessage());
                }
            case "foundPlayer":
                if (waitingGame.opponent != null) {
                    Response response = new Response(true, "found");
                    response.addData("user", waitingGame.opponent.user);
                    response.addData("turn", 1);
                    sendResponse(response);
                    stopGetInput();
                    new HeadOrTailController(waitingGame.opponent, this, round).run();
                    return null;
                } else {
                    WaitingGame getWaitingGame = Database.getInstance().findMatch(waitingGame);
                    if (getWaitingGame == null)
                        return new Response(false, "not found yet");
                    else {
                        Response response = new Response(true, "found");
                        response.addData("user", getWaitingGame.handler.user);
                        response.addData("turn", 0);
                        sendResponse(response);
                        stopGetInput();
                        return null;
                    }
                }
            case "cancelGame":
                if (waitingGame.opponent != null)
                    return new Response(false, "can't cancel game");
                else {
                    Database.getInstance().removeWaitingGame(waitingGame);
                    return new Response(true, "game canceled");
                }
        }

        return new Response(false, "method not found");
    }

    private Response handleUserCommands(Request request) {
        if (request.getMethodToCall().equals("addNewUser")) {
            try {
                String username = request.getParameters().get("username");
                String password = request.getParameters().get("password");
                String nickname = request.getParameters().get("nickname");
                boolean success = UserController.getInstance().addNewUser(username, password, nickname);
                return new Response(success, "");
            } catch (Exception e) {
                return new Response(false, e.getMessage());
            }
        }
        if (request.getMethodToCall().equals("loginUser")) {
            try {
                String username = request.getParameters().get("username");
                String password = request.getParameters().get("password");
                token = UserController.getInstance().loginUser(username, password);
                user = Database.getInstance().getLoggedInUser(token);
                Response response = new Response(true, token);
                response.addData("user", user);
                return response;
            } catch (Exception e) {
                return new Response(false, e.getMessage());
            }
        }
        if (user == null) {
            return new Response(false, "invalid token");
        }
        switch (request.getMethodToCall()) {
            case "logout":
                String token = request.getToken();
                if (Database.getInstance().isUserLoggedIn(token)) {
                    UserController.getInstance().logout(token);
                    return new Response(true, "");
                } else
                    return new Response(false, "");
            case "changeNickname":
                try {
                    UserController.getInstance().changeNickname(user, request.getParameter("nickname"));
                    return new Response(true, "done");
                } catch (Exception e) {
                    return new Response(false, e.getMessage());
                }
            case "changePassword":
                try {
                    String currentPassword = request.getParameter("currentPassword");
                    String newPassword = request.getParameter("newPassword");
                    String repeatPassword = request.getParameter("repeatPassword");
                    UserController.getInstance().changePassword(user, currentPassword, newPassword, repeatPassword);
                    return new Response(true, "password changed");
                } catch (Exception e) {
                    return new Response(false, e.getMessage());
                }
            case "changeProfile":
                try {
                    File file = request.getFile("image");
                    UserController.getInstance().changeProfile(user, file);
                    return new Response(true, "done");
                } catch (Exception e) {
                    return new Response(false, e.getMessage());
                }
            case "getProfileImage":
                try {
                    String username = request.getParameter("username");
                    User user1 = User.getUserByUsername(username);
                    if (user1 == null)
                        return new Response(false, "user not found");
                    File file = new File(user1.getProfileImagePath());
                    Response response = new Response(true, null);
                    response.addFile("image", file);
                    return response;
                } catch (IOException e) {
                    return new Response(false, "image not found");
                }
        }
        return new Response(false, "method not found");
    }

    private Response handleDeckCommands(Request request) {
        if (user == null) {
            return new Response(false, "invalid token");
        }

        switch (request.getMethodToCall()) {
            case "createDeck":
                try {
                    String deckName = request.getParameters().get("deckName");
                    Deck deck = DeckController.getInstance().createDeck(user, deckName);
                    Response response = new Response(true, "");
                    response.addData(deckName, deck);
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Response(false, e.getMessage());
                }
            case "removeDeck":
                try {
                    String deckName = request.getParameters().get("deckName");
                    boolean success = DeckController.getInstance().removeDeck(user, deckName);
                    return new Response(success, "");
                } catch (Exception e) {
                    return new Response(false, e.getMessage());
                }
            case "setActive":
                try {
                    String deckName = request.getParameters().get("deckName");
                    boolean success = DeckController.getInstance().setActive(user, deckName);
                    return new Response(success, "");
                } catch (Exception e) {
                    return new Response(false, e.getMessage());
                }
            case "addCard":
                try {
                    String deckName = request.getParameters().get("deckName");
                    String cardName = request.getParameters().get("cardName");
                    Boolean side = Boolean.valueOf(request.getParameters().get("side"));

                    boolean success = DeckController.getInstance().addCard(user, cardName, deckName, side);
                    return new Response(success, "");
                } catch (Exception e) {
                    return new Response(false, e.getMessage());
                }
            case "removeCard":
                try {
                    String deckName = request.getParameters().get("deckName");
                    String cardName = request.getParameters().get("cardName");
                    Boolean side = Boolean.valueOf(request.getParameters().get("side"));

                    boolean success = DeckController.getInstance().removeCard(user, cardName, deckName, side);
                    return new Response(success, "");
                } catch (Exception e) {
                    return new Response(false, e.getMessage());
                }
        }
        return new Response(false, "method not found");
    }

    public User getUser() {
        return this.user;
    }

    public synchronized boolean isGetInput() {
        return getInput;
    }

    public synchronized void stopGetInput() {
        getInput = false;
    }

    public synchronized void startGetInput() {
        getInput = true;
    }

    public void setView(GameView view) {
        this.view = view;
    }
}