package controller;

import com.google.gson.Gson;
import exceptions.NoPlayerAvailable;
import exceptions.NotSupportedRoundNumber;
import model.Request;
import model.Response;
import model.User;

public class MainMenuController {
    private static MainMenuController instance;

    private MainMenuController() {

    }

    public static MainMenuController getInstance() {
        if (instance == null)
            instance = new MainMenuController();
        return instance;
    }

    public void creatNewGameWithAI(Integer round) {
        if (round != 1 && round != 3)
            throw new NotSupportedRoundNumber();

        GameController gameController = null;
        try {
            gameController = new GameController(Database.getInstance().getCurrentUser(), round);
        } catch (NoPlayerAvailable ignored) {

        }
        //TODO load duel menu
    }

    public void createNewGameWithRealPlayer(int round) {
        Request request = new Request("MainMenuController", "newGame");
        request.addParameter("round", String.valueOf(round));

        Response response = NetworkController.getInstance().sendAndReceive(request);
        if (!response.isSuccess())
            throw new RuntimeException(response.getMessage());

        int turn = Integer.parseInt(response.getData("turn"));
        if (response.getData("user") != null) {
            User first = Database.getInstance().getCurrentUser();
            User second = new Gson().fromJson(response.getData("user"), User.class);
            new HeadOrTailController(first, second, round, turn).run();
            return;
        }
        //TODO show waiting page
        while (true) {
            request = new Request("MainMenuController", "foundPlayer");
            response = NetworkController.getInstance().sendAndReceive(request);

            if (response.isSuccess()) {
                User first = Database.getInstance().getCurrentUser();
                User second = new Gson().fromJson(response.getData("user"), User.class);
                new HeadOrTailController(first, second, round, turn).run();
                return;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        if (secondUsername == null)
//            throw new InvalidInput();
//
//        User firstUser = Database.getInstance().getCurrentUser();
//        User secondUser = User.getUserByUsername(secondUsername);
//        if (secondUser == null)
//            throw new UsernameNotFoundException();
//        if (firstUser.getActiveDeck() == null)
//            throw new NoActiveDeck(firstUser.getUsername());
//        if (secondUser.getActiveDeck() == null)
//            throw new NoActiveDeck(secondUser.getUsername());
//
//        if (firstUser.getUsername().equals(secondUser.getUsername())) {
//            throw new PlayWithYourself();
//        }
//
//        if (!firstUser.getActiveDeck().isDeckValid())
//            throw new InvalidDeckException(firstUser.getUsername());
//        if (!secondUser.getActiveDeck().isDeckValid())
//            throw new InvalidDeckException(secondUser.getUsername());
//
//        if (round != 1 && round != 3)
//            throw new NotSupportedRoundNumber();
//
//        new HeadOrTailController(firstUser, secondUser, round).run();
    }

//    public String enterMenu(Matcher matcher) throws InvalidMenuException {
//        String newMenu = matcher.group(1);
//        if (newMenu.contains("Login")) {
//            new LoginMenu().execute();
//            return "_______MAIN MENU_______";
//        } else if (newMenu.contains("Deck")) {
//            new DeckMenu().execute();
//            return "_______MAIN MENU_______";
//        } else if (newMenu.contains("Export")) {
//            new InterchangeMenu().execute();
//            return "_______MAIN MENU_______";
//        } else if (newMenu.contains("Shop")) {
//            new ShopMenu().execute();
//            return "_______MAIN MENU_______";
//        } else if (newMenu.contains("Profile")) {
//            new ProfileMenu().execute();
//            return "_______MAIN MENU_______";
//        } else if (newMenu.contains("Score")) {
//            new ScoreBoardMenu().execute();
//            return "_______MAIN MENU_______";
//        }
//        throw new InvalidMenuException();
//    }
}
