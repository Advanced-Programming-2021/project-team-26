package model;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;

public class Response {
    private boolean success;
    private String message;
    private HashMap<String, String> data;

    public Response(boolean success, String message) {
        setSuccess(success);
        setMessage(message);
    }

    public String getData(String key) {
        if (data == null)
            return null;
        if (data.containsKey(key))
            return data.get(key);
        return null;
    }

    public void addData(String key, Object value) {
        addData(key, new Gson().toJson(value));
    }

    public void addData(String key, String value) {
        if (data == null)
            data = new HashMap<>();
        data.put(key, value);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean addFile(String key, File file){
        try {
            byte[] content = Files.readAllBytes(file.toPath());
            addData(key, Base64.getEncoder().encodeToString(content));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public File getFile(String key){
        String string = getData(key);
        if (string==null)
            return null;
        byte[] bytes = Base64.getDecoder().decode(string);
        try {
            Path path = Files.createTempFile("tmp","tmp");
            return Files.write(path,bytes).toFile();
        } catch (IOException e) {
            return null;
        }

    }

    public String toJSON() {
        return new Gson().toJson(this);
    }
}
