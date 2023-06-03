package DomainLayer.Users;

import DomainLayer.Stores.Store;
import DomainLayer.Logging.UniversalHandler;
import java.util.logging.*;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Employment {
    private RegisteredUser appointer;
    private RegisteredUser employee;
    private Store store;
    private Role role;
    private LinkedList<Permission> permissions;
    private static final Logger logger=Logger.getLogger("Employment logger");



     public Employment (RegisteredUser appointer, RegisteredUser employee, Store store, Role role){
       try{
            UniversalHandler.GetInstance().HandleError(logger);
            UniversalHandler.GetInstance().HandleInfo(logger);
        }
        catch (Exception ignored){
        }
            this.appointer=appointer;
            this.employee=employee;
            this.store=store;
            this.role=role;
            permissions = new LinkedList<>();

            if(role == Role.StoreOwner){

            }
            else if(role == Role.StoreManager){
                permissions.add(Permission.CanSeeCommentsAndRating);
                permissions.add(Permission.CanSeePurchaseHistory);
            }
    }

    public Employment ( RegisteredUser employee, Store store, Role role){
        try{
            UniversalHandler.GetInstance().HandleError(logger);
            UniversalHandler.GetInstance().HandleInfo(logger);
        }
        catch (Exception ignored){
        }
        this.appointer = null;
        this.employee = employee;
        this.store = store;
        this.role = role;
        permissions = null;
    }


    public RegisteredUser getAppointer() {
        return appointer;
    }

    public RegisteredUser getEmployee() {
        return employee;
    }

    public Store getStore() {
        return store;
    }

    public Role getRole() {
        return role;
    }


    public boolean checkIfFounder() {
        return (getRole()== Role.StoreFounder);
    }
    public boolean checkIfOwner() {
        return (getRole()==Role.StoreOwner);
    }

    public boolean checkIfManager() {
        return (getRole()== Role.StoreManager);
    }

    public void togglePermission(Permission p){
        if (permissions.contains(p)) {
            permissions.remove(p);
            logger.info("Permission " + p.toString() + " removed.");
        } else {
            permissions.add(p);
            logger.info("Permission " + p.toString() + " added.");
        }
    }
    
     public boolean checkIfStoreManager() {
        boolean isStoreManager = role == Role.StoreFounder || role == Role.StoreManager;
        if (isStoreManager) {
            logger.info("User is a store manager.");
        } else {
            logger.info("User is not a store manager.");
        }
        return isStoreManager;

    }
    public boolean canAppointOwner()
    {
        if(getRole()==Role.StoreOwner || getRole()==Role.StoreFounder || permissions.contains(Permission.CanAppointStoreOwner))
            return true;
        return false;
    }
    public boolean canAppointManager()
    {
        if(getRole()==Role.StoreOwner || getRole()==Role.StoreFounder || permissions.contains(Permission.CanAppointStoreManager))
            return true;
        return false;
    }
    @Override
    public String toString() {
        String appointerUserName;
         if(appointer == null)
             appointerUserName= " no appointer ";
         else  {
             appointerUserName = appointer.getUserName();
         }
        String output =  "Employment{" +
                "appointer=" + appointerUserName +
                ", employee=" + employee.getUserName() +
                ", role=" + role +
                ", permissions=" + permissions +
                '}';

        return output;
    }


}
