package AcceptenceTests.StoreTests;

import Bridge.Bridge;
import Bridge.Driver;

import static org.junit.jupiter.api.Assertions.*;

import TestObjects.TestUser;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private int store1=-1;
    private int store3=-1;
    private final int nonExistentStore=-1;
    private int store4=-1;
    private final HashMap<String,String> productIdMap=new HashMap<>();

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
    public void StoreFounderTest(){
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
        int store1=bridge.OpenNewStore("Bob's Milk Emporium");
        assertNotEquals(-1,store1); //OK
        assertEquals(-1,bridge.OpenNewStore("Bob's Milk Emporium")); //store exists
    }
    @Order(2)
    @Test
    public void AddProductTest(){
        String Milk=bridge.AddProduct(store1,"Milk","is milk",100,100); //Ok
        String Emptiness=bridge.AddProduct(store1,"Emptiness","Guaranteed void!",100,100); //Ok
        String Chicken=bridge.AddProduct(store1,"Chicken","100% beef",100,100); //Ok
        String Cheese=bridge.AddProduct(store1,"Cheese","60% holes!",100,100); //Ok
        productIdMap.put("Milk",Milk);
        productIdMap.put("Emptiness",Emptiness);
        productIdMap.put("Chicken",Chicken);
        productIdMap.put("Cheese",Cheese);
        assertNotEquals("Error",bridge.AddProduct(store1,"Cheese","60% holes!",100,100)); //duplicate product
        assertNotEquals("Error",bridge.AddProduct(nonExistentStore,"Milk","is milk",100,100)); //wrong store name
        assertNotEquals("Error",bridge.AddProduct(store1,"","is milk",100,100)); //empty product name
        assertNotEquals("Error",bridge.AddProduct(store1,"Milk","",100,100)); //empty product description
        assertNotEquals("Error",bridge.AddProduct(store1,"Milk","is milk",0,100)); //invalid price
        assertNotEquals("Error",bridge.AddProduct(store1,"Milk","is milk",100,0)); //invalid amount
    }
    @Order(3)
    @Test
    public void RemoveProductTest(){
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
        assertTrue(bridge.RemoveProduct(store1,productIdMap.get("Chicken"))); //Ok
        assertFalse(bridge.RemoveProduct(store1,productIdMap.get("Chicken"))); //remove removed product
        assertFalse(bridge.RemoveProduct(nonExistentStore,productIdMap.get("Chicken"))); //invalid store
        assertFalse(bridge.RemoveProduct(store1,"")); //empty name
    }
    @Order(4)
    @Test
    public void EditNameTest(){
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
        assertTrue(bridge.EditProductName(store1,"Cheese","Blue Cheese")); //Ok
        assertTrue(bridge.EditProductName(store1,"Blue Cheese","Cheese")); //Ok
        assertTrue(bridge.EditProductName(store1,"Cheese","Blue Cheese")); //Ok
        assertFalse(bridge.EditProductName(store1,"Chicken","gigo milk")); //editing removed product
        assertFalse(bridge.EditProductName(store1,"Blue Cheese","Milk")); //editing to existing product name
        assertFalse(bridge.EditProductName(store1,"Blue Cheese","")); //empty name
        assertFalse(bridge.EditProductName(nonExistentStore,"Blue Cheese","fission milk")); //invalid store
    }
    @Order(5)
    @Test
    public void EditDescriptionTest(){
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
        assertTrue(bridge.EditDescription(store1,"Emptiness","very empty"));//OK
        assertFalse(bridge.EditDescription(store1,"Chicken","70% bacon!")); //removed product
        assertFalse(bridge.EditDescription(store1,"Emptiness","")); //empty description
        assertFalse(bridge.EditDescription(nonExistentStore,"Emptiness","70% bacon!")); //invalid store
    }
    @Order(6)
    @Test
    public void EditPriceTest(){
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
        assertTrue(bridge.EditPrice(store1,"Milk",90));//OK
        assertFalse(bridge.EditPrice(store1,"Milk", 0)); //no price
   //     assertFalse(bridge.EditPrice(store1,"Milk", -99)); //false price
        assertFalse(bridge.EditPrice(store1,"Chicken", 99)); //removed product
        assertFalse(bridge.EditPrice(nonExistentStore,"Milk", 99)); //invalid store
    }
    @Order(7)
    @Test
    public void EditAmountTest(){
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
    }
    @Order(8)
    @Test
    public void AppointEmployeesTest(){
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
    @Order(9)
    @Test
    public void RemoveEmployeesTest(){
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
        assertTrue(bridge.RemoveOwner(StoreOwner.getUserName(),store1)); //Ok
        assertTrue(bridge.AppointManager(StoreManager.getUserName(),store1)); //Ok
        assertFalse(bridge.RemoveOwner(StoreOwner.getUserName(),store1)); //user is already owner
        assertFalse(bridge.RemoveOwner(StoreFounder.getUserName(),store1)); //removing self
        assertFalse(bridge.RemoveOwner(notRegistered,store1)); //removing non registered user
        assertFalse(bridge.RemoveOwner(Customer.getUserName(),nonExistentStore)); //removing from an invalid store
        assertFalse(bridge.AppointManager(StoreFounder.getUserName(),store1)); //removing self
        assertFalse(bridge.AppointManager(notRegistered,store1)); //removing non registered user
        assertFalse(bridge.AppointManager(Customer.getUserName(),nonExistentStore)); //removing from an invalid store
        bridge.AppointOwner(StoreOwner.getUserName(),store1);
        bridge.AppointManager(StoreManager.getUserName(),store1);
    }
    @Order(10)
    @Test
    public void CloseStoreTest(){
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
        assertNotEquals(-1,bridge.OpenNewStore("Jeff's Failed Business"));
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
    @Order(11)
    @Test
    public void EditPermissionsTest(){
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
        assertTrue(bridge.AddPermission(StoreManager.getUserName(),store1, new int[]{1}));//Ok
        assertFalse(bridge.AddPermission(StoreManager.getUserName(),store1, new int[]{1})); //already has permissions
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
    @Order(12)
    @Test
    public void StoreOwnerTest(){
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
    @Order(13)
    @Test
    public void ManagerWithPermissionsTest(){
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
    @Order(15)
    @Test
    public void ManagerWithoutPermissionsTest(){
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
    @Order(16)
    @Test
    public void FounderTest2(){
        bridge.Login(StoreFounder2.getUserName(), StoreFounder2.getPassword());
        bridge.AppointManager(StoreManager.getUserName(), store3);
    }
    @Order(17)
    @Test
    public void CustomerTest(){
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
    @Order(18)
    @Test
    public void NotLoggedInTest(){
        assertFalse(bridge.AddProduct(store1,"poor manager's-Milk","is milk",100,100)); //not logged in
        assertFalse(bridge.AddProduct(store1,"poor manager's-Emptiness","Guaranteed void!",100,100)); //not logged in
        assertFalse(bridge.AddProduct(store1,"poor manager's-Chicken","100% beef",100,100)); //not logged in
        assertFalse(bridge.AddProduct(store1,"poor manager's-Cheese","60% holes!",100,100)); //not logged in
        assertFalse(bridge.RemoveProduct(store1,"manager's-Chicken")); //not logged in
        assertFalse(bridge.EditProductName(store1,"owner's-Blue Cheese","owner's-Cheese")); //not logged in
        assertFalse(bridge.EditDescription(store1,"owner's-Emptiness","very empty"));//not logged in
        assertFalse(bridge.EditPrice(store1,"owner's-Milk",90));//not logged in
    }
    private void SearchPrep(){
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
        bridge.AddProduct(store1,"Soup","Mushrooms",1,1);
        bridge.AddProduct(store1,"Bacon","Fresh",1,1);
        bridge.AddProduct(store1,"Grill Pack","Contains Bacon",1,1);
    }
    @Order(19)
    @DisplayName("Store and product search tests")
    @Test
    public void StoreProductSearch(){
        //note: no one is logged in for this test, and no one should be.
        SearchPrep();
        String jibbrish="sdghajskdhaskdfsdf321dhasdcakl";
        assertTrue(bridge.ProductSearch("").isEmpty()); //empty search expects no results
        assertTrue(bridge.ProductSearch(jibbrish).isEmpty()); //jubbrish search expects no results
        assertTrue(bridge.ProductSearch("Soup").contains("Soup"));//query in name keyword
        assertTrue(bridge.ProductSearch("Fresh").contains("Bacon")); //query for description keyword
        List<String> query=bridge.ProductSearch("Bacon"); //query in both name and description
        assertTrue(query.contains("Bacon"));
        assertTrue(query.contains("Grill Pack"));
    }
}
