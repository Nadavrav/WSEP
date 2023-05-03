package PresentationLayer;
import DomainLayer.Facade;
import PresentationLayer.EmployeeMenu.IOController;
import PresentationLayer.EmployeeMenu.initializeData;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class MainPage {
    private static MainPage instance = null;
    private Scanner scanner;
    private Facade facade;

    private initializeData initializeData;

    public MainPage(){
        scanner = new Scanner(System.in);
        facade = new Facade();

    }
    public static MainPage getInstance() {
        if (instance == null)
            instance = new MainPage();
        return instance;
    }

    public void run() throws Exception {
        initializeData.show();
    }



}
