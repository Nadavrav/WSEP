package UnitTests;

import Bridge.*;
import DomainLayer.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTests {

    Bridge bridge= Driver.getBridge();
    @Test
    public void RegisterFail_ExistingUserName()
    {
        Response r = bridge.Register("Username","12345678");
        Response r1 = bridge.Register("Username","12345678");
        assertTrue(r1.isError());
        assertEquals(r1.getMessage(),"This userName already taken");
    }
    @Test
    public void RegisterFail_InvalidPassword()
    {
        Response r = bridge.Register("Username","");
        assertTrue(r.isError());
        assertEquals(r.getMessage(),"password is too short");
    }
    @Test
    public void RegisterFail_BlackUsername()
    {
        Response r = bridge.Register("","12345678");
        assertTrue(r.isError());
        assertEquals(r.getMessage(),"Invalid Username");
    }
    @Test
    public void RegisterFail_IllegalCharacters()
    {
        Response r = bridge.Register("\nasd","12345678");
        assertTrue(r.isError());
        assertEquals(r.getMessage(),"Invalid Username");
    }
    @Test
    public void Login_Success()
    {
    }
    @Test
    public void Multiple_Logins_Success()
    {
    }
    @Test
    public void Login_Fail_NonexistentUsername()
    {
    }
    @Test
    public void Login_Fail_IncorrectPassword()
    {
    }
    @Test
    public void Login_Failed_UserAlreadyLoggedIn()
    {
    }
}
