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

    public void addNewUser(String username, String nickName, String password) {

    }

    public void removeUser(String username) {

    }

    public void changePassword(Matcher matcher) {

    }

    public void changeNickname(Matcher matcher){

    }

    public void loginUser(String username, String password){

    }

    public void logout(){

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
