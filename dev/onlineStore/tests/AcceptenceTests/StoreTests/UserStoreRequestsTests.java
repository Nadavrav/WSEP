package AcceptenceTests.StoreTests;

import Bridge.Bridge;
import Bridge.Driver;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserStoreRequestsTests {
    private Bridge bridge= Driver.getBridge();
    private final String user1="Karen"; //your favorite customer
    private final String user2="Bob"; //a store founder
    private final String user3="Bob's owner son"; //a store owner
    private final String user4="Bob's manager son"; //a store manager
    private final String user5 ="Jeff"; //NOT LOGGED IN
    private final String user6 ="jeff's nemesis"; //NOT REGISTERED
    private final String user7="Robert"; //another store owner
    private final String store1="Bob's milk emporium";
    private final String store3="Robert's yogurt castle";
    private final String store2="non-existent bakery";
    @DisplayName("Running Setup functions, any errors here will break the rest of the tests!")
    @BeforeAll
    public void SetUp(){
        bridge=Driver.getBridge();
        registerUsers();
        assertFalse(bridge.initialize().isError());


    }
    public void registerUsers(){
        //TODO: REGISTER A BUNCH OF USERS ACCORDING TO ABOVE FIELD
        //pass min 8
    }
    @Order(1)
    @Test
    public void testUser1(){
        assertFalse();
        assertFalse(bridge.OpenNewStore(store1).isError()); //OK
        assertTrue(bridge.OpenNewStore(id4,store2).isError()); //not logged in
        assertTrue(bridge.OpenNewStore(id5,store2).isError()); //not registered
        assertTrue(bridge.OpenNewStore(id1,store2).isError()); //store exists
        assertTrue(bridge.OpenNewStore(id1,"").isError()); //no name
    }
    @Order(2)
    @Test
    public void AddProductTest(){
        assertFalse(bridge.AddProduct("Mega milk","Guaranteed to make bones stronger!",100,100).isError()); //Ok
        assertFalse(bridge.AddProduct("ultra milk","bones made of metal now!",100,100).isError()); //Ok
        assertFalse(bridge.AddProduct(id2,"giga milk","bones made of diamond now!",100,100).isError()); //Ok
        assertFalse(bridge.AddProduct(id2,"quantum milk","makes bones grow another dimension!",100,100).isError()); //Ok
        assertTrue(bridge.AddProduct(id2,"quantum milk","makes bones grow another dimension!",100,100).isError()); //duplicate product
        assertTrue(bridge.AddProduct(id1,"Karen's milk","stub").isError()); //non employee tries to add product
        assertTrue(bridge.AddProduct(id2,"","stub",100,100).isError()); //empty name
        assertTrue(bridge.AddProduct(id2,"stub","",100,100).isError()); //empty desc
        assertTrue(bridge.AddProduct(id4,"stub","stub",100,100).isError()); //not logged in
        assertTrue(bridge.AddProduct(id5,"stub","stub",100,100).isError()); //not registered
    }
    @Order(3)
    @DisplayName("Store and product search tests")
    @Test
    public void StoreAndProductSearchTests(){
        String OkQuery="Mega milk"; String emptyQuery=""; String jibbrish="sdghajskdhaskdhasdcakl";
    }
}
