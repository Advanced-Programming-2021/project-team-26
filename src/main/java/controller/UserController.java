package controller;

import model.User;

import java.util.regex.Matcher;

public class UserController {
    private static UserController userController;
    private User user;

//    private UserController(User user) {
//        this.user = user;
//    }

    public static UserController getInstance() {
        if (userController == null)
            userController = new UserController();
        return userController;
    }

    public void addNewUser(Matcher matcher) {

    }
    public void removeUser(Matcher matcher) {

    }

    public void changePassword(Matcher matcher) {

    }

    public void changeNickname(Matcher matcher){

    }

    public void loginUser(Matcher matcher){

    }

    public void logout(Matcher matcher){

    }

    private boolean checkUsernameExistence(String username) {
        return false;
    }

    private boolean checkNicknameExistence(String nickname) {
        return false;
    }

    private boolean checkPassword(String username, String password){
        return true;
    }

}
