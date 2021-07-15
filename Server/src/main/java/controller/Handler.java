package controller;

import com.google.gson.Gson;
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
        switch (request.getMethodToCall()) {
            case "addNewUser":
                try {
                    String username = request.getParameters().get("username");
                    String password = request.getParameters().get("password");
                    String nickname = request.getParameters().get("nickname");
                    boolean success = UserController.getInstance().addNewUser(username, password, nickname);
                    response = new Response(success, "");
                } catch (Exception e) {
                    response = new Response(false, e.getMessage());
                }
                break;
            case "loginUser":
                try {
                    String username = request.getParameters().get("username");
                    String password = request.getParameters().get("password");
                    String token = UserController.getInstance().loginUser(username, password);
                    response = new Response(true, token);
                    response.addData("user", Database.getInstance().getLoggedInUser(token));
                } catch (Exception e) {
                    response = new Response(false, e.getMessage());
                }
                break;
            case "logout":
                String token = request.getToken();
                if (Database.getInstance().isUserLoggedIn(token)) {
                    UserController.getInstance().logout(token);
                    response = new Response(true, "");
                } else
                    response = new Response(false, "");
                break;
            case "changeNickname":
                User user = Database.getInstance().getLoggedInUser(request.getToken());
                if (user == null)
                    response = new Response(false, "invalid token");
                else
                    try {
                        UserController.getInstance().changeNickname(user,request.getParameters().get("nickname"));
                        response = new Response(true,"done");
                    }catch (Exception e){
                        response = new Response(false,e.getMessage());
                    }

                break;
        }
    }

    private void handleDeckCommands() {
        switch (request.getMethodToCall()) {
            case "createDeck":
                //response = DeckController.getInstance().createDeck(request.getParameters().get("deckName"));
                break;
        }
    }
}