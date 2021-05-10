package view;

import java.util.HashMap;
import java.util.Scanner;

public class Scan {
    private static Scan scan;
    private static final Scanner scanner = new Scanner(System.in);;
    public static Scanner getScanner(){
        return scanner;
    }

    private Scan() {}

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

    public String getValue(HashMap<String, String> map, String key, String keyAbbreviation) {
        if (map.containsKey(key))
            return map.get(key);
        else if (map.containsKey(keyAbbreviation))
            return map.get(keyAbbreviation);
        else
            return null;
    }

    public Integer getInteger() {
        String integerString = scanner.nextLine();
        if (integerString.equals("cancel"))
            return null;
        return Integer.parseInt(integerString);
    }
}
