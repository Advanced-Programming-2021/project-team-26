package exceptions;

public class AlreadyAttackedException extends RuntimeException {
    public AlreadyAttackedException() {
        super("this card already attacked");
    }
}
