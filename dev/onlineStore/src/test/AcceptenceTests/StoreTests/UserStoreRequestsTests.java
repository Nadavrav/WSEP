package AcceptenceTests.StoreTests;

import ServiceLayer.ServiceObjects.Fiters.ProductFilters.MaxPriceProductFilter;
import ServiceLayer.ServiceObjects.Fiters.ProductFilters.NameProductFilter;
import ServiceLayer.ServiceObjects.Fiters.ProductFilters.ProductFilter;
import ServiceLayer.ServiceObjects.Fiters.StoreFilters.NameStoreFilter;
import ServiceLayer.ServiceObjects.Fiters.StoreFilters.StoreFilter;
import ServiceLayer.ServiceObjects.ServiceProducts.ServiceStoreProduct;
import ServiceLayer.ServiceObjects.ServiceStore;
import org.junit.jupiter.api.*;
import Bridge.Bridge;
import Bridge.Driver;
import TestObjects.TestUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)


public class UserStoreRequestsTests {
    private Bridge bridge= Driver.getBridge();
    private Integer Milk;
    private Integer Emptiness;
    private Integer Chicken;
    private Integer Cheese;
    private final TestUser Customer=new TestUser("Karen","IWantToSeeYourManager123"); //your favorite customer
    private final TestUser StoreFounder=new TestUser("Bob","milk2077"); //a store founder
    private final TestUser StoreOwner=new TestUser("Bob's owner son","milk2078"); //a store owner
    private final TestUser StoreManager=new TestUser("Bob's manager son","yogurt2079"); //a store manager appointed by founder
    private final TestUser StoreManager2=new TestUser("bob's manager nephew","ManagerUnderBobsOwnerSonUnderBob2080"); //a store manager appointed by store owner
    private final TestUser StoreManager3=new TestUser("son of bob's manager nephew","ManagerUnderBobsOwnerSonBobsOwnerSonUnderBob2081"); //manager appointed by manager 2
    private final TestUser StoreOwner2=new TestUser("managerofbob'smanager nephew","ChainOfCommandTooLong2081"); //owner appointed by manager 3
    private final TestUser notLoggedIn =new TestUser("Jeff","Jeff's password"); //NOT LOGGED IN
    private final TestUser StoreFounder2=new TestUser("Robert","ChainOfCommandTooLong2081"); //another store owner
    private final TestUser Admin=new TestUser("adminMan","AbsolutePower2023");
    private int store1=-1;
    private int store3=-1;
    private final int nonExistentStore=-1000;
    private int store4=-1;
    private final HashMap<String,Integer> productIdMap=new HashMap<>();

    @DisplayName("Running Setup functions, any errors here will break the rest of the tests!")
    @BeforeAll
    public void SetUp(){
        bridge=Driver.getBridge();
        bridge.initialize();
        bridge.EnterMarket(); //1.1
        RegisterUsers();
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
        store1=bridge.OpenNewStore("Bob's Milk Emporium"); //3.2
        Assertions.assertNotEquals(-1,store1); //OK
        bridge.Logout();

        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());

        Milk=bridge.AddProduct(store1,"Milk","is milk",100,100); //Ok
        Emptiness=bridge.AddProduct(store1,"Emptiness","Guaranteed void!",100,100); //Ok
        Chicken=bridge.AddProduct(store1,"Chicken","100% beef",100,100); //Ok
        Cheese=bridge.AddProduct(store1,"Cheese","60% holes!",100,100); //Ok
        Assertions.assertNotEquals("Error",Milk);
        Assertions.assertNotEquals("Error",Emptiness);
        Assertions.assertNotEquals("Error",Chicken);
        Assertions.assertNotEquals("Error",Cheese);
        productIdMap.put("Milk",Milk);
        productIdMap.put("Emptiness",Emptiness);
        productIdMap.put("Chicken",Chicken);
        productIdMap.put("Cheese",Cheese);
        assertTrue(bridge.AppointOwner(StoreOwner.getUserName(),store1));
        bridge.Logout();
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
        bridge.Register(StoreOwner2.getUserName(),StoreOwner2.getPassword());
        bridge.Register(StoreManager2.getUserName(),StoreManager.getPassword());
        bridge.Register(Admin.getUserName(),Admin.getPassword());
        bridge.Register(StoreManager.getUserName(),StoreManager.getPassword());
        bridge.Register(notLoggedIn.getUserName(),notLoggedIn.getPassword());
    }
    @Order(3)
    @Test
    public void RemoveProductTest(){
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
        Chicken=bridge.AddProduct(store1,"Chicken","100% beef",100,100); //Ok
        assertTrue(bridge.RemoveProduct(Chicken, store1)); //Ok
        assertFalse(bridge.RemoveProduct(Chicken, store1)); //remove removed product
        assertFalse(bridge.RemoveProduct(Chicken, store1)); //remove removed product
        assertFalse(bridge.RemoveProduct(-1, store1)); //empty name
    }
    @Order(4)
    @Test
    public void EditNameTest(){
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
        assertTrue(bridge.EditProductName(productIdMap.get("Cheese"), store1, "Blue Cheese")); //Ok
        productIdMap.put("Blue Cheese",productIdMap.get("Cheese"));
        productIdMap.remove("Cheese");
        assertFalse(bridge.EditProductName(productIdMap.get("Chicken"), store1, "beef")); //editing removed product
        assertFalse(bridge.EditProductName(-5, store1, "Milk")); //editing to existing product name
        assertFalse(bridge.EditProductName(-5, store1, "")); //empty name
    }
    @Order(5)
    @Test
    public void EditDescriptionTest(){
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
        assertFalse(bridge.EditDescription(-5, store1, "40% holes"));
        assertTrue(bridge.EditDescription(Chicken, store1, "70% bacon!")); //removed product
        assertTrue(bridge.EditDescription(Emptiness, store1, "")); //empty description
    }
    @Order(6)
    @Test
    public void EditPriceTest(){
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
        assertTrue(bridge.EditPrice(productIdMap.get("Milk"), store1, 90));//OK
        assertFalse(bridge.EditPrice(productIdMap.get("Milk"), store1, 0)); //no price
        assertFalse(bridge.EditPrice(Milk,store1, -99)); //false price
        assertTrue(bridge.RemoveProduct(productIdMap.get("Chicken"), store1)); //O
        assertFalse(bridge.EditPrice(Chicken, store1, 99)); //removed product
        Chicken=bridge.AddProduct(store1,"Chicken","100% beef",100,100); //Ok
    }
    @Order(7)
    @Test
    public void EditAmountTest(){
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
    }
    @Order(8)
    @Test
    public void AppointEmployeesTest(){ //4.4 & 4.6
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
        //assertTrue(bridge.AppointOwner(StoreOwner.getUserName(),store1)); //Ok
        //assertTrue(bridge.AppointManager(StoreManager.getUserName(),store1)); //Ok
        assertFalse(bridge.AppointOwner(StoreOwner.getUserName(),store1)); //user is already owner
        assertTrue(bridge.AppointOwner(StoreFounder.getUserName(),store1));
        //NOT REGISTERED
        String notRegistered = "jeff's nemesis";
        assertFalse(bridge.AppointOwner(notRegistered,store1)); //appointing non registered user
        assertFalse(bridge.AppointOwner(Customer.getUserName(),nonExistentStore)); //appointing in an invalid store
        assertFalse(bridge.AppointManager(StoreManager.getUserName(),store1)); //user is already manager
        assertFalse(bridge.AppointManager(StoreFounder.getUserName(),store1)); //appointing self
        assertFalse(bridge.AppointManager(notRegistered,store1)); //appointing non registered user
        assertFalse(bridge.AppointManager(Customer.getUserName(),nonExistentStore)); //appointing in an invalid store
    }
   // @Order(9)
   // @Test
   // public void RemoveEmployeesTest(){
   //     bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
   //     assertTrue(bridge.RemoveOwner(StoreOwner.getUserName(),store1)); //Ok
   //     assertTrue(bridge.AppointManager(StoreManager.getUserName(),store1)); //Ok
   //     assertFalse(bridge.RemoveOwner(StoreOwner.getUserName(),store1)); //user is already owner
   //     assertFalse(bridge.RemoveOwner(StoreFounder.getUserName(),store1)); //removing self
   //     assertFalse(bridge.RemoveOwner(notRegistered,store1)); //removing non registered user
   //     assertFalse(bridge.RemoveOwner(Customer.getUserName(),nonExistentStore)); //removing from an invalid store
   //     assertFalse(bridge.AppointManager(StoreFounder.getUserName(),store1)); //removing self
   //     assertFalse(bridge.AppointManager(notRegistered,store1)); //removing non registered user
   //     assertFalse(bridge.AppointManager(Customer.getUserName(),nonExistentStore)); //removing from an invalid store
   //     bridge.AppointOwner(StoreOwner.getUserName(),store1);
   //     bridge.AppointManager(StoreManager.getUserName(),store1);
    //}
    @Order(10)
    @Test
    public void CloseStoreTest(){ //4.9
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
        store4 = bridge.OpenNewStore("Jeff's Failed Business");
        assertNotEquals(-1,store4);
        Integer gMilk=bridge.AddProduct(store4,"Goat Milk","Feel the goat!",100,100);
        Assertions.assertNotEquals(-1,gMilk);
        productIdMap.put("gMilk",gMilk);
        assertTrue(bridge.CloseStore(store4)); //Ok
        assertFalse(bridge.CloseStore(nonExistentStore)); //store does not exist
        assertEquals(-1,bridge.AddProduct(store4,"Platypus Milk","Contains real beaks!",100,100)); //adding to closed store
        assertFalse(bridge.RemoveProduct(productIdMap.get("gMilk"), store4)); //remove from closed store
        assertFalse(bridge.EditDescription(productIdMap.get("gMilk"), store4, "Contains real goat hair!")); //editing description in closed store
        assertFalse(bridge.EditProductName(productIdMap.get("gMilk"), -1, "Goat Yogurt")); //editing name in closed store
        assertFalse(bridge.EditPrice(productIdMap.get("gMilk"), store4, 999)); //editing price in closed store
    }
    @Order(11)
    @Test
    public void EditPermissionsTest(){//4.7
        bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
        bridge.AppointOwner(StoreOwner.getUserName(),store1);
        bridge.AppointManager(StoreManager.getUserName(),store1);
        assertTrue(bridge.AddPermission(StoreManager.getUserName(),store1, new int[]{1}));//Ok
        assertTrue(bridge.RemovePermission(StoreManager.getUserName(),store1, new int[]{1})); //OK
        assertTrue(bridge.AddPermission(StoreManager.getUserName(),store1, new int[]{2,3,4,5,6,7,8,9,10})); //add rest
        assertTrue(bridge.AddPermission(StoreManager.getUserName(),store1, new int[]{1,2,3,4,5,6,7,8})); //everyone but 1 is duped
        assertTrue(bridge.AddPermission(StoreManager.getUserName(),store1, new int[]{1}));//Ok
        assertFalse(bridge.RemovePermission(StoreManager.getUserName(),nonExistentStore, new int[]{1})); //invalid store
        assertFalse(bridge.AddPermission(StoreManager.getUserName(),nonExistentStore, new int[]{1})); //invalid store
    }
    @Order(12)
    @Test
    public void StoreOwnerTest(){ //4.3
        bridge.Login(StoreOwner.getUserName(),StoreOwner.getPassword());
        //testing add product for the store owner
        Integer OMilk=bridge.AddProduct(store1,"OMilk","is milk",100,100);
        Integer OEmptiness=bridge.AddProduct(store1,"OEmptiness","Guaranteed void!",100,100); //Ok
        Integer OChicken=bridge.AddProduct(store1,"OChicken","100% beef",100,100); //Ok
        Integer OCheese=bridge.AddProduct(store1,"OCheese","60% holes!",100,100); //Ok
        Assertions.assertNotEquals(-1,OMilk);
        Assertions.assertNotEquals(-1,OEmptiness);
        Assertions.assertNotEquals(-1,OChicken);
        Assertions.assertNotEquals(-1,OCheese);
        productIdMap.put("OMilk",OMilk);
        productIdMap.put("OEmptiness",OEmptiness);
        productIdMap.put("OChicken",OChicken);
        productIdMap.put("OCheese",OCheese);
        //testing remove product
        assertTrue(bridge.RemoveProduct(productIdMap.get("OChicken"), store1)); //Ok
        //testing name edit
        assertTrue(bridge.EditProductName(productIdMap.get("OCheese"), store1, "OBlue Cheese")); //Ok
        productIdMap.put("OBlue Cheese",productIdMap.get("OCheese"));
        productIdMap.remove("OCheese");
        //testing description edit
        assertTrue(bridge.EditDescription(productIdMap.get("OEmptiness"), store1, "very empty"));//OK
        //testing price edit
        assertTrue(bridge.EditPrice(productIdMap.get("OMilk"), store1, 90));//OK
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
    public void ManagerWithPermissionsTest(){// 4.5
        bridge.Login(StoreManager.getUserName(), StoreManager.getPassword());
        Integer MMilk=bridge.AddProduct(store1,"MMilk","is milk",100,100);
        Integer MEmptiness=bridge.AddProduct(store1,"MEmptiness","Guaranteed void!",100,100); //Ok
        Integer MChicken=bridge.AddProduct(store1,"MChicken","100% beef",100,100); //Ok
        Integer MCheese=bridge.AddProduct(store1,"MCheese","60% holes!",100,100); //Ok
        Assertions.assertNotEquals(-1,MMilk);
        Assertions.assertNotEquals(-1,MEmptiness);
        Assertions.assertNotEquals(-1,MChicken);
        Assertions.assertNotEquals(-1,MCheese);
        productIdMap.put("MMilk",MMilk);
        productIdMap.put("MEmptiness",MEmptiness);
        productIdMap.put("MChicken",MChicken);
        productIdMap.put("MCheese",MCheese);
        //testing add product for the store manager
        //testing remove product
        assertTrue(bridge.RemoveProduct(MChicken, store1)); //Ok
        //testing name edit
        assertFalse(bridge.EditProductName(MCheese, store1, "MCheese")); //Ok
        //testing description edit
        assertFalse(bridge.EditDescription(MEmptiness, store1, "very empty"));//OK
        //testing price edit
        assertFalse(bridge.EditPrice(MMilk, store1, 90));//OK
        //owner appointing owner tests
        assertFalse(bridge.AppointOwner(StoreOwner2.getUserName(),store1)); //Ok
        //founder appointing manager tests
        assertFalse(bridge.AppointManager(StoreManager2.getUserName(),store1)); //Ok
        //closed store tests
        assertFalse(bridge.CloseStore(store1)); //only founder can close
        //test manager appointed by another manager
        assertFalse(bridge.AddPermission(StoreManager3.getUserName(),store1, new int[]{1,2,3,4,5}));//Ok
        assertFalse(bridge.RemovePermission(StoreManager3.getUserName(),store1, new int[]{1,2,3,4,5})); //OK
    }
    @Order(15)
    @Test
    public void ManagerWithoutPermissionsTest(){ //4.5
        bridge.Login(StoreManager2.getUserName(), StoreManager2.getPassword());
        //testing add product for the store manager
        assertEquals(-1,bridge.AddProduct(store1,"OMilk","is milk",100,100)); //no permissions
        //testing remove product
        assertFalse(bridge.RemoveProduct(Chicken, store1)); //no permissions
        //testing name edit
        assertFalse(bridge.EditProductName(Cheese, store1, "owner's-Cheese")); //no permissions
        //testing description edit
        assertFalse(bridge.EditDescription(Emptiness, store1, "very empty"));//no permissions
        //testing price edit
        assertFalse(bridge.EditPrice(Milk, store1, 90));//no permissions
        //owner appointing owner tests
        assertFalse(bridge.AppointOwner(StoreOwner2.getUserName(),store1)); //no permissions
        //founder appointing manager tests
        assertFalse(bridge.AppointManager(StoreManager2.getUserName(),store1)); //no permissions
        //closed store tests
        assertFalse(bridge.CloseStore(store1)); //only founder can close
    }
    @Order(17)
    @Test
    public void CustomerTest(){ //
        bridge.Login(Customer.getUserName(), Customer.getPassword());
        //testing add product for the store manager
        assertEquals(-1,bridge.AddProduct(store1,"poor manager's-Milk","is milk",100,100)); //no permissions
        //testing remove product
        assertFalse(bridge.RemoveProduct(Chicken, store1)); //no permissions
        //testing name edit
        assertFalse(bridge.EditProductName(Cheese, store1, "owner's-Cheese")); //no permissions
        //testing description edit
        assertFalse(bridge.EditDescription(Emptiness, store1, "very empty"));//no permissions
        //testing price edit
        assertFalse(bridge.EditPrice(Milk, store1, 90));//no permissions
        //owner appointing owner tests
        assertFalse(bridge.AppointOwner(StoreOwner2.getUserName(),store1)); //no permissions
        //founder appointing manager tests
        assertFalse(bridge.AppointManager(StoreManager2.getUserName(),store1)); //no permissions
        //closed store tests
        assertFalse(bridge.CloseStore(store1)); //only founder can close
    }
    @Order(18)
    @Test
    public void NotLoggedInTest(){ //1.1
        assertEquals(-1,bridge.AddProduct(store1,"poor manager's-Milk","is milk",100,100)); //no permissions
        //testing remove product
        assertFalse(bridge.RemoveProduct(Chicken, store1)); //no permissions
        //testing name edit
        assertFalse(bridge.EditProductName(Cheese, store1, "owner's-Cheese")); //no permissions
        //testing description edit
        assertFalse(bridge.EditDescription(Emptiness, store1, "very empty"));//no permissions
        //testing price edit
        assertFalse(bridge.EditPrice(Milk, store1, 90));//no permissions
        //owner appointing owner tests
        assertFalse(bridge.AppointOwner(StoreOwner2.getUserName(),store1)); //no permissions
        //founder appointing manager tests
        assertFalse(bridge.AppointManager(StoreManager2.getUserName(),store1)); //no permissions
        //closed store tests
        assertFalse(bridge.CloseStore(store1)); //only founder can close
    }
    //private void SearchPrep(){
    //    bridge.Login(StoreFounder.getUserName(),StoreFounder.getPassword());
    //    bridge.AddProduct(store1,"Soup","Mushrooms",1,1);
    //    bridge.AddProduct(store1,"Bacon","Fresh",1,1);
    //    bridge.AddProduct(store1,"Grill Pack","Contains Bacon",1,1);
    //}
    public void FilterSearchPrep(){
        bridge.Login(StoreFounder.getUserName(), StoreFounder.getPassword());
        int store4=bridge.OpenNewStore("Store4");
        Integer Hamburger=bridge.AddProduct(store4,"Hamburger","contains beef and good taste. kosher",30,70);
        Integer Sausage=bridge.AddProduct(store4,"Sausage","contains beef and lots of oil. kosher",15,120); //Ok
        Integer Steak=bridge.AddProduct(store4,"Steak","99% beef, 1% olive oil. kosher",70,35); //Ok
        Integer Cheeseburger=bridge.AddProduct(store4,"Cheeseburger","contains cheese and beef",40,70); //Ok
        productIdMap.put("Hamburger",Hamburger);
        productIdMap.put("Sausage",Sausage);
        productIdMap.put("Steak",Steak);
        productIdMap.put("Cheeseburger",Cheeseburger);
        bridge.Logout();
    }
    @Order(20)
    @Test
    public void FilterSearchTests(){ //2.2
        FilterSearchPrep();
        ServiceStoreProduct ServiceHamburger=new ServiceStoreProduct("Hamburger", 30.0,"test","contains beef and good taste. kosher",5,1);
        ServiceStoreProduct ServiceSausage=new ServiceStoreProduct("Sausage", 15.0,"test","contains beef and lots of oil. kosher",5,1);
        ServiceStoreProduct ServiceSteak=new ServiceStoreProduct("Steak", 70.0,"test","99% beef, 1% olive oil. kosher",5,1);
        ServiceStoreProduct ServiceCheeseburger=new ServiceStoreProduct("Cheeseburger", 40.0,"test","contains cheese and beef",5,1);
        ArrayList<StoreFilter> storeFilters=new ArrayList<>();
        storeFilters.add(new NameStoreFilter("Store4"));
        NameProductFilter nameFilter=new NameProductFilter("burger");
        MaxPriceProductFilter maxPriceFilter=new MaxPriceProductFilter(35);
        ArrayList<ProductFilter> nameProductFilterList =new ArrayList<>();
        nameProductFilterList.add(nameFilter);
        ArrayList<ProductFilter> maxPriceAndNameProductFilterList =new ArrayList<>();
        maxPriceAndNameProductFilterList.add(nameFilter);
        maxPriceAndNameProductFilterList.add(maxPriceFilter);
        List<ServiceStore> nameSearch=bridge.FilterSearch(nameProductFilterList,storeFilters);
        List<ServiceStore> nameAndPriceSearch=bridge.FilterSearch(maxPriceAndNameProductFilterList,storeFilters);
        //testing name filter
        Assertions.assertFalse(nameSearch.isEmpty());
        for(ServiceStore store:nameSearch){
            assertTrue(store.getProductList().contains(ServiceHamburger));
            assertTrue(store.getProductList().contains(ServiceCheeseburger));
            assertFalse(store.getProductList().contains(ServiceSausage));
            assertFalse(store.getProductList().contains(ServiceSteak));
        }
        Assertions.assertFalse(nameSearch.isEmpty());
        for(ServiceStore store:nameAndPriceSearch) {
            //testing name and price filters
            assertTrue(store.getProductList().contains(ServiceHamburger));
            assertFalse(store.getProductList().contains(ServiceCheeseburger));
            assertFalse(store.getProductList().contains(ServiceSausage));
            assertFalse(store.getProductList().contains(ServiceSteak));
        }
    }
    @Order(21)
    @Test
    public void StoreRatingTests(){
        bridge.Login(Customer.getUserName(), Customer.getPassword());
        assertTrue(bridge.RateStore(store1,4));
        assertFalse(bridge.RateStore(nonExistentStore,4)); //invalid store
        assertFalse(bridge.RateStore(store1,6)); //invalid rating
        bridge.Logout();

    }
    @Order(22)
    @Test
    public void StoreRatingAndCommentTests(){
        bridge.Login(Customer.getUserName(), Customer.getPassword());
        assertTrue(bridge.RateAndCommentOnStore(store1,"Manager said a bad word",4));
        assertFalse(bridge.RateAndCommentOnStore(nonExistentStore,"Manager said a bad word",4)); //invalid store
        assertFalse(bridge.RateAndCommentOnStore(store1,"Manager said a bad word",6)); //invalid rating
        bridge.Logout();
    }
    @Order(23)
    @Test
    public void ProductRatingTests(){
        FilterSearchPrep();
        bridge.Login(Customer.getUserName(), Customer.getPassword());
        assertTrue(bridge.RateAndCommentOnProduct(productIdMap.get("Steak"), store1, "I wanted bacon",2));
        assertFalse(bridge.RateAndCommentOnProduct(-1, store1, "I wanted bacon",2)); //invalid product
        assertFalse(bridge.RateAndCommentOnProduct(productIdMap.get("Steak"), store1, "I wanted bacon",6)); //invalid rating
        bridge.Logout();
    }
}
