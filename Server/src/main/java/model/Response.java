package model;

import com.google.gson.Gson;

public class Response {
    private boolean success;
    private String message;

    public Response(boolean success, String message) {
        setSuccess(success);
        setMessage(message);
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

    public String toJSON() {
        return new Gson().toJson(this);
    }
}
