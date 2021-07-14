package controller;

import org.json.JSONException;
import org.json.JSONObject;

public class Transfer {
    private static Transfer transfer;

    private Transfer() {

    }

    public static Transfer getInstance() {
        if (transfer == null) return new Transfer();
        return transfer;
    }

    public String commandMaker(String commandType, String command) {
        JSONObject json = new JSONObject();
        try {
            json.put(commandType, command);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }
}
