package AcceptenceTests.UserTests;

import AcceptenceTests.ProxyClasses.CreditCardProxy;
import Bridge.*;
import DomainLayer.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class CartTests {
    private Bridge bridge= Driver.getBridge();
    private final String userName = "User";
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
     //   assertTrue(bridge.EnterMarket());
     //   assertTrue(bridge.Register(userName,password));
     //   assertTrue(bridge.Login(userName,password));
     //   assertTrue(bridge.OpenNewStore(storeName));
     //   assertTrue(bridge.AddProduct(storeName,"Mega milk","Guaranteed to make bones stronger!",5,10));//TODO: GET ID
     //   assertTrue(bridge.AddProduct(storeName,"Ultra milk","Bones made of metal now!",7,10));//TODO: GET ID
      //  assertTrue(bridge.AddProduct(storeName,"Giga milk","bones made of diamond now!",10,10));//TODO: GET ID
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
    public void Save_Product_Success()
    {
        boolean r = bridge.addToCart(productId_MegaMilk);
        assertTrue(r);
    }
    @Test
    public void Save_Product_Success_MultipleProducts()
    {
        boolean r = bridge.addToCart(productId_MegaMilk);
        assertTrue(r);
        boolean r1 = bridge.addToCart(productId_GigaMilk);
        assertTrue(r1);
        boolean r2 = bridge.addToCart(productId_UltraMilk);
        assertTrue(r2);
    }
    @Test
    public void Save_Product_Fail_ProductDoesntExits()
    {
        boolean r = bridge.addToCart(badProductId);
        assertFalse(r);
    }
    @Test
    public void Save_Product_Fail_SavedTwice()
    {
        boolean r = bridge.addToCart(productId_MegaMilk);
        assertTrue(r);
        boolean r1 = bridge.addToCart(productId_MegaMilk);
        assertFalse(r1);
    }
    @Test
    public void Remove_Product_Success()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk));
        boolean r = bridge.removeFromCart(productId_MegaMilk);
        assertTrue(r);
    }
    @Test
    public void Remove_Product_Success_MultipleProducts()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk));
        assertTrue(bridge.addToCart(productId_GigaMilk));
        assertTrue(bridge.addToCart(productId_UltraMilk));

        boolean r = bridge.removeFromCart(productId_MegaMilk);
        assertTrue(r);
        boolean r1 = bridge.removeFromCart(productId_UltraMilk);
        assertTrue(r1);
        boolean r2 = bridge.removeFromCart(productId_GigaMilk);
        assertTrue(r2);
    }
    @Test
    public void Remove_Product_Fail_ProductDoesntExits()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk));
        boolean r = bridge.removeFromCart(badProductId);
        assertFalse(r);
    }
    @Test
    public void Remove_Product_Fail_RemovedTwice()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk));
        boolean r = bridge.removeFromCart(productId_MegaMilk);
        assertTrue(r);
        boolean r1 = bridge.removeFromCart(productId_MegaMilk);
        assertFalse(r1);
    }
    @Test
    public void OpenCart_Success_EmptyCart()
    {
        String [] EmptyList = {};
        assertFalse(bridge.OpenCart().isError());
        assertEquals(EmptyList,bridge.OpenCart().getValue());
    }
    @Test
    public void OpenCart_Success_CartWithOneItem()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk));
        String [] ProductList = {productId_MegaMilk};
        assertFalse(bridge.OpenCart().isError());
        assertEquals(ProductList,bridge.OpenCart().getValue());
    }
    @Test
    public void OpenCart_Success_CartWithMultipleItems()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk));
        assertTrue(bridge.addToCart(productId_GigaMilk));
        assertTrue(bridge.addToCart(productId_UltraMilk));

        String [] ProductList = {productId_MegaMilk,productId_GigaMilk,productId_UltraMilk};
        assertFalse(bridge.OpenCart().isError());
        assertEquals(ProductList,bridge.OpenCart().getValue());
    }
    @Test
    public void Cart_ChangeItemQuantity_Success()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk));

        boolean r = bridge.CartChangeItemQuantity(productId_MegaMilk,5);
        assertTrue(r);
    }
    @Test
    public void Cart_ChangeItemQuantity_Success_MultipleTimes()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk));

        boolean r = bridge.CartChangeItemQuantity(productId_MegaMilk,5);
        assertTrue(r);
        boolean r1 = bridge.CartChangeItemQuantity(productId_MegaMilk,7);
        assertTrue(r1);
        boolean r2 = bridge.CartChangeItemQuantity(productId_MegaMilk,5);
        assertTrue(r2);
    }
    @Test
    public void Cart_ChangeItemQuantity_Fail_IllegalValue()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk));

        boolean r = bridge.CartChangeItemQuantity(productId_MegaMilk,-1);
        assertFalse(r);
    }
    @Test
    public void Cart_ChangeItemQuantity_Fail_ItemNotInCart()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk));

        boolean r = bridge.CartChangeItemQuantity(productId_GigaMilk,5);
        assertFalse(r);
    }
    @Test
    public void Purchase_Cart_Success()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk));

        int BeforePurchaseQuantity = bridge.GetItemQuantity(productId_MegaMilk);
        boolean r = bridge.PurchaseCart(RealcreditProxy);
        assertTrue(r);
        int AfterPurchaseQuantity = bridge.GetItemQuantity(productId_MegaMilk);
        assertEquals(BeforePurchaseQuantity-1,AfterPurchaseQuantity);
    }
    @Test
    public void Purchase_Cart_Fail_ItemsNotAvailable()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk));

        int BeforePurchaseQuantity = bridge.GetItemQuantity(productId_MegaMilk);
        assertTrue(bridge.CartChangeItemQuantity(productId_MegaMilk,BeforePurchaseQuantity+1));
        boolean r = bridge.PurchaseCart(RealcreditProxy);
        assertFalse(r);
    }
    @Test
    public void Purchase_Cart_Fail_FakeCredit()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk));

        int BeforePurchaseQuantity = bridge.GetItemQuantity(productId_MegaMilk);
        assertTrue(bridge.CartChangeItemQuantity(productId_MegaMilk,BeforePurchaseQuantity+1));
        boolean r = bridge.PurchaseCart(FakecreditProxy);
        assertFalse(r);
    }
}
