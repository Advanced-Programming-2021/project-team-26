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
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    public Handler(Socket socket) throws IOException {
        this.socket = socket;
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    public void run() {
        while (true) {
            try {
                String input = dataInputStream.readUTF();
                Request request = new Gson().fromJson(input, Request.class);
                Response response = process(request);
                dataOutputStream.writeUTF(response.toJSON());
                dataOutputStream.flush();
                Logger.log(input + " -> " + response.toJSON());
            } catch (IOException e) {
                Logger.log("Client disconnected");
                break;
            }
        }

    }

    private Response process(Request request) {
        switch (request.getController()) {
            case "UserController":
                return handleUserCommands(request);
            case "DeckController":
                return handleDeckCommands(request);
        }
        return new Response(false, "controller not found");
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
                String token = UserController.getInstance().loginUser(username, password);
                Response response = new Response(true, token);
                response.addData("user", Database.getInstance().getLoggedInUser(token));
                return response;
            } catch (Exception e) {
                return new Response(false, e.getMessage());
            }
        }
        User user = Database.getInstance().getLoggedInUser(request.getToken());
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
                    return new Response(false,"image not found");
                }
        }
        return new Response(false, "method not found");
    }

    private Response handleDeckCommands(Request request) {
        User user = Database.getInstance().getLoggedInUser(request.getToken());
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

                break;
            case "removeDeck":
                try {
                    String deckName = request.getParameters().get("deckName");
                    boolean success = DeckController.getInstance().removeDeck(user, deckName);
                    return new Response(success, "");
                } catch (Exception e) {
                    return new Response(false, e.getMessage());
                }
        }
        return new Response(false, "method not found");
    }
}