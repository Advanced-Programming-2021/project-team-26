package controller;

import exceptions.*;
import model.User;
import view.Menu;
import view.Scan;

import java.util.HashMap;
import java.util.regex.Matcher;

public class UserController {
    private static UserController userController;

    private UserController() {
    }

    public static UserController getInstance() {
        if (userController == null)
            userController = new UserController();
        return userController;
    }

    public boolean addNewUser(String username, String password, String nickname) throws InvalidInput, DuplicateUsername, DuplicateNickname {
        if (username.equals("") || password.equals("") || nickname.equals(""))
            throw new InvalidInput();

        if (User.checkUsernameExistence(username))
            throw new DuplicateUsername(username);
        if (User.checkNicknameExistence(nickname))
            throw new DuplicateNickname(nickname);

        new User(username, password, nickname);
        return true;
    }

    public String removeUser(Matcher matcher) throws InvalidInput, WrongUsernamePassword {
        HashMap<String, String> input = Scan.getInstance().parseInput(matcher.group());

        String username = null;
        if (input.containsKey("username"))
            username = input.get("username");
        else if (input.containsKey("u"))
            username = input.get("u");
        if (username == null)
            throw new InvalidInput();

        String password = null;
        if (input.containsKey("password"))
            password = input.get("password");
        else if (input.containsKey("p"))
            password = input.get("p");
        if (password == null)
            throw new InvalidInput();

        User user = User.getUserByUsername(username);
        if (user == null || !user.getPassword().equals(password))
            throw new WrongUsernamePassword();

        User.removeUser(user.getUsername());
        return "user removed successfully!";
    }

    public void changePassword(String currentPassword, String newPassword, String repeatPassword) throws InvalidInput, WrongPassword, SamePassword {
        if (!newPassword.equals(repeatPassword))
            throw new RuntimeException("repeat password not matched");
        if (!Database.getInstance().getCurrentUser().getPassword().equals(currentPassword))
            throw new WrongPassword();

        if (currentPassword.equals(newPassword))
            throw new SamePassword();

        Database.getInstance().getCurrentUser().setPassword(newPassword);
    }

    public void changeNickname(String nickname) throws InvalidInput, DuplicateNickname {
        if (Database.getInstance().getCurrentUser().getNickname().equals(nickname))
            return;
        if (User.checkNicknameExistence(nickname))
            throw new DuplicateNickname(nickname);

        Database.getInstance().getCurrentUser().setNickname(nickname);
    }

    public boolean loginUser(String username, String password) throws InvalidInput, WrongUsernamePassword {
        if (username.equals("") || password.equals(""))
            throw new InvalidInput();

        User user = User.getUserByUsername(username);
        if (user == null || !user.getPassword().equals(password))
            throw new WrongUsernamePassword();
        Database.getInstance().setCurrentUser(user);
        return true;
    }

    public String logout() {
        Database.getInstance().setCurrentUser(null);
        //TODO edit here
        Menu.exitMenu(null);
        return "user logged out successfully!";
    }
}
