package UnitTests;

import DomainLayer.Stores.Store;
import DomainLayer.Users.Employment;
import DomainLayer.Users.Permission;
import DomainLayer.Users.RegisteredUser;
import DomainLayer.Users.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class EmploymentTest {
    RegisteredUser Apointer,Apointee;
    Store store;
    @BeforeEach
    public void setup()
    {
        try {
            String password = "123456789";
            Apointer = new RegisteredUser("ValidApointerName", password);
            Apointee = new RegisteredUser("ValidApointeeName", password);
            store = new Store("StoreName");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            Assertions.assertTrue(false);//we shouldn't get here
        }
    }
    @Test
    public void testCheckIfOwnerWithStoreFounderRole() {
        Employment employment = new Employment(Apointer,Apointee,store, Role.StoreFounder);
        assertTrue(employment.checkIfFounder());
    }

    @Test
    public void testCheckIfOwnerWithStoreOwnerRole() {
        Employment employment = new Employment(Apointer,Apointee,store, Role.StoreOwner);
        assertTrue(employment.checkIfOwner());
    }

    @Test
    public void testCheckIfOwnerWithStoreManager() {
        Employment employment = new Employment(Apointer,Apointee,store, Role.StoreManager);
        assertFalse(employment.checkIfOwner());
    }

    @Test
    public void testCheckIfManagerWithStoreFounderRole() {
        Employment employment = new Employment(Apointer,Apointee,store, Role.StoreFounder);
        assertFalse(employment.checkIfManager());
    }

    @Test
    public void testCheckIfManagerWithStoreOwnerRole() {
        Employment employment = new Employment(Apointer,Apointee,store, Role.StoreOwner);
        assertFalse(employment.checkIfManager());
    }

    @Test
    public void testCheckIfManagerWithStoreManager() {
        Employment employment = new Employment(Apointer,Apointee,store, Role.StoreManager);
        assertTrue(employment.checkIfManager());
    }

    @Test
    public void testCheckIfStoreManagerWithStoreFounderRole() {
        Employment employment = new Employment(Apointer,Apointee,store, Role.StoreFounder);
        assertTrue(employment.checkIfStoreManager());
    }

    @Test
    public void testCheckIfStoreManagerWithStoreOwnerRole() {
        Employment employment = new Employment(Apointer,Apointee,store, Role.StoreOwner);
        assertFalse(employment.checkIfStoreManager());
    }

    @Test
    public void testCheckIfStoreManagerWithStoreManager() {
        Employment employment = new Employment(Apointer,Apointee,store, Role.StoreManager);
        assertTrue(employment.checkIfStoreManager());
    }
}