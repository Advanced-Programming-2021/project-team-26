package exceptions;

public class DeckNameDoesntExistException extends RuntimeException {
    public DeckNameDoesntExistException(String deckName) {
        super("deck with name " + deckName + " does not exist");
    }
}
