package AcceptenceTests.UserTests;

import AcceptenceTests.ProxyClasses.CreditCardProxy;
import Bridge.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class CartTests {
    private Bridge bridge= Driver.getBridge();
    private final String userName = "User";
    private final String password = "12345678";
    private final String storeName = "Super";
    private int storeId=-1;
    private Integer productId_MegaMilk = -1;//Product that exits
    private Integer productId_UltraMilk = -1;//Product that exits
    private Integer productId_GigaMilk = -1;//Product that exits
    private final Integer badProductId = -1;//Product that doesn't exist
    private CreditCardProxy RealcreditProxy = new CreditCardProxy(); // A credit card Proxy class
    private CreditCardProxy FakecreditProxy = new CreditCardProxy(); // A credit card Proxy class
    @BeforeAll
    public void Setup()
    {
        assertTrue(bridge.EnterMarket());
        assertTrue(bridge.Register(userName,password));
        assertTrue(bridge.Login(userName,password));
        storeId=bridge.OpenNewStore(storeName);
        assertNotEquals(-1,storeId);
        productId_MegaMilk = bridge.AddProduct(storeId,"Mega milk","Guaranteed to make bones stronger!",5,10);
        productId_UltraMilk = bridge.AddProduct(storeId,"Ultra milk","Bones made of metal now!",7,10);
      productId_GigaMilk = bridge.AddProduct(storeId,"Giga milk","bones made of diamond now!",10,10);
      assertNotEquals(-1,productId_GigaMilk);
        assertNotEquals(-1,productId_UltraMilk);
        assertNotEquals(-1,productId_MegaMilk);
        assertTrue(bridge.Logout());
        assertTrue(bridge.ExitMarket());
        this.RealcreditProxy.setReal();
        this.FakecreditProxy.setFake();
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
    public void Save_Product_Success()
    {
        boolean r = bridge.addToCart(productId_MegaMilk, storeId);
        assertTrue(r);
    }
    @Test
    public void Save_Product_Success_MultipleProducts()
    {
        boolean r = bridge.addToCart(productId_MegaMilk, storeId);
        assertTrue(r);
        boolean r1 = bridge.addToCart(productId_GigaMilk, storeId);
        assertTrue(r1);
        boolean r2 = bridge.addToCart(productId_UltraMilk, storeId);
        assertTrue(r2);
    }
    @Test
    public void Save_Product_Fail_ProductDoesntExits()
    {
        boolean r = bridge.addToCart(badProductId, storeId);
        assertFalse(r);
    }
    @Test
    public void Save_Product_Fail_SavedTwice()
    {
        boolean r = bridge.addToCart(productId_MegaMilk, storeId);
        assertTrue(r);
        boolean r1 = bridge.addToCart(productId_MegaMilk, storeId);
        assertFalse(r1);
    }
    @Test
    public void Remove_Product_Success()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk, storeId));
        boolean r = bridge.removeFromCart(productId_MegaMilk, storeId);
        assertTrue(r);
    }
    @Test
    public void Remove_Product_Success_MultipleProducts()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk, storeId));
        assertTrue(bridge.addToCart(productId_GigaMilk, storeId));
        assertTrue(bridge.addToCart(productId_UltraMilk, storeId));

        boolean r = bridge.removeFromCart(productId_MegaMilk, storeId);
        assertTrue(r);
        boolean r1 = bridge.removeFromCart(productId_UltraMilk, storeId);
        assertTrue(r1);
        boolean r2 = bridge.removeFromCart(productId_GigaMilk, storeId);
        assertTrue(r2);
    }
    @Test
    public void Remove_Product_Fail_ProductDoesntExits()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk, storeId));
        boolean r = bridge.removeFromCart(badProductId, storeId);
        assertFalse(r);
    }
    @Test
    public void Remove_Product_Fail_RemovedTwice()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk, storeId));
        boolean r = bridge.removeFromCart(productId_MegaMilk, storeId);
        assertTrue(r);
        boolean r1 = bridge.removeFromCart(productId_MegaMilk, storeId);
        assertFalse(r1);
    }
    @Test
    public void OpenCart_Success_EmptyCart()
    {
        String EmptyList = "";
        assertFalse(bridge.OpenCart().isError());
        assertEquals(EmptyList,bridge.OpenCart().getValue());
    }
    @Test
    public void OpenCart_Success_CartWithOneItem()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk, storeId));
        String ProductList = "Store Id : 0\n0-1\n\n";
        assertFalse(bridge.OpenCart().isError());
        assertEquals(ProductList,bridge.OpenCart().getValue());
    }
    @Test
    public void OpenCart_Success_CartWithMultipleItems()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk, storeId));
        assertTrue(bridge.addToCart(productId_GigaMilk, storeId));
        assertTrue(bridge.addToCart(productId_UltraMilk, storeId));

        String ProductList = "Store Id : 0\n0-1\n0-2\n0-3\n\n";
        assertFalse(bridge.OpenCart().isError());
        assertEquals(ProductList,bridge.OpenCart().getValue());
    }
    @Test
    public void Cart_ChangeItemQuantity_Success()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk, storeId));

        boolean r = bridge.CartChangeItemQuantity(productId_MegaMilk,storeId,5);
        assertTrue(r);
    }
    @Test
    public void Cart_ChangeItemQuantity_Success_MultipleTimes()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk, storeId));

        boolean r = bridge.CartChangeItemQuantity(productId_MegaMilk,storeId,5);
        assertTrue(r);
        boolean r1 = bridge.CartChangeItemQuantity(productId_MegaMilk,storeId,7);
        assertTrue(r1);
        boolean r2 = bridge.CartChangeItemQuantity(productId_MegaMilk,storeId,5);
        assertTrue(r2);
    }
    @Test
    public void Cart_ChangeItemQuantity_Fail_IllegalValue()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk, storeId));

        boolean r = bridge.CartChangeItemQuantity(productId_MegaMilk,storeId,-1);
        assertFalse(r);
    }
    @Test
    public void Cart_ChangeItemQuantity_Fail_ItemNotInCart()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk, storeId));

        boolean r = bridge.CartChangeItemQuantity(productId_GigaMilk,storeId,5);
        assertFalse(r);
    }
    @Test
    public void Purchase_Cart_Success()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk, storeId));

        int BeforePurchaseQuantity = bridge.GetItemQuantity(productId_MegaMilk);
        boolean r = bridge.PurchaseCart(RealcreditProxy);
        assertTrue(r);
        int AfterPurchaseQuantity = bridge.GetItemQuantity(productId_MegaMilk);
        assertEquals(BeforePurchaseQuantity-1,AfterPurchaseQuantity);
    }
    @Test
    public void Purchase_Cart_Fail_ItemsNotAvailable()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk, storeId));

        int BeforePurchaseQuantity = bridge.GetItemQuantity(productId_MegaMilk);
        assertTrue(bridge.CartChangeItemQuantity(productId_MegaMilk,storeId,BeforePurchaseQuantity+1));
        boolean r = bridge.PurchaseCart(RealcreditProxy);
        assertFalse(r);
    }
    @Test
    public void Purchase_Cart_Fail_FakeCredit()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk, storeId));

        int BeforePurchaseQuantity = bridge.GetItemQuantity(productId_MegaMilk);
        assertTrue(bridge.CartChangeItemQuantity(productId_MegaMilk,storeId,BeforePurchaseQuantity+1));
        boolean r = bridge.PurchaseCart(FakecreditProxy);
        assertFalse(r);
    }
}
