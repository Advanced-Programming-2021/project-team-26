package exceptions;

public class FullSideDeckException extends RuntimeException{
    public void printMessage(){
        System.out.println("side deck is full");
    }
}
