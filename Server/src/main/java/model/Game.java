package model;

import controller.*;
import exceptions.NoPlayerAvailable;
import model.cards.Card;
import view.Print;

import java.util.ArrayList;

public class Game {
    public final static int LIFE_POINT = 8000;
    private final Board[] boards = new Board[2];
    private final User[] users = new User[2];
    private final int[] lifePoints = new int[2];
    private GameController gameController;
    private boolean firstTurn = true;
    private int turn = 1;
    private Phase phase = Phase.END;
    private boolean finished = false;
    private boolean summonOrSetThisTurn = false;
    private int surrenderPlayer = -1;
    private int winner = -1;
    public Game(GameController gameController, User first, User second, Deck firstDeck, Deck secondDeck) throws NoPlayerAvailable {
        if (first == null || second == null)
            throw new NoPlayerAvailable();
        this.gameController = gameController;
        users[0] = first;
        users[1] = second;
        boards[0] = new Board(gameController, firstDeck);
        boards[1] = new Board(gameController, secondDeck);
        lifePoints[0] = LIFE_POINT;
        lifePoints[1] = LIFE_POINT;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        boards[0].setGameController(gameController);
        boards[1].setGameController(gameController);
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getSurrenderPlayer() {
        return surrenderPlayer;
    }

    public void setSurrenderPlayer(int surrenderPlayer) {
        this.surrenderPlayer = surrenderPlayer;
        winner = 1 - surrenderPlayer;
        finished = true;
    }

    public boolean isSummonOrSetThisTurn() {
        return summonOrSetThisTurn;
    }

    public void setSummonOrSetThisTurn(boolean summonOrSetThisTurn) {
        this.summonOrSetThisTurn = summonOrSetThisTurn;
    }

    public void nextPhase() {
        if (finished)
            return;
        Phase nextPhase = this.phase.nextPhase();
        if (firstTurn && nextPhase == Phase.BATTLE)
            nextPhase = null;
        if (nextPhase == null) {
            changeTurn();
            nextPhase = Phase.DRAW;
        }
        this.phase = nextPhase;
        gameController.getViews()[0].updatePhase();
        gameController.getViews()[1].updatePhase();
        if (this.phase == Phase.DRAW) {
            drawPhase();
            nextPhase();
        } else if (this.phase == Phase.STANDBY) {
            standByPhase();
            nextPhase();
        } else if (this.phase == Phase.MAIN1 || this.phase == Phase.MAIN2 || this.phase == Phase.BATTLE) {
            if (this.phase == Phase.MAIN1) {
                for (MonsterController monster : getThisBoard().getMonstersZone())
                    monster.runMonsterEffectOnEachTurn();
            }
            if (getThisUser() instanceof Ai) {
                switch (this.phase) {
                    case MAIN1:
                        ((Ai) getThisUser()).mainPhase1();
                        break;
                    case MAIN2:
                        ((Ai) getThisUser()).mainPhase2();
                        break;
                    case BATTLE:
                        ((Ai) getThisUser()).battlePhase();
                        break;
                }
                nextPhase();
            } else
                Print.getInstance().printGame(this);
        } else if (this.phase == Phase.END) {
            nextPhase();
        }
    }

    public void standByPhase() {
        getThisBoard().standByPhase();
    }

    public void drawPhase() {
        Card card = getThisBoard().addCardToHand();
        if (card == null) {
            finished = true;
            winner = 1 - turn;
            gameController.endGame();
            return;
        }
        Print.getInstance().printMessage("new card added to the hand : " + card.getName());
    }

    public void changeTurn() {
        if (firstTurn && this.turn == 0)
            firstTurn = false;
        this.turn = 1 - this.turn;
        summonOrSetThisTurn = false;
        gameController.deselect();
        ArrayList<MonsterController> monsterControllers = MonsterController.getAllMonsterControllers();
        for (MonsterController monsterController : monsterControllers) {
            monsterController.nextTurn();
            monsterController.setHasActivateEffectThisTurn(false);
            monsterController.setHasPositionChanged(false);
        }

        for (SpellTrapController spellTrapController : SpellController.getAllSpellControllers()) {
            spellTrapController.nextTurn();
        }

        ArrayList<SpellTrapController> spellTrapControllers = SpellController.getAllSpellControllers();
        for (SpellTrapController spellTrapController : spellTrapControllers) {
            if (spellTrapController instanceof SpellController) {
                SpellController spellController = (SpellController) spellTrapController;
                spellController.endActivation();
            }
        }

        Print.getInstance().printMessage("its " + getThisUser().getNickname() + "’s turn");
    }

    public void temporaryChangeTurn() {
        this.turn = 1 - this.turn;
        gameController.deselect();
        gameController.setTemporaryTurnChange(!gameController.isTemporaryTurnChange());
        Print.getInstance().printMessage("now it will be " + users[turn].getUsername() + "’s turn");
        Print.getInstance().printGame(this);
    }

    public int getTurn() {
        return turn;
    }

    public User getUser(int turn) {
        return users[turn];
    }

    public User getThisUser() {
        return users[turn];
    }

    public User getOtherUser() {
        return users[1 - turn];
    }

    public Board getBoard(int turn) {
        return boards[turn];
    }

    public Board getThisBoard() {
        return boards[turn];
    }

    public Board getOtherBoard() {
        return boards[1 - turn];
    }

    public int getLifePoint(int turn) {
        return lifePoints[turn];
    }

    public int getThisLifePoint() {
        return lifePoints[turn];
    }

    public int getOtherLifePint() {
        return lifePoints[1 - turn];
    }

    public void decreaseLifePoint(int turn, int amount) {
        lifePoints[turn] -= amount;
        gameController.getViews()[0].updateLifePoint();
        gameController.getViews()[1].updateLifePoint();
        if (lifePoints[turn] <= 0) {
            finished = true;
            winner = 1 - turn;
            gameController.endGame();
        }
    }

    public void decreaseThisLifePoint(int amount) {
        decreaseLifePoint(turn, amount);
    }

    public void decreaseOtherLifePoint(int amount) {
        decreaseLifePoint(1 - turn, amount);
    }

    public Phase getPhase() {
        return this.phase;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public Response handle(Request request) {
        switch (request.getParameter("function")){
            case "getTurn":
                Response response = new Response(true,"");
                response.addData("turn",this.turn);
                return response;
            case "getLifePoint":
                int wantedTurn = Integer.parseInt(request.getParameter("turn"));
                response = new Response(true,"");
                response.addData("lp",getLifePoint(wantedTurn));
                return response;
            case "getPhase":
                response = new Response(true,"");
                response.addData("phase",getPhase());
        }
        return new Response(false,"method not found");
    }
}
