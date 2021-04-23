package controller;

import com.opencsv.CSVReader;
import model.cards.Card;
import model.cards.SpellTrap;
import model.cards.monster.Monster;
import model.cards.spell.Spell;
import model.cards.trap.Trap;

import java.io.FileReader;
import java.util.HashMap;

public class Database {
    private static final String monsterPath;
    private static final String spellTrapPath;
    private static Database database;

    static {
        monsterPath = "Monster.csv";
        spellTrapPath = "SpellTrap.csv";
    }

    private Database() {

    }

    public static Database getInstance() {
        if (database == null)
            database = new Database();
        return database;
    }

    public HashMap<String, Card> getAllCards() {
        HashMap<String , Card> cards = new HashMap<>();
        HashMap<String,Monster> monsters = getAllMonsters();
        if(monsters==null)
            return null;
        cards.putAll(monsters);

        HashMap<String,SpellTrap> spellTraps = getAllSpellTraps();
        if(spellTraps==null)
            return null;
        cards.putAll(spellTraps);
        return cards;
    }


    public HashMap<String, Monster> getAllMonsters() {
        HashMap<String, Monster> monsters = new HashMap<>();
        try {
            FileReader monsterFile = new FileReader(getClass().getClassLoader().getResource(monsterPath).getPath());
            CSVReader reader = new CSVReader(monsterFile);

            String[] nextRecord;
            reader.readNext();

            while ((nextRecord = reader.readNext()) != null) {
                monsters.put(nextRecord[0], new Monster(nextRecord));
            }

            return monsters;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public HashMap<String, SpellTrap> getAllSpellTraps() {
        HashMap<String, SpellTrap> spellTraps = new HashMap<>();
        try {
            FileReader spellTrapFile = new FileReader(getClass().getClassLoader().getResource(spellTrapPath).getPath());
            CSVReader reader = new CSVReader(spellTrapFile);

            String[] nextRecord;
            reader.readNext();

            while ((nextRecord = reader.readNext()) != null) {
                if (nextRecord[1].equals("Trap"))
                    spellTraps.put(nextRecord[0], new Trap(nextRecord));
                else if(nextRecord[1].equals("Spell"))
                    spellTraps.put(nextRecord[0], new Spell(nextRecord));
            }

            return spellTraps;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public HashMap<String, Spell> getAllSpells() {
        HashMap<String, Spell> spells = new HashMap<>();
        try {
            FileReader spellTrapFile = new FileReader(getClass().getClassLoader().getResource(spellTrapPath).getPath());
            CSVReader reader = new CSVReader(spellTrapFile);

            String[] nextRecord;
            reader.readNext();

            while ((nextRecord = reader.readNext()) != null) {
                if(nextRecord[1].equals("Spell"))
                    spells.put(nextRecord[0], new Spell(nextRecord));
            }

            return spells;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public HashMap<String, Trap> getAllTraps() {
        HashMap<String, Trap> traps = new HashMap<>();
        try {
            FileReader spellTrapFile = new FileReader(getClass().getClassLoader().getResource(spellTrapPath).getPath());
            CSVReader reader = new CSVReader(spellTrapFile);

            String[] nextRecord;
            reader.readNext();

            while ((nextRecord = reader.readNext()) != null) {
                if(nextRecord[1].equals("Trap"))
                    traps.put(nextRecord[0], new Trap(nextRecord));
            }

            return traps;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
