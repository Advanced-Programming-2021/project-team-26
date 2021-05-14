package view;

import controller.MonsterController;
import controller.SpellTrapController;
import model.Board;
import model.Game;
import model.cards.Card;
import model.cards.monster.Monster;
import model.cards.spell.Spell;
import model.cards.trap.Trap;

public class Print {
    private static Print print;


    private Print() {

    }

    public static Print getInstance() {
        if (print == null)
            print = new Print();
        return print;
    }

    public void printMessage(String message) {
        System.out.println(message);
    }


    public void printCard(Card card) {
        if (card instanceof Monster) {
            Monster monster = (Monster) card;
            System.out.println("Name: " + monster.getName());
            System.out.println("Level:" + monster.getLevel());
            System.out.println("Type: " + monster.getType());
            System.out.println("ATK: " + monster.getAttackPower());
            System.out.println("DEF:" + monster.getDefencePower());
            System.out.println("Description: " + monster.getDescription());
        } else if (card instanceof Spell) {
            Spell spell = (Spell) card;
            System.out.println("Name: " + spell.getName());
            System.out.println("Spell");
            System.out.println("Type: " + spell.getType());
            System.out.println("Description: " + spell.getDescription());
        } else if (card instanceof Trap) {
            Trap trap = (Trap) card;
            System.out.println("Name: " + trap.getName());
            System.out.println("Spell");
            System.out.println("Type: " + trap.getType());
            System.out.println("Description: " + trap.getDescription());
        }

    }

    private void printMultipleTimes(String s, int times) {
        for (int i = 0; i < times; i++)
            System.out.print(s);
    }

    private void printBoard(Board board) {
        //first line
        //  field zone
        if (board.getFieldZone() == null)
            System.out.print("E");
        else
            System.out.print("O");

        printMultipleTimes("\t", 6);

        // graveyard number
        int graveyardNumber = board.getGraveyard().size();
        System.out.print(graveyardNumber);
        System.out.print("\n");

        //second line
        //  monster zone
        printMultipleTimes("\t", 1);
        for (int i : new int[]{4, 2, 0, 1, 3}) {
            MonsterController monster = board.getMonstersZone()[i];
            if (monster == null)
                System.out.print("E");
            else {
                System.out.print(monster.getPosition().getString());
            }
            printMultipleTimes("\t", 1);
        }
        System.out.print("\n");

        //third line
        //  spell zone
        printMultipleTimes("\t", 1);
        for (int i : new int[]{4, 2, 0, 1, 3}) {
            SpellTrapController spellTrap = board.getSpellTrapZone()[i];
            if (spellTrap == null)
                System.out.print("E");
            else {
                System.out.print(spellTrap.getPosition().getString());
            }
            printMultipleTimes("\t", 1);
        }
        System.out.print("\n");

        //fourth line
        // deck number
        printMultipleTimes("\t", 6);
        int deckNumber = board.getDeck().size();
        System.out.print(deckNumber);
        System.out.print("\n");

        //fifth line
        //hand number
        int handNumber = board.getHand().size();
        printMultipleTimes("c\t", handNumber);
        System.out.print("\n");
    }

    private void printBoardReverse(Board board) {
        //first line
        //hand number
        int handNumber = board.getHand().size();
        printMultipleTimes("c\t", handNumber);
        System.out.print("\n");

        //second line
        // deck number
        int deckNumber = board.getDeck().size();
        System.out.print(deckNumber);
        System.out.print("\n");

        //third line
        //  spell zone
        printMultipleTimes("\t", 1);
        for (int i : new int[]{3, 1, 0, 2, 4}) {
            SpellTrapController spellTrap = board.getSpellTrapZone()[i];
            if (spellTrap == null)
                System.out.print("E");
            else {
                System.out.print(spellTrap.getPosition().getString());
            }
            printMultipleTimes("\t", 1);
        }
        System.out.print("\n");

        //fourth line
        //  monster zone
        printMultipleTimes("\t", 1);
        for (int i : new int[]{3, 1, 0, 2, 4}) {
            MonsterController monster = board.getMonstersZone()[i];
            if (monster == null)
                System.out.print("E");
            else {
                System.out.print(monster.getPosition().getString());
            }
            printMultipleTimes("\t", 1);
        }
        System.out.print("\n");

        //fifth line
        // graveyard number
        int graveyardNumber = board.getGraveyard().size();
        System.out.print(graveyardNumber);
        printMultipleTimes("\t", 6);
        //  field zone
        if (board.getFieldZone() == null)
            System.out.print("E");
        else
            System.out.print("O");
        System.out.print("\n");
    }

    public void printGame(Game game) {
        int turn = game.getTurn();
        int otherTurn = 2 - turn;
        System.out.println(game.getUser(otherTurn).getUsername() + ":" +
                game.getLifePoint(otherTurn));
        printBoardReverse(game.getBoard(otherTurn));
        printMultipleTimes("-", 20);
        printBoard(game.getBoard(turn));
        System.out.println(game.getUser(turn).getUsername() + ":" +
                game.getLifePoint(turn));
    }
}
