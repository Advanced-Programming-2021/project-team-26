package model;

import com.google.gson.Gson;
import controller.Database;

import java.util.HashMap;

public class Request {
    private String token;
    private String controller;
    private String methodToCall;
    private HashMap<String, String> parameters;

    public Request(String controller, String methodToCall, HashMap<String, String> parameters) {
        setToken();
        setController(controller);
        setMethodToCall(methodToCall);
        setParameters(parameters);
    }

    public Request(String controller, String methodToCall) {
        setToken();
        setController(controller);
        setMethodToCall(methodToCall);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setToken() {
        this.token = Database.getInstance().getToken();
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getMethodToCall() {
        return methodToCall;
    }

    public void setMethodToCall(String methodToCall) {
        this.methodToCall = methodToCall;
    }

    public HashMap<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(String key,String value){
        if(parameters==null)
            parameters = new HashMap<>();
        parameters.put(key,value);
    }

//    public String commandMaker(String controller, String methodToCall, HashMap<String, String> parameters) {
//        JSONObject json = new JSONObject();
//        try {
//            json.put("controller", controller);
//            json.put("methodToCall", methodToCall);
//
//            JSONObject json1 = new JSONObject();
//
//            for (String parameterType : parameters.keySet()) {
//                json1.put(parameterType, parameters.get(parameterType));
//            }
//            JSONArray parametersArray = new JSONArray();
//            parametersArray.put(json1);
//
//            json.put("parameters", parametersArray);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return json.toString();
//    }

    public String toJSON() {
        return new Gson().toJson(this);
    }
}
