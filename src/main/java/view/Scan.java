package view;

import java.util.Scanner;

public class Scan {
    private static Scan scan;
    private Scanner scanner;

    private Scan(){
        scanner = new Scanner(System.in);
    }

    public static Scan getInstance(){
        if(scan==null)
            scan = new Scan();
        return scan;
    }

    public String getString(){
        return scanner.nextLine();
    }
}
