package AcceptenceTests.UserTests;

import AcceptenceTests.ProxyClasses.CreditCardProxy;
import Bridge.*;
import DomainLayer.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PurchaseHistory {

    private Bridge bridge= Driver.getBridge();
    private final String AdminUsername = "Admin";
    private final String AdminPassword = "Admin";
    private final String StoreOwnerName = "StoreOwner";
    private final String StoreWorkerNameWithPerms = "StoreWorker";
    private final String NormalUser = "NormalUser";
    private final String password = "12345678";
    private final String storeName = "Super";
    private final String productId_MegaMilk = "0";//Product that exits
    private final String productId_UltraMilk = "1";//Product that exits
    private final String productId_GigaMilk = "2";//Product that exits
    private final String badProductId = "-1";//Product that doesnt exist
    private CreditCardProxy RealcreditProxy = new CreditCardProxy(); // A credit card Proxy class
    private CreditCardProxy FakecreditProxy = new CreditCardProxy(); // A credit card Proxy class
    @BeforeAll
    public void Setup()
    {
     //   int [] perms = {1,2,3,4,5,6,7,8,9,10,11};
     //   assertTrue(bridge.EnterMarket());
     //   assertTrue(bridge.Register(StoreOwnerName,password));
     //   assertTrue(bridge.Register(StoreWorkerNameWithPerms,password));
     //   assertTrue(bridge.Register(NormalUser,password));
     //   assertTrue(bridge.Login(StoreOwnerName,password));
     //   assertTrue(bridge.OpenNewStore(storeName));
     //   assertTrue(bridge.AddProduct(storeName,"Mega milk","Guaranteed to make bones stronger!",5,10));//TODO: GET ID
     //   assertTrue(bridge.AddProduct(storeName,"Ultra milk","Bones made of metal now!",7,10));//TODO: GET ID
     //   assertTrue(bridge.AddProduct(storeName,"Giga milk","bones made of diamond now!",10,10));//TODO: GET ID
     //   assertTrue(bridge.AddPermission(StoreWorkerNameWithPerms,storeName,perms));
     //   assertTrue(bridge.Logout());
     //   assertTrue(bridge.ExitMarket());
     //   this.RealcreditProxy.setReal();
     //   this.FakecreditProxy.setFake();
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

    @Test
    public void GetPurchaseHistory_Success_StoreOwner_EmptyHistory()
    {
     //   String[] EmptyList = {};
     //   assertTrue(bridge.Login(StoreOwnerName,password));
     //   Response<String[]> r = bridge.GetPurchaseHistory(storeName);
     //   assertFalse(r.isError());
     //   assertEquals(EmptyList,r.getValue());
    }
    @Test
    public void GetPurchaseHistory_Success_StoreOwner_NotEmptyHistory()
    {
     //   String[] PurchaseHistory = {"StoreWorker Bought 1 Mega milk","StoreWorker Bought 1 Ultra milk"};
     //   //Adding items to purchase history
     //   assertTrue(bridge.Login(NormalUser,password));
     //   assertTrue(bridge.addToCart(productId_MegaMilk));
     //   assertTrue(bridge.addToCart(productId_UltraMilk));
     //   assertTrue(bridge.PurchaseCart(RealcreditProxy));
     //   assertTrue(bridge.Logout());
//
//
     //   assertTrue(bridge.Login(StoreOwnerName,password));
     //   Response<String[]> r = bridge.GetPurchaseHistory(storeName);
     //   assertFalse(r.isError());
     //   assertEquals(PurchaseHistory,r.getValue());
    }
    @Test
    public void GetPurchaseHistory_Success_StoreOwner_NotEmptyHistory_MultipleUsers()
    {
     //   String[] PurchaseHistory = {"StoreWorker Bought 1 Mega milk","StoreWorker Bought 1 Ultra milk","NormalUser Bought 1 Ultra milk"};
     //   //Adding items to purchase history
     //   assertTrue(bridge.Login(StoreWorkerNameWithPerms,password));
     //   assertTrue(bridge.addToCart(productId_MegaMilk));
     //   assertTrue(bridge.addToCart(productId_UltraMilk));
     //   assertTrue(bridge.PurchaseCart(RealcreditProxy));
     //   assertTrue(bridge.Logout());
     //   //Adding more items through a different user
     //   assertTrue(bridge.Login(NormalUser,password));
     //   assertTrue(bridge.addToCart(productId_GigaMilk));
     //   assertTrue(bridge.PurchaseCart(RealcreditProxy));
     //   assertTrue(bridge.Logout());
//
//
     //   assertTrue(bridge.Login(StoreOwnerName,password));
     //   Response<String[]> r = bridge.GetPurchaseHistory(storeName);
     //   assertFalse(r.isError());
     //   assertEquals(PurchaseHistory,r.getValue());
    }
    @Test
    public void GetPurchaseHistory_Fail_NotStoreOwner_NotLoggedIn()
    {
       // String[] PurchaseHistory = {"NormalUser Bought 1 Mega milk","NormalUser Bought 1 Ultra milk"};
       // //Adding items to purchase history
       // assertTrue(bridge.Login(NormalUser,password));
       // assertTrue(bridge.addToCart(productId_MegaMilk));
       // assertTrue(bridge.addToCart(productId_UltraMilk));
       // assertTrue(bridge.PurchaseCart(RealcreditProxy));
       // assertTrue(bridge.Logout());
//
//
       // Response<String[]> r = bridge.GetPurchaseHistory(storeName);
       // assertTrue(r.isError());
    }
    @Test
    public void GetPurchaseHistory_Fail_NotStoreOwner_LoggedInUser()
    {
        String[] PurchaseHistory = {"NormalUser Bought 1 Mega milk","NormalUser Bought 1 Ultra milk"};
        //Adding items to purchase history
        assertTrue(bridge.Login(NormalUser,password));
        assertTrue(bridge.addToCart(productId_MegaMilk));
        assertTrue(bridge.addToCart(productId_UltraMilk));
        assertTrue(bridge.PurchaseCart(RealcreditProxy));
        assertTrue(bridge.Logout());

     //   assertTrue(bridge.Login(NormalUser,password));
     //   Response<String[]> r = bridge.GetPurchaseHistory(storeName);
     //   assertTrue(r.isError());
    }
    @Test
    public void GetPurchaseHistory_Success_Admin_EmptyHistory()
    {
     //   String[] EmptyList = {};
     //   assertTrue(bridge.Login(AdminUsername,AdminPassword));
   //     Response<String[]> r = bridge.GetPurchaseHistory(storeName);
     //   assertFalse(r.isError());
     //   assertEquals(EmptyList,r.getValue());
    }
    @Test
    public void GetPurchaseHistory_Success_Admin_NotEmptyHistory()
    {
    //    String[] PurchaseHistory = {"StoreWorker Bought 1 Mega milk","StoreWorker Bought 1 Ultra milk"};
    //    //Adding items to purchase history
    //    assertTrue(bridge.Login(NormalUser,password));
    //    assertTrue(bridge.addToCart(productId_MegaMilk));
    //    assertTrue(bridge.addToCart(productId_UltraMilk));
    //    assertTrue(bridge.PurchaseCart(RealcreditProxy));
    //    assertTrue(bridge.Logout());
//
//
    //    assertTrue(bridge.Login(AdminUsername,AdminPassword));
    //    Response<String[]> r = bridge.GetPurchaseHistory(storeName);
    //    assertFalse(r.isError());
    //    assertEquals(PurchaseHistory,r.getValue());
    }


}
