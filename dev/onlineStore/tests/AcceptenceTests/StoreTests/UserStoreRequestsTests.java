package AcceptenceTests.StoreTests;

import Bridge.Bridge;
import Bridge.Driver;

import static org.junit.jupiter.api.Assertions.*;

import TestObjects.TestUser;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)


public class UserStoreRequestsTests {
    private Bridge bridge= Driver.getBridge();
    private final TestUser Customer=new TestUser("Karen","IWantToSeeYourManager123"); //your favorite customer
    private final TestUser StoreFounder=new TestUser("Bob","milk2077"); //a store founder
    private final TestUser StoreOwner=new TestUser("Bob's owner son","milk2078"); //a store owner
    private final TestUser StoreManager=new TestUser("Bob's manager son","yogurt2079"); //a store manager appointed by founder
    private final TestUser StoreManager2=new TestUser("bob's manager nephew","ManagerUnderBobsOwnerSonUnderBob2080"); //a store manager appointed by store owner
    private final TestUser StoreManager3=new TestUser("bob's manager son of bob's manager nephew","ManagerUnderBobsOwnerSonBobsOwnerSonUnderBob2081"); //manager appointed by manager 2
    private final TestUser StoreOwner2=new TestUser("bob's owner son of bob's manager son of bob's manager nephew","ChainOfCommandTooLong2081"); //owner appointed by manager 3
    private final TestUser notLoggedIn =new TestUser("Jeff","Jeff's password"); //NOT LOGGED IN
    private final String notRegistered ="jeff's nemesis"; //NOT REGISTERED
    private final TestUser StoreFounder2=new TestUser("Robert","ChainOfCommandTooLong2081"); //another store owner
    private final TestUser Admin=new TestUser("adminMan","AbsolutePower2023");
    private final String store1="Bob's milk emporium";
    private final String store3="Robert's yogurt castle";
    private final String nonExistentStore="non-existent bakery";
    private final String store4="Bob's closed store";

    @DisplayName("Running Setup functions, any errors here will break the rest of the tests!")
    @BeforeAll
    public void SetUp(){
        bridge=Driver.getBridge();
        bridge.initialize();
        bridge.EnterMarket();
        RegisterUsers();
    }
    @AfterAll
    public void Exit(){
        bridge.ExitMarket();
    }
    @AfterEach
    public void Login(){
        bridge.Logout();
    }
    public void RegisterUsers(){
        bridge.Register(Customer.getUserName(), Customer.getPassword());
        bridge.Register(StoreFounder.getUserName(),StoreFounder.getPassword());
        bridge.Register(StoreOwner.getUserName(),StoreOwner.getPassword());
        bridge.Register(StoreOwner2.getUserName(),StoreFounder2.getPassword());
        bridge.Register(StoreManager2.getUserName(),StoreManager.getPassword());
        bridge.Register(Admin.getUserName(),Admin.getPassword());
        bridge.Register(StoreManager.getUserName(),StoreManager.getPassword());
        bridge.Register(notLoggedIn.getUserName(),notLoggedIn.getPassword());
    }
    @Order(1)
    @Test
    public void TestStoreFounder(){
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
        assertTrue(bridge.OpenNewStore(store1)); //OK
        assertFalse(bridge.OpenNewStore(store1)); //store exists

    }
    @Test
    public void TestAddProduct(){
        assertTrue(bridge.AddProduct(store1,"Milk","is milk",100,100)); //Ok
        assertTrue(bridge.AddProduct(store1,"Emptiness","Guaranteed void!",100,100)); //Ok
        assertTrue(bridge.AddProduct(store1,"Chicken","100% beef",100,100)); //Ok
        assertTrue(bridge.AddProduct(store1,"Cheese","60% holes!",100,100)); //Ok
        assertFalse(bridge.AddProduct(store1,"Cheese","60% holes!",100,100)); //duplicate product
        assertFalse(bridge.AddProduct(nonExistentStore,"Milk","is milk",100,100)); //wrong store name
        assertFalse(bridge.AddProduct(store1,"","is milk",100,100)); //empty product name
        assertFalse(bridge.AddProduct(store1,"Milk","",100,100)); //empty product description
        assertFalse(bridge.AddProduct(store1,"Milk","is milk",0,100)); //invalid price
        //assertFalse(bridge.AddProduct(store1,"Milk","is milk",-20,100)); //invalid price
        assertFalse(bridge.AddProduct(store1,"Milk","is milk",100,0)); //invalid amount
        //assertFalse(bridge.AddProduct(store1,"Milk","is milk",100,-100)); //invalid amount
    }
    @Test
    public void TestRemoveProduct(){
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
        assertTrue(bridge.RemoveProduct(store1,"Chicken")); //Ok
        assertFalse(bridge.RemoveProduct(store1,"Chicken")); //remove removed product
        assertFalse(bridge.RemoveProduct(nonExistentStore,"Chicken")); //invalid store
        assertFalse(bridge.RemoveProduct(store1,"")); //empty name
    }
    @Test
    public void TestEditName(){
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
        assertTrue(bridge.EditProductName(store1,"Cheese","Blue Cheese")); //Ok
        assertTrue(bridge.EditProductName(store1,"Blue Cheese","Cheese")); //Ok
        assertTrue(bridge.EditProductName(store1,"Cheese","Blue Cheese")); //Ok
        assertFalse(bridge.EditProductName(store1,"Chicken","gigo milk")); //editing removed product
        assertFalse(bridge.EditProductName(store1,"Blue Cheese","Milk")); //editing to existing product name
        assertFalse(bridge.EditProductName(store1,"Blue Cheese","")); //empty name
        assertFalse(bridge.EditProductName(nonExistentStore,"Blue Cheese","fission milk")); //invalid store
    }
    @Test
    public void TestEditDescription(){
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
        assertTrue(bridge.EditDescription(store1,"Emptiness","very empty"));//OK
        assertFalse(bridge.EditDescription(store1,"Chicken","70% bacon!")); //removed product
        assertFalse(bridge.EditDescription(store1,"Emptiness","")); //empty description
        assertFalse(bridge.EditDescription(nonExistentStore,"Emptiness","70% bacon!")); //invalid store
    }
    @Test
    public void TestEditPrice(){
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
        assertTrue(bridge.EditPrice(store1,"Milk",90));//OK
        assertFalse(bridge.EditPrice(store1,"Milk", 0)); //no price
   //     assertFalse(bridge.EditPrice(store1,"Milk", -99)); //false price
        assertFalse(bridge.EditPrice(store1,"Chicken", 99)); //removed product
        assertFalse(bridge.EditPrice(nonExistentStore,"Milk", 99)); //invalid store
    }
    @Test
    public void TestEditAmountBadUserInputs(){
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());


    }
    @Test
    public void TestAppointEmployees(){
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
        assertTrue(bridge.AppointOwner(StoreOwner.getUserName(),store1)); //Ok
        assertTrue(bridge.AppointManager(StoreManager.getUserName(),store1)); //Ok
        assertFalse(bridge.AppointOwner(StoreOwner.getUserName(),store1)); //user is already owner
        assertFalse(bridge.AppointOwner(StoreFounder.getUserName(),store1)); //appointing self and already has role
        assertFalse(bridge.AppointOwner(notRegistered,store1)); //appointing non registered user
        assertFalse(bridge.AppointOwner(Customer.getUserName(),nonExistentStore)); //appointing in an invalid store
        assertFalse(bridge.AppointManager(StoreManager.getUserName(),store1)); //user is already manager
        assertFalse(bridge.AppointManager(StoreFounder.getUserName(),store1)); //appointing self
        assertFalse(bridge.AppointManager(notRegistered,store1)); //appointing non registered user
        assertFalse(bridge.AppointManager(Customer.getUserName(),nonExistentStore)); //appointing in an invalid store

    }
    @Test
    public void TestCloseStore(){
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
        assertTrue(bridge.OpenNewStore(store4));
        assertTrue(bridge.AddProduct(store4,"Goat Milk","Feel the goat!",100,100));
        assertTrue(bridge.CloseStore(store4)); //Ok
        assertFalse(bridge.CloseStore(store4)); //store already closed
        assertFalse(bridge.CloseStore(nonExistentStore)); //store does not exist
        assertFalse(bridge.AddProduct(store4,"Platypus Milk","Contains real beaks!",100,100)); //adding to closed store
        assertFalse(bridge.RemoveProduct(store4,"Goat Milk")); //remove from closed store
        assertFalse(bridge.EditDescription(store4,"Goat Milk","Contains real goat hair!")); //editing description in closed store
        assertFalse(bridge.EditProductName(store4,"Goat Milk","Goat Yogurt")); //editing name in closed store
        assertFalse(bridge.EditPrice(store4,"Goat Milk",999)); //editing price in closed store
    }
    @Test
    public void TestEditPermissions(){
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
        assertTrue(bridge.AddPermission(StoreManager.getUserName(),store1, new int[]{1}));//Ok
        assertFalse(bridge.AddPermission(StoreManager.getUserName(),store1, new int[]{1})); //dupe
        assertFalse(bridge.AddPermission(StoreManager.getUserName(),store1, new int[]{})); //no changes requested
        assertTrue(bridge.RemovePermission(StoreManager.getUserName(),store1, new int[]{1})); //OK
        assertTrue(bridge.AddPermission(StoreManager.getUserName(),store1, new int[]{2,3,4,5,6,7,8,9,10,11})); //add rest
        assertFalse(bridge.AddPermission(StoreManager.getUserName(),store1, new int[]{1,2,3,4,5,6,7,8})); //everyone but 1 is duped
        assertTrue(bridge.AddPermission(StoreManager.getUserName(),store1, new int[]{1}));//Ok
        assertFalse(bridge.AddPermission(StoreOwner.getUserName(),store1, new int[]{1})); //owner has no permission restrictions
        assertFalse(bridge.RemovePermission(StoreOwner.getUserName(),store1, new int[]{1})); //owner has no permission restrictions
        assertFalse(bridge.AddPermission(StoreFounder.getUserName(),store1, new int[]{1})); //self has no permission restrictions
        assertFalse(bridge.RemovePermission(StoreFounder.getUserName(),store1, new int[]{1})); //self has no permission restrictions
        assertFalse(bridge.RemovePermission(StoreManager.getUserName(),nonExistentStore, new int[]{1})); //invalid store
        assertFalse(bridge.AddPermission(StoreManager.getUserName(),nonExistentStore, new int[]{1})); //invalid store
    }
    @Order(3)
    @Test
    public void TestStoreOwner(){
        bridge.Login(StoreOwner.getUserName(),StoreOwner.getPassword());
        //testing add product for the store owner
        assertTrue(bridge.AddProduct(store1,"owner's-Milk","is milk",100,100)); //Ok
        assertTrue(bridge.AddProduct(store1,"owner's-Emptiness","Guaranteed void!",100,100)); //Ok
        assertTrue(bridge.AddProduct(store1,"owner's-Chicken","100% beef",100,100)); //Ok
        assertTrue(bridge.AddProduct(store1,"owner's-Cheese","60% holes!",100,100)); //Ok
        //testing remove product
        assertTrue(bridge.RemoveProduct(store1,"owner's-Chicken")); //Ok
        //testing name edit
        assertTrue(bridge.EditProductName(store1,"owner's-Cheese","owner's-Blue Cheese")); //Ok
        //testing description edit
        assertTrue(bridge.EditDescription(store1,"owner's-Emptiness","very empty"));//OK
        //testing price edit
        assertTrue(bridge.EditPrice(store1,"owner's-Milk",90));//OK
        //owner appointing owner tests
        assertTrue(bridge.AppointOwner(StoreOwner2.getUserName(),store1)); //Ok
        //founder appointing manager tests
        assertTrue(bridge.AppointManager(StoreManager2.getUserName(),store1)); //Ok
        //closed store tests
        assertFalse(bridge.CloseStore(store1)); //only founder can sloe
        //testing permission adding
        assertTrue(bridge.AddPermission(StoreManager2.getUserName(),store1, new int[]{1,2,3,4,5}));//Ok
        assertTrue(bridge.RemovePermission(StoreManager2.getUserName(),store1, new int[]{1,2,3,4,5})); //OK
    }
    @Order(4)
    @Test
    public void TestManagerWithPermissions(){
        bridge.Login(StoreManager.getUserName(), StoreManager.getPassword());
        //testing add product for the store manager
        assertTrue(bridge.AddProduct(store1,"manager's-Milk","is milk",100,100)); //Ok
        assertTrue(bridge.AddProduct(store1,"manager's-Emptiness","Guaranteed void!",100,100)); //Ok
        assertTrue(bridge.AddProduct(store1,"manager's-Chicken","100% beef",100,100)); //Ok
        assertTrue(bridge.AddProduct(store1,"manager's-Cheese","60% holes!",100,100)); //Ok
        //testing remove product
        assertTrue(bridge.RemoveProduct(store1,"manager's-Chicken")); //Ok
        //testing name edit
        assertTrue(bridge.EditProductName(store1,"owner's-Blue Cheese","owner's-Cheese")); //Ok
        //testing description edit
        assertTrue(bridge.EditDescription(store1,"owner's-Emptiness","very empty"));//OK
        //testing price edit
        assertTrue(bridge.EditPrice(store1,"owner's-Milk",90));//OK
        //owner appointing owner tests
        assertTrue(bridge.AppointOwner(StoreOwner2.getUserName(),store1)); //Ok
        //founder appointing manager tests
        assertTrue(bridge.AppointManager(StoreManager2.getUserName(),store1)); //Ok
        //closed store tests
        assertFalse(bridge.CloseStore(store1)); //only founder can close
        //test manager apponted by another manager
        assertTrue(bridge.AddPermission(StoreManager3.getUserName(),store1, new int[]{1,2,3,4,5}));//Ok
        assertTrue(bridge.RemovePermission(StoreManager3.getUserName(),store1, new int[]{1,2,3,4,5})); //OK
    }
    @Order(5)
    @Test
    public void TestManagerWithoutPermissions(){
        bridge.Login(StoreManager2.getUserName(), StoreManager2.getPassword());
        //testing add product for the store manager
        assertFalse(bridge.AddProduct(store1,"poor manager's-Milk","is milk",100,100)); //no permissions
        assertFalse(bridge.AddProduct(store1,"poor manager's-Emptiness","Guaranteed void!",100,100)); //no permissions
        assertFalse(bridge.AddProduct(store1,"poor manager's-Chicken","100% beef",100,100)); //no permissions
        assertFalse(bridge.AddProduct(store1,"poor manager's-Cheese","60% holes!",100,100)); //no permissions
        //testing remove product
        assertFalse(bridge.RemoveProduct(store1,"manager's-Chicken")); //no permissions
        //testing name edit
        assertFalse(bridge.EditProductName(store1,"owner's-Blue Cheese","owner's-Cheese")); //no permissions
        //testing description edit
        assertFalse(bridge.EditDescription(store1,"owner's-Emptiness","very empty"));//no permissions
        //testing price edit
        assertFalse(bridge.EditPrice(store1,"owner's-Milk",90));//no permissions
        //owner appointing owner tests
        assertFalse(bridge.AppointOwner(StoreOwner2.getUserName(),store1)); //no permissions
        //founder appointing manager tests
        assertFalse(bridge.AppointManager(StoreManager2.getUserName(),store1)); //no permissions
        //closed store tests
        assertFalse(bridge.CloseStore(store1)); //only founder can close
    }
    @Order(6)
    @Test
    public void TestFounder2(){
        bridge.Login(StoreFounder2.getUserName(), StoreFounder2.getPassword());
        bridge.AppointManager(StoreManager.getUserName(), store3);
    }
    @Order(7)
    @Test
    public void TestCustomer(){
        bridge.Login(Customer.getUserName(), Customer.getPassword());
        //testing add product for the store manager
        assertFalse(bridge.AddProduct(store1,"poor manager's-Milk","is milk",100,100)); //no permissions
        //assertFalse(bridge.AddProduct(store1,"poor manager's-Emptiness","Guaranteed void!",100,100)); //no permissions
        //assertFalse(bridge.AddProduct(store1,"poor manager's-Chicken","100% beef",100,100)); //no permissions
        //assertFalse(bridge.AddProduct(store1,"poor manager's-Cheese","60% holes!",100,100)); //no permissions
        //testing remove product
        assertFalse(bridge.RemoveProduct(store1,"manager's-Chicken")); //no permissions
        //testing name edit
        assertFalse(bridge.EditProductName(store1,"owner's-Blue Cheese","owner's-Cheese")); //no permissions
        //testing description edit
        assertFalse(bridge.EditDescription(store1,"owner's-Emptiness","very empty"));//no permissions
        //testing price edit
        assertFalse(bridge.EditPrice(store1,"owner's-Milk",90));//no permissions
        //owner appointing owner tests
        assertFalse(bridge.AppointOwner(StoreOwner2.getUserName(),store1)); //no permissions
        //founder appointing manager tests
        assertFalse(bridge.AppointManager(StoreManager2.getUserName(),store1)); //no permissions
        //closed store tests
        assertFalse(bridge.CloseStore(store1)); //no permissions
    }
    @Order(8)
    @Test
    public void TestNotLoggedIn(){
        //testing add product
        assertFalse(bridge.AddProduct(store1,"poor manager's-Milk","is milk",100,100)); //not logged in
        assertFalse(bridge.AddProduct(store1,"poor manager's-Emptiness","Guaranteed void!",100,100)); //not logged in
        assertFalse(bridge.AddProduct(store1,"poor manager's-Chicken","100% beef",100,100)); //not logged in
        assertFalse(bridge.AddProduct(store1,"poor manager's-Cheese","60% holes!",100,100)); //not logged in
        //testing remove product
        assertFalse(bridge.RemoveProduct(store1,"manager's-Chicken")); //not logged in
        //testing name edit
        assertFalse(bridge.EditProductName(store1,"owner's-Blue Cheese","owner's-Cheese")); //not logged in
        //testing description edit
        assertFalse(bridge.EditDescription(store1,"owner's-Emptiness","very empty"));//not logged in
        //testing price edit
        assertFalse(bridge.EditPrice(store1,"owner's-Milk",90));//not logged in
        assertFalse(bridge.AppointOwner(StoreOwner2.getUserName(),store1)); //not logged in
        assertFalse(bridge.AppointManager(StoreManager2.getUserName(),store1)); //not logged in
        //closed store tests
        assertFalse(bridge.CloseStore(store1)); //not logged in
    }
    @Order(4)
    @DisplayName("Store and product search tests")
    @Test
    public void StoreAndProductSearchTests(){
        String OkQuery="Milk"; String emptyQuery=""; String jibbrish="sdghajskdhaskdhasdcakl";
    }
}
