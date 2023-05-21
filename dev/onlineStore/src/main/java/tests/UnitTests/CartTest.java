package tests.UnitTests;

import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Users.Bag;
import DomainLayer.Users.Cart;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {
    StoreProduct p1,p2,p3,p4;
    int StoreId1,StoreId2;
    @BeforeEach
    void setup()
    {
        p1 = new StoreProduct(0,"Milk",5,"Milk",20,"Its Milk what did you expect");
        p2 = new StoreProduct(1,"Bread",7.2,"Bread",6,"Just a whole loaf of bread");
        p3 = new StoreProduct(0,"Butter",3.4,"Butter",6,"A Golden Brick");
        p4 = new StoreProduct(1,"Eggs",6.8,"Eggs",6,"What came first?");
        StoreId1 = 0;
        StoreId2 = 1;
    }
    @Test
    public void testAddProductToCartWithNewStore() {
        Cart cart = new Cart();
        cart.addProductToCart(StoreId1, p1);
        Bag actualBag  = cart.getBags().get(StoreId1);
        Assertions.assertNotNull(actualBag);
        assertEquals("Name: Milk Description: Its Milk what did you expect Category: Milk price per unit: 5.0 Amount: 1 total price: 5.0\n", actualBag.bagToString());
    }

    @Test
    public void testAddProductToCartWithExistingStore() {
        Cart cart = new Cart();
        cart.addProductToCart(StoreId1, p1);
        cart.addProductToCart(StoreId1, p3);
        Bag actualBag  = cart.getBags().get(StoreId1);

        Assertions.assertNotNull(actualBag);
        String actualBagString = actualBag.bagToString();
        String expectedP1 = "Name: Milk Description: Its Milk what did you expect Category: Milk price per unit: 5.0 Amount: 1 total price: 5.0";
        String expectedP2 = "Name: Butter Description: A Golden Brick Category: Butter price per unit: 3.4 Amount: 1 total price: 3.4";
        assertTrue(actualBag.bagToString().contains(expectedP1));
        assertTrue(actualBag.bagToString().contains(expectedP2));
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
        Assertions.assertNotNull(actualBag);
        String expectedP1 = "Name: Milk Description: Its Milk what did you expect Category: Milk price per unit: 5.0 Amount: 1 total price: 5.0";
        String expectedP3 = "Name: Butter Description: A Golden Brick Category: Butter price per unit: 3.4 Amount: 1 total price: 3.4";
        String expectedP2 = "Name: Eggs Description: What came first? Category: Eggs price per unit: 6.8 Amount: 1 total price: 6.8";
        String expectedP4 = "Name: Bread Description: Just a whole loaf of bread Category: Bread price per unit: 7.2 Amount: 1 total price: 7.2";
        assertTrue(actualBag.bagToString().contains(expectedP1));
        assertTrue(actualBag.bagToString().contains(expectedP3));
        Assertions.assertNotNull(actualBag2);
        assertTrue(actualBag2.bagToString().contains(expectedP2));
        assertTrue(actualBag2.bagToString().contains(expectedP4));
    }
    @Test
    void removeProductFromCart() {
    }
}