package exceptions;

public class CannotAttackDirectlyException extends RuntimeException {
    public CannotAttackDirectlyException() {
        super("you can’t attack the opponent directly");
    }
}
