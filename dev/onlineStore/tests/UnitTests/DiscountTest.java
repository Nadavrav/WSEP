package UnitTests;

import DomainLayer.Stores.Discounts.Conditions.BasicConditions.BagConditions.MaxBagPriceCondition;
import DomainLayer.Stores.Discounts.Conditions.BasicConditions.BagConditions.MinBagPriceCondition;
import DomainLayer.Stores.Discounts.Conditions.BasicConditions.ProductConditions.*;
import DomainLayer.Stores.Discounts.Discount;
import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Users.Bag;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DiscountTest {

    private Bag fullBag;
    private final StoreProduct bread=new StoreProduct(0,"Bread",10,"Wheat Foods",1,"Made of whole wheat");;
    private final StoreProduct milk=new StoreProduct(1,"Milk",5,"Dairy",1,"Freshly milked");;
    private final StoreProduct yogurt=new StoreProduct(2,"Yogurt",15,"Dairy",1,"Extra chunky");;
    private final StoreProduct chicken=new StoreProduct(3,"Chicken",30,"Meat",1,"40% chicken");;
    private final StoreProduct steak=new StoreProduct(4,"Steak",100,"Meat",1,"May contain peanuts");

    /**
     * for anyone looking at these tests: unless changed during a test there is exactly 1 of each product in fullBag, who's prices add up to 160
     */
    @BeforeAll
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
        Discount discount10=new Discount("10% discount on everything!",10);
        Discount discount100=new Discount("100% discount on everything!",100);
        double saved=discount10.calcDiscountAmount(fullBag);
        double free=discount100.calcDiscountAmount(fullBag);
        assertEquals(16,saved);
        assertEquals(160,free);
    }
    @Test
    public void basicNameDiscountTest(){
        NameCondition nameCondition=new NameCondition("Steak");
        Discount discount=new Discount("10% discount on steaks!",10,nameCondition);
        double saved=discount.calcDiscountAmount(fullBag);
        assertEquals(saved,10);

    }
    @Test
    public void basicCategoryDiscountTest(){
        CategoryCondition categoryCondition=new CategoryCondition("Meat");
        Discount discount=new Discount("10% discount on meats!!",10,categoryCondition);
        double saved=discount.calcDiscountAmount(fullBag);
        assertEquals(saved,13); //10% of steak and chicken, priced 100 and 30

    }
    @Test
    public void basicPriceDiscountTest(){
        MinPriceCondition minPriceCondition =new MinPriceCondition(20);
        MaxPriceCondition maxPriceCondition =new MaxPriceCondition(99.9);

        Discount minDiscount=new Discount("50% discount on products priced above 20",50,minPriceCondition);
        Discount maxDiscount=new Discount("10% discount on products priced below 99.9",10,maxPriceCondition);
        double maxSaved=maxDiscount.calcDiscountAmount(fullBag);
        double minSaved=minDiscount.calcDiscountAmount(fullBag);

        assertEquals(6,maxSaved);
        assertEquals(65,minSaved);
    }
    @Test
    public void basicAmountDiscountTest(){
        MinQuantityCondition minQuantityCondition =new MinQuantityCondition(17); //chicken and steak
        MaxQuantityCondition maxQuantityCondition =new MaxQuantityCondition(10); //everything but steak
        fullBag.changeProductAmount(steak,20);
        fullBag.changeProductAmount(bread,15);
        Discount minDiscount=new Discount("50% discount if there is at least 17 of a product",50,minQuantityCondition);
        Discount maxDiscount=new Discount("10% discount if there is at at most 10 of a product",10,maxQuantityCondition);
        double maxSaved=maxDiscount.calcDiscountAmount(fullBag);
        double minSaved=minDiscount.calcDiscountAmount(fullBag);
        assertEquals(50*20,minSaved);
        assertEquals(5,maxSaved);
        fullBag.changeProductAmount(steak,1);
        fullBag.changeProductAmount(bread,1);
    }
    @Test
    public void basicTotalPriceTest(){
        MaxBagPriceCondition maxBagPriceCondition =new MaxBagPriceCondition(200); //chicken and steak
        MinBagPriceCondition minBagPriceCondition =new MinBagPriceCondition(200); //everything but steak
        Discount minDiscount=new Discount("50% discount if total price above 200 ",50,minBagPriceCondition);
        Discount maxDiscount=new Discount("10% discount if total price below 200",10,maxBagPriceCondition);
        double maxSaved=maxDiscount.calcDiscountAmount(fullBag);
        double minSaved=minDiscount.calcDiscountAmount(fullBag);
        assertEquals(0,minSaved);
        assertEquals(16,maxSaved);
        fullBag.changeProductAmount(steak,2);
        maxSaved=maxDiscount.calcDiscountAmount(fullBag);
        minSaved=minDiscount.calcDiscountAmount(fullBag);
        assertEquals(130,minSaved);
        assertEquals(0,maxSaved);
    }
}
