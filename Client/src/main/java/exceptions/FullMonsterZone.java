package exceptions;

public class FullMonsterZone extends RuntimeException {
    public FullMonsterZone() {
        super("monster card zone is full");
    }
}
