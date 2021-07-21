package controller;

import com.google.gson.Gson;
import model.Request;
import model.Response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Caller {
    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;

    public Caller(Socket socket) throws IOException {
        this.socket = socket;
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    public Response sendAndReceive(Request request) {
        if (!sendRequest(request))
            return null;
        return getResponse();
    }

    public boolean sendRequest(Request request) {
        try {
            Logger.log("caller send:"+request.toJSON());
            dataOutputStream.writeUTF(request.toJSON());
            dataOutputStream.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Response getResponse() {
        try {
            String responseString = dataInputStream.readUTF();
            Logger.log("caller get:"+responseString);
            return new Gson().fromJson(responseString, Response.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
