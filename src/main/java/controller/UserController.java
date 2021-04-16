package controller;

import model.User;

public class UserController {
    private static UserController userController;
    private User user;

    private UserController(User user) {
        this.user = user;
    }

    public static UserController getInstance(User user) {
        if (userController == null)
            userController = new UserController(user);
        return userController;
    }

    public void addNewUser(String username, String nickName, String password) {

    }

    public void removeUser(String username) {

    }

    public void changePassword(String oldPassword, String newPassword) {

    }

    public void changeNickname(String username,String nickname){

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
