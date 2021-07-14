package controller;

import com.google.gson.Gson;

import java.util.HashMap;

public class Transfer {
    private String controller;
    private String methodToCall;
    private HashMap<String, String> parameters;

    public Transfer(String controller, String methodToCall, HashMap<String, String> parameters) {
        setController(controller);
        setMethodToCall(methodToCall);
        setParameters(parameters);
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

    public String toJSON() {
        return new Gson().toJson(this);
    }
}
