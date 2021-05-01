package controller;

import exceptions.DuplicateNickname;
import exceptions.DuplicateUsername;
import exceptions.InvalidInput;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

class UserControllerTest {
    @Test
    public void createNewUser() {
        String user1 = "user create --nickname nick1 -u user1 -p thisIsPass";
        Matcher matcher1 = Pattern.compile(user1).matcher(user1);
        matcher1.find();

        String user2 = "user create --username user2 --nickname nick2 -p thisIsPass";
        Matcher matcher2 = Pattern.compile(user2).matcher(user2);
        matcher2.find();
        try {
            UserController.getInstance().addNewUser(matcher1);
            UserController.getInstance().addNewUser(matcher2);
        } catch (Exception e) {
            fail();
        }
        assertNotNull(Database.getInstance().readUser("user1"));
        assertNotNull(Database.getInstance().readUser("user2"));
    }

    @Test
    public void createDuplicate() {
        String user1 = "user create --nickname mohammad -u hello -p thisIsPass";
        Matcher matcher1 = Pattern.compile(user1).matcher(user1);
        matcher1.find();

        String user2 = "user create --username hello --nickname ali -p thisIsPass";
        Matcher matcher2 = Pattern.compile(user2).matcher(user2);
        matcher2.find();

        try {
            UserController.getInstance().addNewUser(matcher1);
            UserController.getInstance().addNewUser(matcher2);
            fail();
        } catch (Exception e) {
            if (!(e instanceof DuplicateUsername))
                fail();
        }

        String user3 = "user create --username tmp --nickname mohammad -p thisIsPass";
        Matcher matcher3 = Pattern.compile(user3).matcher(user3);
        matcher3.find();

        try {
            UserController.getInstance().addNewUser(matcher3);
            fail();
        } catch (Exception e) {
            if (!(e instanceof DuplicateNickname))
                fail();
        }
    }

    @Test
    public void invalidInput() {
        String user1 = "user create --use hello --nickname ali -p thisIsPass";
        Matcher matcher1 = Pattern.compile(user1).matcher(user1);
        matcher1.find();

        UserController userController = UserController.getInstance();
        try {
            userController.addNewUser(matcher1);
            fail();
        } catch (Exception e) {
            if (!(e instanceof InvalidInput))
                fail();
        }

        String user2 = "user create hello ali -p thisIsPass";
        Matcher matcher2 = Pattern.compile(user2).matcher(user2);
        matcher2.find();

        try {
            userController.addNewUser(matcher2);
            fail();
        } catch (Exception e) {
            if (!(e instanceof InvalidInput))
                fail();
        }
    }
}