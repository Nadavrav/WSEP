package DomainLayer.Users;

import DomainLayer.Stores.Store;

import java.util.LinkedList;

public class Employment {
    private RegisteredUser appointer;
    private RegisteredUser employee;
    private Store store;
    private Role role;
    private LinkedList<Permission> permissions;



    public Employment (RegisteredUser appointer, RegisteredUser employee, Store store, Role role){
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
        this.appointer=null;
        this.employee=employee;
        this.store=store;
        this.role=role;
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


    public boolean checkIfOwner() {
        return (getRole()== Role.StoreFounder ||  getRole()==Role.StoreOwner);
    }

    public boolean checkIfManager() {
        return (getRole()== Role.StoreManager);
    }

    public void togglePermission(Permission p){
        if(permissions.contains(p)){
            permissions.remove(p);
        }
        else{
            permissions.add(p);
        }
}
    public boolean checkIfStoreManager() {
        return (getRole()== Role.StoreFounder ||  getRole()==Role.StoreManager);

    }
    @Override
    public String toString() {
        return "Employment{" +
                "appointer=" + appointer.getUserName() +
                ", employee=" + employee.getUserName() +
                ", role=" + role +
                ", permissions=" + permissions +
                '}';
    }


}
