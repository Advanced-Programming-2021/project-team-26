package controller;

import exceptions.InvalidDeckException;
import exceptions.NoActiveDeck;
import exceptions.NoPlayerAvailable;
import exceptions.NotSupportedRoundNumber;
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

    public Handler createNewGameWithRealPlayer(Handler handler, int round) {
        User user = handler.getUser();
        if (user.getActiveDeck() == null)
            throw new NoActiveDeck(user.getUsername());

        if (!user.getActiveDeck().isDeckValid())
            throw new InvalidDeckException(user.getUsername());

        if (round != 1 && round != 3)
            throw new NotSupportedRoundNumber();

        return Database.getInstance().findMatch(handler, round);
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
