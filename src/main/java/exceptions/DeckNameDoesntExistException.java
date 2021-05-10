package exceptions;

public class DeckNameDoesntExistException extends RuntimeException {
    private String deckName;

    public DeckNameDoesntExistException(String deckName) {
        this.deckName = deckName;
    }

    @Override
    public String getMessage() {
        return "deck with name " + deckName + " does not exist";
    }
}
