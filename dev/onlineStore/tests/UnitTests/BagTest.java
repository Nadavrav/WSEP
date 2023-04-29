package UnitTests;

import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Users.Bag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {

    StoreProduct p1,p2,p3,p4;
    int StoreId1,StoreId2;
    @BeforeEach
    void setup()
    {
        p1 = new StoreProduct(0,"Milk",5,"Milk",20,"Its Milk what did you expect"); //store 0
        p2 = new StoreProduct(1,"Bread",7.2,"Bread",6,"Just a whole loaf of bread"); //store 0
        p3 = new StoreProduct(0,"Butter",3.4,"Butter",6,"A Golden Brick"); //store 1
        p4 = new StoreProduct(1,"Eggs",6.8,"Eggs",6,"What came first?"); //store 1
        StoreId1 = 0;
        StoreId2 = 1;
    }
    @Test
    public void testAddProductWithNewProduct() {
        Bag bag = new Bag(StoreId1);
        bag.addProduct(p1);
        String ActualproductList = bag.bagToString();
        String expectedList = "Product Id: 0-0 ,Product Name: Milk ,Product Price: 5.0\n";
        assertEquals(expectedList, ActualproductList);
    }

    @Test
    public void testAddProductWithExistingProduct() {
        Bag bag = new Bag(StoreId1);
        bag.addProduct(p1);
        bag.addProduct(p1);
        String ActualproductList = bag.bagToString();
        String expectedList = "Product Id: 0-0 ,Product Name: Milk ,Product Price: 5.0\n";
        assertEquals(expectedList, ActualproductList);
    }
    @Test
    public void testAddProductWithMultipleProducts() {
        Bag bag = new Bag(StoreId1);
        bag.addProduct(p1);
        bag.addProduct(p2);
        String ActualproductList = bag.bagToString();
        String expectedList = "Product Id: 0-0 ,Product Name: Milk ,Product Price: 5.0\n" +
                "Product Id: 1-0 ,Product Name: Bread ,Product Price: 7.2\n";
        assertEquals(expectedList, ActualproductList);
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
        for(int i=0; i<10;i++)
            bag.addProduct(p1);
        double actualTotalAmount = bag.calculateTotalAmount();
        double expectedTotalAmount = p1.getPrice()*10;
        assertEquals(expectedTotalAmount, actualTotalAmount);
    }
    @Test
    public void testCalculateTotalAmountWithMultipleProducts() {
        Bag bag = new Bag(StoreId1);
        bag.addProduct(p1);
        bag.addProduct(p2);
        double actualTotalAmount = bag.calculateTotalAmount();
        double expectedTotalAmount = p1.getPrice()+p2.getPrice();
        assertEquals(expectedTotalAmount, actualTotalAmount);
    }
}