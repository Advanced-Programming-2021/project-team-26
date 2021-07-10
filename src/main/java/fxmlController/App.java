package fxmlController;

import fxmlController.Children.Welcome;
import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.util.Stack;

public class App extends Application {
    private static final Stack<MenuParent> menus = new Stack<>();
    private static Stage stage;
    public static AudioClip buttonClick = new AudioClip(App.class.getResource("/Assets/Sounds/eatBomb.wav").toString());

    public static Stage getStage() {
        return stage;
    }

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
        buttonClick.play();
        stage = primaryStage;
        stage.setResizable(false);
        new Welcome().run();
        stage.show();
    }
}
