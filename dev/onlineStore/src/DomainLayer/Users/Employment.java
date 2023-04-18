package DomainLayer.Users;

import DomainLayer.Stores.Store;

public class Employment {
    private RegisteredUser appointer;
    private RegisteredUser employee;
    private Store store;
    private Role role;

    public Employment (RegisteredUser appointer, RegisteredUser employee, Store store, Role role){
        this.appointer=appointer;
        this.employee=employee;
        this.store=store;
        this.role=role;
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
    public boolean checkIfStoreManager() {
        return (getRole()== Role.StoreFounder ||  getRole()==Role.StoreManager);
    }
}
