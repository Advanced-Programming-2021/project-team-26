package view;

import java.util.HashMap;
import java.util.Scanner;

abstract public class Menu {
    public static Scanner scanner;
    private HashMap<Integer, Menu> subMenus;
    public Menu(Menu menu){
    }

    abstract protected void run();
}
