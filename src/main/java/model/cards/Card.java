package model.cards;

import controller.exceptions.CardNotFoundException;
import controller.exceptions.MonsterNotFoundException;
import controller.exceptions.SpellNotFoundException;
import controller.exceptions.TrapNotFoundException;
import model.cards.monster.Monster;
import model.cards.spell.Spell;
import model.cards.trap.Trap;

public abstract class Card {
    protected String name;
    protected String Description;
    protected int price;
    //copy constructor
    public Card(Card o){
        setName(o.name);
        setDescription(o.Description);
        setPrice(o.price);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName(){
        return this.name;
    }

    public static Card getCard(String name) throws CardNotFoundException {
        try {
            return Monster.getMonster(name);
        }catch (MonsterNotFoundException monsterNotFoundException){
            try {
                return Spell.getSpell(name);
            }catch (SpellNotFoundException spellNotFoundException){
                try {
                    return Trap.getTrap(name);
                }catch (TrapNotFoundException trapNotFoundException){
                    throw new CardNotFoundException();
                }
            }
        }
    }
}
