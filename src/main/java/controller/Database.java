package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;
import model.Ai;
import model.User;
import model.cards.Card;
import model.cards.SpellTrap;
import model.cards.monster.Monster;
import model.cards.spell.Spell;
import model.cards.trap.Trap;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

public class Database {
    private static Database database;
    private final String monsterPath;
    private final String spellTrapPath;
    private final String cardsJsonPath;
    private final String monstersJsonPath;
    private final String spellsJsonPath;
    private final String trapsJsonPath;
    private final String userDirectoryPath;
    private final String profileDirectoryPath;
    private final String databasePath;
    private final Gson gson;
    private boolean debuggingMode = true;
    private User currentUser;

    {
        currentUser = null;
    }

    {
        monsterPath = getClass().getClassLoader().getResource("Monster.csv").getPath();
        spellTrapPath = getClass().getClassLoader().getResource("SpellTrap.csv").getPath();
        cardsJsonPath = getClass().getClassLoader().getResource("").getPath() + "Cards";
        monstersJsonPath = cardsJsonPath + File.separator + "Monsters";
        spellsJsonPath = cardsJsonPath + File.separator + "Spells";
        trapsJsonPath = cardsJsonPath + File.separator + "Traps";
        databasePath = System.getProperty("user.dir") + File.separator + "database";
        userDirectoryPath = System.getProperty("user.dir") + File.separator + "database" + File.separator + "Users";
        profileDirectoryPath = System.getProperty("user.dir") + File.separator + "database" + File.separator + "Profile";
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
    }

    private Database() {
        createFolder(databasePath);
        createFolder(userDirectoryPath);
        createFolder(profileDirectoryPath);
        createFolder(cardsJsonPath);
        createFolder(monstersJsonPath);
        createFolder(spellsJsonPath);
        createFolder(trapsJsonPath);
    }

    public static Database getInstance() {
        if (database == null)
            database = new Database();
        return database;
    }

    public boolean isDebuggingMode() {
        return debuggingMode;
    }

    public void setDebuggingMode(boolean debuggingMode) {
        this.debuggingMode = debuggingMode;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void createFolder(String path) {
        File file = new File(path);
        file.mkdir();
    }

    public HashMap<String, Card> getAllCards() {
        HashMap<String, Monster> monsters = getAllMonsters();
        if (monsters == null)
            return null;
        HashMap<String, Card> cards = new HashMap<>(monsters);

        HashMap<String, SpellTrap> spellTraps = getAllSpellTraps();
        if (spellTraps == null)
            return null;
        cards.putAll(spellTraps);
        return cards;
    }


    public HashMap<String, Monster> getAllMonsters() {
        HashMap<String, Monster> monsters = new HashMap<>();
        try {
            FileReader monsterFile = new FileReader(monsterPath);
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
            FileReader spellTrapFile = new FileReader(spellTrapPath);
            CSVReader reader = new CSVReader(spellTrapFile);

            String[] nextRecord;
            reader.readNext();

            while ((nextRecord = reader.readNext()) != null) {
                if (nextRecord[1].equals("Trap"))
                    spellTraps.put(nextRecord[0], new Trap(nextRecord));
                else if (nextRecord[1].equals("Spell"))
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
            FileReader spellTrapFile = new FileReader(spellTrapPath);
            CSVReader reader = new CSVReader(spellTrapFile);

            String[] nextRecord;
            reader.readNext();

            while ((nextRecord = reader.readNext()) != null) {
                if (nextRecord[1].equals("Spell"))
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
            FileReader spellTrapFile = new FileReader(spellTrapPath);
            CSVReader reader = new CSVReader(spellTrapFile);

            String[] nextRecord;
            reader.readNext();

            while ((nextRecord = reader.readNext()) != null) {
                if (nextRecord[1].equals("Trap"))
                    traps.put(nextRecord[0], new Trap(nextRecord));
            }

            return traps;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void writeCard(Card card) {
        if (card instanceof Monster)
            writeMonster((Monster) card);
        else if (card instanceof Spell)
            writeSpell((Spell) card);
        else if (card instanceof Trap)
            writeTrap((Trap) card);
    }

    public void writeMonster(Monster monster) {
        try {
            String path = monstersJsonPath + File.separator + monster.getName() + ".json";
            FileWriter file = new FileWriter(path);
            gson.toJson(monster, file);
            file.close();
        } catch (IOException ignored) {

        }
    }

    public void writeSpell(Spell spell) {
        try {
            String path = spellsJsonPath + File.separator + spell.getName() + ".json";
            FileWriter file = new FileWriter(path);
            gson.toJson(spell, file);
            file.close();
        } catch (IOException ignored) {

        }
    }

    public void writeTrap(Trap trap) {
        try {
            String path = trapsJsonPath + File.separator + trap.getName() + ".json";
            FileWriter file = new FileWriter(path);
            gson.toJson(trap, file);
            file.close();
        } catch (IOException ignored) {

        }
    }

    public Card readCard(String name) {
        Card card;
        if ((card = readMonster(name)) != null)
            return card;
        else if ((card = readSpell(name)) != null)
            return card;
        else if ((card = readTrap(name)) != null)
            return card;
        return null;
    }

    public Monster readMonster(String name) {
        try {
            String path = monstersJsonPath + File.separator + name + ".json";
            FileReader file = new FileReader(path);
            return gson.fromJson(file, Monster.class);
        } catch (Exception e) {
            return null;
        }
    }

    public Spell readSpell(String name) {
        try {
            String path = spellsJsonPath + File.separator + name + ".json";
            FileReader file = new FileReader(path);
            return gson.fromJson(file, Spell.class);
        } catch (Exception e) {
            return null;
        }
    }

    public Trap readTrap(String name) {
        try {
            String path = trapsJsonPath + File.separator + name + ".json";
            FileReader file = new FileReader(path);
            return gson.fromJson(file, Trap.class);
        } catch (Exception e) {
            return null;
        }
    }

    public User readUser(String username) {
        try {
            String path = userDirectoryPath + File.separator + username + ".json";
            FileReader userFile = new FileReader(path);
            try {
                userFile.close();
            } catch (IOException e) {
                return null;
            }
            return gson.fromJson(userFile, User.class);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public boolean writeUser(User user) {
        if (!(user instanceof Ai)) {
            try {
                String path = userDirectoryPath + File.separator + user.getUsername() + ".json";
                FileWriter fileWriter = new FileWriter(path);
                gson.toJson(user, fileWriter);
                fileWriter.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public HashMap<String, User> getAllUsers() {
        File userDirectory = new File(userDirectoryPath);
        File[] userFiles = userDirectory.listFiles();

        if (userFiles != null) {
            HashMap<String, User> allUsers = new HashMap<>();
            for (File userFile : userFiles) {
                if (!userFile.getName().matches(".*json"))
                    continue;
                try {
                    FileReader fileReader = new FileReader(userFile);
                    User user = gson.fromJson(fileReader, User.class);
                    allUsers.put(user.getUsername(), user);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return allUsers;
        } else {
            return null;
        }
    }

    public void removeUser(String username) {
        String path = userDirectoryPath + File.separator + username + ".json";
        File file = new File(path);
        file.delete();
    }

    public String writeProfile(File file, String name) {
        File dest = new File(profileDirectoryPath + File.separator + name);
        try {
            Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return "database" + File.separator + "Profile" + File.separator + name;
        } catch (IOException e) {
            return null;
        }
    }
}
