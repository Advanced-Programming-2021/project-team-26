package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class NetworkController {
    private static NetworkController controller;


    private NetworkController() {

    }

    public static NetworkController getInstance() {
        if (controller == null) return new NetworkController();
        return controller;
    }

    public void setupServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true) {
                Socket socket = serverSocket.accept();
                startNewThread(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startNewThread(Socket socket) {
        new Thread(() -> {
            try {
               DataInputStream  dataInputStream = new DataInputStream(socket.getInputStream());
               DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                getInputAndProcess(dataInputStream, dataOutputStream);
                dataInputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void getInputAndProcess(DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException {
        while (true) {
            try {
                String input = dataInputStream.readUTF();
                String result = process(input);
                dataOutputStream.writeUTF(result);
                dataOutputStream.flush();
            } catch (SocketException e) {
                System.out.println("Client disconnected");
                break;
            }
        }
    }

    private String process(String input) {
        Handler handler =  new Handler(input);
        handler.run();

        return handler.getResult();
    }
}