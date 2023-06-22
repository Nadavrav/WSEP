package UnitTests;

import DomainLayer.Facade;
import DomainLayer.Stores.Conditions.BasicConditions.BooleanConditions.BetweenDatesCondition;
import DomainLayer.Stores.Conditions.BasicConditions.BooleanConditions.MinTotalProductAmountCondition;
import DomainLayer.Stores.Conditions.BasicConditions.FilterConditions.CategoryCondition;
//import DomainLayer.Stores.Conditions.ComplexConditions.CompositeConditions.BooleanAfterFilterCondition;
import DomainLayer.Stores.Conditions.ComplexConditions.CheckForCondition;
import DomainLayer.Stores.Policies.Policy;
import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Users.Bag;
import DAL.TestsFlags;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PolicyTests {
    Facade facade;
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
            TestsFlags.getInstance().setTests();
            facade=Facade.getInstance();
            facade.resetData();
            visitorId = facade.enterNewSiteVisitor();
            facade.register(visitorId, "user", "admin123456");
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
            String holderName="nadia safadi";
            String visitorCard ="1234123412341234";
            String expireDate = "4/28";
            int cvv = 123;
            String id ="206469017";
            String address = "4036";
            String city ="Nazareth";
            String country ="Israel";
            String zip = "1613101";
            facade.purchaseCart(visitorId, holderName,visitorCard,expireDate,cvv,id,address,city,country,zip);
        }
        catch (Exception e){
            fail();
        }
    }
    @Test
    public void DatePolicyTest(){
        try {
            String holderName="nadia safadi";
            String visitorCard ="1234123412341234";
            String expireDate = "4/28";
            int cvv = 123;
            String id ="206469017";
            String address = "4036";
            String city ="Nazareth";
            String country ="Israel";
            String zip = "1613101";
            BetweenDatesCondition condition=new BetweenDatesCondition(18,2,2030,18,2,2030);
            facade.AddStorePolicy(visitorId,storeId,new Policy("All products can only be bought on the 18.2.2030",1,condition));
            facade.purchaseCart(visitorId, holderName,visitorCard,expireDate,cvv,id,address,city,country,zip);
            fail();
        }
        catch (Exception ignored){
        }
    }
    //policy to have at least 5 dairy products to buy a cart
    @Test
    public void MinProductAmountTest(){
            CheckForCondition condition=new CheckForCondition(new CategoryCondition("Dairy"),new MinTotalProductAmountCondition(5));
        String holderName="nadia safadi";
        String visitorCard ="1234123412341234";
        String expireDate = "4/28";
        int cvv = 123;
        String id ="206469017";
        String address = "4036";
        String city ="Nazareth";
        String country ="Israel";
        String zip = "1613101";
        try {

            facade.AddStorePolicy(visitorId,storeId,new Policy("Cart must have at least 5 dairy products to make a purchase",1,condition));
            facade.purchaseCart(visitorId, holderName,visitorCard,expireDate,cvv,id,address,city,country,zip);
            fail();
        }
        catch (Exception ignored){
            try{
                facade.changeCartProductQuantity(milk.getProductId(),storeId,5,visitorId);
                facade.purchaseCart(visitorId, holderName,visitorCard,expireDate,cvv,id,address,city,country,zip);
            }
            catch (Exception e){
                fail();
            }
        }
    }
}
