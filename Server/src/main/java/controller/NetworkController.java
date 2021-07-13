package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkController {
    private static NetworkController controller;
    private static Socket socket;
    private static ServerSocket serverSocket;
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

    public static ServerSocket getServerSocket() {
        return serverSocket;
    }

    public static void setServerSocket(ServerSocket serverSocket) {
        NetworkController.serverSocket = serverSocket;
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

    public void setupServer() {
        try {
            serverSocket = new ServerSocket(8080);
            socket = serverSocket.accept();
            new Thread(() -> {
                try {
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
//                    getInputAndProcess(dataInputStream, dataOutputStream);
                    dataInputStream.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}