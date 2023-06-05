package UnitTests;

import DomainLayer.Facade;
import DomainLayer.Stores.Conditions.BasicConditions.BooleanConditions.DateCondition;
import DomainLayer.Stores.Conditions.BasicConditions.BooleanConditions.MinTotalProductAmountCondition;
import DomainLayer.Stores.Conditions.BasicConditions.FilterConditions.CategoryCondition;
//import DomainLayer.Stores.Conditions.ComplexConditions.CompositeConditions.BooleanAfterFilterCondition;
import DomainLayer.Stores.Conditions.ComplexConditions.AndCondition;
import DomainLayer.Stores.Conditions.ComplexConditions.CheckForCondition;
import DomainLayer.Stores.Conditions.ComplexConditions.CheckIfCondition;
import DomainLayer.Stores.Policies.Policy;
import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Users.Bag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PolicyTests {
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
            facade.addProductToCart(bread.getProductId(), storeId,1,visitorId);
            facade.addProductToCart(milk.getProductId(), storeId,1,visitorId);
            facade.addProductToCart(yogurt.getProductId(), storeId,1,visitorId);
            facade.addProductToCart(chicken.getProductId(), storeId,1,visitorId);
            facade.addProductToCart(steak.getProductId(), storeId,1,visitorId);
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
            facade.AddStorePolicy(visitorId,storeId,new Policy("All products can only be bought on the 18.2.2030",condition));
            facade.purchaseCart(visitorId, 4444, "Space");
            fail();
        }
        catch (Exception ignored){
        }
    }
    //policy to have at least 5 dairy products to buy a cart
    @Test
    public void MinProductAmountTest(){
            CheckForCondition condition=new CheckForCondition(new CategoryCondition("Dairy"),new MinTotalProductAmountCondition(5));
        try {
            facade.AddStorePolicy(visitorId,storeId,new Policy("Cart must have at least 5 dairy products to make a purchase",condition));
            facade.purchaseCart(visitorId, 4444, "Space");
            fail();
        }
        catch (Exception ignored){
            try{
                facade.changeCartProductQuantity(milk.getProductId(),storeId,5,visitorId);
                facade.purchaseCart(visitorId,444,"The Void");
            }
            catch (Exception e){
                fail();
            }
        }
    }
}
