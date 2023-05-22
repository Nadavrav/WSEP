package tests.UnitTests;

import DomainLayer.Facade;
import DomainLayer.Stores.Conditions.BasicConditions.BooleanConditions.DateCondition;
import DomainLayer.Stores.Conditions.BasicConditions.BooleanConditions.MaxBagPriceCondition;
import DomainLayer.Stores.Conditions.BasicConditions.BooleanConditions.MinBagPriceCondition;
import DomainLayer.Stores.Conditions.BasicConditions.BooleanConditions.MinTotalProductAmountCondition;
import DomainLayer.Stores.Conditions.BasicConditions.FilterConditions.*;
import DomainLayer.Stores.Conditions.BooleanLogicConditions.AndCondition;
import DomainLayer.Stores.Conditions.BooleanLogicConditions.OrCondition;
import DomainLayer.Stores.Conditions.BooleanLogicConditions.WrapperCondition;
import DomainLayer.Stores.Conditions.BooleanLogicConditions.XorCondition;
import DomainLayer.Stores.Conditions.ComplexConditions.MultiAndCondition;
import DomainLayer.Stores.Discounts.AdditiveDiscount;
import DomainLayer.Stores.Discounts.BasicDiscount;
import DomainLayer.Stores.Discounts.MaxSelectiveDiscount;
import DomainLayer.Stores.Policies.Policy;
import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Stores.Store;
import DomainLayer.Users.Bag;
import DomainLayer.Users.RegisteredUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PolicyTests {
    //TODO: ADD POLICY TESTS
    Facade facade=Facade.getInstance();
    int storeId=0;
    private Bag fullBag;
    int visitorId;
    private final StoreProduct bread=new StoreProduct(0,"Bread",10,"Wheat Foods",1,"Made of whole wheat");
    private final StoreProduct milk=new StoreProduct(1,"Milk",5,"Dairy",1,"Freshly milked");
    private final StoreProduct yogurt=new StoreProduct(2,"Yogurt",15,"Dairy",1,"Extra chunky");
    private final StoreProduct chicken=new StoreProduct(3,"Chicken",30,"Meat",1,"40% chicken");
    private final StoreProduct steak=new StoreProduct(4,"Steak",100,"Meat",1,"May contain peanuts");

    /**
     * for anyone looking at these tests: unless changed during a test there is exactly 1 of each product in fullBag, who's prices add up to 160
     */
    @BeforeEach
    public void setUp(){
        try {
            facade.resetData();
            visitorId = facade.EnterNewSiteVisitor();
            facade.Register(visitorId, "user", "admin123456");
            facade.login(visitorId, "user", "admin123456");
            storeId=facade.OpenNewStore(visitorId,"Store1");
            facade.AddProduct(visitorId,storeId,bread);
            facade.AddProduct(visitorId,storeId,milk);
            facade.AddProduct(visitorId,storeId,yogurt);
            facade.AddProduct(visitorId,storeId,chicken);
            facade.AddProduct(visitorId,storeId,steak);
            facade.addProductToCart(bread.getProductId(), storeId,visitorId);
            facade.addProductToCart(milk.getProductId(), storeId,visitorId);
            facade.addProductToCart(yogurt.getProductId(), storeId,visitorId);
            facade.addProductToCart(chicken.getProductId(), storeId,visitorId);
            facade.addProductToCart(steak.getProductId(), storeId,visitorId);
        }
        catch (Exception ignored){
            fail();
        }

    }
    @Test
    public void NoConditionPolicyTest(){
        try {
            facade.purchaseCart(visitorId, 4444, "Space");
        }
        catch (Exception e){
            fail();
        }
    }
    @Test
    public void DatePolicyTest(){
        try {
            DateCondition condition=new DateCondition(18,2,2030);
            facade.AddStorePolicy(visitorId,storeId,new Policy(condition));
            facade.purchaseCart(visitorId, 4444, "Space");
            fail();
        }
        catch (Exception ignored){
        }
    }
}
