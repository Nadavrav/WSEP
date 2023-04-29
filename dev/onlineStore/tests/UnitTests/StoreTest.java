package UnitTests;

import DomainLayer.Stores.Rating;
import DomainLayer.Stores.Store;
import DomainLayer.Stores.Products.StoreProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class StoreTest {
    StoreProduct p1,p2;
    String p1Name="Milk";
    String p2Name="Butter";
    String p1Cate="Milk";
    String p2Cate="Butter";
    String p1Desc="Its Milk what did you expect";
    String p2Desc="A Golden Brick";

    double p1Price = 5;
    double p2Price = 3.4;
    int p1Quan = 20;
    int p2Quan = 6;
    Store s=new Store("Store1");
    Store s1=new Store("Store2");
    @BeforeEach
    void setup()
    {
        p1 = new StoreProduct("1-0","Milk",5,"Milk",20,"Its Milk what did you expect");
        p2 = new StoreProduct("1-1","Butter",3.4,"Butter",6,"A Golden Brick");
    }
    @Test
    void closeStore() {

        s.CloseStore();
        assertFalse(s.getActive());
    }

    @Test
    void addNewProduct() {
        s.AddNewProduct(p1Name,p1Price,p1Quan,p1Cate,p1Desc);
        StoreProduct actual = s.getProducts().get("1-0");
        assertEquals(p1,actual);//Make this check if the product was actually added
    }
    @Test
    void addNewProductMultiple() {
        s.AddNewProduct(p1Name,p1Price,p1Quan,p1Cate,p1Desc);
        s1.AddNewProduct(p2Name,p2Price,p2Quan,p2Cate,p2Desc);
        StoreProduct actual1 = s.getProducts().get("1-0");
        assertEquals(p1,actual1);//Make this check if the product was actually added
        StoreProduct actual2 = s.getProducts().get("1-1");
        assertEquals(p2,actual2);//Make this check if the product was actually added
    }

    @Test
    void removeProduct() {
        setup();
        s.RemoveProduct(p1.getProductId());
        s1.RemoveProduct(p1.getProductId());
        s.RemoveProduct(p2.getProductId());
        s1.RemoveProduct(p2.getProductId());
        // check if  products removed
        assertFalse(s.getProducts().values().contains(p1));
        assertFalse(s1.getProducts().values().contains(p1));
        assertFalse(s.getProducts().values().contains(p2));
        assertFalse(s1.getProducts().values().contains(p2));

    }

    @Test
    void searchProductByName() throws Exception {
        setup();
        LinkedList<StoreProduct> searchResults = s.SearchProductByName(p1Name);
        assertTrue(searchResults.contains(p1));
        assertFalse(searchResults.contains(p2));
        s.setActive(false);
        searchResults = s.SearchProductByName(p1Name);
        assertEquals(0, searchResults.size());
        // for s1
        LinkedList<StoreProduct> searchResults1 = s1.SearchProductByName(p1Name);
        assertTrue(searchResults1.contains(p1));
        assertFalse(searchResults1.contains(p2));
        s.setActive(false);
        searchResults1 = s1.SearchProductByName(p1Name);
        assertEquals(0, searchResults1.size());
    }

    @Test
    void searchProductByCategory() {
        setup();
        try {
            LinkedList<StoreProduct> searchResults = s.SearchProductByCategory(p1Cate);
            assertTrue(searchResults.contains(p1));
            assertFalse(searchResults.contains(p2));
            s.setActive(false);
            searchResults = s.SearchProductByCategory(p1Cate);
            assertEquals(0, searchResults.size());
            // for s1
            LinkedList<StoreProduct> searchResults1 = s1.SearchProductByCategory(p1Cate);
            assertTrue(searchResults1.contains(p1));
            assertFalse(searchResults1.contains(p2));
            s.setActive(false);
            searchResults1 = s1.SearchProductByCategory(p1Cate);
            assertEquals(0, searchResults1.size());
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    void searchProductByKey() {
        setup();
        try {
            LinkedList<StoreProduct> searchResults = (LinkedList<StoreProduct>) s.SearchProductByKey("Milk");
            assertTrue(searchResults.contains(p1));
            assertFalse(searchResults.contains(p2));
            s.setActive(false);
            searchResults = (LinkedList<StoreProduct>) s.SearchProductByKey("Milk");
            assertEquals(0, searchResults.size());
            // for s1
            LinkedList<StoreProduct> searchResults1 = (LinkedList<StoreProduct>) s1.SearchProductByKey("Milk");
            assertTrue(searchResults1.contains(p1));
            assertFalse(searchResults1.contains(p2));
            s.setActive(false);
            searchResults1 = (LinkedList<StoreProduct>) s1.SearchProductByKey("Milk");
            assertEquals(0, searchResults1.size());
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    // we have a add new product method
    void addProduct() {
    }

    @Test
    void updateProductQuantity() {
        setup();
        p1.setQuantity(3);
        p2.setQuantity(5);
        assertEquals(3,p1.getQuantity());
        assertEquals(5,p2.getQuantity());
    }

    @Test
    void increaseProductQuantity() {
        setup();
     int save = p1.getQuantity();
     int save1 = p2.getQuantity();
     p1.IncreaseQuantity(3);
     p2.IncreaseQuantity(4);
     assertEquals(save+3,p1.getQuantity());
     assertEquals(save1+4,p2.getQuantity());
    }

    @Test
    void updateProductName() {
        setup();
        p1.setName("Milk1");
        p2.setName("Butter1");
        assertEquals("Milk1",p1.getName());
        assertEquals("Butter1",p2.getName());
    }

    @Test
    void updateProductPrice() {
        setup();
        p1.setPrice(5.6);
        p2.setPrice(12.7);
        assertEquals(5.6,p1.getPrice());
        assertEquals(12.7,p2.getPrice());
    }

    @Test
    void updateProductCategory() {
        setup();
        p1.setCategory("Milk1");
        p2.setCategory("Butter1");
        assertEquals("Milk1",p1.getCategory());
        assertEquals("Butter1",p2.getCategory());
    }

    @Test
    void updateProductDescription() {
        setup();
        p1.setDescription("cant you see its a milk");
        p2.setDescription("Butter for lover");
        assertEquals("cant you see its a milk",p1.getDescription());
        assertEquals("Butter for lover",p2.getDescription());
    }

    @Test
    void addRating() throws Exception {
        setup();
        p1.addRating("majd",4);
        p1.addRating("natalie",2);
        assertEquals(3,p1.getRating());
        p2.addRating("majd",5);
        p2.addRating("natalie",3);
        assertEquals(4,p2.getRating());

    }

    @Test
    void addRatingAndComment() throws Exception {
        setup();
        p1.addRatingAndComment("majd",4,"its so delicious");
        p1.addRatingAndComment("natalie",1,"to fats");
        Rating rate= new Rating(4,"its so delicious");
        Rating rate1= new Rating(1,"to fats");
        assertEquals(rate,p1.getRateMap().get("majd"));
        assertEquals(rate1,p1.getRateMap().get("natalie"));
    }
}