package PresentationLayer.EmployeeMenu;

import DomainLayer.Facade;
import DomainLayer.Response;

public class PresentationControlle{
    private static PresentationControlle instance = null;
    private Facade facade;
    private IOController io;
    private String activeEmployee;

    public static PresentationControlle getInstance() {
        if (instance == null)
            instance = new PresentationControlle();
        return instance;
    }

    private PresentationControlle() {
        facade = new Facade();
        io = IOController.getInstance();
        activeEmployee = null;
    }



}
