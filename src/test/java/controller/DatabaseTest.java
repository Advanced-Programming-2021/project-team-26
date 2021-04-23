package controller;

import model.cards.Card;
import model.cards.SpellTrap;
import model.cards.monster.Monster;
import model.cards.spell.Spell;
import model.cards.trap.Trap;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DatabaseTest {

    @Test
    void getAllCards() {
        Database database = Database.getInstance();
        HashMap<String, Card> cards = database.getAllCards();
        assertNotEquals(null,cards);
        assertEquals(76,cards.size());
    }

    @Test
    void getAllMonsters() {
        Database database = Database.getInstance();
        HashMap<String, Monster> monsters = database.getAllMonsters();
        assertNotEquals(null,monsters);
        assertEquals(41,monsters.size());
    }

    @Test
    void getAllSpellTrap() {
        Database database = Database.getInstance();
        HashMap<String, SpellTrap> spellTraps = database.getAllSpellTraps();
        assertNotEquals(null,spellTraps);
        assertEquals(35,spellTraps.size());
    }

    @Test
    void getAllSpell() {
        Database database = Database.getInstance();
        HashMap<String, Spell> spells = database.getAllSpells();
        assertNotEquals(null,spells);
        assertEquals(23,spells.size());
    }

    @Test
    void getAllTrap() {
        Database database = Database.getInstance();
        HashMap<String, Trap> traps = database.getAllTraps();
        assertNotEquals(null,traps);
        assertEquals(12,traps.size());
    }
}