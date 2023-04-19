package AcceptenceTests.UserTests;

import Bridge.*;
import DomainLayer.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class GuestTests {

    Bridge bridge= Driver.getBridge();
    public void RuntTests(){
        EnterStore();
        ExitStore();
        RegisterSuccess();
        RegisterFail_ExistingUserName();
        Login_Success();
        Login_Fail();
        Logout_Success();
        //TODO: MORE
    }
    @Test
    public void EnterStore()
    {
        Response r = bridge.EnterMarket();
        assertFalse(r.isError());
        bridge.ExitMarket();
    }
    @Test
    public void ExitStore()
    {
        bridge.EnterMarket();
        Response r = bridge.ExitMarket();
        assertFalse(r.isError());
    }
    @Test
    public void ExitStoreAndReEnter()
    {
        bridge.EnterMarket();

        Response r = bridge.ExitMarket();
        assertFalse(r.isError());
        Response r1 = bridge.EnterMarket();
        assertFalse(r1.isError());

        bridge.ExitMarket();
        //TODO: CHECK IF THE CART IS EMPTY
    }
    @Test
    public void RegisterSuccess()
    {
        bridge.EnterMarket();

        Response r = bridge.Register("Username","12345678");
        assertFalse(r.isError());
        assertEquals(r.getMessage(),"Success to register");

        bridge.ExitMarket();
    }
    @Test
    public void RegisterFail_ExistingUserName()
    {
        bridge.EnterMarket();

        Response r = bridge.Register("Username","12345678");
        assertFalse(r.isError());
        Response r1 = bridge.Register("Username","12345678");
        assertFalse(r1.isError());
        assertEquals(r1.getMessage(),"This userName already taken");

        bridge.ExitMarket();
    }
    @Test
    public void Login_Success()
    {
        bridge.EnterMarket();

        Response r = bridge.Register("Username","12345678");

        Response r1 = bridge.Login("Username","12345678");
        assertFalse(r1.isError());
        Response r3 = bridge.IsOnline("Username");
        assertTrue((boolean)r3.getValue());//Checks the user is online

        bridge.ExitMarket();
    }
    @Test
    public void Login_Fail()
    {
        bridge.EnterMarket();

        Response r = bridge.Register("Username","12345678");

        Response r1 = bridge.Login("Username","1234567");
        assertTrue(r1.isError());
        assertEquals(r1.getMessage(),"wrong password");

        bridge.ExitMarket();
    }
    @Test
    public void Logout_Success()
    {
        bridge.EnterMarket();

        Response r = bridge.Register("Username","12345678");
        Response r1 = bridge.Login("Username","12345678");

        Response r2 = bridge.IsOnline("Username");
        assertFalse((boolean)r2.getValue());//Checks the user is offline

        bridge.ExitMarket();
    }
}
