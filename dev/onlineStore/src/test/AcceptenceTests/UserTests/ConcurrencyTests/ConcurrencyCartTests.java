package AcceptenceTests.UserTests.ConcurrencyTests;

import ServiceLayer.Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import Bridge.Bridge;
import Bridge.Driver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class ConcurrencyCartTests {
    private final Bridge bridge= Driver.getBridge();

    private final String userName1 = "User";
    private final String password1 = "12345678";
    private final String userName2 = "User2";
    private final String password2 = "9876543211";
    private final String storeName = "Super";
    private int storeId=-1;
    private int productId_MegaMilk = -1;//Product that exits
    private int productId_UltraMilk = -1;//Product that exits
    private int productId_GigaMilk = -1;//Product that exits
    private final int badProductId = -1;//Product that doesn't exist
    private int RealcreditProxy = 123; // A credit card Proxy class
    @BeforeAll
    public void Setup()
    {
        assertTrue(bridge.EnterMarket());
        assertTrue(bridge.Register(userName2,password2));
        assertTrue(bridge.Register(userName1,password1));
        assertTrue(bridge.Login(userName1,password1));
        storeId=bridge.OpenNewStore(storeName);
        assertNotEquals(-1,storeId);
        productId_MegaMilk = bridge.AddProduct(storeId,"Mega milk","Guaranteed to make bones stronger!",5,1);
        productId_UltraMilk = bridge.AddProduct(storeId,"Ultra milk","Bones made of metal now!",7,10);
        productId_GigaMilk = bridge.AddProduct(storeId,"Giga milk","bones made of diamond now!",10,10);
        assertTrue(bridge.Logout());
    }
    @Test
    public void ConcurrencyPurchaseTests(){
        ExecutorService executor= Executors.newFixedThreadPool(2);
        final Service user1=new Service();
        final Service user2=new Service();
        user1.EnterNewSiteVisitor();
        user2.EnterNewSiteVisitor();
        Future<Boolean> f1=executor.submit(() -> {user1.login(userName2,password2); user1.addProductToCart(productId_MegaMilk,storeId ); return user1.PurchaseCart(927391237,"Deadvlei, Namibia").getValue().isEmpty();});
        Future<Boolean> f2=executor.submit(() -> {user2.login(userName1,password1); user2.addProductToCart(productId_MegaMilk,storeId ); return user2.PurchaseCart(927391237,"Deadvlei, Namibia").getValue().isEmpty();});
        try {
            boolean r1=f1.get();
            boolean r2=f2.get();

            Assertions.assertTrue( (r1 & !r2) | (!r1 & r2)); //expecting exactly one of them to fail and one to pass
        }
        catch (Exception e){
            Assertions.fail("Thread Error - see exception type doc");
        }
        executor.shutdown();
    }
}