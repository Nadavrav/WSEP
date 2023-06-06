package AcceptenceTests.StoreTests;
import Bridge.Bridge;
import DomainLayer.Facade;
import DomainLayer.Response;
import DomainLayer.Stores.Discounts.Discount;
import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Users.Bag;
import ServiceLayer.Service;
import ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords.*;
import ServiceLayer.ServiceObjects.ServiceDiscounts.*;
import com.jayway.jsonpath.internal.function.numeric.Min;
import org.junit.jupiter.api.*;
import Bridge.Driver;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreateDiscountTests {
    private Bridge bridge= Driver.getBridge();
    int storeId=0;
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
            bridge=Driver.getBridge();
            bridge.reset();
            bridge.EnterMarket();
            bridge.Register("user", "admin123456");
            bridge.Login("user", "admin123456");
            storeId=bridge.OpenNewStore( "Store1");
            bridge.AddProduct(storeId, bread.getName(), bread.getDescription(),bread.getCategory(), bread.getPrice(),bread.getQuantity());
            bridge.AddProduct(storeId, milk.getName(), milk.getDescription(),milk.getCategory(), milk.getPrice(),milk.getQuantity());
            bridge.AddProduct(storeId, yogurt.getName(), yogurt.getDescription(),yogurt.getCategory(), yogurt.getPrice(),yogurt.getQuantity());
            bridge.AddProduct(storeId, chicken.getName(), chicken.getDescription(),chicken.getCategory(), chicken.getPrice(),chicken.getQuantity());
            bridge.AddProduct(storeId, steak.getName(), steak.getDescription(),steak.getCategory(), steak.getPrice(),steak.getQuantity());
            bridge.addToCart(bread.getProductId(), storeId);
            bridge.addToCart(milk.getProductId(), storeId);
            bridge.addToCart(yogurt.getProductId(), storeId);
            bridge.addToCart(chicken.getProductId(), storeId);
            bridge.addToCart(steak.getProductId(), storeId);

        }
        catch (Exception ignored) {
            fail();
        }
    }
    @Test
    public void AddNameDiscount(){
        ServiceBasicDiscount serviceBasicDiscount=new ServiceBasicDiscount("50% discounts on steaks",50,new NameConditionRecord("Steak"));
        Response<ServiceDiscountInfo> response=bridge.addDiscount(serviceBasicDiscount,storeId);
        Response<Collection<ServiceDiscountInfo>> discounts=bridge.getDiscountInfo(storeId);
        assertFalse(response.isError());
        assertFalse(discounts.isError());
        for(ServiceDiscountInfo info:discounts.getValue())
            assertEquals("50% discounts on steaks",info.description);
        Response<ServiceAppliedDiscount> appliedDiscount=bridge.getBagDiscountInfo(storeId);
        assertFalse(appliedDiscount.isError());
        assertEquals(appliedDiscount.getValue().calcTotalSavings(),50);
    }
    @Test
    public void AddCategoryDiscount(){
        ServiceBasicDiscount serviceBasicDiscount=new ServiceBasicDiscount("50% discounts on meat products",50,new CategoryConditionRecord("Meat"));
        Response<ServiceDiscountInfo> response=bridge.addDiscount(serviceBasicDiscount,storeId);
        Response<Collection<ServiceDiscountInfo>> discounts=bridge.getDiscountInfo(storeId);
        assertFalse(response.isError());
        assertFalse(discounts.isError());
        for(ServiceDiscountInfo info:discounts.getValue())
            assertEquals("50% discounts on meat products",info.description);
        Response<ServiceAppliedDiscount> appliedDiscount=bridge.getBagDiscountInfo(storeId);
        assertFalse(appliedDiscount.isError());
        assertEquals(appliedDiscount.getValue().calcTotalSavings(),65);
    }
    @Test
    public void AddMinPriceDiscount(){
        ServiceBasicDiscount serviceBasicDiscount=new ServiceBasicDiscount("50% discounts products priced above 20",50,new MinPriceConditionRecord(20));
        Response<ServiceDiscountInfo> response=bridge.addDiscount(serviceBasicDiscount,storeId);
        Response<Collection<ServiceDiscountInfo>> discounts=bridge.getDiscountInfo(storeId);
        assertFalse(response.isError());
        assertFalse(discounts.isError());
        for(ServiceDiscountInfo info:discounts.getValue())
            assertEquals("50% discounts products priced above 20",info.description);
        Response<ServiceAppliedDiscount> appliedDiscount=bridge.getBagDiscountInfo(storeId);
        assertFalse(appliedDiscount.isError());
        assertEquals(appliedDiscount.getValue().calcTotalSavings(),65);
    }
    @Test
    public void AddMinPriceAndCategoryDiscount(){
        ServiceBasicDiscount minPriceDiscount=new ServiceBasicDiscount("50% discounts products priced above 45",50,new MinPriceConditionRecord(45));
        Response<ServiceDiscountInfo> response1=bridge.addDiscount(minPriceDiscount,storeId);
        ServiceBasicDiscount categoryDiscount=new ServiceBasicDiscount("50% discounts on meat products",50,new CategoryConditionRecord("Meat"));
        Response<ServiceDiscountInfo> response2=bridge.addDiscount(categoryDiscount,storeId);
        ServiceBasicDiscount andDiscount=new ServiceBasicDiscount("50% discount for meat products priced above 45",50,new AndConditionRecord(response1.getValue().id,response2.getValue().id));
        Response<ServiceDiscountInfo> response3=bridge.addDiscount(andDiscount,storeId);
        assertFalse(response1.isError());
        assertFalse(response2.isError());
        assertFalse(response3.isError());
        Response<Collection<ServiceDiscountInfo>> discounts=bridge.getDiscountInfo(storeId);
        for(ServiceDiscountInfo info:discounts.getValue())
            assertEquals("50% discount for meat products priced above 45",info.description);
        Response<ServiceAppliedDiscount> appliedDiscount=bridge.getBagDiscountInfo(storeId);
        assertFalse(appliedDiscount.isError());
        assertEquals(appliedDiscount.getValue().calcTotalSavings(),50);
    }
    @Test
    public void AddComplexDiscount(){
        ServiceBasicDiscount minPriceDiscount=new ServiceBasicDiscount("50% discounts products priced above 45 NIS",50,new MinPriceConditionRecord(45));
        Response<ServiceDiscountInfo> response1=bridge.addDiscount(minPriceDiscount,storeId);
        ServiceBasicDiscount categoryDiscount1=new ServiceBasicDiscount("50% discounts on meat products",50,new CategoryConditionRecord("Meat"));
        Response<ServiceDiscountInfo> response2=bridge.addDiscount(categoryDiscount1,storeId);
        ServiceBasicDiscount andDiscount1=new ServiceBasicDiscount("50% discount for meat products priced above 45 NIS",50,new AndConditionRecord(response1.getValue().id,response2.getValue().id));
        Response<ServiceDiscountInfo> response3=bridge.addDiscount(andDiscount1,storeId);
        ServiceBasicDiscount maxPriceDiscount=new ServiceBasicDiscount("50% discount for products priced below 10 NIS",50,new MaxPriceConditionRecord(10));
        Response<ServiceDiscountInfo> response4=bridge.addDiscount(maxPriceDiscount,storeId);
        ServiceBasicDiscount categoryDiscount2=new ServiceBasicDiscount("50% discounts on dairy products",50,new CategoryConditionRecord("Dairy"));
        Response<ServiceDiscountInfo> response5=bridge.addDiscount(categoryDiscount2,storeId);
        ServiceBasicDiscount andDiscount2=new ServiceBasicDiscount("50% discount for dairy products priced below 10",50,new AndConditionRecord(response4.getValue().id,response5.getValue().id));
        Response<ServiceDiscountInfo> response6=bridge.addDiscount(andDiscount2,storeId);
        assertFalse(response1.isError());
        assertFalse(response2.isError());
        assertFalse(response3.isError());
        assertFalse(response4.isError());
        assertFalse(response5.isError());
        assertFalse(response6.isError());
        Response<Collection<ServiceDiscountInfo>> discounts=bridge.getDiscountInfo(storeId);
        HashSet<String> descriptions=new HashSet<>();
        for(ServiceDiscountInfo serviceDiscountInfo:discounts.getValue())
            descriptions.add(serviceDiscountInfo.description);
        assertTrue(descriptions.contains("50% discount for dairy products priced below 10"));
        assertTrue(descriptions.contains("50% discount for meat products priced above 45 NIS"));
        Response<ServiceAppliedDiscount> appliedDiscount=bridge.getBagDiscountInfo(storeId);
        assertFalse(appliedDiscount.isError());
        assertEquals(appliedDiscount.getValue().calcTotalSavings(),52.5);
    }
    @Test
    public void MaxBetweenComplexDiscount(){
        ServiceBasicDiscount minPriceDiscount=new ServiceBasicDiscount("50% discounts products priced above 45 NIS",50,new MinPriceConditionRecord(45));
        Response<ServiceDiscountInfo> response1=bridge.addDiscount(minPriceDiscount,storeId);
        assertFalse(response1.isError());
        ServiceBasicDiscount categoryDiscount1=new ServiceBasicDiscount("50% discounts on meat products",50,new CategoryConditionRecord("Meat"));
        Response<ServiceDiscountInfo> response2=bridge.addDiscount(categoryDiscount1,storeId);
        assertFalse(response2.isError());
        ServiceBasicDiscount andDiscount1=new ServiceBasicDiscount("50% discount for meat products priced above 45 NIS",50,new AndConditionRecord(response1.getValue().id,response2.getValue().id));
        Response<ServiceDiscountInfo> response3=bridge.addDiscount(andDiscount1,storeId);
        assertFalse(response3.isError());
        ServiceBasicDiscount maxPriceDiscount=new ServiceBasicDiscount("50% discount for products priced below 10 NIS",50,new MaxPriceConditionRecord(10));
        Response<ServiceDiscountInfo> response4=bridge.addDiscount(maxPriceDiscount,storeId);
        assertFalse(response4.isError());
        ServiceBasicDiscount categoryDiscount2=new ServiceBasicDiscount("50% discounts on dairy products",50,new CategoryConditionRecord("Dairy"));
        Response<ServiceDiscountInfo> response5=bridge.addDiscount(categoryDiscount2,storeId);
        assertFalse(response5.isError());
        ServiceBasicDiscount andDiscount2=new ServiceBasicDiscount("50% discount for dairy products priced below 10",50,new AndConditionRecord(response4.getValue().id,response5.getValue().id));
        Response<ServiceDiscountInfo> response6=bridge.addDiscount(andDiscount2,storeId);
        assertFalse(response6.isError());
        String desc="Min between 50% discount for dairy products priced below 10 and 50% discount for meat products priced above 45 NIS";
        ServiceMultiDiscount serviceMultiDiscount=new ServiceMultiDiscount(DiscountType.MinBetweenDiscount,desc);
        serviceMultiDiscount.addDiscount(response6.getValue().id);
        serviceMultiDiscount.addDiscount(response3.getValue().id);
        Response<ServiceDiscountInfo> response7=bridge.addDiscount(serviceMultiDiscount,storeId);
        assertFalse(response7.isError());
        Response<Collection<ServiceDiscountInfo>> discounts=bridge.getDiscountInfo(storeId);
        for(ServiceDiscountInfo serviceDiscountInfo:discounts.getValue())
            assertEquals(serviceDiscountInfo.description,desc);
        Response<ServiceAppliedDiscount> appliedDiscount=bridge.getBagDiscountInfo(storeId);
        assertFalse(appliedDiscount.isError());
        assertEquals(appliedDiscount.getValue().calcTotalSavings(),2.5);
    }
}
