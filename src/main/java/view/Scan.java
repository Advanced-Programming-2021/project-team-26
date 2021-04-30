package view;

import java.util.HashMap;
import java.util.Scanner;

public class Scan {
    private static Scan scan;
    private final Scanner scanner;

    private Scan() {
        scanner = new Scanner(System.in);
    }

    public static Scan getInstance() {
        if (scan == null)
            scan = new Scan();
        return scan;
    }

    public String getString() {
        return scanner.nextLine();
    }

    public HashMap<String, String> parseInput(String[] words) {
        HashMap<String, String> values = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            if (words[i].startsWith("-")) {
                if (i + 1 >= words.length || words[i + 1].startsWith("-")) {
                    values.put(words[i].replaceFirst("^-{1,2}", ""), null);
                } else {
                    values.put(words[i].replaceFirst("^-{1,2}", ""), words[i + 1]);
                    i++;
                }
            }
        }
        return values;
    }
}
