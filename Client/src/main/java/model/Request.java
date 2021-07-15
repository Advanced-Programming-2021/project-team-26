package model;

import com.google.gson.Gson;
import controller.Database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
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

    public void addParameter(String key, String value) {
        if (parameters == null)
            parameters = new HashMap<>();
        parameters.put(key, value);
    }

    public void addParameter(String key, Object value) {
        addParameter(key, new Gson().toJson(value));
    }

    public String getParameter(String key) {
        if (parameters.containsKey(key))
            return parameters.get(key);
        return null;
    }

    public void addFile(String key, File file) throws IOException {
        byte[] content = Files.readAllBytes(file.toPath());
        addParameter(key, Base64.getEncoder().encodeToString(content));
    }

    public File getFile(String key) {
        String string = getParameter(key);
        if (string == null)
            return null;
        byte[] bytes = Base64.getDecoder().decode(string);
        try {
            Path path = Files.createTempFile("tmp", "tmp");
            return Files.write(path, bytes).toFile();
        } catch (IOException e) {
            return null;
        }

    }

    public String toJSON() {
        return new Gson().toJson(this);
    }
}
