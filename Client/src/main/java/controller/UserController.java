package controller;

import com.google.gson.Gson;
import exceptions.*;
import fxmlController.App;
import model.Request;
import model.Response;
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

    private void initializeUserDecks() {

    }

    public boolean addNewUser(String username, String password, String nickname) throws InvalidInput, DuplicateUsername, DuplicateNickname {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("password", password);
        parameters.put("nickname", nickname);
        Request request = new Request("UserController", "addNewUser", parameters);
        Response response = NetworkController.getInstance().sendAndReceive(request);
        if (response.isSuccess())
            return true;
        throw new RuntimeException(response.getMessage());
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

        Request request = new Request("UserController", "changePassword");
        request.addParameter("currentPassword", currentPassword);
        request.addParameter("newPassword", newPassword);
        request.addParameter("repeatPassword", repeatPassword);
        Response response = NetworkController.getInstance().sendAndReceive(request);
        if (response.isSuccess())
            Database.getInstance().getCurrentUser().setPassword(newPassword);
        throw new RuntimeException(response.getMessage());
    }

    public void changeNickname(String nickname) throws InvalidInput, DuplicateNickname {
        if (Database.getInstance().getCurrentUser().getNickname().equals(nickname))
            return;
        if (nickname.equals(""))
            return;
        Request request = new Request("UserController", "changeNickname");
        request.addParameter("nickname", nickname);
        Response response = NetworkController.getInstance().sendAndReceive(request);
        if (response.isSuccess())
            Database.getInstance().getCurrentUser().setNickname(nickname);
        else
            throw new RuntimeException(response.getMessage());
    }

    public boolean loginUser(String username, String password) throws InvalidInput, WrongUsernamePassword {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("password", password);
        Request request = new Request("UserController", "loginUser", parameters);
        Response response = NetworkController.getInstance().sendAndReceive(request);
        if (response.isSuccess()) {
            User user = new Gson().fromJson(response.getData("user"), User.class);
            Database.getInstance().setCurrentUser(user, response.getMessage());
            return true;
        }
        throw new RuntimeException(response.getMessage());
    }

    public String logout() {
        NetworkController.getInstance().sendAndReceive(new Request("UserController", "logout", null));
        Database.getInstance().setCurrentUser(null, null);
        App.popMenu();
        Menu.exitMenu(null);
        return "user logged out successfully!";
    }
}
