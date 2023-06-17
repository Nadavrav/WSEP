package UnitTests;
import DomainLayer.Facade;
import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Users.Bag;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

public class BidTests {
    Facade facade=Facade.getInstance();
    @BeforeEach
    public void setUp(){
        try {
            facade.resetData();
            facade.loadData();
            int id1=facade.EnterNewSiteVisitor();
            int id2=facade.EnterNewSiteVisitor();
            int id3=facade.EnterNewSiteVisitor();
            facade.Register(id1,"visitor1","visitor1234");
            facade.Register(id2,"owner1","owner12345");
            facade.Register(id3,"owner2","owner54321");
            facade.login(id1,"visitor1","visitor1234");
            facade.login(id2,"owner1","owner12345");
            facade.login(id3,"owner2","owner54321");
            int storeId=facade.OpenNewStore(id2,"Store");
            facade.appointNewStoreOwner(id2,"owner2",storeId);
            facade.acceptEmploymentRequest(id2,storeId,"owner2");
            int pId1=facade.AddProduct(id1,storeId,"Product1",10,"Cat1",10,"desc1");
            int pId2=facade.AddProduct(id1,storeId,"Product2",10,"Cat2",10,"desc2");
            int pId3=facade.AddProduct(id1,storeId,"Product3",10,"Cat3",10,"desc3");
            //facade.addBid(1,pId1,storeId,2,15);

        }
        catch (Exception e){
            fail();
        }
    }
    @Test
    public void test(){
        assertTrue(true);
    }
}
