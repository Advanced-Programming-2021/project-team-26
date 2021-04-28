package controller;

import model.Deck;
import model.User;
import model.cards.Card;
import model.cards.SpellTrap;
import model.cards.monster.Monster;
import model.cards.spell.Spell;
import model.cards.trap.Trap;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    @Test
    void getAllCards() {
        Database database = Database.getInstance();
        HashMap<String, Card> cards = database.getAllCards();
        assertNotEquals(null, cards);
        assertEquals(76, cards.size());
    }

    @Test
    void getAllMonsters() {
        Database database = Database.getInstance();
        HashMap<String, Monster> monsters = database.getAllMonsters();
        assertNotEquals(null, monsters);
        assertEquals(41, monsters.size());
    }

    @Test
    void getAllSpellTrap() {
        Database database = Database.getInstance();
        HashMap<String, SpellTrap> spellTraps = database.getAllSpellTraps();
        assertNotEquals(null, spellTraps);
        assertEquals(35, spellTraps.size());
    }

    @Test
    void getAllSpell() {
        Database database = Database.getInstance();
        HashMap<String, Spell> spells = database.getAllSpells();
        assertNotEquals(null, spells);
        assertEquals(23, spells.size());
    }

    @Test
    void getAllTrap() {
        Database database = Database.getInstance();
        HashMap<String, Trap> traps = database.getAllTraps();
        assertNotEquals(null, traps);
        assertEquals(12, traps.size());
    }

    @Test
    void readNotAvailableUser() {
        String username = "NotAvailable";
        Database database = Database.getInstance();
        User user = database.readUser(username);
        assertNull(user);
    }

    @Test
    void writeNewUser() {
        String username = "NewUser";
        String nickname = "NickName";
        String password = "Password";

        User user = new User(username, password, nickname);
        user.addCardToUserCards(Card.getCard("Magic Cylinder"));
        Deck deck = new Deck("this",user.getUsername());
        user.addDeckToUserDecks(deck);

        Database database = Database.getInstance();
        database.writeUser(user);
    }

    @Test
    void readUser(){
        String username = "NewUser";
        String nickname = "NickName";
        String password = "Password";

        User user = new User(username, password, nickname);
        user.addCardToUserCards(Card.getCard("Magic Cylinder"));
        user.addCardToUserCards(Card.getCard("Supply Squad"));

        Database database = Database.getInstance();
        database.writeUser(user);
        User readUser = database.readUser(username);
        assertNotNull(readUser);
        assertEquals(username,readUser.getUsername());
    }

    @Test
    void readAllUsers() {
        User user1 = new User("user1","password","nickname1");
        User user2 = new User("user2","password","nickname2");

        Database database = Database.getInstance();
        database.writeUser(user1);
        database.writeUser(user2);

        HashMap<String,User> allUsers = database.getAllUsers();
        assertNotNull(allUsers);
        assertEquals(3,allUsers.size());
    }
}