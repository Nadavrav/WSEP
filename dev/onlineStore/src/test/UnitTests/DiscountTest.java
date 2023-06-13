package UnitTests;

import DomainLayer.Stores.Conditions.BasicConditions.BooleanConditions.MaxBagPriceCondition;
import DomainLayer.Stores.Conditions.BasicConditions.BooleanConditions.MinBagPriceCondition;
import DomainLayer.Stores.Conditions.BasicConditions.BooleanConditions.MinTotalProductAmountCondition;
import DomainLayer.Stores.Conditions.BasicConditions.FilterConditions.*;
import DomainLayer.Stores.Conditions.ComplexConditions.*;
//import DomainLayer.Stores.Conditions.ComplexConditions.CompositeConditions.BooleanAfterFilterCondition;
//import DomainLayer.Stores.Conditions.ComplexConditions.CompositeConditions.FilterOnlyIfCondition;
//import DomainLayer.Stores.Conditions.ComplexConditions.MultiFilters.MultiAndCondition;
import DomainLayer.Stores.Discounts.AdditiveDiscount;
import DomainLayer.Stores.Discounts.BasicDiscount;
import DomainLayer.Stores.Discounts.MaxSelectiveDiscount;
import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Users.Bag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
        fullBag.addProduct(bread,1);
        fullBag.addProduct(milk,1);
        fullBag.addProduct(yogurt,1);
        fullBag.addProduct(chicken,1);
        fullBag.addProduct(steak,1);
    }
    @Test
    public void NoConditionDiscountTest(){
        BasicDiscount basicDiscount10 =new BasicDiscount("10% discount on everything!",1,10);
        BasicDiscount basicDiscount100 =new BasicDiscount("100% discount on everything!",2,100);
        double saved= basicDiscount10.calcDiscountAmount(fullBag);
        double free= basicDiscount100.calcDiscountAmount(fullBag);
        Assertions.assertEquals(16,saved);
        Assertions.assertEquals(160,free);
    }
    @Test
    public void basicNameDiscountTest(){
        NameCondition nameCondition=new NameCondition("Steak");
        BasicDiscount basicDiscount =new BasicDiscount("10% basicDiscount on steaks!",1,10,nameCondition);
        double saved= basicDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(saved,10);

    }
    @Test
    public void basicCategoryDiscountTest(){
        CategoryCondition categoryCondition=new CategoryCondition("Meat");
        BasicDiscount basicDiscount =new BasicDiscount("10% basicDiscount on meats!!",1,10,categoryCondition);
        double saved= basicDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(saved,13); //10% of steak and chicken, priced 100 and 30

    }
    @Test
    public void basicPriceDiscountTest(){
        MinPriceCondition minPriceCondition =new MinPriceCondition(20);
        MaxPriceCondition maxPriceCondition =new MaxPriceCondition(99.9);

        BasicDiscount minBasicDiscount =new BasicDiscount("50% discount on products priced above 20",1,50,minPriceCondition);
        BasicDiscount maxBasicDiscount =new BasicDiscount("10% discount on products priced below 99.9",2,10,maxPriceCondition);
        double maxSaved= maxBasicDiscount.calcDiscountAmount(fullBag);
        double minSaved= minBasicDiscount.calcDiscountAmount(fullBag);

        Assertions.assertEquals(6,maxSaved);
        Assertions.assertEquals(65,minSaved);
    }
    @Test
    public void basicAmountDiscountTest(){
        MinQuantityCondition minQuantityCondition =new MinQuantityCondition(17); //chicken and steak
        MaxQuantityCondition maxQuantityCondition =new MaxQuantityCondition(10); //everything but steak
        fullBag.changeProductAmount(steak,20);
        fullBag.changeProductAmount(bread,15);
        BasicDiscount minBasicDiscount =new BasicDiscount("50% discount if there is at least 17 of a product",1,50,minQuantityCondition);
        BasicDiscount maxBasicDiscount =new BasicDiscount("10% discount if there is at at most 10 of a product",2,10,maxQuantityCondition);
        double maxSaved= maxBasicDiscount.calcDiscountAmount(fullBag);
        double minSaved= minBasicDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(50*20,minSaved);
        Assertions.assertEquals(5,maxSaved);
    }
    @Test
    public void basicTotalPriceTest(){
        MaxBagPriceCondition maxBagPriceCondition =new MaxBagPriceCondition(200); //chicken and steak
        MinBagPriceCondition minBagPriceCondition =new MinBagPriceCondition(200); //everything but steak
        BasicDiscount minBasicDiscount =new BasicDiscount("50% discount if total price above 200 ",1,50,minBagPriceCondition);
        BasicDiscount maxBasicDiscount =new BasicDiscount("10% discount if total price below 200",2,10,maxBagPriceCondition);
        double maxSaved= maxBasicDiscount.calcDiscountAmount(fullBag);
        double minSaved= minBasicDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(0,minSaved);
        Assertions.assertEquals(16,maxSaved);
        fullBag.changeProductAmount(steak,2);
        maxSaved= maxBasicDiscount.calcDiscountAmount(fullBag);
        minSaved= minBasicDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(130,minSaved);
        Assertions.assertEquals(0,maxSaved);
    }
    @Test
    public void SpecificProductAmountDiscountTest(){
        NameCondition nameCondition=new NameCondition("Steak");
        MinQuantityCondition minQuantityCondition=new MinQuantityCondition(2);
        AndCondition nameAndMinQuantityCondition=new AndCondition(nameCondition,minQuantityCondition);
        //nameAndMinQuantityCondition.addCondition(nameCondition);
        //nameAndMinQuantityCondition.addCondition(minQuantityCondition);
        BasicDiscount basicDiscount =new BasicDiscount("50% basicDiscount on steaks if you buy 2",1,50,nameAndMinQuantityCondition);
        double noDiscount= basicDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(0,noDiscount);
        fullBag.changeProductAmount(steak,3);
        double yesDiscount= basicDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(150,yesDiscount);
    }
    //TODO: BORING CHORE-ADD MORE BASIC TESTS

//There is a 50% meat discount only if the bag contains at least 5 breads and also at least 6 dairy products
    @Test
    public void ComplexAndDiscount(){
        CheckForCondition breadCondition=new CheckForCondition(new NameCondition("Bread"),new MinTotalProductAmountCondition(5));
        CheckForCondition dairyCondition=new CheckForCondition(new CategoryCondition("Dairy"),new MinTotalProductAmountCondition(6));
        CheckIfCondition andCondition=new CheckIfCondition(breadCondition,dairyCondition);
        CheckIfCondition multiAndCondition=new CheckIfCondition(andCondition,new CategoryCondition("Meat"));
        //multiAndCondition.addCondition(andCondition);
        //multiAndCondition.addCondition(new CategoryCondition("Meat"));
        BasicDiscount basicDiscount =new BasicDiscount(" 50% meat discount if you buy least 5 breads and also at least 6 dairy products",1,50,multiAndCondition);
        double noDiscount= basicDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(0,noDiscount);
        fullBag.changeProductAmount(bread,5);
        noDiscount= basicDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(0,noDiscount);
        fullBag.changeProductAmount(yogurt,3);
        fullBag.changeProductAmount(milk,3);
        fullBag.changeProductAmount(bread,1);
        noDiscount= basicDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(0,noDiscount);
        fullBag.changeProductAmount(bread,6);
        double discount= basicDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(65,discount); //50% off 1 steak and 1 chicken, 50+15
    }
    //There is a 50% meat discount only if the bag contains at least 5 breads or at least 6 dairy products

    @Test
    public void ComplexOrDiscount(){
        CheckForCondition breadCondition=new CheckForCondition(new NameCondition("Bread"),new MinTotalProductAmountCondition(5));
        CheckForCondition dairyCondition=new CheckForCondition(new CategoryCondition("Dairy"),new MinTotalProductAmountCondition(6));
        OrCondition orCondition=new OrCondition(breadCondition,dairyCondition);
        CheckIfCondition multiAndCondition=new CheckIfCondition(orCondition,new CategoryCondition("Meat"));
       // multiAndCondition.addCondition(orCondition);
       // multiAndCondition.addCondition(new CategoryCondition("Meat"));
        BasicDiscount basicDiscount =new BasicDiscount(" 50% meat discount if you buy least 5 breads or at least 6 dairy products",1,50,multiAndCondition);
        double noDiscount= basicDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(0,noDiscount);
        fullBag.changeProductAmount(bread,5);
        double discount= basicDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(65,discount);
        fullBag.changeProductAmount(bread,1);
        fullBag.changeProductAmount(yogurt,3);
        fullBag.changeProductAmount(milk,3);
        discount= basicDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(65,discount);
        fullBag.changeProductAmount(bread,5);
        discount= basicDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(65,discount); //50% off 1 steak and 1 chicken, 50+15
    }

    //There is a 50% meat discount only if the bag contains at least 5 breads or at least 6 dairy products, but not both
    @Test
    public void ComplexXorDiscount(){
        CheckForCondition breadCondition=new CheckForCondition(new NameCondition("Bread"),new MinTotalProductAmountCondition(5));
        CheckForCondition dairyCondition=new CheckForCondition(new CategoryCondition("Dairy"),new MinTotalProductAmountCondition(6));
        XorCondition xorCondition=new XorCondition(breadCondition,dairyCondition);
        CheckIfCondition multiAndCondition=new CheckIfCondition(xorCondition,new CategoryCondition("Meat"));
        //multiAndCondition.addCondition(xorCondition);
        //multiAndCondition.addCondition(new CategoryCondition("Meat"));
        BasicDiscount basicDiscount =new BasicDiscount(" 50% meat discount if you buy least 5 breads or at least 6 dairy products, but not both",1,50,multiAndCondition);
        double noDiscount= basicDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(0,noDiscount);
        fullBag.changeProductAmount(bread,5);
        double discount= basicDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(65,discount);
        fullBag.changeProductAmount(bread,1);
        fullBag.changeProductAmount(yogurt,3);
        fullBag.changeProductAmount(milk,3);
        discount= basicDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(65,discount);
        fullBag.changeProductAmount(bread,5);
        noDiscount= basicDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(0,noDiscount);
    }
    //If the value of the basket is higher than NIS 200 and the basket also contains at least 3 yogurts, then there is a 50% discount on dairy products.
    @Test
    public void ComplexDiscount(){
        CheckForCondition minYogurtAmount=new CheckForCondition(new NameCondition("Yogurt"),new MinTotalProductAmountCondition(3));
        MinBagPriceCondition minBagPriceCondition=new MinBagPriceCondition(200);
        AndCondition andCondition=new AndCondition(minBagPriceCondition,minYogurtAmount);
        CheckIfCondition dairyDiscount=new CheckIfCondition(andCondition,new CategoryCondition("Dairy"));
        BasicDiscount basicDiscount =new BasicDiscount("If the value of the basket is higher than NIS 200 and the basket also contains at least 3 yogurts, then there is a 50% discount on dairy products",1,50,dairyDiscount);
        double noDiscount= basicDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(0,noDiscount);
        fullBag.changeProductAmount(yogurt,3); //160+2*15=190 (added 2 yogurts)
        noDiscount= basicDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(0,noDiscount);
        fullBag.changeProductAmount(milk,3); //190+10=200 (added 2 milk cartons)
        double discount= basicDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(30,discount); //15 from 3 milk, 45 from 3 yogurt, 60 total
    }
    //The discount given is the maximum between the value in NIS of 25% of the cost
    // of all meat products in the basket and the value in NIS of 20% of the cost of the milk cartons in the basket.
    @Test
    public void MaxBetweenMultipleDiscountsTest(){
        NameCondition milkCondition=new NameCondition("Milk");
        CategoryCondition meatCategoryCondition=new CategoryCondition("Meat");
        BasicDiscount meatsDiscount=new BasicDiscount("20% discount on all milk cartons",1,20,milkCondition);
        BasicDiscount milkDiscount =new BasicDiscount("25% discount on all meat products",2,25,meatCategoryCondition);
        MaxSelectiveDiscount maxSelectiveDiscount=new MaxSelectiveDiscount("20% discount on all milk cartons or 25% discount on all meat products, the larger of them",3);
        maxSelectiveDiscount.addDiscount(meatsDiscount);
        maxSelectiveDiscount.addDiscount(milkDiscount);
        double discount= maxSelectiveDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(32.5,discount);
        fullBag.changeProductAmount(milk,100);
        discount= maxSelectiveDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(100,discount);
        fullBag.changeProductAmount(steak,2);
        discount= maxSelectiveDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(100,discount);
        fullBag.changeProductAmount(steak,5);
        discount= maxSelectiveDiscount.calcDiscountAmount(fullBag);
        Assertions.assertEquals(132.5,discount);
    }
   //There is a 5% discount on dairy products, and in addition, there is a 20% discount on every store, so there is a total of 25% discount on dairy products
   @Test
   public void AdditiveDiscountsTest(){
       CategoryCondition dairyCategoryCondition=new CategoryCondition("Dairy");
       BasicDiscount storeDiscount=new BasicDiscount("20% on the whole store",1,20);
       BasicDiscount dairyDiscount =new BasicDiscount("50% discount dairy products",2,50,dairyCategoryCondition);
       AdditiveDiscount additiveDiscount=new AdditiveDiscount("20% discount on the whole store, and 5 discount on all dairy products",3);
       additiveDiscount.addDiscount(storeDiscount);
       additiveDiscount.addDiscount(dairyDiscount);
       double discount= additiveDiscount.calcDiscountAmount(fullBag);
       Assertions.assertEquals(42,discount); //dairy = 20, rest of store=140, 70% on dairy saving 14 and 20% on rest saving 28, total saved 42
   }
}
