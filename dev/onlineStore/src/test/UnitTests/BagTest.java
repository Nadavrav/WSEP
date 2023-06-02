package UnitTests;

import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Users.Bag;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

class BagTest {

    StoreProduct p1,p2,p3,p4;
    int StoreId1,StoreId2;
    @BeforeEach
    void setup()
    {
        p1 = new StoreProduct(0,"Milk",5,"Milk",20,"Its Milk what did you expect"); //store 0
        p2 = new StoreProduct(1,"Bread",7.2,"Bread",6,"Just a whole loaf of bread"); //store 0
        p3 = new StoreProduct(2,"Butter",3.4,"Butter",6,"A Golden Brick"); //store 1
        p4 = new StoreProduct(3,"Eggs",6.8,"Eggs",6,"What came first?"); //store 1
        StoreId1 = 0;
        StoreId2 = 1;
    }
    @Test
    public void testAddProductWithNewProduct() {
        Bag bag = new Bag(StoreId1);
        bag.addProduct(p1,1);
        String ActualproductList = bag.bagToString();
        String expectedList = "Name: Milk Description: Its Milk what did you expect Category: Milk price per unit: 5.0 Amount: 1 total price: 5.0\n";
       assertEquals(expectedList, ActualproductList);
    }

    @Test
    public void testAddProductWithExistingProduct() {
        Bag bag = new Bag(StoreId1);
        try {
            bag.addProduct(p1,1);
            bag.addProduct(p1,1);
        }
        catch (Exception ignored){}
        String ActualproductList = bag.bagToString();
        String expectedList = "Name: Milk Description: Its Milk what did you expect Category: Milk price per unit: 5.0 Amount: 1 total price: 5.0\n";
        assertEquals(expectedList, ActualproductList);
    }
    //todo: notice-- this type of test is problematic, expectedList assumes a specific order in which the strings are printed- and the system doesn't have to follow that
    @Test
    public void testAddProductWithMultipleProducts() {
       // Bag bag = new Bag(StoreId1);
       // bag.addProduct(p1);
       // bag.addProduct(p2);
       // String ActualproductList = bag.bagToString();
       // HashSet<String>
       // String expectedList = "Name: Milk Description: Its Milk what did you expect Category: Milk price per unit: 5.0 Amount: 1 total price: 5.0\n"+
       // "Name: Bread Description: Just a whole loaf of bread Category: Bread price per unit: 7.2 Amount: 1 total price: 7.2\n";
       // assertEquals(expectedList, ActualproductList);
        //TODO: CHANGE WHEN SERVICE OBJECTS ARE IMPLEMENTED
    }
    @Test
    public void testCalculateTotalAmountWithNoProducts() {
        Bag bag = new Bag(StoreId1);
        double actualTotalAmount = bag.calculateTotalAmount();
        double expectedTotalAmount = 0;
        assertEquals(expectedTotalAmount, actualTotalAmount);
    }
    @Test
    public void testCalculateTotalAmountOneProductMultipleTimes() {
        Bag bag = new Bag(StoreId1);
        bag.addProduct(p1,1);
        bag.changeProductAmount(p1,10);
        double actualTotalAmount = bag.calculateTotalAmount();
        double expectedTotalAmount = p1.getPrice()*10;
        assertEquals(expectedTotalAmount, actualTotalAmount);
    }
    @Test
    public void testCalculateTotalAmountWithMultipleProducts() {
        Bag bag = new Bag(StoreId1);
        bag.addProduct(p1,1);
        bag.addProduct(p2,1);
        double actualTotalAmount = bag.calculateTotalAmount();
        double expectedTotalAmount = p1.getPrice()+p2.getPrice();
        assertEquals(expectedTotalAmount, actualTotalAmount);
    }
}