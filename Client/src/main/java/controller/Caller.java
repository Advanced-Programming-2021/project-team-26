package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Caller extends Thread{
    private GameController gameController;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private boolean end;

    public Caller(GameController gameController,Socket socket) throws IOException {
        this.gameController = gameController;
        this.socket = socket;
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        while (!end){

        }
    }

    public void end() {
        this.end = true;
        //TODO terminate thread
    }
}
