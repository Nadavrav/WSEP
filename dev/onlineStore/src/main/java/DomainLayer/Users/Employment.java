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
    private int storeID;
    private Role role;
    private LinkedList<Permission> permissions;
    private static final Logger logger=Logger.getLogger("Employment logger");



     public Employment (RegisteredUser appointer, RegisteredUser employee, int storeID, Role role){
       try{
            UniversalHandler.GetInstance().HandleError(logger);
            UniversalHandler.GetInstance().HandleInfo(logger);
        }
        catch (Exception ignored){
        }
            this.appointer=appointer;
            this.employee=employee;
            this.storeID=storeID;
            this.role=role;
            permissions = new LinkedList<>();

            if(role == Role.StoreOwner || role == Role.StoreFounder){
                for (Permission p:Permission.values()) {
                    permissions.add(p);
                }
            }
            else if(role == Role.StoreManager){
                permissions.add(Permission.CanSeeCommentsAndRating);
                permissions.add(Permission.CanSeePurchaseHistory);
            }
    }

    public Employment ( RegisteredUser employee, int storeID, Role role){
        try{
            UniversalHandler.GetInstance().HandleError(logger);
            UniversalHandler.GetInstance().HandleInfo(logger);
        }
        catch (Exception ignored){
        }
        this.appointer = null;
        this.employee = employee;
        this.storeID = storeID;
        this.role = role;
        permissions = new LinkedList<>();

        for (Permission p:Permission.values()) {
            permissions.add(p);
        }

    }


    public RegisteredUser getAppointer() {
        return appointer;
    }

    public RegisteredUser getEmployee() {
        return employee;
    }

    public int getStore() {
        return storeID;
    }

    public Role getRole() {
        return role;
    }


    public boolean checkIfFounder() {
        return (getRole()== Role.StoreFounder);
    }
    public boolean checkIfOwner() {
        return (getRole()==Role.StoreOwner || getRole()==Role.StoreFounder);
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


    public boolean CanManageStock() {
        if(getRole()==Role.StoreOwner || getRole()==Role.StoreFounder || permissions.contains(Permission.CanManageStock))
            return true;
        return false;
    }

    public boolean CanChangePolicyAndDiscounts() {
        if(getRole()==Role.StoreOwner || getRole()==Role.StoreFounder || permissions.contains(Permission.CanChangePolicyAndDiscounts))
            return true;
        return false;
    }

    public boolean CanChangePermissionsForStoreManager() {
        if(getRole()==Role.StoreOwner || getRole()==Role.StoreFounder || permissions.contains(Permission.CanChangePermissionsForStoreManager))
            return true;
        return false;
    }

    public boolean CanSeeStaffAndPermissions() {
        if(getRole()==Role.StoreOwner || getRole()==Role.StoreFounder || permissions.contains(Permission.CanSeeStaffAndPermissions))
            return true;
        return false;
    }

    public boolean CanSeePurchaseHistory() {
        if(getRole()==Role.StoreOwner || getRole()==Role.StoreFounder || permissions.contains(Permission.CanSeePurchaseHistory))
            return true;
        return false;
    }

    public LinkedList<Permission> getPermisssions() {
         return permissions;
    }
}
