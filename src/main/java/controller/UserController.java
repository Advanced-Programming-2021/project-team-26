package controller;

public class UserController {
    private static UserController userController;

    private UserController(){

    }

    public static UserController getInstance(){
        if (userController == null)
            userController = new UserController();
        return userController;
    }

    private void addNewUser(String username, String nickName, String password){

    }

    private void removeUser(String username){

    }

    private void changePassword(String username, String oldPassword, String newPassword){

    }
}
