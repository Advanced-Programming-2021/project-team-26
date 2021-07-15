package controller;

import com.google.gson.Gson;
import model.Deck;
import model.Request;
import model.Response;
import model.User;

public class Handler {
    private String command;
    private Request request;
    private Response response;

    public Handler(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Request getTransfer() {
        return request;
    }

    public void setTransfer(Request request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void run() {
        request = new Gson().fromJson(command, Request.class);

        switch (request.getController()) {
            case "UserController":
                handleUserCommands();
                break;
            case "DeckController":
                handleDeckCommands();
                break;
        }
    }

    private void handleUserCommands() {
        if (request.getMethodToCall().equals("addNewUser")) {
            try {
                String username = request.getParameters().get("username");
                String password = request.getParameters().get("password");
                String nickname = request.getParameters().get("nickname");
                boolean success = UserController.getInstance().addNewUser(username, password, nickname);
                response = new Response(success, "");
            } catch (Exception e) {
                response = new Response(false, e.getMessage());
            }
            return;
        }
        if (request.getMethodToCall().equals("loginUser")) {
            try {
                String username = request.getParameters().get("username");
                String password = request.getParameters().get("password");
                String token = UserController.getInstance().loginUser(username, password);
                response = new Response(true, token);
                response.addData("user", Database.getInstance().getLoggedInUser(token));
            } catch (Exception e) {
                response = new Response(false, e.getMessage());
            }
            return;
        }
        User user = Database.getInstance().getLoggedInUser(request.getToken());
        if (user == null) {
            response = new Response(false, "invalid token");
            return;
        }
        switch (request.getMethodToCall()) {
            case "logout":
                String token = request.getToken();
                if (Database.getInstance().isUserLoggedIn(token)) {
                    UserController.getInstance().logout(token);
                    response = new Response(true, "");
                } else
                    response = new Response(false, "");
                break;
            case "changeNickname":
                try {
                    UserController.getInstance().changeNickname(user, request.getParameter("nickname"));
                    response = new Response(true, "done");
                } catch (Exception e) {
                    response = new Response(false, e.getMessage());
                }

                break;
            case "changePassword":
                try {
                    String currentPassword = request.getParameter("currentPassword");
                    String newPassword = request.getParameter("newPassword");
                    String repeatPassword = request.getParameter("repeatPassword");
                    UserController.getInstance().changePassword(user, currentPassword, newPassword, repeatPassword);
                } catch (Exception e) {
                    response = new Response(false, e.getMessage());
                }
                break;
        }
    }

    private void handleDeckCommands() {
        User user = Database.getInstance().getLoggedInUser(request.getToken());
        if (user == null) {
            response = new Response(false, "invalid token");
            return;
        }

        switch (request.getMethodToCall()) {
            case "createDeck":
                try {
                    String deckName = request.getParameters().get("deckName");
                    Deck deck = DeckController.getInstance().createDeck(user, deckName);
                    response = new Response(true, "");
                    response.addData(deckName, deck);
                } catch (Exception e) {
                    e.printStackTrace();
                    response = new Response(false, e.getMessage());
                }

                break;
            case "removeDeck":
                try {
                    String deckName = request.getParameters().get("deckName");
                    boolean success = DeckController.getInstance().removeDeck(user, deckName);
                    response = new Response(success, "");
                } catch (Exception e) {
                    response = new Response(false, e.getMessage());
                }
        }
    }
}