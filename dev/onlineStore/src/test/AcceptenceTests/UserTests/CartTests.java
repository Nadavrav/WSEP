package AcceptenceTests.UserTests;

import DomainLayer.Response;
import org.junit.jupiter.api.*;
import Bridge.Bridge;
import Bridge.Driver;

import java.util.List;

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
    private Integer RealcreditProxy = 123; // A credit card
    private String address = "home";
    @BeforeAll
    public void Setup()
    {
        assertTrue(bridge.EnterMarket());
        assertTrue(bridge.Register(userName,password));
        assertTrue(bridge.Login(userName,password));
        storeId=bridge.OpenNewStore(storeName);
        Assertions.assertNotEquals(-1,storeId);
        productId_MegaMilk = bridge.AddProduct(storeId,"Mega milk","Guaranteed to make bones stronger!",5,10);
        productId_UltraMilk = bridge.AddProduct(storeId,"Ultra milk","Bones made of metal now!",7,10);
      productId_GigaMilk = bridge.AddProduct(storeId,"Giga milk","bones made of diamond now!",10,10);
      Assertions.assertNotEquals(-1,productId_GigaMilk);
        Assertions.assertNotEquals(-1,productId_UltraMilk);
        Assertions.assertNotEquals(-1,productId_MegaMilk);
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
     * 2.3
     */
    @Test
    public void Save_Product_Success()
    {
        boolean r = bridge.addToCart(productId_MegaMilk, storeId);
        Assertions.assertTrue(r);
    }
    @Test
    public void Save_Product_Success_MultipleProducts()
    {
        boolean r = bridge.addToCart(productId_MegaMilk, storeId);
        Assertions.assertTrue(r);
        boolean r1 = bridge.addToCart(productId_GigaMilk, storeId);
        Assertions.assertTrue(r1);
        boolean r2 = bridge.addToCart(productId_UltraMilk, storeId);
        Assertions.assertTrue(r2);
    }
    @Test
    public void Save_Product_Fail_ProductDoesntExits()
    {
        boolean r = bridge.addToCart(badProductId, storeId);
        Assertions.assertFalse(r);
    }
    @Test
    public void Save_Product_Fail_SavedTwice()
    {
        boolean r = bridge.addToCart(productId_MegaMilk, storeId);
        Assertions.assertTrue(r);
        boolean r1 = bridge.addToCart(productId_MegaMilk, storeId);
        Assertions.assertFalse(r1);
    }

    /**
     * 2.4
     */
    @Test
    public void Remove_Product_Success()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk, storeId));
        boolean r = bridge.removeFromCart(productId_MegaMilk, storeId);
        Assertions.assertTrue(r);
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
        Assertions.assertTrue(r1);
        boolean r2 = bridge.removeFromCart(productId_GigaMilk, storeId);
        Assertions.assertTrue(r2);
    }
    @Test
    public void Remove_Product_Fail_ProductDoesntExits()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk, storeId));
        boolean r = bridge.removeFromCart(badProductId, storeId);
        Assertions.assertFalse(r);
    }
    @Test
    public void Remove_Product_Fail_RemovedTwice()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk, storeId));
        boolean r = bridge.removeFromCart(productId_MegaMilk, storeId);
        Assertions.assertTrue(r);
        boolean r1 = bridge.removeFromCart(productId_MegaMilk, storeId);
        Assertions.assertFalse(r1);
    }
    @Test
    public void OpenCart_Success_EmptyCart()
    {
        assertFalse(bridge.OpenCart().isError());
    }
    @Test
    public void OpenCart_Success_CartWithOneItem()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk, storeId));
        String expectedStore = "Store Id : "+storeId;
        assertFalse(bridge.OpenCart().isError());
        String expectedProducts = "Name: Mega milk Description: Guaranteed to make bones stronger! Category: test price per unit: 5.0 Amount: 1 total price: 5.0";
        assertTrue(bridge.OpenStringCart().getValue().contains(expectedStore));
        assertTrue(bridge.OpenStringCart().getValue().contains(expectedProducts));
    }
    @Test
    public void OpenCart_Success_CartWithMultipleItems()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk, storeId));
        assertTrue(bridge.addToCart(productId_GigaMilk, storeId));
        assertTrue(bridge.addToCart(productId_UltraMilk, storeId));

        String ProductList = "Store Id : "+storeId;
        String product1Str = "Name: Mega milk Description: Guaranteed to make bones stronger! Category: test price per unit: 5.0 Amount: 1 total price: 5.0\n";
        String product2Str = "Name: Ultra milk Description: Bones made of metal now! Category: test price per unit: 7.0 Amount: 1 total price: 7.0\n";
        String product3Str = "Name: Giga milk Description: bones made of diamond now! Category: test price per unit: 10.0 Amount: 1 total price: 10.0\n";

        assertFalse(bridge.OpenCart().isError());
        String actual = bridge.OpenStringCart().getValue();
        assertTrue(actual.contains(ProductList));
        assertTrue(actual.contains(product1Str));
        assertTrue(actual.contains(product2Str));
        assertTrue(actual.contains(product3Str));

    }
    @Test
    public void Cart_ChangeItemQuantity_Success()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk, storeId));

        boolean r = bridge.CartChangeItemQuantity(productId_MegaMilk,storeId,5);
        Assertions.assertTrue(r);
    }
    @Test
    public void Cart_ChangeItemQuantity_Success_MultipleTimes()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk, storeId));

        boolean r = bridge.CartChangeItemQuantity(productId_MegaMilk,storeId,5);
        Assertions.assertTrue(r);
        boolean r1 = bridge.CartChangeItemQuantity(productId_MegaMilk,storeId,7);
        Assertions.assertTrue(r1);
        boolean r2 = bridge.CartChangeItemQuantity(productId_MegaMilk,storeId,5);
        Assertions.assertTrue(r2);
    }
    @Test
    public void Cart_ChangeItemQuantity_Fail_IllegalValue()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk, storeId));

        boolean r = bridge.CartChangeItemQuantity(productId_MegaMilk,storeId,-1);
        Assertions.assertFalse(r);
    }
    @Test
    public void Cart_ChangeItemQuantity_Fail_ItemNotInCart()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk, storeId));

        boolean r = bridge.CartChangeItemQuantity(productId_GigaMilk,storeId,5);
        Assertions.assertFalse(r);
    }

    /**
     * 2.5
     */
    @Test
    public void Purchase_Cart_Success()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk, storeId));

        int BeforePurchaseQuantity = bridge.GetItemQuantity(storeId,productId_MegaMilk);
        Response<List<String>> r= bridge.PurchaseCart(RealcreditProxy,address);
        assertFalse(r.isError());
        int AfterPurchaseQuantity = bridge.GetItemQuantity(storeId,productId_MegaMilk);
        Assertions.assertEquals(BeforePurchaseQuantity-1,AfterPurchaseQuantity);
    }
    @Test
    public void Purchase_Cart_Fail_ItemsNotAvailable()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk, storeId));

        int BeforePurchaseQuantity = bridge.GetItemQuantity(storeId,productId_MegaMilk);
        assertTrue(bridge.CartChangeItemQuantity(productId_MegaMilk,storeId,BeforePurchaseQuantity+1));
        Response<List<String>> r= bridge.PurchaseCart(RealcreditProxy,address);
        Assertions.assertFalse(r.isError());
    }
    @Test
    public void Purchase_Cart_Fail_FakeCredit()
    {
        assertTrue(bridge.addToCart(productId_MegaMilk, storeId));

        int BeforePurchaseQuantity = bridge.GetItemQuantity(storeId,productId_MegaMilk);
        assertTrue(bridge.CartChangeItemQuantity(productId_MegaMilk,storeId,BeforePurchaseQuantity+1));
        Response<List<String>> r= bridge.PurchaseCart(RealcreditProxy,address);
        Assertions.assertFalse(r.isError());
    }
}
