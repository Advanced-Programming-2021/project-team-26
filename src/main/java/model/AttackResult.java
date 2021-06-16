package model;

import controller.MonsterController;
import model.cards.monster.Monster;

public class AttackResult {
    private int myLPDecrease = 0;
    private int opLPDecrease = 0;
    private boolean removeMyCard = false;
    private boolean removeOpCard = false;
    private Monster flippedCard = null;
    private String message = null;

    public AttackResult(int myLPDecrease, int opLPDecrease, boolean removeMyCard, boolean removeOpCard, Monster flippedCard) {
        this.myLPDecrease = myLPDecrease;
        this.opLPDecrease = opLPDecrease;
        this.removeMyCard = removeMyCard;
        this.removeOpCard = removeOpCard;
        this.flippedCard = flippedCard;
    }

    public AttackResult(int myLPDecrease, int opLPDecrease, boolean removeMyCard, boolean removeOpCard) {
        this(myLPDecrease, opLPDecrease, removeMyCard, removeOpCard, null);
    }

    public AttackResult(MonsterController attacker, MonsterController defender) {
        int damage;
        switch (defender.getPosition()) {
            case ATTACK:
                damage = attacker.getCard().getAttackPower() - defender.getCard().getAttackPower();
                if (damage > 0) {
                    removeOpCard = true;
                    opLPDecrease = damage;
                } else if (damage == 0) {
                    removeMyCard = true;
                    removeOpCard = true;
                } else {
                    myLPDecrease = -damage;
                    removeMyCard = true;
                }
            case DEFENCE_DOWN:
                flippedCard = defender.getCard();
            case DEFENCE_UP:
                damage = attacker.getCard().getAttackPower() - defender.getCard().getDefencePower();
                if (damage > 0) {
                    removeOpCard = true;
                } else if (damage < 0) {
                    myLPDecrease = -damage;
                }
        }
    }

    public int getMyLPDecrease() {
        return myLPDecrease;
    }

    public void setMyLPDecrease(int myLPDecrease) {
        this.myLPDecrease = myLPDecrease;
    }

    public int getOpLPDecrease() {
        return opLPDecrease;
    }

    public void setOpLPDecrease(int opLPDecrease) {
        this.opLPDecrease = opLPDecrease;
    }

    public boolean isRemoveMyCard() {
        return removeMyCard;
    }

    public void setRemoveMyCard(boolean removeMyCard) {
        this.removeMyCard = removeMyCard;
    }

    public boolean isRemoveOpCard() {
        return removeOpCard;
    }

    public void setRemoveOpCard(boolean removeOpCard) {
        this.removeOpCard = removeOpCard;
    }

    public Monster getFlippedCard() {
        return flippedCard;
    }

    public void setFlippedCard(Monster flippedCard) {
        this.flippedCard = flippedCard;
    }

    public String getMessage() {
        if (message != null)
            return message;
        String opCardNamePart = null;
        if (flippedCard != null)
            opCardNamePart = "opponent’s monster card was " + flippedCard.getName();
        String destroyPart;
        if (removeMyCard && removeOpCard)
            destroyPart = "both you and your opponent monster cards are destroyed";
        else if (removeMyCard)
            destroyPart = "Your monster card is destroyed";
        else if (removeOpCard)
            destroyPart = "your opponent’s monster is destroyed";
        else
            destroyPart = "no card is destroyed";

        String damagePart = null;
        if (myLPDecrease > 0)
            damagePart = "you received " + myLPDecrease + " battle damage";
        if (opLPDecrease > 0)
            damagePart = "your opponent received " + myLPDecrease + " battle damage";

        String result = "";
        if (opCardNamePart != null)
            result += opCardNamePart;

        if (!result.equals("")) {
            result += " and ";
        }
        result += destroyPart;

        if (damagePart != null) {
            result += " and ";
            result += damagePart;
        }
        return result;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
