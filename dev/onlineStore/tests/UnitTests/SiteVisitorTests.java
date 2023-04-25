package UnitTests;

import DomainLayer.Stores.StoreProduct;
import DomainLayer.Users.Cart;
import DomainLayer.Users.SiteVisitor;
import org.junit.jupiter.api.BeforeAll;
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
        StoreProduct p1 = new StoreProduct("0-0","Milk",5,"Milk",5,"Its Milk what did you expect");
        int StoreId = 0;
        visitor.addProductToCart(StoreId,p1);
        assertEquals("Store Id : 0 \n 0-0",visitor.getCart().cartToString());
    }

    @Test
    void cartToString() {
    }

    @Test
    void getCart() {
    }
}