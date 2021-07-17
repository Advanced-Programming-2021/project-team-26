package controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import model.Request;
import model.Response;

public class HeadOrTail {
    private final HeadOrTailController controller;
    private final int turn;
    private final Handler handler;
    private boolean isClosed = false;

    public HeadOrTail(Handler handler, HeadOrTailController controller, int turn) {
        this.handler = handler;
        this.controller = controller;
        this.turn = turn;
    }

    public void run() {
        new Thread(() -> {
            while (!isClosed) {
                Request request = handler.getRequest();
                if (request.getMethodToCall().equals("throwCoin")) {
                    throwCoin();
                    return;
                } else
                    handler.sendResponse(new Response(false, ""));
            }
        }).start();
    }

    void throwCoin() {
        controller.getSign(handler,turn);
    }

    public void close() {
        isClosed = true;
    }
}
