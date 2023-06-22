package AcceptenceTests.UserTests;

import Bridge.Bridge;
import DomainLayer.Response;
import DomainLayer.Stores.Products.StoreProduct;

import ServiceLayer.ServiceObjects.ServiceBag;
import ServiceLayer.ServiceObjects.ServiceBid;
import ServiceLayer.ServiceObjects.ServiceCart;
import ServiceLayer.ServiceObjects.ServiceProducts.ServiceCartProduct;
import org.junit.jupiter.api.*;
import Bridge.Driver;


import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BidCounterOfferTests {
    private Bridge storeBridge= Driver.getBridge();
    private Bridge userBridge= Driver.getBridge();
    private int steakId;
    private int breadId;
    private int milkId;
    private int yogurtId;
    private int chickenId;

    int storeId=0;
    //ignore ids, use above
    private final StoreProduct bread=new StoreProduct(0,"Bread",10,"Wheat Foods",1,"Made of whole wheat");
    private final StoreProduct milk=new StoreProduct(1,"Milk",5,"Dairy",1,"Freshly milked");
    private final StoreProduct yogurt=new StoreProduct(2,"Yogurt",15,"Dairy",1,"Extra chunky");
    private final StoreProduct chicken=new StoreProduct(3,"Chicken",30,"Meat",1,"40% chicken");
    private final StoreProduct steak=new StoreProduct(4,"Steak",100,"Meat",1,"May contain peanuts");

    /**
     * for anyone looking at these tests: unless changed during a test there is exactly 1 of each product in fullBag, who's prices add up to 160
     */
    @BeforeEach
    public void setUp() {
        try {
            storeBridge= Driver.getBridge();
            storeBridge.reset();
            storeBridge.EnterMarket();
            storeBridge.Register("user", "admin123456");
            storeBridge.Login("user", "admin123456");
            storeId=storeBridge.OpenNewStore( "Store1");
            breadId=storeBridge.AddProduct(storeId, bread.getName(), bread.getDescription(),bread.getCategory(), bread.getPrice(),bread.getQuantity());
            milkId=storeBridge.AddProduct(storeId, milk.getName(), milk.getDescription(),milk.getCategory(), milk.getPrice(),milk.getQuantity());
            yogurtId=storeBridge.AddProduct(storeId, yogurt.getName(), yogurt.getDescription(),yogurt.getCategory(), yogurt.getPrice(),yogurt.getQuantity());
            chickenId=storeBridge.AddProduct(storeId, chicken.getName(), chicken.getDescription(),chicken.getCategory(), chicken.getPrice(),chicken.getQuantity());
            steakId=storeBridge.AddProduct(storeId, steak.getName(), steak.getDescription(),steak.getCategory(), steak.getPrice(),steak.getQuantity());
            userBridge.EnterMarket();
            userBridge.Register("user1", "admin123456");
            userBridge.Login("user1", "admin123456");
        }
        catch (Exception ignored) {
            fail();
        }
    }
    @Test
    public void counterOfferTest(){
        Response<ServiceBid> r=userBridge.addNewBid(breadId,storeId,1,7);
        assertFalse(r.isError());
        ServiceBid bid=r.getValue();
        storeBridge.counterOfferBid(bid.getProductId(),bid.getStoreId(),bid.getUserName(),8,"I dont like your name");
        Response<Collection<ServiceBid>> r1=userBridge.getUserBids();
        assertFalse(r1.isError());
        assertEquals(r1.getValue().size(),1);
        for(ServiceBid serviceBid:r1.getValue()) {
            assertTrue(serviceBid.getProductId() == bid.getProductId() && serviceBid.getStoreId() == bid.getStoreId() &&
                    serviceBid.getUserName().equals(bid.getUserName()) && serviceBid.getNewPrice() == 8);
            userBridge.acceptCounterOffer(serviceBid.getProductId(),serviceBid.getStoreId());
        }
        Response<ServiceCart> r3=userBridge.getCartProducts();
        assertFalse(r3.isError());
        ServiceCart serviceCart=r3.getValue();
        assertEquals(serviceCart.getBags().size(),1);
        for (ServiceBag serviceBag:serviceCart.getBags())
            for(ServiceCartProduct serviceCartProduct:serviceBag.getProductList())
                assertTrue(serviceCartProduct.getId() == bid.getProductId() && serviceCartProduct.getAmount() == bid.getAmount() &&
                        serviceCartProduct.getPrice() == 8);
    }
    @Test
    public void counterOfferRejectTest(){
        Response<ServiceBid> r=userBridge.addNewBid(breadId,storeId,1,7);
        assertFalse(r.isError());
        ServiceBid bid=r.getValue();
        storeBridge.counterOfferBid(bid.getProductId(),bid.getStoreId(),bid.getUserName(),8,"I dont like your name");
        Response<Collection<ServiceBid>> r1=userBridge.getUserBids();
        assertFalse(r1.isError());
        assertEquals(r1.getValue().size(),1);
        for(ServiceBid serviceBid:r1.getValue()) {
            assertTrue(serviceBid.getProductId() == bid.getProductId() && serviceBid.getStoreId() == bid.getStoreId() &&
                    serviceBid.getUserName().equals(bid.getUserName()) && serviceBid.getNewPrice() == 8);
            userBridge.rejectCounterOffer(serviceBid.getProductId(),serviceBid.getStoreId());
        }
        Response<ServiceCart> r3=userBridge.getCartProducts();
        assertFalse(r3.isError());
        ServiceCart serviceCart=r3.getValue();
        assertEquals(serviceCart.getBags().size(),0);
    }
}
