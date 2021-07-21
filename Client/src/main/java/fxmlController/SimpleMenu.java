package fxmlController;

import javafx.scene.Scene;

public class SimpleMenu extends MenuParent{
    public SimpleMenu(String title) {
        super(title);
    }

    @Override
    public void run() throws Exception {

    }

    public void setScene(Scene scene){
        this.scene = scene;
    }
}
