package exceptions;

public class NoPlayerAvailable extends RuntimeException{
    public  NoPlayerAvailable(){
        super("there is no player with this username");
    }
}
