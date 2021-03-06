package controller;

import exceptions.*;
import model.User;

import java.io.File;

public class UserController {
    private static UserController userController;

    private UserController() {

    }

    public static UserController getInstance() {
        if (userController == null)
            userController = new UserController();
        return userController;
    }

    private void initializeUserDecks() {

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

    public void changePassword(User user, String currentPassword, String newPassword, String repeatPassword) throws InvalidInput, WrongPassword, SamePassword {
        if (!newPassword.equals(repeatPassword))
            throw new RuntimeException("repeat password not matched");
        if (!Database.getInstance().getCurrentUser().getPassword().equals(currentPassword))
            throw new WrongPassword();

        if (currentPassword.equals(newPassword))
            throw new SamePassword();

        user.setPassword(newPassword);
    }

    public void changeNickname(User user, String nickname) throws InvalidInput, DuplicateNickname {
        if (user.getNickname().equals(nickname))
            return;
        if (User.checkNicknameExistence(nickname))
            throw new DuplicateNickname(nickname);

        user.setNickname(nickname);
    }

    public String loginUser(String username, String password) throws InvalidInput, WrongUsernamePassword {
        if (username.equals("") || password.equals(""))
            throw new InvalidInput();

        User user = User.getUserByUsername(username);
        if (user == null || !user.getPassword().equals(password))
            throw new WrongUsernamePassword();
        if (Database.getInstance().isUsernameLoggedIn(username))
            throw new RuntimeException("User Already Logged In");
        return Database.getInstance().addLoggedInUser(user);
    }

    public void logout(String token) {
        Database.getInstance().removeLoggedInUser(token);
    }

    public void changeProfile(User user, File file) {
        if (!user.setProfileImage(file))
            throw new RuntimeException("failed");
    }
}
