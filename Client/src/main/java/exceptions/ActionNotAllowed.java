package exceptions;

public class ActionNotAllowed extends RuntimeException {
    public ActionNotAllowed() {
        super("action not allowed in this phase");
    }
}
