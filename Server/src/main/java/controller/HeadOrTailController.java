package controller;

import exceptions.NoPlayerAvailable;
import model.Request;
import model.Response;

import java.util.concurrent.ThreadLocalRandom;

public class HeadOrTailController {
    int winner = -1;
    int starter;
    Handler[] players = new Handler[2];
    HeadOrTail[] headOrTails = new HeadOrTail[2];
    int round;
    boolean[] isSignSet = new boolean[]{false, false};

    public HeadOrTailController(Handler firstUser, Handler secondUser, int round) {
        players[0] = firstUser;
        players[1] = secondUser;

        headOrTails[0] = new HeadOrTail(firstUser, this, 0);
        headOrTails[1] = new HeadOrTail(secondUser, this, 1);

        this.round = round;
    }

    public void run() {
        players[0].stopGetInput();
        players[1].stopGetInput();
        headOrTails[0].run();
        headOrTails[1].run();
    }

    public void getSign(Handler handler, int turn) {
        String result;
        if (turn == 0) {
            isSignSet[0] = true;
            result = "B";
        } else {
            isSignSet[1] = true;
            result = "#";
        }

        handler.sendResponse(new Response(true, result));
        if (isSignSet[0] && isSignSet[1])
            startThrowCoin();
    }

    private void startThrowCoin() {
        int result = ThreadLocalRandom.current().nextInt(2);
        winner = result;

        players[0].sendResponse(new Response(true, String.valueOf(result)));
        players[1].sendResponse(new Response(true, String.valueOf(result)));


        Request request = players[winner].getRequest();
        starter = Integer.parseInt(request.getParameter("starter"));
        Response response = new Response(true,"");
        response.addData("starter",String.valueOf(starter));
        players[1-winner].sendResponse(response);
        startGame();
    }

    private void startGame() {
        headOrTails[0].close();
        headOrTails[1].close();

        try {
            new GameController(players[starter], players[1 - starter], round).run();
        } catch (NoPlayerAvailable noPlayerAvailable) {
            noPlayerAvailable.printStackTrace();
        }
    }
}
