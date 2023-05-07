package UnitTests;

import DomainLayer.Stores.Discounts.Conditions.BasicConditions.BagConditions.MaxBagPriceCondition;
import DomainLayer.Stores.Discounts.Conditions.BasicConditions.BagConditions.MinBagPriceCondition;
import DomainLayer.Stores.Discounts.Conditions.BasicConditions.ProductConditions.*;
import DomainLayer.Stores.Discounts.Conditions.LogicConditions.AndCondition;
import DomainLayer.Stores.Discounts.Discounts.BasicDiscount;
import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Users.Bag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DiscountTest {

    private Bag fullBag;
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
        fullBag=new Bag(1);
        fullBag.addProduct(bread);
        fullBag.addProduct(milk);
        fullBag.addProduct(yogurt);
        fullBag.addProduct(chicken);
        fullBag.addProduct(steak);
    }
    @Test
    public void NoConditionDiscountTest(){
        BasicDiscount basicDiscount10 =new BasicDiscount("10% discount on everything!",10);
        BasicDiscount basicDiscount100 =new BasicDiscount("100% discount on everything!",100);
        double saved= basicDiscount10.calcDiscountAmount(fullBag);
        double free= basicDiscount100.calcDiscountAmount(fullBag);
        assertEquals(16,saved);
        assertEquals(160,free);
    }
    @Test
    public void basicNameDiscountTest(){
        NameCondition nameCondition=new NameCondition("Steak");
        BasicDiscount basicDiscount =new BasicDiscount("10% basicDiscount on steaks!",10,nameCondition);
        double saved= basicDiscount.calcDiscountAmount(fullBag);
        assertEquals(saved,10);

    }
    @Test
    public void basicCategoryDiscountTest(){
        CategoryCondition categoryCondition=new CategoryCondition("Meat");
        BasicDiscount basicDiscount =new BasicDiscount("10% basicDiscount on meats!!",10,categoryCondition);
        double saved= basicDiscount.calcDiscountAmount(fullBag);
        assertEquals(saved,13); //10% of steak and chicken, priced 100 and 30

    }
    @Test
    public void basicPriceDiscountTest(){
        MinPriceCondition minPriceCondition =new MinPriceCondition(20);
        MaxPriceCondition maxPriceCondition =new MaxPriceCondition(99.9);

        BasicDiscount minBasicDiscount =new BasicDiscount("50% discount on products priced above 20",50,minPriceCondition);
        BasicDiscount maxBasicDiscount =new BasicDiscount("10% discount on products priced below 99.9",10,maxPriceCondition);
        double maxSaved= maxBasicDiscount.calcDiscountAmount(fullBag);
        double minSaved= minBasicDiscount.calcDiscountAmount(fullBag);

        assertEquals(6,maxSaved);
        assertEquals(65,minSaved);
    }
    @Test
    public void basicAmountDiscountTest(){
        MinQuantityCondition minQuantityCondition =new MinQuantityCondition(17); //chicken and steak
        MaxQuantityCondition maxQuantityCondition =new MaxQuantityCondition(10); //everything but steak
        fullBag.changeProductAmount(steak,20);
        fullBag.changeProductAmount(bread,15);
        BasicDiscount minBasicDiscount =new BasicDiscount("50% discount if there is at least 17 of a product",50,minQuantityCondition);
        BasicDiscount maxBasicDiscount =new BasicDiscount("10% discount if there is at at most 10 of a product",10,maxQuantityCondition);
        double maxSaved= maxBasicDiscount.calcDiscountAmount(fullBag);
        double minSaved= minBasicDiscount.calcDiscountAmount(fullBag);
        assertEquals(50*20,minSaved);
        assertEquals(5,maxSaved);
    }
    @Test
    public void basicTotalPriceTest(){
        MaxBagPriceCondition maxBagPriceCondition =new MaxBagPriceCondition(200); //chicken and steak
        MinBagPriceCondition minBagPriceCondition =new MinBagPriceCondition(200); //everything but steak
        BasicDiscount minBasicDiscount =new BasicDiscount("50% discount if total price above 200 ",50,minBagPriceCondition);
        BasicDiscount maxBasicDiscount =new BasicDiscount("10% discount if total price below 200",10,maxBagPriceCondition);
        double maxSaved= maxBasicDiscount.calcDiscountAmount(fullBag);
        double minSaved= minBasicDiscount.calcDiscountAmount(fullBag);
        assertEquals(0,minSaved);
        assertEquals(16,maxSaved);
        fullBag.changeProductAmount(steak,2);
        maxSaved= maxBasicDiscount.calcDiscountAmount(fullBag);
        minSaved= minBasicDiscount.calcDiscountAmount(fullBag);
        assertEquals(130,minSaved);
        assertEquals(0,maxSaved);
    }
    @Test
    public void SpecificProductAmountDiscountTest(){
        NameCondition nameCondition=new NameCondition("Steak");
        MinQuantityCondition minQuantityCondition=new MinQuantityCondition(2);
        AndCondition nameAndMinQuantityCondition=new AndCondition(nameCondition,minQuantityCondition);
        BasicDiscount basicDiscount =new BasicDiscount("50% basicDiscount on steaks if you buy 2",50,nameAndMinQuantityCondition);
        double noDiscount= basicDiscount.calcDiscountAmount(fullBag);
        assertEquals(0,noDiscount);
        fullBag.changeProductAmount(steak,3);
        double yesDiscount= basicDiscount.calcDiscountAmount(fullBag);
        assertEquals(150,yesDiscount);
    }
    //TODO: BORING CHORE-ADD MORE BASIC TESTS


    //@Test
    //public void XorAndBagPriceDiscount(){
    //    NameCondition nameCondition=new NameCondition("Steak");
    //    MinBagPriceCondition minPriceCondition=new MinBagPriceCondition(259);
    //    AndCondition nameAndMinQuantityCondition=new AndCondition(nameCondition,minPriceCondition);
    //    BasicDiscount basicDiscount =new BasicDiscount("50% basicDiscount on steaks total bag price above 250",50,nameAndMinQuantityCondition);
    //    double noDiscount= basicDiscount.calcDiscountAmount(fullBag);
    //    assertEquals(0,noDiscount); //total amount: 160
    //    fullBag.changeProductAmount(steak,2);
    //    double yesDiscount= basicDiscount.calcDiscountAmount(fullBag); //total amount: 160+100=260
    //    assertEquals(100,yesDiscount); //total price above 250, 50% basicDiscount on 2 steaks
    //}
}
