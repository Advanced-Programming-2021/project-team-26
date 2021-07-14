package controller;

import com.google.gson.Gson;

public class Handler {
    private String command;
    private Transfer transfer;
    private String result;

    public Handler(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Transfer getTransfer() {
        return transfer;
    }

    public void setTransfer(Transfer transfer) {
        this.transfer = transfer;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void run() {
        transfer = new Gson().fromJson(command, Transfer.class);

        switch (transfer.getController()) {
            case "DeckController":
                handleDeckCommands();
                break;
        }
    }

    private void handleDeckCommands() {
        switch (transfer.getMethodToCall()) {
            case "createDeck":
                result = DeckController.getInstance().createDeck(transfer.getParameters().get("deckName"));
                break;
        }
    }
}