package fxmlController;

import controller.GameController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class GameView implements Initializable {

    private final GameController controller;
    private final int turn;
    @FXML
    private ProgressBar opponentLPProgress;
    @FXML
    private ProgressBar myLPProgress;
    @FXML
    private ImageView opponentProfile;
    @FXML
    private ImageView myProfile;
    @FXML
    private Text myUsername;
    @FXML
    private Text myNickname;
    @FXML
    private Text opponentNickname;
    @FXML
    private Text opponentUsername;
    @FXML
    private Text opponentLP;
    @FXML
    private Text myLP;

    public GameView(GameController controller, int turn) {
        this.controller = controller;
        this.turn = turn;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myLP.setText(String.valueOf(controller.getGame().getLifePoint(turn)));
        myNickname.setText(controller.getGame().getUser(turn).getNickname());
        myProfile.setImage(controller.getGame().getUser(turn).getProfileImage());
        myUsername.setText(controller.getGame().getUser(turn).getUsername());

        opponentLP.setText(String.valueOf(controller.getGame().getLifePoint(1 - turn)));
        opponentNickname.setText(controller.getGame().getUser(1 - turn).getNickname());
        opponentProfile.setImage(controller.getGame().getUser(1 - turn).getProfileImage());
        opponentUsername.setText(controller.getGame().getUser(1 - turn).getUsername());
    }
}
