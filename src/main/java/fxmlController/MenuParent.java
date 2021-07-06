package fxmlController;

import javafx.scene.Scene;


abstract public class MenuParent {
    protected Scene scene;
    protected String title;


    public MenuParent(String title) {
        this.title = title;
    }

    abstract public void run() throws Exception;
}
