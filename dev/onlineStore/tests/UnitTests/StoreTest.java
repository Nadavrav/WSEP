package UnitTests;

import DomainLayer.Stores.Store;
import DomainLayer.Stores.StoreProduct;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    }

    @Test
    void searchProductByName() {
    }

    @Test
    void searchProductByCategory() {
    }

    @Test
    void searchProductByKey() {
    }

    @Test
    void addProduct() {
    }

    @Test
    void updateProductQuantity() {
    }

    @Test
    void increaseProductQuantity() {
    }

    @Test
    void updateProductName() {
    }

    @Test
    void updateProductPrice() {
    }

    @Test
    void updateProductCategory() {
    }

    @Test
    void updateProductDescription() {
    }

    @Test
    void addRating() {
    }

    @Test
    void addRatingAndComment() {
    }
}