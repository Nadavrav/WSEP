package AcceptenceTests.UserTests;

import DomainLayer.Response;
import ServiceLayer.ServiceObjects.PurchaseRecord;
import ServiceLayer.ServiceObjects.ServiceProducts.ServiceProduct;
import ServiceLayer.ServiceObjects.ServiceProducts.ServiceStoreProduct;
import org.junit.jupiter.api.*;
import Bridge.Bridge;
import Bridge.Driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PurchaseHistory {

    private final Bridge bridge= Driver.getBridge();
    private final String AdminUsername = "admin";
    private final String AdminPassword = "admin1234";
    private final String StoreFounerName = "StoreOwner";
    private final String StoreWorkerOwnerPerms = "StoreWorker";
    private final String NormalUser = "NormalUser";
    private final String password = "12345678";
    private final String storeName = "Super";
    private int storeId=-1;
    private Integer productId_Milk = -1;//Product that exits
    private Integer productId_Cheese = -1;//Product that exits
    private Integer productId_Hamburger = -1;//Product that exits
    private final Integer badProductId = -1;//Product that doesn't exist
    private final int RealcreditProxy = 123; // A credit card Proxy class
    private String address = "home";

  //  private final HashMap<String,String> productIdMapper=new HashMap<>();
    private final HashMap<String,ServiceProduct> serviceProductMap=new HashMap<>();
    @BeforeAll
    public void Setup()
    {
        int [] perms = {1,2,3,4,5,6,7,8,9,10,11};
        assertTrue(bridge.EnterMarket());
        assertTrue(bridge.Register(StoreFounerName,password));
        assertTrue(bridge.Register(StoreWorkerOwnerPerms,password));
        assertTrue(bridge.Register(NormalUser,password));
        assertTrue(bridge.Login(StoreFounerName,password));
        storeId=bridge.OpenNewStore(storeName);
        Assertions.assertNotEquals(-1,storeId);
        productId_Milk =bridge.AddProduct(storeId,"Milk","Made by a cow",5,10);
        productId_Cheese =bridge.AddProduct(storeId,"Cheese","contains 3% bullet holes",7,10);
        productId_Hamburger =bridge.AddProduct(storeId,"Hamburger","generously donated by the cow community",10,10);
        Assertions.assertNotEquals("-1", productId_Milk);
        Assertions.assertNotEquals("-1", productId_Cheese);
        Assertions.assertNotEquals("-1", productId_Hamburger);
        //productIdMapper.put(productId_Milk, productId_Milk);
        //productIdMapper.put(productId_Cheese, productId_Cheese);
        //productIdMapper.put(productId_Hamburger, productId_Hamburger);
        serviceProductMap.put("Milk",new ServiceStoreProduct("Milk",5.0,"test","Made by a cow",2.5,1));
        serviceProductMap.put("Cheese",new ServiceStoreProduct("Cheese",7.0,"test","contains 3% bullet holes",2.5,1));
        serviceProductMap.put("Hamburger",new ServiceStoreProduct("Hamburger",10.0,"test","generously donated by the cow community",2.5,1));
        assertTrue(bridge.AppointOwner(StoreWorkerOwnerPerms,storeId));
        assertTrue(bridge.Logout());
        assertTrue(bridge.ExitMarket());
    }
    @BeforeEach
    public void OpenSys()
    {
        assertTrue(bridge.EnterMarket());
    }
    @AfterEach
    public void CloseSys()
    {
        assertTrue(bridge.ExitMarket());
    }

    /**
     * 3.7
     */
    @Test
    public void GetPurchaseHistory_Success_StoreFounder_EmptyHistory()
    {
        assertTrue(bridge.Login(StoreFounerName,password));
        List<String> r = bridge.StorePurchaseHistory(storeId);
        assertNotNull(r);
        assertTrue(bridge.Logout());
    }
    @Test
    public void GetPurchaseHistory_Success_StoreFounder_NotEmptyHistory()
    {
        ArrayList<PurchaseRecord> purchaseRecords=new ArrayList<>();
        purchaseRecords.add(new PurchaseRecord(serviceProductMap.get(productId_Milk),NormalUser));
        purchaseRecords.add(new PurchaseRecord(serviceProductMap.get(productId_Cheese),NormalUser));
        //Adding items to purchase history
        assertTrue(bridge.Login(NormalUser,password));
        assertTrue(bridge.addToCart(productId_Milk, storeId));
        assertTrue(bridge.addToCart(productId_Cheese, storeId));
        assertFalse(bridge.PurchaseCart(RealcreditProxy,address).isError());
        assertTrue(bridge.Logout());
        assertTrue(bridge.Login(StoreFounerName,password));
        List<String> r = bridge.StorePurchaseHistory(storeId);
        assertNotNull(r);
        String expectedP1 = "Name: Cheese Description: contains 3% bullet holes Category: test price per unit: 7.0 Amount: 1 total price: 7.0";
        String expectedP2 = "Name: Milk Description: Made by a cow Category: test price per unit: 5.0 Amount: 1 total price: 5.0\n";
        assertTrue(r.toString().contains(expectedP1));
        assertTrue(r.toString().contains(expectedP2));
        assertTrue(bridge.Logout());
    }
    @Test
    public void GetPurchaseHistory_Success_StoreOwner_NotEmptyHistory_MultipleUsers()
    {
        //String[] PurchaseHistory = {"StoreWorker Bought 1 Mega milk","StoreWorker Bought 1 Ultra milk","NormalUser Bought 1 Ultra milk"};
        ArrayList<PurchaseRecord> purchaseRecords=new ArrayList<>();
        //Adding items to purchase history
        assertTrue(bridge.Login(StoreWorkerOwnerPerms,password));
        assertTrue(bridge.addToCart(productId_Milk, storeId));
        assertTrue(bridge.addToCart(productId_Hamburger, storeId));
        assertFalse(bridge.PurchaseCart(RealcreditProxy,address).isError());
        assertTrue(bridge.Logout());
        //Adding more items through a different user
        assertTrue(bridge.Login(NormalUser,password));
        assertTrue(bridge.addToCart(productId_Hamburger, storeId));
        purchaseRecords.add(new PurchaseRecord(serviceProductMap.get(productId_Hamburger),NormalUser));
        assertFalse(bridge.PurchaseCart(RealcreditProxy,address).isError());
        assertTrue(bridge.Logout());
        assertTrue(bridge.Login(StoreFounerName,password));
        List<String> r = bridge.StorePurchaseHistory(storeId);
        assertNotNull(r);
        String expectedP1 = "Name: Cheese Description: contains 3% bullet holes Category: test price per unit: 7.0 Amount: 1 total price: 7.0";
        String expectedP2 = "Name: Milk Description: Made by a cow Category: test price per unit: 5.0 Amount: 1 total price: 5.0\n";
        assertTrue(r.toString().contains(expectedP1));
        assertTrue(r.toString().contains(expectedP2));
        assertTrue(bridge.Logout());
    }
    @Test
    public void GetPurchaseHistory_Fail_NotStoreOwner_NotLoggedIn()
    {
       // String[] PurchaseHistory = {"NormalUser Bought 1 Mega milk","NormalUser Bought 1 Ultra milk"};
        //Adding items to purchase history
        assertTrue(bridge.Login(NormalUser,password));
        assertTrue(bridge.addToCart(productId_Milk, storeId));
        assertTrue(bridge.addToCart(productId_Cheese, storeId));
        assertFalse(bridge.PurchaseCart(RealcreditProxy,address).isError());
        assertTrue(bridge.Logout());

        List<String> r = bridge.StorePurchaseHistory(storeId);
        assertTrue(r == null);
    }
    @Test
    public void GetPurchaseHistory_Fail_NotStoreOwner_LoggedInUser()
    {
        //Adding items to purchase history
        assertTrue(bridge.Login(NormalUser,password));
        assertTrue(bridge.addToCart(productId_Milk, storeId));
        assertTrue(bridge.addToCart(productId_Cheese, storeId));
        assertFalse(bridge.PurchaseCart(RealcreditProxy,address).isError());
        assertTrue(bridge.Logout());

        assertTrue(bridge.Login(NormalUser,password));
        List<String> r = bridge.StorePurchaseHistory(storeId);
        assertNull(r);
        assertTrue(bridge.Logout());
    }
    @Test
    public void GetPurchaseHistory_Success_Admin_EmptyHistory()
    {
        assertTrue(bridge.Login(AdminUsername,AdminPassword));
        List<String> r = bridge.StorePurchaseHistory(storeId);
        assertNotNull(r);
        assertTrue(bridge.Logout());
    }
    @Test
    public void GetPurchaseHistory_Success_Admin_NotEmptyHistory()
    {
        ArrayList<PurchaseRecord> purchaseRecords=new ArrayList<>();
        purchaseRecords.add(new PurchaseRecord(serviceProductMap.get(productId_Milk),NormalUser));
        purchaseRecords.add(new PurchaseRecord(serviceProductMap.get(productId_Cheese),NormalUser));
        //Adding items to purchase history
        assertTrue(bridge.Login(NormalUser,password));
        assertTrue(bridge.addToCart(productId_Cheese, storeId));
        assertTrue(bridge.addToCart(productId_Milk, storeId));
        assertFalse(bridge.PurchaseCart(RealcreditProxy,address).isError());
        assertTrue(bridge.Logout());

        assertTrue(bridge.Login(AdminUsername,AdminPassword));
        List<String> r = bridge.StorePurchaseHistory(storeId);
        assertNotNull(r);
        String expectedP1 = "Name: Cheese Description: contains 3% bullet holes Category: test price per unit: 7.0 Amount: 1 total price: 7.0";
        String expectedP2 = "Name: Milk Description: Made by a cow Category: test price per unit: 5.0 Amount: 1 total price: 5.0\n";
        assertTrue(r.toString().contains(expectedP1));
        assertTrue(r.toString().contains(expectedP2));
        assertTrue(bridge.Logout());
    }


}
