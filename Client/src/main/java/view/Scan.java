package view;

import java.util.HashMap;
import java.util.Scanner;

public class Scan {
    private static final Scanner scanner = new Scanner(System.in);
    private static Scan scan;

    private Scan() {
    }

    public static Scanner getScanner() {
        return scanner;
    }

    public static Scan getInstance() {
        if (scan == null)
            scan = new Scan();
        return scan;
    }

    public String getString() {
        return scanner.nextLine();
    }

    public HashMap<String, String> parseInput(String string) {
        String[] words = string.split("\\s+");
        HashMap<String, String> values = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            if (words[i].startsWith("-")) {
                String key = words[i].replaceFirst("^-{1,2}", "");
                StringBuilder value = null;
                i++;
                while (i < words.length && !words[i].startsWith("-")) {
                    if (value == null)
                        value = new StringBuilder(words[i]);
                    else
                        value.append(" ").append(words[i]);
                    i++;
                }
                i--;
                values.put(key, value != null ? value.toString() : null);
            }
        }
        return values;
    }

    public String getValue(HashMap<String, String> map, String key, String keyAbbreviation) {
        if (map.containsKey(key))
            return map.get(key);
        else return map.getOrDefault(keyAbbreviation, null);
    }

    public Integer getInteger() {
        String integerString = scanner.nextLine();
        if (integerString.equals("cancel"))
            return null;
        return Integer.parseInt(integerString);
    }
}
