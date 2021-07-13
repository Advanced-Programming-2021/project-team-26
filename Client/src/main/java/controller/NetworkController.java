package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetworkController {
    private static NetworkController controller;
    private static Socket socket;
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;

    private NetworkController() {

    }

    public static Socket getSocket() {
        return socket;
    }

    public static void setSocket(Socket socket) {
        NetworkController.socket = socket;
    }

    public static DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public static void setDataInputStream(DataInputStream dataInputStream) {
        NetworkController.dataInputStream = dataInputStream;
    }

    public static DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public static void setDataOutputStream(DataOutputStream dataOutputStream) {
        NetworkController.dataOutputStream = dataOutputStream;
    }

    public static NetworkController getInstance() {
        if (controller == null) return new NetworkController();
        return controller;
    }

    public void setupClient() {
        try {
            socket = new Socket("localhost", 8080);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}