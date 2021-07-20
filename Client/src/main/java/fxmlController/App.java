package fxmlController;

import fxmlController.Children.Welcome;
import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.util.Stack;

public class App extends Application {
    private static final Stack<MenuParent> menus = new Stack<>();
//    public static AudioClip buttonClick = new AudioClip(App.class.getResource("/Assets/Sounds/buttonClick.wav").toString());
//    public static Media media;
//    public static MediaView mediaView = null;
//    private static boolean areSoundsActive = true;
    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

//    public static boolean isAreSoundsActive() {
//        return areSoundsActive;
//    }

//    public static void setAreSoundsActive(boolean areSoundsActive) {
//        App.areSoundsActive = areSoundsActive;
//    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void pushMenu(MenuParent menu) {
        menus.push(menu);
        menu.scene.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE)
                popMenu();
        });
        stage.setTitle(menu.title);
        stage.setScene(menu.scene);
    }

    public static void pushMenu(MenuParent menu, boolean escape) {
        if (escape)
            pushMenu(menu);
        else {
            menus.push(menu);
            stage.setTitle(menu.title);
            stage.setScene(menu.scene);
        }
    }

    public static void popMenu() {
        menus.pop();
        if (menus.empty())
            System.exit(0);
        else {
            stage.setTitle(menus.lastElement().title);
            stage.setScene(menus.lastElement().scene);
        }
        System.gc();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setResizable(false);
//        media = new Media(getClass().getResource("/Assets/YuGiOh_entry.mp4").toExternalForm());
//        mediaView = new MediaView(new MediaPlayer(media));
//
//        if (areSoundsActive) mediaView.getMediaPlayer().play();
//        mediaView.getMediaPlayer().autoPlayProperty().setValue(true);
        new Welcome().run();
        stage.show();
    }
}
