package UnitTests;

import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Users.Cart;
import DomainLayer.Users.SiteVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class SiteVisitorTest {
    SiteVisitor visitor;
    @BeforeEach
    void setup()
    {
        try{
            visitor = new SiteVisitor();
        }
        catch (Exception e)
        {
            System.out.println("Shouldn't Happen");
        }
    }
    @Test
    void exitSiteVisitor_NormalExit() {
        int currentVisitorId = visitor.getVisitorId();
        try {
            visitor.ExitSiteVisitor(currentVisitorId);
            LinkedList<AtomicInteger> FreeVisitorId = visitor.getFreeVisitorID();
            assertFalse(FreeVisitorId.contains(new AtomicInteger(currentVisitorId)));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            assertTrue(false);//Used to mark this if an error occurs
        }
    }
    @Test
    void exitSiteVisitor_ExitTwice() {
        int currentVisitorId = visitor.getVisitorId();
        try {
            visitor.ExitSiteVisitor(currentVisitorId);
            LinkedList<AtomicInteger> FreeVisitorId = visitor.getFreeVisitorID();
            assertFalse(FreeVisitorId.contains(new AtomicInteger(currentVisitorId)));
            visitor.ExitSiteVisitor(currentVisitorId);
            FreeVisitorId = visitor.getFreeVisitorID();
            assertFalse(FreeVisitorId.contains(new AtomicInteger(currentVisitorId)));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            assertTrue(false);//Used to mark this if an error occurs
        }
    }
    @Test
    void exitSiteVisitor_ExitFakeId() {
        int currentVisitorId = visitor.getVisitorId();
        int fakeVisitorId = 123;
        try {
            visitor.ExitSiteVisitor(currentVisitorId);
            assertThrows(Exception.class,()->visitor.ExitSiteVisitor(fakeVisitorId));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            assertTrue(false);//Used to mark this if an error occurs
        }
    }

    @Test
    void checkVisitorId_CurrentUser() {
        int currentVisitorId = visitor.getVisitorId();
        assertTrue(visitor.checkVisitorId(currentVisitorId));
    }
    @Test
    void checkVisitorId_FakeUser() {
        int currentVisitorId = visitor.getVisitorId();
        int fakeVisitorId = 123;
        assertFalse(visitor.checkVisitorId(fakeVisitorId));
    }

    @Test
    void addProductToCart() {
        StoreProduct p1 = new StoreProduct(0,"Milk",5,"Milk",5,"Its Milk what did you expect");
        int StoreId = 0;
        visitor.addProductToCart(StoreId,p1);
        String expected = "Store Id : 0\n" +
                "Name: Milk Description: Its Milk what did you expect Category: Milk price per unit: 5.0 Amount: 1 total price: 5.0\n";
        assertEquals(expected,visitor.getCart().cartToString());
    }
    @Test
    void addProductToCart_MulipleProducts() {
        StoreProduct p1 = new StoreProduct(0,"Milk",5,"Milk",5,"Its Milk what did you expect");
        StoreProduct p2 = new StoreProduct(0,"Bread",7.2,"Bread",6,"Just a whole loaf of bread");

        int StoreId = 0;
        visitor.addProductToCart(StoreId,p1);
        visitor.addProductToCart(StoreId,p2);
        String expected = "Store Id : 0\n" +
                "Name: Milk Description: Its Milk what did you expect Category: Milk price per unit: 5.0 Amount: 1 total price: 5.0\n" +
                "Name: Bread Description: Just a whole loaf of bread Category: Bread price per unit: 7.2 Amount: 1 total price: 7.2\n";
        assertEquals(expected,visitor.getCart().cartToString());
    }
    @Test
    void addProductToCart_MulipleProducts2() {
        StoreProduct p1 = new StoreProduct(0,"Milk",5,"Milk",5,"Its Milk what did you expect");
        StoreProduct p2 = new StoreProduct(0,"Bread",7.2,"Bread",6,"Just a whole loaf of bread");
        StoreProduct p3 = new StoreProduct(2,"Butter",3.4,"Butter",6,"A Golden Brick");
        StoreProduct p4 = new StoreProduct(2,"Eggs",6.8,"Eggs",6,"What came first?");
        int StoreId = 0;
        visitor.addProductToCart(StoreId,p1);
        visitor.addProductToCart(StoreId,p3);
        String expected = "Store Id : 0\n" +
                "Name: Milk Description: Its Milk what did you expect Category: Milk price per unit: 5.0 Amount: 1 total price: 5.0\n" +
                "Name: Butter Description: A Golden Brick Category: Butter price per unit: 3.4 Amount: 1 total price: 3.4\n";
        assertEquals(expected,visitor.getCart().cartToString());
    }
    @Test
    void addProductToCart_MulipleProductsDiffrentShops() {
        StoreProduct p1 = new StoreProduct(0,"Milk",5,"Milk",5,"Its Milk what did you expect");
        StoreProduct p2 = new StoreProduct(1,"Bread",7.2,"Bread",6,"Just a whole loaf of bread");
        StoreProduct p3 = new StoreProduct(0,"Butter",3.4,"Butter",6,"A Golden Brick");
        StoreProduct p4 = new StoreProduct(1,"Eggs",6.8,"Eggs",6,"What came first?");
        int StoreId1 = 0;
        int StoreId2 = 1;
        visitor.addProductToCart(StoreId1,p1);
        visitor.addProductToCart(StoreId1,p3);
        visitor.addProductToCart(StoreId2,p2);
        visitor.addProductToCart(StoreId2,p4);
        String expected = "Store Id : 0\n" +
                "Name: Milk Description: Its Milk what did you expect Category: Milk price per unit: 5.0 Amount: 1 total price: 5.0\n" +
                "Name: Butter Description: A Golden Brick Category: Butter price per unit: 3.4 Amount: 1 total price: 3.4\n" +
                "Store Id : 1\n" +
                "Name: Bread Description: Just a whole loaf of bread Category: Bread price per unit: 7.2 Amount: 1 total price: 7.2\n" +
                "Name: Eggs Description: What came first? Category: Eggs price per unit: 6.8 Amount: 1 total price: 6.8\n";
        assertEquals(expected,visitor.getCart().cartToString());
    }
    @Test
    void cartToString_EmptyCart() {
        StoreProduct p1 = new StoreProduct(0,"Milk",5,"Milk",5,"Its Milk what did you expect");
        StoreProduct p2 = new StoreProduct(0,"Bread",7.2,"Bread",6,"Just a whole loaf of bread");
        String expected = "";
        String actual = visitor.cartToString();
        assertEquals(expected,actual);

    }
    @Test
    void cartToString_NotEmptyCart() {
        StoreProduct p1 = new StoreProduct(0,"Milk",5,"Milk",5,"Its Milk what did you expect");
        StoreProduct p2 = new StoreProduct(0,"Bread",7.2,"Bread",6,"Just a whole loaf of bread");
        int StoreId = 0;
        visitor.addProductToCart(StoreId,p1);
        visitor.addProductToCart(StoreId,p2);
        String expected = "Store Id : 0\n" +
                "Name: Milk Description: Its Milk what did you expect Category: Milk price per unit: 5.0 Amount: 1 total price: 5.0\n" +
                "Name: Bread Description: Just a whole loaf of bread Category: Bread price per unit: 7.2 Amount: 1 total price: 7.2\n";
        String actual = visitor.cartToString();
        assertEquals(expected,actual);

    }
}