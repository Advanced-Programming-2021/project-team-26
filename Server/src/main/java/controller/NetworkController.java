package controller;

import model.Response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class NetworkController {
    private static NetworkController controller;


    private NetworkController() {

    }

    public static NetworkController getInstance() {
        if (controller == null)
            controller = new NetworkController();
        return controller;
    }

    public void setupServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true) {
                Socket socket = serverSocket.accept();
                Handler handler = new Handler(socket);
                handler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startNewThread(Socket socket) {
        new Thread(() -> {
            try {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
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
                Response response = process(input);
                dataOutputStream.writeUTF(response.toJSON());
                dataOutputStream.flush();
                Logger.log(input + " -> " + response.toJSON());
            } catch (SocketException | EOFException e) {
                Logger.log("Client disconnected");
                break;
            }
        }
    }

    private Response process(String input) {
//        Handler handler = new Handler(input);
//        handler.run();
//
//        return handler.getResponse();
        return null;
    }
}

class Logger{
    public static void log(String message){
        System.out.println(message);
    }
}