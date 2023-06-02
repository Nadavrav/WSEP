package UnitTests;

import DomainLayer.Users.RegisteredUser;
import DomainLayer.Users.SiteVisitor;
import org.junit.jupiter.api.Assertions;
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
            Assertions.assertTrue(false);//we shouldn't get here
        }
    }
    @Test
    void Register_LongName()
    {
        try {
            Assertions.assertThrows(IllegalArgumentException.class,()->{
                user = new RegisteredUser("This is an extremely long name that should be accepted by the program", "123456789");
            });
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            Assertions.assertTrue(false);//we shouldn't get here
        }
    }
    @Test
    void Register_NullName()
    {
        try {
            Assertions.assertThrows(NullPointerException.class,()->{
                user = new RegisteredUser(null, "123456789");
            });
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            Assertions.assertTrue(false);//we shouldn't get here
        }
    }
    @Test
    void Register_LongPassword()
    {
        try {
            Assertions.assertThrows(IllegalArgumentException.class,()->{
                user = new RegisteredUser("ValidUserName", "123456789123456789123456789123456789");
            });
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            Assertions.assertTrue(false);//we shouldn't get here
        }
    }
    @Test
    void Register_ShortPassword()
    {
        try {
            Assertions.assertThrows(IllegalArgumentException.class,()->{
                user = new RegisteredUser("ValidUserName", "123");
            });
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            Assertions.assertTrue(false);//we shouldn't get here
        }
    }
    @Test
    void Register_NullPassword()
    {
        try {
            Assertions.assertThrows(NullPointerException.class,()->{
                user = new RegisteredUser("ValidUserName", null);
            });
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            Assertions.assertTrue(false);//we shouldn't get here
        }
    }
    @Test
    void Login_GoodLogin()
    {
        try {
            int visitorId = 1;
            String password = "123456789";
            user = new RegisteredUser("ValidUserName", password);
            assertFalse(user.isLoggedIn());
            user.login(password,visitorId);
            assertTrue(user.isLoggedIn());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            Assertions.assertTrue(false);//we shouldn't get here
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
            assertFalse(user.isLoggedIn());
            Assertions.assertThrows(IllegalArgumentException.class,()->user.login(fakepassword,visitorId));
            assertFalse(user.isLoggedIn());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            Assertions.assertTrue(false);//we shouldn't get here
        }
    }
    @Test
    void Login_NullPassword()
    {
        try {
            int visitorId = 1;
            String password = "123456789";
            user = new RegisteredUser("ValidUserName", password);
            assertFalse(user.isLoggedIn());
            Assertions.assertThrows(NullPointerException.class,()->user.login(null,visitorId));
            assertFalse(user.isLoggedIn());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            Assertions.assertTrue(false);//we shouldn't get here
        }
    }
    @Test
    void Login_LoginTwice()
    {
        try {
            int visitorId = 1;
            String password = "123456789";
            user = new RegisteredUser("ValidUserName", password);
            assertFalse(user.isLoggedIn());
            user.login(password,visitorId);
            assertTrue(user.isLoggedIn());
            Assertions.assertThrows(Exception.class,()->user.login(password,visitorId));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            Assertions.assertTrue(false);//we shouldn't get here
        }
    }
}