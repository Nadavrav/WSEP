package PresentationLayer.EmployeeMenu;
public class EmployeeModule {
    initializeData initializeData;
    public void run() throws Exception {
        IOController io = IOController.getInstance();
        io.println("If you want to Log in Pleas Enter 1 ");
        io.println("If you are not Registered you can Sign Up by Entering 2: ");
        io.println("visit As Gust By Entering 3: ");
        int init = io.getInt();
        if (init == 1) {
            io.println("UserName: ");
            String useerName = io.getString();
            io.println("Password: ");
            String Password = io.getString();
            
        }

        if (init == 2) {
        }
        if (init == 3) {
        }

    }
}
