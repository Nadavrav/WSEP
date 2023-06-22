package UnitTests;

import DomainLayer.Stores.Products.Product;
import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Stores.Products.StoreProductObserver;
import DAL.TestsFlags;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StoreProductTest {
    StoreProductObserver observer1,observer2;
    StoreProduct p1,p2,p3,p4;
    int StoreId1,StoreId2;
    @BeforeEach
    void setup()
    {
        TestsFlags.getInstance().setTests();
        p1 = new StoreProduct(0,"Milk",5,"Milk",5,"Its Milk what did you expect");
        p2 = new StoreProduct(1,"Bread",7.2,"Bread",6,"Just a whole loaf of bread");
        p3 = new StoreProduct(0,"Butter",3.4,"Butter",6,"A Golden Brick");
        p4 = new StoreProduct(1,"Eggs",6.8,"Eggs",6,"What came first?");
        StoreId1 = 0;
        StoreId2 = 1;
        observer1 = new StoreProductObserver() {
            @Override
            public void updateFields(Product product) {
            }

            @Override
            public boolean stillInCart() {
                return false;
            }
        };
        observer2 = new StoreProductObserver() {
            @Override
            public void updateFields(Product product) {
            }

            @Override
            public boolean stillInCart() {
                return false;
            }
        };
    }
    @Test
    void CreateProduct_NullId() {
        Assertions.assertThrows(NullPointerException.class,()->p1 = new StoreProduct(null,"Milk",5,"Milk",5,"Its Milk what did you expect"));
    }
    @Test
    void CreateProduct_InvalidId() {
        Assertions.assertThrows(IllegalArgumentException.class,()->p1 = new StoreProduct(-1,"Milk",5,"Milk",5,"Its Milk what did you expect"));
    }
    @Test
    void CreateProduct_Nullname() {
        Assertions.assertThrows(NullPointerException.class,()->p1 = new StoreProduct(0,null,5,"Milk",5,"Its Milk what did you expect"));
    }
    @Test
    void CreateProduct_negativePrice() {
        Assertions.assertThrows(IllegalArgumentException.class,()->p1 = new StoreProduct(0,"Milk",-5,"Milk",5,"Its Milk what did you expect"));
    }
    @Test
    void CreateProduct_NegativeQuantity() {
        Assertions.assertThrows(IllegalArgumentException.class,()->p1 = new StoreProduct(0,"Milk",5,"Milk",-5,"Its Milk what did you expect"));
    }
    @Test
    void CreateProduct_NullCategory() {
        Assertions.assertThrows(NullPointerException.class,()->p1 = new StoreProduct(0,"Milk",5,null,5,"Its Milk what did you expect"));
    }
    @Test
    void CreateProduct_NullDesc() {
        Assertions.assertThrows(NullPointerException.class,()->p1 = new StoreProduct(0,"Milk",5,"Milk",5,null));
    }
    @Test
    public void testgetAverageRatingWithNoRatings() {
        double actualResult = p1.getAverageRating();
        double expectedResult = 0;
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testgetAverageRatingWithOneRating() {
        try {
            p1.EditRating("name", 3);
            double actualResult = p1.getAverageRating();
            double expectedResult = 3.0;
            Assertions.assertEquals(expectedResult, actualResult);
        }
        catch (Exception e)
        {//Should never happen
            System.out.println(e.getMessage());
            Assertions.assertTrue(false);
        }
    }

    @Test
    public void testgetAverageRatingWithMultipleRatings() {
        try {
            p1.EditRating("name", 3);
            p1.EditRating("name1", 3);
            p1.EditRating("name2", 3);
            double actualResult = p1.getAverageRating();
            double expectedResult = 3;
            Assertions.assertEquals(expectedResult, actualResult);
        }
        catch (Exception e)
        {//Should never happen
            System.out.println(e.getMessage());
            Assertions.assertTrue(false);
        }
    }

    @Test
    public void testUpdateQuantityWithPositiveQuantity() {
        p1.UpdateQuantity(3);
        int actualResult = p1.getQuantity();
        int expectedResult = 3;
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testUpdateQuantityWithZeroQuantity() {
        p1.UpdateQuantity(0);
        int actualResult = p1.getQuantity();
        int expectedResult = 0;
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testUpdateQuantityWithNegativeQuantity() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            p1.UpdateQuantity(-3);
        });
    }

    @Test
    public void testIncreaseQuantityWithPositiveQuantity() {
        p1.IncreaseQuantity(3);
        int actualResult = p1.getQuantity();
        int expectedResult = 8;
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIncreaseQuantityWithZeroQuantity() {
        p1.IncreaseQuantity(0);
        int actualResult = p1.getQuantity();
        int expectedResult = 5;
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIncreaseQuantityWithNegativeQuantity() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            p1.IncreaseQuantity(-3);
        });
    }

    @Test
    public void testEditRatingWithNewUser() throws Exception {
        p1.EditRating("John", 4);
        double actualResult = p1.getAverageRating();
        double expectedResult = 4.0;
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testEditRatingWithExistingUser() throws Exception {
        p1.EditRating("John", 4);
        p1.EditRating("John", 2);
        double actualResult = p1.getAverageRating();
        double expectedResult = 2.0;
        Assertions.assertEquals(expectedResult, actualResult);
    }
    @Test
    public void testEditRatingMultipleUsers() throws Exception {
        p1.addRatingAndComment("John", 4, "Great product!");
        p1.addRatingAndComment("Steve", 4.1, "Great product!");
        p1.addRatingAndComment("Calvin", 3.3, "Its ok");
        p1.addRatingAndComment("Dave", 5, "Amazing product!");

        double actualRating = p1.getAverageRating();
        double expectedRating = 4.1;
        Assertions.assertEquals(expectedRating, actualRating);
    }
    @Test
    public void testEditRatingAndCommentWithNewUser() throws Exception {
        p1.addRatingAndComment("John", 4, "Great product!");
        double actualRating = p1.getAverageRating();
        double expectedRating = 4.0;
        Assertions.assertEquals(expectedRating, actualRating);
    }

    @Test
    public void testEditRatingAndCommentWithExistingUser() throws Exception {
        p1.addRatingAndComment("John", 4, "Great product!");
        p1.addRatingAndComment("Steve", 2, "Not so good");
        double actualRating = p1.getAverageRating();
        double expectedRating = 3.0;
        Assertions.assertEquals(expectedRating, actualRating);
    }
    @Test
    public void testEditRatingAndCommentWithnullUsername() throws Exception {
       Assertions.assertThrows(NullPointerException.class,()-> p1.addRatingAndComment(null, 4, "Great product!"));
    }
    @Test
    public void testEditRatingAndCommentWithNegativeRating() throws Exception {
        Assertions.assertThrows(IllegalArgumentException.class,()-> p1.addRatingAndComment("John", -4, "Great product!"));
    }
    @Test
    public void testEditRatingAndCommentWithToHighRating() throws Exception {
        Assertions.assertThrows(IllegalArgumentException.class,()-> p1.addRatingAndComment("John", 40, "Great product!"));
    }
    @Test
    public void testAddObserver() {
        StoreProduct storeProduct = new StoreProduct(0,"Milk",5,"Milk",5,"Its Milk what did you expect");
        StoreProductObserver observer = new StoreProductObserver() {
            @Override
            public void updateFields(Product product) {

            }

            @Override
            public boolean stillInCart() {
                return false;
            }
        };
        storeProduct.addObserver(observer);
        Map<WeakReference<StoreProductObserver>, Object> observers = storeProduct.getObservers();
        Assertions.assertNotNull(observers);
        Assertions.assertEquals(1, observers.size());
        for (WeakReference<StoreProductObserver> observerRef : observers.keySet()) {
            assertEquals(observer, observerRef.get());
        }
    }
    @Test
    public void testAddObserverWithNullObserver() {
        StoreProduct storeProduct = new StoreProduct(0,"Milk",5,"Milk",5,"Its Milk what did you expect");
        storeProduct.addObserver(null);
        Map<WeakReference<StoreProductObserver>, Object> observers = storeProduct.getObservers();
        Assertions.assertNotNull(observers);
        Assertions.assertEquals(0, observers.size());
    }

    @Test
    public void testAddMultipleObservers() {
        StoreProduct product = new StoreProduct(0,"Milk",5,"Milk",5,"Its Milk what did you expect");
        product.addObserver(observer1);
        product.addObserver(observer2);

        Map<WeakReference<StoreProductObserver>, Object> observers = product.getObservers();
        Assertions.assertEquals(2, observers.size());
        Assertions.assertNotNull(observers);
    }
    @Test
    public void testNotifyObservers_RemoveNullObserver() {
        StoreProduct product = new StoreProduct(0,"Milk",5,"Milk",5,"Its Milk what did you expect");
        observer1 = new StoreProductObserver() {
            @Override
            public void updateFields(Product product) {

            }

            @Override
            public boolean stillInCart() {
                return true;
            }
        };
        observer2 = null;

        product.addObserver(observer1);
        product.addObserver(observer2);

        product.notifyObservers();

        Map<WeakReference<StoreProductObserver>, Object> observers = product.getObservers();
        Assertions.assertEquals(1, observers.size());
        Assertions.assertNotNull(observers);
        Assertions.assertFalse(observers.containsKey(new WeakReference<>(observer2)));
    }
    @Test
    public void testNotifyObservers_RemoveObserverNotInCart() {
        StoreProduct product = new StoreProduct(0,"Milk",5,"Milk",5,"Its Milk what did you expect");
        observer1 = new StoreProductObserver() {
            @Override
            public void updateFields(Product product) {

            }

            @Override
            public boolean stillInCart() {
                return false;
            }
        };
        observer2 = new StoreProductObserver() {
            @Override
            public void updateFields(Product product) {

            }

            @Override
            public boolean stillInCart() {
                return true;
            }
        };

        product.addObserver(observer1);
        product.addObserver(observer2);

        try {
            product.notifyObservers();

            Map<WeakReference<StoreProductObserver>, Object> observers = product.getObservers();
            Assertions.assertEquals(1, observers.size());
            Assertions.assertNotNull(observers);
            Assertions.assertFalse(observers.containsKey(new WeakReference<>(observer1)));
        }
        catch (Exception e)
        {

        }
    }
    @Test
    public void testNotifyObservers_UpdateFields() {
        StoreProduct product = new StoreProduct(0,"Milk",5,"Milk",5,"Its Milk what did you expect");
        final boolean[] updateFieldsCalled = {false};
        observer1 = new StoreProductObserver() {
            @Override
            public void updateFields(Product product) {
                updateFieldsCalled[0] = true;
            }

            @Override
            public boolean stillInCart() {
                return true;
            }
        };

        product.addObserver(observer1);

        product.notifyObservers();

        Assertions.assertTrue(updateFieldsCalled[0]);
    }
}