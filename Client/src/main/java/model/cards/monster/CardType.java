package model.cards.monster;

public enum CardType {
    NORMAL("Normal"),
    RITUAL("Ritual"),
    EFFECT("Effect");

    private final String string;
    CardType (String string){
        this.string = string;
    }

    public static CardType stringToCardType(String string) {
        for (CardType cardType : CardType.values()){
            if(cardType.string.equals(string))
                return cardType;
        }
        return null;
    }
}
