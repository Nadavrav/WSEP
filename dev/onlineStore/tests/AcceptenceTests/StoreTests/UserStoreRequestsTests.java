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
    private  int id1=0, id2=0,id3=0,id4=0,id5=-1; //TODO: GET FROM LOGIN/REGISTER?
    @BeforeAll
    public void SetUp(){
        bridge=Driver.getBridge();
        registerAndLoginUsers();
    }
    public void registerAndLoginUsers(){
        //TODO: WHEN LOGIN BRIDGE IS DONE. CAN MULTIPLE PEOPLE BE LOGGED IN AT THE SAME TIME?
    }
    @Order(1)
    @DisplayName("Running opening store tests")
    @Test
    public void OpenStoreTests(){
        assertFalse(bridge.OpenNewStore(id2,store1).isError()); //OK
        assertTrue(bridge.OpenNewStore(id4,store2).isError()); //not logged in
        assertTrue(bridge.OpenNewStore(id5,store2).isError()); //not registered
        assertTrue(bridge.OpenNewStore(id1,store2).isError()); //store exists
        assertTrue(bridge.OpenNewStore(id1,"").isError()); //no name
    }
    @Order(2)
    @DisplayName("Running add product tests")
    @Test
    public void AddProductTest(){
        assertFalse(bridge.AddProduct(id2,"Mega milk","Guaranteed to make bones stronger!").isError()); //Ok
        assertFalse(bridge.AddProduct(id2,"ultra milk","bones made of metal now!").isError()); //Ok
        assertFalse(bridge.AddProduct(id2,"giga milk","bones made of diamond now!").isError()); //Ok
        assertFalse(bridge.AddProduct(id2,"quantum milk","makes bones grow another dimension!").isError()); //Ok
        assertTrue(bridge.AddProduct(id2,"quantum milk","makes bones grow another dimension!").isError()); //duplicate product
        assertTrue(bridge.AddProduct(id1,"Karen's milk","stub").isError()); //non employee tries to add product
        assertTrue(bridge.AddProduct(id2,"","stub").isError()); //empty name
        assertTrue(bridge.AddProduct(id2,"stub","").isError()); //empty desc
        assertTrue(bridge.AddProduct(id4,"stub","stub").isError()); //not logged in
        assertTrue(bridge.AddProduct(id5,"stub","stub").isError()); //not registered
    }
    @Order(3)
    @DisplayName("Store and product search tests")
    @Test
    public void StoreAndProductSearchTests(){
        String OkQuery="Mega milk"; String emptyQuery=""; String jibbrish="sdghajskdhaskdhasdcakl";
        //TODO: NEED TO ESTABLISH A RETURN TYPE IN RESPONSE TO VERIFY SEARCH CORRECTNESS
    }
}
