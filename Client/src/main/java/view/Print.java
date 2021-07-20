package view;

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


}
