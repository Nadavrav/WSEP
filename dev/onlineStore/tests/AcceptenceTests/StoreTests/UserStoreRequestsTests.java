package AcceptenceTests.StoreTests;

import Bridge.Bridge;
import Bridge.Driver;

import DomainLayer.Response;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserStoreRequestsTests {
    private Bridge bridge= Driver.getBridge();
    private final String user1="Karen";
    private final String user2="Bob";
    private final String user3="Bob's son";
    private final String user4="Jeff"; //NOT LOGGED IN
    private final String user5="Jeoff"; //NOT REGISTERED
    private final String store1="Bob's milk emporium";
    private final String store2="non-existent bakery";
    @DisplayName("Running Setup functions, any errors here will break the rest of the tests!")
    @BeforeAll
    public void SetUp(){
        bridge=Driver.getBridge();
        registerAndLoginUsers();
        assertFalse(bridge.initialize().isError());


    }
    public void registerAndLoginUsers(){
        //TODO: REGISTER AND LOGIN A BUNCH OF USERS
        //pass min 8
    }
    @Order(1)
    @DisplayName("Running opening store tests")
    @Test
    public void OpenStoreTests(){
//        assertFalse(bridge.OpenNewStore(id2,store1).isError()); //OK
//        assertTrue(bridge.OpenNewStore(id4,store2).isError()); //not logged in
//        assertTrue(bridge.OpenNewStore(id5,store2).isError()); //not registered
//        assertTrue(bridge.OpenNewStore(id1,store2).isError()); //store exists
//        assertTrue(bridge.OpenNewStore(id1,"").isError()); //no name
    }
    @Order(2)
    @DisplayName("Running add product tests")
    @Test
    public void AddProductTest(){
//        assertFalse(bridge.AddProduct("Mega milk","Guaranteed to make bones stronger!",100,100).isError()); //Ok
//        assertFalse(bridge.AddProduct("ultra milk","bones made of metal now!",100,100).isError()); //Ok
//        assertFalse(bridge.AddProduct(id2,"giga milk","bones made of diamond now!",100,100).isError()); //Ok
//        assertFalse(bridge.AddProduct(id2,"quantum milk","makes bones grow another dimension!",100,100).isError()); //Ok
//        assertTrue(bridge.AddProduct(id2,"quantum milk","makes bones grow another dimension!",100,100).isError()); //duplicate product
//        assertTrue(bridge.AddProduct(id1,"Karen's milk","stub").isError()); //non employee tries to add product
//        assertTrue(bridge.AddProduct(id2,"","stub",100,100).isError()); //empty name
//        assertTrue(bridge.AddProduct(id2,"stub","",100,100).isError()); //empty desc
//        assertTrue(bridge.AddProduct(id4,"stub","stub",100,100).isError()); //not logged in
//        assertTrue(bridge.AddProduct(id5,"stub","stub",100,100).isError()); //not registered
    }
    @Order(3)
    @DisplayName("Store and product search tests")
    @Test
    public void StoreAndProductSearchTests(){
        String OkQuery="Mega milk"; String emptyQuery=""; String jibbrish="sdghajskdhaskdhasdcakl";
        //TODO: NEED TO ESTABLISH AN APPROXIMATE RETURN TYPE IN RESPONSE TO VERIFY SEARCH CORRECTNESS
    }
}
