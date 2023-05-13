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
        p1 = new StoreProduct(1,"Milk",5,"Milk",20,"Its Milk what did you expect");
        p2 = new StoreProduct(1,"Butter",3.4,"Butter",6,"A Golden Brick");
    }
    @Test
    void closeStore() {

        s.CloseStore();
        assertFalse(s.getActive());
    }

    @Test
    void addNewProduct() {
        Integer pid = s.AddNewProduct(p1Name,p1Price,p1Quan,p1Cate,p1Desc);
        StoreProduct actual = s.getProducts().get(pid);
        assertNotNull(actual);//Make this check if the product was actually added
        assertEquals(p1.getName(),actual.getName());
        assertEquals(p1.getPrice(),actual.getPrice());
        assertEquals(p1.getCategory(),actual.getCategory());
        assertEquals(p1.getQuantity(),actual.getQuantity());
        assertEquals(p1.getDescription(),actual.getDescription());
    }
    @Test
    void addNewProductMultiple() {
        Integer pid1 = s.AddNewProduct(p1Name,p1Price,p1Quan,p1Cate,p1Desc);
        Integer pid2 = s1.AddNewProduct(p2Name,p2Price,p2Quan,p2Cate,p2Desc);
        StoreProduct actual1 = s.getProducts().get(pid1);
        StoreProduct actual2 = s.getProducts().get(pid2);
        assertNotNull(actual1);
        assertEquals(p1.getName(),actual1.getName());
        assertEquals(p1.getPrice(),actual1.getPrice());
        assertEquals(p1.getCategory(),actual1.getCategory());
        assertEquals(p1.getQuantity(),actual1.getQuantity());
        assertEquals(p1.getDescription(),actual1.getDescription());
        assertNotNull(actual2);
        assertEquals(p1.getName(),actual2.getName());
        assertEquals(p1.getPrice(),actual2.getPrice());
        assertEquals(p1.getCategory(),actual2.getCategory());
        assertEquals(p1.getQuantity(),actual2.getQuantity());
        assertEquals(p1.getDescription(),actual2.getDescription());
    }

    @Test
    void removeProduct() {
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
        Integer pid1 = s.AddNewProduct(p1Name,p1Price,p1Quan,p1Cate,p1Desc);
        Integer pid2 = s1.AddNewProduct(p2Name,p2Price,p2Quan,p2Cate,p2Desc);
        StoreProduct actual1 = s.getProducts().get(pid1);
        StoreProduct actual2 = s1.getProducts().get(pid2);
        LinkedList<StoreProduct> searchResults = s.SearchProductByName(p1Name);
        assertTrue(searchResults.contains(actual1));
        assertFalse(searchResults.contains(actual2));
    }

    @Test
    void updateProductQuantity() {
        p1.setQuantity(3);
        p2.setQuantity(5);
        assertEquals(3,p1.getQuantity());
        assertEquals(5,p2.getQuantity());
    }

    @Test
    void increaseProductQuantity() {
     int save = p1.getQuantity();
     int save1 = p2.getQuantity();
     p1.IncreaseQuantity(3);
     p2.IncreaseQuantity(4);
     assertEquals(save+3,p1.getQuantity());
     assertEquals(save1+4,p2.getQuantity());
    }

    @Test
    void updateProductName() {
        p1.setName("Milk1");
        p2.setName("Butter1");
        assertEquals("Milk1",p1.getName());
        assertEquals("Butter1",p2.getName());
    }

    @Test
    void updateProductPrice() {
        p1.setPrice(5.6);
        p2.setPrice(12.7);
        assertEquals(5.6,p1.getPrice());
        assertEquals(12.7,p2.getPrice());
    }

    @Test
    void updateProductCategory() {
        p1.setCategory("Milk1");
        p2.setCategory("Butter1");
        assertEquals("Milk1",p1.getCategory());
        assertEquals("Butter1",p2.getCategory());
    }

    @Test
    void updateProductDescription() {
        p1.setDescription("cant you see its a milk");
        p2.setDescription("Butter for lover");
        assertEquals("cant you see its a milk",p1.getDescription());
        assertEquals("Butter for lover",p2.getDescription());
    }

    @Test
    void addRating() throws Exception {
        p1.EditRating("majd",4);
        p1.EditRating("natalie",2);
        assertEquals(3,p1.getAverageRating());
        p2.EditRating("majd",5);
        p2.EditRating("natalie",3);
        assertEquals(4,p2.getAverageRating());

    }

    @Test
    void addRatingAndComment() throws Exception {
        p1.addRatingAndComment("majd",4,"its so delicious");
        p1.addRatingAndComment("natalie",1,"to fats");
        Rating rate= new Rating(4,"its so delicious");
        Rating rate1= new Rating(1,"to fats");
        assertEquals(rate.getRating(),p1.getRateMap().get("majd").getRating());
        assertEquals(rate.getComment(),p1.getRateMap().get("majd").getComment());
        assertEquals(rate1.getRating(),p1.getRateMap().get("natalie").getRating());
        assertEquals(rate1.getComment(),p1.getRateMap().get("natalie").getComment());
    }
    @Test
    void CreateProduct_Nullname() {
        assertThrows(NullPointerException.class,()->s.AddNewProduct(null,5.0,5,"Milk","Its Milk what did you expect"));
    }
    @Test
    void CreateProduct_negativePrice() {
        assertThrows(IllegalArgumentException.class,()->s.AddNewProduct("Milk",-5.0,5,"Milk","Its Milk what did you expect"));
    }
    @Test
    void CreateProduct_NegativeQuantity() {
        assertThrows(IllegalArgumentException.class,()->s.AddNewProduct("Milk",5.0,-5,"Milk","Its Milk what did you expect"));
    }
    @Test
    void CreateProduct_NullCategory() {
        assertThrows(NullPointerException.class,()->s.AddNewProduct("Milk",5.0,5,null,"Its Milk what did you expect"));
    }
    @Test
    void CreateProduct_NullDesc() {
        assertThrows(NullPointerException.class,()->s.AddNewProduct("Milk",5.0,5,"Milk",null));
    }
}