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

    public String changePassword(Matcher matcher) throws InvalidInput, WrongPassword, SamePassword {
        HashMap<String, String> input = Scan.getInstance().parseInput(matcher.group());

        if (!input.containsKey("password") || !input.containsKey("p"))
            throw new InvalidInput();

        String currentPassword = null;
        if (input.containsKey("current"))
            currentPassword = input.get("current");
        else if (input.containsKey("c"))
            currentPassword = input.get("c");
        if (currentPassword == null)
            throw new InvalidInput();

        String newPassword = null;
        if (input.containsKey("new"))
            newPassword = input.get("new");
        else if (input.containsKey("n"))
            newPassword = input.get("n");
        if (newPassword == null)
            throw new InvalidInput();

        if (!Database.getInstance().getCurrentUser().getPassword().equals(currentPassword))
            throw new WrongPassword();

        if (currentPassword.equals(newPassword))
            throw new SamePassword();

        Database.getInstance().getCurrentUser().setPassword(newPassword);
        return "password changed successfully!";
    }

    public String changeNickname(Matcher matcher) throws InvalidInput, DuplicateNickname {
        HashMap<String, String> input = Scan.getInstance().parseInput(matcher.group());

        String nickname = null;
        if (input.containsKey("nickname"))
            nickname = input.get("nickname");
        else if (input.containsKey("n"))
            nickname = input.get("n");
        if (nickname == null)
            throw new InvalidInput();

        if (User.checkNicknameExistence(nickname))
            throw new DuplicateNickname(nickname);

        Database.getInstance().getCurrentUser().setNickname(nickname);
        return "nickname changed successfully!";
    }

    public String loginUser(Matcher matcher) throws InvalidInput, WrongUsernamePassword {
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
        Database.getInstance().setCurrentUser(user);
//        new MainMenu().execute();
        return "______LOGIN MENU______";
    }

    public String logout(Matcher matcher) {
        Database.getInstance().setCurrentUser(null);
        Menu.exitMenu(null);
        return "user logged out successfully!";
    }
}
