package controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Transfer {
    private static Transfer transfer;

    private Transfer() {

    }

    public static Transfer getInstance() {
        if (transfer == null) return new Transfer();
        return transfer;
    }

    public String commandMaker(String controller, String methodToCall, HashMap<String, String> parameters) {
        JSONObject json = new JSONObject();
        try {
            json.put("controller", controller);
            json.put("methodToCall", methodToCall);

            JSONObject json1 = new JSONObject();

            for (String parameterType : parameters.keySet()) {
                json1.put(parameterType, parameters.get(parameterType));
            }
            JSONArray parametersArray = new JSONArray();
            parametersArray.put(json1);

            json.put("parameters", parametersArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }
}
