package controller;

import exceptions.CardNotFoundException;
import exceptions.InvalidInput;
import exceptions.InvalidSelection;
import exceptions.NoCardSelectedException;
import model.*;
import model.cards.Card;
import model.cards.SpellTrap;
import model.cards.spell.Spell;
import model.cards.trap.Trap;
import view.Scan;

import java.util.HashMap;

public abstract class SpellTrapController {

    protected GameController gameController;
    protected SpellTrapPosition position;
    protected boolean canSpellTrapsBeActive = false;
    protected boolean isHandAccessible = false;
    protected boolean isRivalMonsterZoneAccessible = false;
    protected boolean isOurMonsterZoneAccessible = false;
    protected boolean isRivalGraveyardAccessible = false;
    protected boolean isOurGraveyardAccessible = false;
    protected Card selectedCard = null;
    protected CardAddress selectedCardAddress = null;

    public static SpellTrapController getInstance(GameController gameController, SpellTrap spellTrap, SpellTrapPosition position) {
        if (spellTrap instanceof Spell)
            return SpellController.getInstance(gameController, spellTrap, position);
        else if (spellTrap instanceof Trap)
            return TrapController.getInstance(gameController, spellTrap, position);
        return null;
    }

    public void setCanSpellTrapsBeActive(boolean canSpellTrapsBeActive) {
        this.canSpellTrapsBeActive = canSpellTrapsBeActive;
    }

    public void setHandAccessible(boolean handAccessible) {
        isHandAccessible = handAccessible;
    }

    public void setRivalMonsterZoneAccessible(boolean rivalMonsterZoneAccessible) {
        isRivalMonsterZoneAccessible = rivalMonsterZoneAccessible;
    }

    public void setOurMonsterZoneAccessible(boolean ourMonsterZoneAccessible) {
        isOurMonsterZoneAccessible = ourMonsterZoneAccessible;
    }

    public void setRivalGraveyardAccessible(boolean rivalGraveyardAccessible) {
        isRivalGraveyardAccessible = rivalGraveyardAccessible;
    }

    public void setOurGraveyardAccessible(boolean ourGraveyardAccessible) {
        isOurGraveyardAccessible = ourGraveyardAccessible;
    }

    public void setSelectedCard(Card selectedCard) {
        this.selectedCard = selectedCard;
    }

    public void setSelectedCardAddress(CardAddress selectedCardAddress) {
        this.selectedCardAddress = selectedCardAddress;
    }

    public SpellTrapPosition getPosition() {
        return position;
    }

    protected void setPosition(SpellTrapPosition position) {
        this.position = position;
    }

    public GameController getGameController() {
        return gameController;
    }

    protected void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void remove() {

    }

    public void activate() {

    }

    public abstract SpellTrap getCard();

    public abstract void standBy();

    public boolean conditionMet() {
        return true;
    }

    public void select(String selectCommand) throws InvalidSelection, CardNotFoundException, InvalidInput, NoCardSelectedException {
        HashMap<String, String> input = Scan.getInstance().parseInput(selectCommand.split("\\s+"));
        Game game = gameController.getGame();
        String addressNumber;


        if ((addressNumber = Scan.getInstance().getValue(input, "monster", "m")) != null) {
            int monsterNumber = Integer.parseInt(addressNumber);
            if (monsterNumber > Board.CARD_NUMBER_IN_ROW)
                throw new InvalidSelection();

            if (input.containsKey("opponent") || input.containsKey("o") && isRivalMonsterZoneAccessible) {
                if (game.getOtherBoard().getMonstersZone()[monsterNumber - 1] != null) {
                    selectedCard = game.getOtherBoard().getMonstersZone()[monsterNumber - 1].getMonster();
                    selectedCardAddress = new CardAddress(Place.MonsterZone, Owner.Opponent, monsterNumber - 1);
                }
            } else if (isOurMonsterZoneAccessible) {
                if (game.getThisBoard().getMonstersZone()[monsterNumber - 1] != null) {
                    selectedCard = game.getThisBoard().getMonstersZone()[monsterNumber - 1].getMonster();
                    selectedCardAddress = new CardAddress(Place.MonsterZone, Owner.Me, monsterNumber - 1);
                }
            } else throw new InvalidSelection();

            if (selectedCard == null)
                throw new CardNotFoundException();

        } else if ((addressNumber = Scan.getInstance().getValue(input, "hand", "h")) != null && isHandAccessible) {
            int handNumber = Integer.parseInt(addressNumber);
            if (handNumber > game.getThisBoard().getHand().size())
                throw new InvalidSelection();

            selectedCard = game.getThisBoard().getHand().get(handNumber - 1);
            selectedCardAddress = new CardAddress(Place.Hand, Owner.Me, handNumber - 1);

            if (this.selectedCard == null)
                throw new CardNotFoundException();

        } else if ((addressNumber = Scan.getInstance().getValue(input, "graveyard", "g")) != null) {
            int graveyardNumber = Integer.parseInt(addressNumber);

            if (input.containsKey("opponent") || input.containsKey("o") && isRivalGraveyardAccessible) {
                if (graveyardNumber > game.getOtherBoard().getGraveyard().size())
                    throw new InvalidSelection();
                else {
                    this.selectedCard = game.getOtherBoard().getGraveyard().get(graveyardNumber - 1);
                    selectedCardAddress = new CardAddress(Place.Graveyard, Owner.Opponent, graveyardNumber - 1);
                }
            } else if (isOurGraveyardAccessible) {
                if (graveyardNumber > game.getThisBoard().getGraveyard().size())
                    throw new InvalidSelection();
                else {
                    this.selectedCard = game.getThisBoard().getGraveyard().get(graveyardNumber - 1);
                    selectedCardAddress = new CardAddress(Place.Graveyard, Owner.Me, graveyardNumber - 1);
                }
            } else throw new InvalidSelection();

            if (selectedCard == null)
                throw new CardNotFoundException();

        } else if (input.containsKey("-d")) {
            if (selectedCard == null) {
                throw new NoCardSelectedException();
            }
            deselect();
        } else
            throw new InvalidInput();

        if (selectedCard == null)
            throw new CardNotFoundException();
    }

    public void deselect() {
        selectedCardAddress = null;
        selectedCard = null;
    }
}
