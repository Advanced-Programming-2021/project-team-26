package controller;

import com.google.gson.Gson;
import model.Deck;
import model.Request;
import model.Response;
import model.User;

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
    Handler opponent;
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
        Database.getInstance().removeWaitingGame(this);
        user = null;
        token = null;
        opponent = null;
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
            case "GameController":
                if (view == null)
                    break;
                return view.handle(request);
        }
        return new Response(false, "controller not found");
    }

    private Response handleMainMenuCommands(Request request) {
        if (user == null) {
            return new Response(false, "invalid token");
        }

        switch (request.getMethodToCall()) {
            case "newGame":
                try {
                    int round = Integer.parseInt(request.getParameter("round"));
                    opponent = MainMenuController.getInstance().createNewGameWithRealPlayer(this, round);
                    this.round = round;
                    Response response = new Response(true, "game created");
                    if (opponent != null) {
                        opponent.opponent = this;
                        response.addData("user", opponent.user);
                        response.addData("turn", String.valueOf(1));
                        stopGetInput();
                    } else {
                        response.addData("turn", String.valueOf(0));
                    }
                    return response;
                } catch (Exception e) {
                    return new Response(false, e.getMessage());
                }
            case "foundPlayer":
                if (opponent != null) {
                    Response response = new Response(true, "found");
                    response.addData("user", opponent.user);
                    sendResponse(response);
                    stopGetInput();
                    new HeadOrTailController(this, opponent, round).run();
                    return null;
                } else
                    return new Response(false, "not found yet");
            case "cancelGame":
                if(opponent!=null)
                    return new Response(false,"can't cancel game");
                else{
                    Database.getInstance().removeWaitingGame(this);
                    return new Response(true,"game canceled");
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
                    File file = new File(user.getProfileImagePath());
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
                    System.out.println(1);
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

    public synchronized boolean isGetInput(){
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