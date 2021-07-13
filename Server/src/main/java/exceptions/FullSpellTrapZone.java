package exceptions;

public class FullSpellTrapZone extends RuntimeException {
    public FullSpellTrapZone() {
        super("spell card zone is full");
    }
}
