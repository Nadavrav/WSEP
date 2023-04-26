package UnitTests;

import DomainLayer.Stores.History;
import DomainLayer.Stores.StoreProduct;
import DomainLayer.Users.Bag;
import DomainLayer.Users.Cart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class HistoryTest {
    Cart shoppingCart;
    History h;
    @BeforeEach
    void setup(){
        shoppingCart = new Cart();
        h= new History();
        StoreProduct p1 = new StoreProduct("0-0","Milk",5,"Milk",5,"Its Milk what did you expect");
        int StoreId = 0;
        shoppingCart.addProductToCart(StoreId,p1);
    }
    @Test
    void addPurchasedShoppingCart() {
        h.AddPurchasedShoppingCart(shoppingCart);
        LinkedList<Bag> bagsHistory = h.getShoppingBags();
        assertEquals(shoppingCart.getBags().get(0).bagToString(),bagsHistory.get(0).bagToString());
    }
    @Test
    void addPurchasedShoppingCart_Multiple() {
        StoreProduct p2 = new StoreProduct("1-0","Bread",7.2,"Bread",6,"Just a whole loaf of bread");
        StoreProduct p3 = new StoreProduct("0-1","Butter",3.4,"Butter",6,"A Golden Brick");
        StoreProduct p4 = new StoreProduct("1-1","Eggs",6.8,"Eggs",6,"What came first?");
        int StoreId1 = 0;
        int StoreId2 = 1;
        shoppingCart.addProductToCart(StoreId1,p3);
        shoppingCart.addProductToCart(StoreId2,p2);
        shoppingCart.addProductToCart(StoreId2,p4);

        h.AddPurchasedShoppingCart(shoppingCart);
        LinkedList<Bag> bagsHistory = h.getShoppingBags();
        assertEquals(shoppingCart.getBags().get(0).bagToString()+""+shoppingCart.getBags().get(1).bagToString(),bagsHistory.get(0).bagToString()+""+bagsHistory.get(1).bagToString());
    }
    @Test
    void addPurchasednullCart() {//shouldnt realy happend but we need to check that the system can handle this if it does
        assertThrows(Exception.class,()->h.AddPurchasedShoppingCart(null));
    }
}