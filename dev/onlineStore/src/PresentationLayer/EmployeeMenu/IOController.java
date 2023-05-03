package PresentationLayer.EmployeeMenu;

import java.util.Scanner;

public class IOController {
    private static IOController controller = null;
    private Scanner scanner;

    private IOController() {
        scanner = new Scanner(System.in);
    }

    public static IOController getInstance() {
        if (controller == null)
            controller = new IOController();
        return controller;
    }

    public String getString() {
        return scanner.nextLine();
    }

    public int getInt() {
        String value = "";
        Integer ret = null;
        do {
            try {
                value = scanner.nextLine();
                ret = Integer.parseInt(value);
            } catch (Exception ignored) {
                System.out.println("Please enter an integer.");
            }
        } while (ret == null);
        return ret;
    }

    public float getFloat() {
        String value = "";
        Float ret = null;
        do {
            try {
                value = scanner.nextLine();
                ret = Float.parseFloat(value);
            } catch (Exception ignored) {
                System.out.println("Please enter an integer / float.");
            }
        } while (ret == null);
        return ret;
    }

    public void print(String str) {
        System.out.print(str);
    }

    public void println(String str) {
        System.out.println(str);
    }

}
