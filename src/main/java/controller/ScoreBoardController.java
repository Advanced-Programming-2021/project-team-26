package controller;

import model.User;
import view.Print;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.regex.Matcher;

public class ScoreBoardController {

    public String showScoreBoard(Matcher matcher) {
        HashMap<String, User> usersWithNames = User.getAllUsers();
        ArrayList<User> users = new ArrayList<>(usersWithNames.values());

        sortUsersBasedScore(users);
        int rank = 1;
        Print print = Print.getInstance();

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (i > 0 && users.get(i - 1).getScore() > user.getScore())
                rank = i + 1;
            print.printMessage(rank + "- " + user.getNickname() + ": " + user.getScore());
        }
        return  null;
    }

    private void sortUsersBasedScore(ArrayList<User> users) {
        users.sort(Comparator.comparing(User::getScore).reversed().thenComparing(User::getNickname));
    }
}

