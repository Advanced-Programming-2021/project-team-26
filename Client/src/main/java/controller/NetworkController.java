package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetworkController {
    private static NetworkController controller;
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;

    private NetworkController() {

    }

    public static NetworkController getInstance() {
        if (controller == null) {
            setupClient();
            return new NetworkController();
        }
        return controller;
    }

    public static void setupClient() {
        try {
            Socket socket = new Socket("localhost", 8080);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData(String data) {
        try {
            dataOutputStream.writeUTF(data);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readData() {
        try {
            return dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}