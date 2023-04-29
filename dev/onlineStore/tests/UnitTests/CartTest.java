package UnitTests;

import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Users.Bag;
import DomainLayer.Users.Cart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {
    StoreProduct p1,p2,p3,p4;
    int StoreId1,StoreId2;
    @BeforeEach
    void setup()
    {
        p1 = new StoreProduct("0-0","Milk",5,"Milk",20,"Its Milk what did you expect");
        p2 = new StoreProduct("1-0","Bread",7.2,"Bread",6,"Just a whole loaf of bread");
        p3 = new StoreProduct("0-1","Butter",3.4,"Butter",6,"A Golden Brick");
        p4 = new StoreProduct("1-1","Eggs",6.8,"Eggs",6,"What came first?");
        StoreId1 = 0;
        StoreId2 = 1;
    }
    @Test
    public void testAddProductToCartWithNewStore() {
        Cart cart = new Cart();
        cart.addProductToCart(StoreId1, p1);
        Bag actualBag  = cart.getBags().get(StoreId1);
        assertNotNull(actualBag);
        assertEquals("Product Id: 0-0 ,Product Name: Milk ,Product Price: 5.0\n", actualBag.bagToString());
    }

    @Test
    public void testAddProductToCartWithExistingStore() {
        Cart cart = new Cart();
        cart.addProductToCart(StoreId1, p1);
        cart.addProductToCart(StoreId1, p3);
        Bag actualBag  = cart.getBags().get(StoreId1);
        assertNotNull(actualBag);
        assertEquals("Product Id: 0-0 ,Product Name: Milk ,Product Price: 5.0\n" +
                "Product Id: 0-1 ,Product Name: Butter ,Product Price: 3.4\n", actualBag.bagToString());
    }
    @Test
    public void testAddProductToCartWithMultipleStoreProducts() {
        Cart cart = new Cart();
        cart.addProductToCart(StoreId1, p1);
        cart.addProductToCart(StoreId1, p3);
        cart.addProductToCart(StoreId2, p2);
        cart.addProductToCart(StoreId2, p4);
        Bag actualBag  = cart.getBags().get(StoreId1);
        Bag actualBag2  = cart.getBags().get(StoreId2);
        assertNotNull(actualBag);
        assertEquals("Product Id: 0-0 ,Product Name: Milk ,Product Price: 5.0\n" +
                "Product Id: 0-1 ,Product Name: Butter ,Product Price: 3.4\n", actualBag.bagToString());
        assertNotNull(actualBag2);
        assertEquals("Product Id: 1-0 ,Product Name: Bread ,Product Price: 7.2\n" +
                "Product Id: 1-1 ,Product Name: Eggs ,Product Price: 6.8\n", actualBag2.bagToString());
    }
    @Test
    void removeProductFromCart() {
    }
}