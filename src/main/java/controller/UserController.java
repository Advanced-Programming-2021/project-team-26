package controller;

import exceptions.*;
import model.User;
import view.Scan;

import java.util.HashMap;
import java.util.regex.Matcher;

public class UserController {
    private static UserController userController;
    private final HashMap<String, User> allUsers;
    private User user;

    private UserController() {
        allUsers = Database.getInstance().getAllUsers();
    }

    public static UserController getInstance() {
        if (userController == null)
            userController = new UserController();
        return userController;
    }

    public void addNewUser(Matcher matcher) throws InvalidInput, DuplicateUsername, DuplicateNickname {
        String[] rawInput = matcher.group().split("\\s+");
        HashMap<String, String> input = Scan.getInstance().parseInput(rawInput);

        String username = null;
        if (input.containsKey("username"))
            username = input.get("username");
        else if (input.containsKey("u"))
            username = input.get("u");
        if (username == null)
            throw new InvalidInput();

        String nickname = null;
        if (input.containsKey("nickname"))
            nickname = input.get("nickname");
        else if (input.containsKey("n"))
            nickname = input.get("n");
        if (nickname == null)
            throw new InvalidInput();

        String password = null;
        if (input.containsKey("password"))
            password = input.get("password");
        else if (input.containsKey("p"))
            password = input.get("p");
        if (password == null)
            throw new InvalidInput();

        if (checkUsernameExistence(username))
            throw new DuplicateUsername();
        if (checkNicknameExistence(nickname))
            throw new DuplicateNickname();

        User user = new User(username, password, nickname);
        allUsers.put(username, user);
        Database.getInstance().writeUser(user);
    }

    public void removeUser(Matcher matcher) {

    }

    public void changePassword(Matcher matcher) throws InvalidInput, WrongPassword, SamePassword {
        String[] rawInput = matcher.group().split("\\s+");
        HashMap<String, String> input = Scan.getInstance().parseInput(rawInput);

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

        if (!user.getPassword().equals(currentPassword))
            throw new WrongPassword();

        if (currentPassword.equals(newPassword))
            throw new SamePassword();

        user.setPassword(newPassword);
    }

    public void changeNickname(Matcher matcher) throws InvalidInput, DuplicateNickname {
        String[] rawInput = matcher.group().split("\\s+");
        HashMap<String, String> input = Scan.getInstance().parseInput(rawInput);

        String nickname = null;
        if (input.containsKey("nickname"))
            nickname = input.get("nickname");
        else if (input.containsKey("n"))
            nickname = input.get("n");
        if (nickname == null)
            throw new InvalidInput();

        if (checkNicknameExistence(nickname))
            throw new DuplicateNickname();

        user.setNickname(nickname);
    }

    public void loginUser(Matcher matcher) throws InvalidInput, WrongUsernamePassword {
        String[] rawInput = matcher.group().split("\\s+");
        HashMap<String, String> input = Scan.getInstance().parseInput(rawInput);

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

        user = User.getUserByUsername(username);
        if (user == null || !user.getPassword().equals(password))
            throw new WrongUsernamePassword();

    }

    public void logout(Matcher matcher) {
        user = null;
    }

    private boolean checkUsernameExistence(String username) {
        return allUsers.containsKey(username);
    }

    private boolean checkNicknameExistence(String nickname) {
        for (User user : allUsers.values()) {
            if (user.getNickname().equals(nickname))
                return true;
        }
        return false;
    }

    private boolean checkPassword(String username, String password) {
        return true;
    }

}
