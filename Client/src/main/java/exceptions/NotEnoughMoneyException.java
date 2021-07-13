package exceptions;

public class NotEnoughMoneyException extends RuntimeException {
    public  NotEnoughMoneyException (){
        super("not enough money");
    }
}
