package fxmlController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Stack;

public class App extends Application {
    private static final Stack<MenuParent> menus = new Stack<>();
    private static Stage stage;

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

    public static Parent getFXML(String name) throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(Welcome.class.getResource("/fxml/" + name + ".fxml")));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setResizable(false);
        stage.show();
        new Welcome().run();
    }
}