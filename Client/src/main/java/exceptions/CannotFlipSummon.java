package exceptions;

public class CannotFlipSummon extends RuntimeException {
    public CannotFlipSummon() {
        super("you can’t flip summon this card");
    }
}
