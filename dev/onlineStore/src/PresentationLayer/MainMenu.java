package PresentationLayer;

import PresentationLayer.EmployeeMenu.EmployeeModule;
import PresentationLayer.EmployeeMenu.IOController;
import PresentationLayer.EmployeeMenu.initializeData;


import java.util.Scanner;


public class MainMenu {

        private static MainMenu instance = null;
        Scanner scanner;
        EmployeeModule employeeMenu;
        MainPage mainPage;
        initializeData initializeData;

        private MainMenu() throws Exception {
            scanner = new Scanner(System.in);
            initializeData.initializeData();
            employeeMenu = new EmployeeModule();
        }

        public static MainMenu getInstance() throws Exception {
            if (instance == null)
                instance = new MainMenu();
            return instance;
        }

        public void run() throws Exception {
            show();
            mainPage.run();
            employeeMenu.run();
        }



    private void show() {
            System.out.println("$$$ << Welcome to The Site >> $$$");
            System.out.println("\nThis is Our Main Page that you can see some of our Stores and Products :");

        }


        private int getInput() {
            while (true) {
                try {
                    int input = Integer.parseInt(scanner.nextLine());
                    return input >= 1 && input <= 3 ? input : null;
                } catch (Exception e) {
                    System.out.println("Enter only numbers between [1-3]!");
                }
            }
        }


}
