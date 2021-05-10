package exceptions;

public class FullMainDeckException extends RuntimeException {
    public void printMessage() {
        System.out.println("main deck is full");
    }
}
