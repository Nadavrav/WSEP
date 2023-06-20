package UnitTests;
import DomainLayer.Facade;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class BidTests {
    Facade facade=Facade.getInstance();
    int product1;
    int product2;
    int product3;
    int storeId;
    int id1;
    int id2;
    int id3;
    @BeforeEach
    public void setUp(){
        try {
            facade.resetData();
            facade.loadData();
            id1=facade.enterNewSiteVisitor();
            id2=facade.enterNewSiteVisitor();
            id3=facade.enterNewSiteVisitor();
            facade.register(id1,"visitor1","visitor1234");
            facade.register(id2,"owner1","owner12345");
            facade.register(id3,"owner2","owner54321");
            facade.login(id1,"visitor1","visitor1234");
            facade.login(id2,"owner1","owner12345");
            facade.login(id3,"owner2","owner54321");
            storeId=facade.OpenNewStore(id2,"Store");
           //facade.appointNewStoreOwner(id2,"owner2",storeId);
           //facade.acceptEmploymentRequest(id2,storeId,"owner2");
            product1=facade.AddProduct(id2,storeId,"Product1",10,"Cat1",10,"Product1 description");
            product2=facade.AddProduct(id2,storeId,"Product2",30,"Cat2",10,"Product2 description");
            product3=facade.AddProduct(id2,storeId,"Product3",10,"Cat3",10,"Product3");


        }
        catch (Exception e){
            fail();
        }
    }
    @Test
    public void acceptBidTest(){
        try {
            facade.addBid(id1, product1, storeId, 2, 7);
            facade.voteOnBid(id2,product1,"visitor1",storeId,true);
            facade.addBid(id1, product2, storeId, 2, 7);
            facade.voteOnBid(id2,product2,"visitor1",storeId,true);
            Collection<Bag> products=facade.getProductsInMyCart(id1).getBags().values();
            for(Bag bag:products){
                for(CartProduct cartProduct:bag.getProducts()){
                    assertEquals(cartProduct.getPrice(),7);
                    String desc=cartProduct.getDescription();
                    System.out.println(desc);
                }
            }
        }
        catch (Exception e){
            fail();
        }
    }
    @Test
    public void rejectBidTest(){
        try {
            facade.addBid(id1, product1, storeId, 2, 7);
            facade.voteOnBid(id2,product1,"visitor1",storeId,false);
            facade.addBid(id1, product2, storeId, 2, 7);
            facade.voteOnBid(id2,product2,"visitor1",storeId,false);
            Collection<Bag> products=facade.getProductsInMyCart(id1).getBags().values();
            for(Bag bag:products){
                assertTrue(bag.getProducts().isEmpty());
            }
        }
        catch (Exception e){
            fail();
        }
    }
    @Test
    public void mixedVotingBidTest(){
        try {
            facade.addBid(id1, product1, storeId, 2, 7);
            facade.voteOnBid(id2,product1,"visitor1",storeId,true);
            facade.addBid(id1, product2, storeId, 2, 10);
            facade.voteOnBid(id2,product2,"visitor1",storeId,false);
            Collection<Bag> products=facade.getProductsInMyCart(id1).getBags().values();
            for(Bag bag:products){
                for(CartProduct cartProduct:bag.getProducts()){
                    assertNotEquals(cartProduct.getPrice(),10);
                }
            }
        }
        catch (Exception e){
            fail();
        }
    }
}
