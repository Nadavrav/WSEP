package DAL.DTOs;

import DomainLayer.Users.Permission;
import DomainLayer.Users.RegisteredUser;
import DomainLayer.Users.Role;

import java.util.LinkedList;

public class employmentDTO {
    private String appointer;
    private String employee;
    private int storeID;
    private int role;
    private String permissions;

    public employmentDTO(String employee,int storeId, String appointer,int role, String permissions)
    {
        this.employee = employee;
        this.storeID = storeId;
        this.appointer = appointer;
        this.role = role;
        this.permissions = permissions;
    }
    public String getAppointer() {
        return appointer;
    }

    public String getEmployee() {
        return employee;
    }

    public int getStoreID() {
        return storeID;
    }

    public int getRole() {
        return role;
    }

    public String getPermissions() {
        return permissions;
    }

}
