package view;

import model.cards.Card;
import view.menus.Errors;

public class Print {
    private static Print print;
    private Print(){

    }

    public static Print getInstance(){
        if(print==null)
            print = new Print();
        return print;
    }

    public void printMessage(String message){
        System.out.println(message);
    }

    public void printError(Errors error){
        //TODO
    }

    public void printCard(Card card){
        //TODO
    }
}
