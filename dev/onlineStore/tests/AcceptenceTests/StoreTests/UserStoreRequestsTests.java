package AcceptenceTests.StoreTests;

import Bridge.Bridge;
import Bridge.Driver;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserStoreRequestsTests {
    private Bridge bridge= Driver.getBridge();
    private final String Customer="Karen"; //your favorite customer
    private final String StoreFounder="Bob"; //a store founder
    private final String StoreOwner="Bob's owner son"; //a store owner
    private final String StoreManager="Bob's manager son"; //a store manager appointed by founder
    private final String StoreManager2="bob's manager nephew"; //a store manager appointed by store owner
    private final String StoreManager3="bob's manager son of bob's manager nephew"; //manager appointed by manager 2
    private final String StoreOwner2="bob's owner son of bob's manager son of bob's manager nephew"; //owner appinted by manager 3
    private final String notLoggedIn ="Jeff"; //NOT LOGGED IN
    private final String notRegistered ="jeff's nemesis"; //NOT REGISTERED
    private final String StoreFounder2="Robert"; //another store owner
    private final String Admin="adminMan";
    private final String store1="Bob's milk emporium";
    private final String store3="Robert's yogurt castle";
    private final String nonExistentStore="non-existent bakery";
    private final String store4="Bob's failed store";

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
        bridge.Register(Customer,"IWantToSeeYourManager123");
        bridge.Register(StoreFounder,"milk2077");
        bridge.Register(StoreOwner,"milk2078");
        bridge.Register(StoreManager,"yogurt2079");
        bridge.Register(StoreManager2,"ManagerUnderBobsOwnerSonUnderBob2080");
        bridge.Register(StoreManager3,"ManagerUnderBobsOwnerSonBobsOwnerSonUnderBob2081");
        bridge.Register(StoreOwner2,"ChainOfCommandTooLong2081");
        bridge.Register(notLoggedIn,"Jeff's password");
        bridge.Register(StoreFounder2,"yogurtIsTheBest654");
        bridge.Register(Admin,"AbsolutePower2023");
    }
    @Order(1)
    @Test
    public void TestStoreFounder(){
        bridge.Login(StoreFounder,"milk2077");
        assertTrue(bridge.OpenNewStore(store1)); //OK
        assertFalse(bridge.OpenNewStore(store1)); //store exists
        //testing add product for the store founder
        assertTrue(bridge.AddProduct(store1,"Milk","is milk",100,100)); //Ok
        assertTrue(bridge.AddProduct(store1,"Emptiness","Guaranteed void!",100,100)); //Ok
        assertTrue(bridge.AddProduct(store1,"Chicken","100% beef",100,100)); //Ok
        assertTrue(bridge.AddProduct(store1,"Cheese","60% holes!",100,100); //Ok
        assertFalse(bridge.AddProduct(store1,"Cheese","60% holes!",100,100)); //duplicate product
        assertFalse(bridge.AddProduct(nonExistentStore,"Milk","is milk",100,100)); //wrong store name
        assertFalse(bridge.AddProduct(store1,"","is milk",100,100)); //empty product name
        assertFalse(bridge.AddProduct(store1,"Milk","",100,100)); //empty product description
        assertFalse(bridge.AddProduct(store1,"Milk","is milk",0,100)); //invalid price
        assertFalse(bridge.AddProduct(store1,"Milk","is milk",-20,100)); //invalid price
        assertFalse(bridge.AddProduct(store1,"Milk","is milk",100,0)); //invalid amount
        assertFalse(bridge.AddProduct(store1,"Milk","is milk",100,-100)); //invalid amount
        //testing remove product
        assertTrue(bridge.RemoveProduct(store1,"Chicken")); //Ok
        assertFalse(bridge.RemoveProduct(store1,"Chicken")); //remove removed product
        assertFalse(bridge.RemoveProduct(nonExistentStore,"Chicken")); //invalid store
        assertFalse(bridge.RemoveProduct(store1,"")); //empty name
        //TODO: TEST IN THE FUTURE EDITING THIS STORE AND VALID PRODUCTS IN THIS STORE FROM ANOTHER STORE FOUNDER
        //testing name edit
        assertTrue(bridge.EditProductName(store1,"Cheese","Blue Cheese")); //Ok
        assertTrue(bridge.EditProductName(store1,"Blue Cheese","Cheese")); //Ok
        assertTrue(bridge.EditProductName(store1,"Cheese","Blue Cheese")); //Ok
        assertFalse(bridge.EditProductName(store1,"Chicken","gigo milk")); //editing removed product
        assertFalse(bridge.EditProductName(store1,"Blue Cheese","Milk")); //editing to existing product name
        assertFalse(bridge.EditProductName(store1,"Blue Cheese","")); //empty name
        assertFalse(bridge.EditProductName(nonExistentStore,"Blue Cheese","fission milk")); //invalid store
        //testing description edit
        assertTrue(bridge.EditDescription(store1,"Emptiness","very empty"));//OK
        assertFalse(bridge.EditDescription(store1,"Chicken","70% bacon!")); //removed product
        assertFalse(bridge.EditDescription(store1,"Emptiness","")); //empty description
        assertFalse(bridge.EditDescription(nonExistentStore,"Emptiness","70% bacon!")); //invalid store
        //testing price edit
        assertTrue(bridge.EditPrice(store1,"Milk",90));//OK
        assertFalse(bridge.EditPrice(store1,"Milk", 0)); //no price
        assertFalse(bridge.EditPrice(store1,"Milk", -99)); //false price
        assertFalse(bridge.EditPrice(store1,"Chicken", 99)); //removed product
        assertFalse(bridge.EditPrice(nonExistentStore,"Milk", 99)); //invalid store
        //founder appointing owner tests
        assertTrue(bridge.AppointOwner(StoreOwner,store1)); //Ok
        assertFalse(bridge.AppointOwner(StoreOwner,store1)); //user is already owner
        assertFalse(bridge.AppointOwner(StoreFounder,store1)); //appointing self and already has role
        assertFalse(bridge.AppointOwner(notRegistered,store1)); //appointing non registered user
        assertFalse(bridge.AppointOwner(Customer,nonExistentStore)); //appointing in an invalid store
        //founder appointing manager tests
        assertTrue(bridge.AppointManager(StoreManager,store1)); //Ok
        assertFalse(bridge.AppointManager(StoreManager,store1)); //user is already manager
        assertFalse(bridge.AppointManager(StoreFounder,store1)); //appointing self
        assertFalse(bridge.AppointManager(notRegistered,store1)); //appointing non registered user
        assertFalse(bridge.AppointManager(Customer,nonExistentStore)); //appointing in an invalid store
        //closed store tests
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
        //testing permission adding
        assertTrue(bridge.AddPermission(StoreManager,store1, new int[]{1}));//Ok
        assertFalse(bridge.AddPermission(StoreManager,store1, new int[]{1})); //dupe
        assertTrue(bridge.RemovePermission(StoreManager,store1, new int[]{1})); //OK
        assertTrue(bridge.AddPermission(StoreManager,store1, new int[]{2,3,4,5,6,7,8,9,10,11})); //add rest
        assertFalse(bridge.AddPermission(StoreManager,store1, new int[]{1,2,3,4,5,6,7,8})); //everyone but 1 is dupe
        assertTrue(bridge.AddPermission(StoreManager,store1, new int[]{1}));//Ok
        assertFalse(bridge.AddPermission(StoreOwner,store1, new int[]{1})); //owner has no permission restrictions
        assertFalse(bridge.RemovePermission(StoreOwner,store1, new int[]{1})); //owner has no permission restrictions
        assertFalse(bridge.AddPermission(StoreFounder,store1, new int[]{1})); //self has no permission restrictions
        assertFalse(bridge.RemovePermission(StoreFounder,store1, new int[]{1})); //self has no permission restrictions
        assertFalse(bridge.RemovePermission(StoreManager,nonExistentStore, new int[]{1})); //invalid store
        assertFalse(bridge.AddPermission(StoreManager,nonExistentStore, new int[]{1})); //invalid store
    }
    @Order(2)
    @Test
    public void TestStoreFounder(){
        bridge.Login(StoreOwner,"milk2078");
        //testing add product for the store owner
        assertTrue(bridge.AddProduct(store1,"owner's-Milk","is milk",100,100)); //Ok
        assertTrue(bridge.AddProduct(store1,"owner's-Emptiness","Guaranteed void!",100,100)); //Ok
        assertTrue(bridge.AddProduct(store1,"owner's-Chicken","100% beef",100,100)); //Ok
        assertTrue(bridge.AddProduct(store1,"owner's-Cheese","60% holes!",100,100); //Ok
        //testing remove product
        assertTrue(bridge.RemoveProduct(store1,"owner's-Chicken")); //Ok
        //testing name edit
        assertTrue(bridge.EditProductName(store1,"owner's-Cheese","owner's-Blue Cheese")); //Ok
        assertTrue(bridge.EditProductName(store1,"owner's-Blue Cheese","owner's-Cheese")); //Ok
        assertTrue(bridge.EditProductName(store1,"owner's-Cheese","owner's-Blue Cheese")); //Ok
        //testing description edit
        assertTrue(bridge.EditDescription(store1,"owner's-Emptiness","very empty"));//OK
        //testing price edit
        assertTrue(bridge.EditPrice(store1,"owner's-Milk",90));//OK
        //owner appointing owner tests
        assertTrue(bridge.AppointOwner(StoreOwner2,store1)); //Ok
        //founder appointing manager tests
        assertTrue(bridge.AppointManager(StoreManager2,store1)); //Ok
        //closed store tests
        assertFalse(bridge.CloseStore(store1)); //only founder can sloe
        //testing permission adding
        assertTrue(bridge.AddPermission(StoreManager2,store1, new int[]{1}));//Ok
        assertFalse(bridge.AddPermission(StoreManager2,store1, new int[]{1})); //dupe
        assertTrue(bridge.RemovePermission(StoreManager2,store1, new int[]{1})); //OK
        assertFalse(bridge.AddPermission(StoreManager2,store1, new int[]{1,2,3,4,5,6,7,8})); //Ok
        assertFalse(bridge.RemovePermission(StoreManager2,store1, new int[]{1,2,3,4,5,6,7,8})); //Ok
        assertFalse(bridge.AddPermission(StoreFounder,store1, new int[]{1})); //founder has no permission restrictions
        assertFalse(bridge.RemovePermission(StoreFounder,store1, new int[]{1})); //founder has no permission restrictions
        assertFalse(bridge.AddPermission(StoreOwner,store1, new int[]{1})); //self has no permission restrictions
        assertFalse(bridge.RemovePermission(StoreOwner,store1, new int[]{1})); //self has no permission restrictions
        assertFalse(bridge.RemovePermission(StoreManager2,nonExistentStore, new int[]{1})); //invalid store
        assertFalse(bridge.AddPermission(StoreManager2,nonExistentStore, new int[]{1})); //invalid store
    }
    @Order(2)
    @Test
    public void TestManagerWithPermissions(){
        bridge.Login(StoreManager,"yogurt2079");
        //testing add product for the store manager
        assertTrue(bridge.AddProduct(store1,"manager's-Milk","is milk",100,100)); //Ok
        assertTrue(bridge.AddProduct(store1,"manager's-Emptiness","Guaranteed void!",100,100)); //Ok
        assertTrue(bridge.AddProduct(store1,"manager's-Chicken","100% beef",100,100)); //Ok
        assertTrue(bridge.AddProduct(store1,"manager's-Cheese","60% holes!",100,100); //Ok
        //testing remove product
        assertTrue(bridge.RemoveProduct(store1,"manager's-Chicken")); //Ok
        //testing name edit
        assertTrue(bridge.EditProductName(store1,"owner's-Blue Cheese","owner's-Cheese")); //Ok
        //testing description edit
        assertTrue(bridge.EditDescription(store1,"owner's-Emptiness","very empty"));//OK
        //testing price edit
        assertTrue(bridge.EditPrice(store1,"owner's-Milk",90));//OK
        //owner appointing owner tests
        assertTrue(bridge.AppointOwner(StoreOwner2,store1)); //Ok
        //founder appointing manager tests
        assertTrue(bridge.AppointManager(StoreManager2,store1)); //Ok
        //closed store tests
        assertFalse(bridge.CloseStore(store1)); //only founder can close
    }
    @Order(4)
    @DisplayName("Store and product search tests")
    @Test
    public void StoreAndProductSearchTests(){
        String OkQuery="Milk"; String emptyQuery=""; String jibbrish="sdghajskdhaskdhasdcakl";
    }
}
