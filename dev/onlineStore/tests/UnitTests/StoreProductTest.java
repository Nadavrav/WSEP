package UnitTests;

import DomainLayer.Stores.Products.StoreProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StoreProductTest {
    StoreProduct p1,p2,p3,p4;
    int StoreId1,StoreId2;
    @BeforeEach
    void setup()
    {
        p1 = new StoreProduct("0-0","Milk",5,"Milk",5,"Its Milk what did you expect");
        p2 = new StoreProduct("1-0","Bread",7.2,"Bread",6,"Just a whole loaf of bread");
        p3 = new StoreProduct("0-1","Butter",3.4,"Butter",6,"A Golden Brick");
        p4 = new StoreProduct("1-1","Eggs",6.8,"Eggs",6,"What came first?");
        StoreId1 = 0;
        StoreId2 = 1;
    }
    @Test
    void CreateProduct_NullId() {
        assertThrows(NullPointerException.class,()->p1 = new StoreProduct(null,"Milk",5,"Milk",5,"Its Milk what did you expect"));
    }
    @Test
    void CreateProduct_InvalidId() {
        assertThrows(IllegalArgumentException.class,()->p1 = new StoreProduct("-0","Milk",5,"Milk",5,"Its Milk what did you expect"));
    }
    @Test
    void CreateProduct_Nullname() {
        assertThrows(NullPointerException.class,()->p1 = new StoreProduct("0-0",null,5,"Milk",5,"Its Milk what did you expect"));
    }
    @Test
    void CreateProduct_negativePrice() {
        assertThrows(IllegalArgumentException.class,()->p1 = new StoreProduct("0-0","Milk",-5,"Milk",5,"Its Milk what did you expect"));
    }
    @Test
    void CreateProduct_NegativeQuantity() {
        assertThrows(IllegalArgumentException.class,()->p1 = new StoreProduct("0-0","Milk",5,"Milk",-5,"Its Milk what did you expect"));
    }
    @Test
    void CreateProduct_NullCategory() {
        assertThrows(NullPointerException.class,()->p1 = new StoreProduct("0-0","Milk",5,null,5,"Its Milk what did you expect"));
    }
    @Test
    void CreateProduct_NullDesc() {
        assertThrows(NullPointerException.class,()->p1 = new StoreProduct("0-0","Milk",5,"Milk",5,null));
    }
    @Test
    void getStoreIdByProductId_validId() {
        assertEquals(0,p1.getStoreIdByProductId("0-0"));
        assertEquals(0,p1.getStoreIdByProductId("0-1"));
        assertEquals(1,p1.getStoreIdByProductId("1-0"));
        assertEquals(1,p1.getStoreIdByProductId("1-1"));
    }
    @Test
    void getStoreIdByProductId_nullId() {
        assertThrows(NullPointerException.class,()->p1.getStoreIdByProductId(null));
    }
    @Test
    void getStoreIdByProductId_InvalidId() {
        assertThrows(IllegalArgumentException.class,()->p1.getStoreIdByProductId("-1"));
    }
    @Test
    public void testValidProductId() throws Exception {
        String productId = "123-456";
        p1.isValidProductId(productId);
    }

    @Test
    public void testNullProductId() {
        assertThrows(NullPointerException.class, () -> {
            String productId = null;
            p1.isValidProductId(productId);
        });
    }

    @Test
    public void testEmptyProductId() {
        assertThrows(NullPointerException.class, () -> {
            String productId = "";
            p1.isValidProductId(productId);
        });
    }

    @Test
    public void testInvalidProductId() {
        assertThrows(Exception.class, () -> {
            String productId = "123456";
            p1.isValidProductId(productId);
        });
    }

    @Test
    public void testInvalidProductIdWithDashAtBeginning() {
        assertThrows(Exception.class, () -> {
            String productId = "-456";
            p1.isValidProductId(productId);
        });
    }

    @Test
    public void testInvalidProductIdWithDashAtEnd() {
        assertThrows(NullPointerException.class, () -> {
            String productId = "123-";
            p1.isValidProductId(productId);
        });
    }

    @Test
    public void testInvalidProductIdWithNonNumericPrefix() {
        assertThrows(Exception.class, () -> {
            String productId = "abc-456";
            p1.isValidProductId(productId);
        });
    }

    @Test
    public void testInvalidProductIdWithNonNumericSuffix() {
        assertThrows(Exception.class, () -> {
            String productId = "123-def";
            p1.isValidProductId(productId);
        });
    }

    @Test
    public void testGetAverageRatingWithNoRatings() {
        double actualResult = p1.GetAverageRating();
        double expectedResult = 0;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetAverageRatingWithOneRating() {
        try {
            p1.addRating("name", 3);
            double actualResult = p1.GetAverageRating();
            double expectedResult = 3.5;
            assertEquals(expectedResult, actualResult);
        }
        catch (Exception e)
        {//Should never happen
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    public void testGetAverageRatingWithMultipleRatings() {
        try {
            p1.addRating("name", 3);
            p1.addRating("name1", 3);
            p1.addRating("name2", 3);
            double actualResult = p1.GetAverageRating();
            double expectedResult = 3;
            assertEquals(expectedResult, actualResult);
        }
        catch (Exception e)
        {//Should never happen
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    public void testUpdateQuantityWithPositiveQuantity() {
        p1.UpdateQuantity(3);
        int actualResult = p1.getQuantity();
        int expectedResult = 3;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testUpdateQuantityWithZeroQuantity() {
        p1.UpdateQuantity(0);
        int actualResult = p1.getQuantity();
        int expectedResult = 0;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testUpdateQuantityWithNegativeQuantity() {
        assertThrows(IllegalArgumentException.class, () -> {
            p1.UpdateQuantity(-3);
        });
    }

    @Test
    public void testIncreaseQuantityWithPositiveQuantity() {
        p1.IncreaseQuantity(3);
        int actualResult = p1.getQuantity();
        int expectedResult = 8;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIncreaseQuantityWithZeroQuantity() {
        p1.IncreaseQuantity(0);
        int actualResult = p1.getQuantity();
        int expectedResult = 5;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIncreaseQuantityWithNegativeQuantity() {
        assertThrows(IllegalArgumentException.class, () -> {
            p1.IncreaseQuantity(-3);
        });
    }

    @Test
    public void testAddRatingWithNewUser() throws Exception {
        p1.addRating("John", 4);
        double actualResult = p1.GetAverageRating();
        double expectedResult = 4.0;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testAddRatingWithExistingUser() throws Exception {
        p1.addRating("John", 4);
        p1.addRating("John", 2);
        double actualResult = p1.GetAverageRating();
        double expectedResult = 3.0;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testAddRatingAndCommentWithNewUser() throws Exception {
        p1.addRatingAndComment("John", 4, "Great product!");
        double actualRating = p1.GetAverageRating();
        double expectedRating = 4.0;
        assertEquals(expectedRating, actualRating);
    }

    @Test
    public void testAddRatingAndCommentWithExistingUser() throws Exception {
        p1.addRatingAndComment("John", 4, "Great product!");
        p1.addRatingAndComment("John", 2, "Not so good");
        double actualRating = p1.GetAverageRating();
        double expectedRating = 3.0;
        assertEquals(expectedRating, actualRating);
    }

}