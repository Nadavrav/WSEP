package UnitTests;

import DomainLayer.Users.RegisteredUser;
import DomainLayer.Users.SiteVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisteredUserTest {
    RegisteredUser user;


    @Test
    void Register_ValidInput()
    {
        try {
            user = new RegisteredUser("ValidUserName", "123456789");
            assertEquals("ValidUserName",user.getUserName());
           // assertEquals("123456789",user.getPassword()); //think it's bad to add hash/password getters
            user.login("123456789",10);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            assertTrue(false);//we shouldn't get here
        }
    }
    @Test
    void Register_LongName()
    {
        try {
            assertThrows(IllegalArgumentException.class,()->{
                user = new RegisteredUser("This is an extremely long name that should be accepted by the program", "123456789");
            });
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            assertTrue(false);//we shouldn't get here
        }
    }
    @Test
    void Register_ShortName()
    {
        try {
            assertThrows(IllegalArgumentException.class,()->{
                user = new RegisteredUser("Short", "123456789");
            });
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            assertTrue(false);//we shouldn't get here
        }
    }
    @Test
    void Register_NullName()
    {
        try {
            assertThrows(IllegalArgumentException.class,()->{
                user = new RegisteredUser(null, "123456789");
            });
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            assertTrue(false);//we shouldn't get here
        }
    }
    @Test
    void Register_LongPassword()
    {
        try {
            assertThrows(IllegalArgumentException.class,()->{
                user = new RegisteredUser("ValidUserName", "123456789123456789123456789123456789");
            });
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            assertTrue(false);//we shouldn't get here
        }
    }
    @Test
    void Register_ShortPassword()
    {
        try {
            assertThrows(IllegalArgumentException.class,()->{
                user = new RegisteredUser("ValidUserName", "123");
            });
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            assertTrue(false);//we shouldn't get here
        }
    }
    @Test
    void Register_NullPassword()
    {
        try {
            assertThrows(IllegalArgumentException.class,()->{
                user = new RegisteredUser("ValidUserName", null);
            });
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            assertTrue(false);//we shouldn't get here
        }
    }
    @Test
    void Login_GoodLogin()
    {
        try {
            int visitorId = 1;
            String password = "123456789";
            user = new RegisteredUser("ValidUserName", password);
            assertEquals(0,user.getVisitorId());
            user.login(password,visitorId);
            assertEquals(1,user.getVisitorId());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            assertTrue(false);//we shouldn't get here
        }
    }
    @Test
    void Login_IncorrectPassword()
    {
        try {
            int visitorId = 1;
            String password = "123456789";
            String fakepassword = "123";
            user = new RegisteredUser("ValidUserName", password);
            assertEquals(0,user.getVisitorId());
            assertThrows(IllegalArgumentException.class,()->user.login(fakepassword,visitorId));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            assertTrue(false);//we shouldn't get here
        }
    }
    @Test
    void Login_NullPassword()
    {
        try {
            int visitorId = 1;
            String password = "123456789";
            user = new RegisteredUser("ValidUserName", password);
            assertEquals(0,user.getVisitorId());
            assertThrows(IllegalArgumentException.class,()->user.login(null,visitorId));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            assertTrue(false);//we shouldn't get here
        }
    }
    @Test
    void Login_LoginTwice()
    {
        try {
            int visitorId = 1;
            String password = "123456789";
            user = new RegisteredUser("ValidUserName", password);
            assertEquals(0,user.getVisitorId());
            user.login(password,visitorId);
            assertThrows(Exception.class,()->user.login(password,visitorId));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            assertTrue(false);//we shouldn't get here
        }
    }
}