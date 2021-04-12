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

    //copy constructor
    public Card(Card o){
        this.name = o.name;
        this.Description = o.Description;
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
