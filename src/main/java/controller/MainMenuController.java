package controller;

import exceptions.*;
import model.User;
import view.Scan;
import view.menus.*;

import java.util.HashMap;
import java.util.regex.Matcher;

public class MainMenuController {
    public MainMenuController() {

    }

    public String creatNewGameWithAI(Matcher matcher) {
        HashMap<String, String> input = Scan.getInstance().parseInput(matcher.group());

        String roundString = Scan.getInstance().getValue(input, "round", "r");
        if (roundString == null)
            throw new InvalidInput();
        int round;
        try {
            round = Integer.parseInt(roundString);
        } catch (Exception e) {
            throw new InvalidInput();
        }
        if (round != 1 && round != 3)
            throw new NotSupportedRoundNumber();

        GameController gameController = null;
        try {
            gameController = new GameController(Database.getInstance().getCurrentUser(), round);
        } catch (NoPlayerAvailable ignored) {

        }
        new DuelMenu(gameController).execute();
        return null;
    }

    public String createNewGameWithRealPlayer(Matcher matcher) {
        HashMap<String, String> input = Scan.getInstance().parseInput(matcher.group());
        String secondUsername = Scan.getInstance().getValue(input, "second-player", "sp");
        if (secondUsername == null)
            throw new InvalidInput();
        String roundString = Scan.getInstance().getValue(input, "rounds", "r");
        if (roundString == null)
            throw new InvalidInput();
        int round;
        try {
            round = Integer.parseInt(roundString);
        } catch (Exception e) {
            throw new InvalidInput();
        }
        User firstUser = Database.getInstance().getCurrentUser();
        User secondUser = User.getUserByUsername(secondUsername);
        if (secondUser == null)
            throw new UsernameNotFoundException();
        if (firstUser.getActiveDeck() == null)
            throw new NoActiveDeck(firstUser.getUsername());
        if (secondUser.getActiveDeck() == null)
            throw new NoActiveDeck(secondUser.getUsername());

        if (firstUser.getUsername().equals(secondUser.getUsername())) {
            throw new PlayWithYourself();
        }

        if (firstUser.getActiveDeck().isDeckValid())
            throw new InvalidDeckException(firstUser.getUsername());
        if (secondUser.getActiveDeck().isDeckValid())
            throw new InvalidDeckException(secondUser.getUsername());

        if (round != 1 && round != 3)
            throw new NotSupportedRoundNumber();

        try {
            GameController gameController = new GameController(firstUser, secondUser, round);
            DuelMenu.setGameController(gameController);
            new DuelMenu(gameController).execute();
        } catch (NoPlayerAvailable ignored) {

        }
        return null;
    }

    public String enterMenu(Matcher matcher) throws InvalidMenuException{
        String newMenu = matcher.group(1);
        if (newMenu.contains("Login")) {
            new LoginMenu().execute();
            return "_______MAIN MENU_______";
        }
        else if (newMenu.contains("Deck")) {
            new DeckMenu().execute();
            return "_______MAIN MENU_______";
        }
        else if (newMenu.contains("Export")) {
            new InterchangeMenu().execute();
            return "_______MAIN MENU_______";
        }
        else if (newMenu.contains("Shop")) {
            new ShopMenu().execute();
            return "_______MAIN MENU_______";
        }
        else if (newMenu.contains("Profile")) {
            new ProfileMenu().execute();
            return "_______MAIN MENU_______";
        }
        else if (newMenu.contains("Score")) {
            new ScoreBoardMenu().execute();
            return "_______MAIN MENU_______";
        }
        throw new InvalidMenuException();
    }
}
