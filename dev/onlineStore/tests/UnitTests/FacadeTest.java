package UnitTests;

import DomainLayer.Facade;
import DomainLayer.Users.Permission;
import DomainLayer.Users.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FacadeTest {
    Facade f = Facade.getInstance();
    @BeforeEach
    void setup()
    {
        f = Facade.getInstance();
    }
    @AfterEach
    void teardown()
    {
        f.resetData();
    }
    @Test
    void enterNewSiteVisitor() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            assertTrue(f.getOnlineList().get(visitorId)!=null);
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void enterNewSiteVisitorTwice() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            assertTrue(f.getOnlineList().get(visitorId)!=null);
            int visitorId2 = f.EnterNewSiteVisitor();
            assertTrue(f.getOnlineList().get(visitorId2)!=null);
            
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void exitSiteVisitor() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            f.ExitSiteVisitor(visitorId);
            assertTrue(f.getOnlineList().get(visitorId)==null);

        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void exitSiteVisitorExitTwice() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            f.ExitSiteVisitor(visitorId);
            assertTrue(f.getOnlineList().get(visitorId)==null);
            //TODO CHECK WHY EXCEPTUION WASNT THROWN
            assertThrows(Exception.class,()->f.ExitSiteVisitor(visitorId));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }

    @Test
    void register_OkData() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            f.Register(visitorId,Username,"123456789");
            assertTrue(f.getRegisteredUserList().get(Username)!=null);
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void register_nullUsername() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = null;
            assertThrows(IllegalArgumentException.class,()->f.Register(visitorId,Username,"123456789"));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void register_nullPassword() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            assertThrows(IllegalArgumentException.class,()->f.Register(visitorId,Username,null));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void register_sameUsername() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            f.Register(visitorId,Username,"123456789");
            assertTrue(f.getRegisteredUserList().get(Username)!=null);
            assertThrows(Exception.class,()->f.Register(visitorId,Username,"123456789"));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void register_badPassword() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            assertThrows(IllegalArgumentException.class,()->f.Register(visitorId,Username,"123"));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void register_badVisitorId() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            assertThrows(Exception.class,()->f.Register(1000,Username,"123456789"));//TODO CHECK WHY NOT THROWN
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void login_OkData() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            assertTrue(f.getOnlineList().get(visitorId)!=null);
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void login_nullUsername() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            f.Register(visitorId,Username,password);
            assertThrows(Exception.class,()->f.login(visitorId,null,password));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void login_nullPassword() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            f.Register(visitorId,Username,password);
            assertThrows(Exception.class,()->f.login(visitorId,Username,null));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void login_BadVisitorId() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            f.Register(visitorId,Username,password);
            assertThrows(Exception.class,()->f.login(100,Username,password));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void login_Twice() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            assertTrue(f.getOnlineList().get(visitorId)!=null);
            assertThrows(Exception.class,()->f.login(visitorId,Username,password));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void logout_ok() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            assertTrue(f.getOnlineList().get(visitorId)!=null);
            f.logout(visitorId);
            assertTrue(f.getOnlineList().get(visitorId)!=null);
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void logout_notRegistered() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            assertThrows(Exception.class,()->f.logout(visitorId));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void logout_notLogedIn() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            f.Register(visitorId,Username,password);
            assertThrows(Exception.class,()->f.logout(visitorId));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void logout_InvalidVisitorId() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            f.Register(visitorId,Username,password);
            assertThrows(Exception.class,()->f.logout(100));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void addProductToCart_ok() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            int pid =f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.addProductToCart(pid,storeId,visitorId);
            String actual = f.getProductsInMyCart(visitorId);
            assertEquals("Store Id : 33\n" +
                    "Product Id: 33-0 ,Product Name: Milk ,Product Price: 5.0\n",actual);
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void addProductToCart_MultipleProducts() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            String pName2 = "Bread";
            double pPrice2 = 6.0;
            String pCat2 = "Bread";
            int pQuan2 = 10;
            String pDesc2 = "Bread";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            int pid1=f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            int pid2=f.AddProduct(visitorId,storeId,pName2,pPrice2,pCat2,pQuan2,pDesc2);
            f.addProductToCart(pid1,storeId,visitorId);
            f.addProductToCart(pid2,storeId,visitorId);
            String actual = f.getProductsInMyCart(visitorId);
            assertEquals("Store Id : 37\n" +
                    "Product Id: 37-1 ,Product Name: Bread ,Product Price: 6.0\n" +
                    "Product Id: 37-0 ,Product Name: Milk ,Product Price: 5.0\n",actual);
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void addProductToCart_BadProductId() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            String pName2 = "Bread";
            double pPrice2 = 6.0;
            String pCat2 = "Bread";
            int pQuan2 = 10;
            String pDesc2 = "Bread";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.AddProduct(visitorId,storeId,pName2,pPrice2,pCat2,pQuan2,pDesc2);
            assertThrows(Exception.class,()->f.addProductToCart(0,1000,visitorId));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void addProductToCart_BadVisitorId() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            String pName2 = "Bread";
            double pPrice2 = 6.0;
            String pCat2 = "Bread";
            int pQuan2 = 10;
            String pDesc2 = "Bread";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.AddProduct(visitorId,storeId,pName2,pPrice2,pCat2,pQuan2,pDesc2);
            assertThrows(Exception.class,()->f.addProductToCart(0,0,1000));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void appointNewStoreOwner_ok() {
        try {
            int visitorId = f.EnterNewSiteVisitor();//Enter Site
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            f.Register(visitorId,Username2,password2);//Register 2 user
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);//register first user
            f.login(visitorId,Username,password);//login first user
            int storeId = f.OpenNewStore(visitorId,"MyStore");// open a new store
            f.appointNewStoreOwner(visitorId,Username2,storeId);// appoint use2 to be store manager of store 1 which user 1 is the founder of
            assertEquals(Role.StoreOwner,f.getEmploymentList().get(Username).get(storeId).getRole());
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void appointNewStoreOwner_AppointTwice() {
        try {
            int visitorId = f.EnterNewSiteVisitor();//Enter Site
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            f.Register(visitorId,Username2,password2);//Register 2 user
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);//register first user
            f.login(visitorId,Username,password);//login first user
            int storeId = f.OpenNewStore(visitorId,"MyStore");// open a new store
            f.appointNewStoreOwner(visitorId,Username2,storeId);// appoint use2 to be store manager of store 1 which user 1 is the founder of
            assertEquals(Role.StoreOwner,f.getEmploymentList().get(Username).get(storeId).getRole());
            assertThrows(Exception.class,()->f.appointNewStoreOwner(visitorId,Username2,storeId));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void appointNewStoreOwner_notAllowedToAppoint() {
        try {
            int visitorId = f.EnterNewSiteVisitor();//Enter Site
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            f.Register(visitorId,Username2,password2);//Register 2 user
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);//register first user
            f.login(visitorId,Username,password);//login first user
            int storeId = f.OpenNewStore(visitorId,"MyStore");// open a new store
            f.logout(visitorId);
            f.login(visitorId,Username2,password2);
            assertThrows(Exception.class,()->f.appointNewStoreOwner(visitorId,Username2,storeId));// appoint use2 to be store manager of store 1 which user 1 is the founder of
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void appointNewStoreOwner_badId() {
        try {
            int visitorId = f.EnterNewSiteVisitor();//Enter Site
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            f.Register(visitorId,Username2,password2);//Register 2 user
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);//register first user
            f.login(visitorId,Username,password);//login first user
            int storeId = f.OpenNewStore(visitorId,"MyStore");// open a new store
            assertThrows(Exception.class,()->f.appointNewStoreOwner(100,Username2,storeId));// appoint use2 to be store manager of store 1 which user 1 is the founder of
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void appointNewStoreOwner_badStoreId() {
        try {
            int visitorId = f.EnterNewSiteVisitor();//Enter Site
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            f.Register(visitorId,Username2,password2);//Register 2 user
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);//register first user
            f.login(visitorId,Username,password);//login first user
            int storeId = f.OpenNewStore(visitorId,"MyStore");// open a new store
            assertThrows(Exception.class,()->f.appointNewStoreOwner(visitorId,Username2,100));// appoint use2 to be store manager of store 1 which user 1 is the founder of
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void appointNewStoreOwner_closedStore() {
        try {
            int visitorId = f.EnterNewSiteVisitor();//Enter Site
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            f.Register(visitorId,Username2,password2);//Register 2 user
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);//register first user
            f.login(visitorId,Username,password);//login first user
            //TODO CHECK WHY STORE OWNER DOESNT GET SAVED
            int storeId = f.OpenNewStore(visitorId,"MyStore");// open a new store
            f.CloseStore(visitorId,storeId);
            assertThrows(Exception.class,()->f.appointNewStoreOwner(visitorId,Username2,storeId));// appoint use2 to be store manager of store 1 which user 1 is the founder of
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void appointNewStoreOwner_NonexistentUsername() {
        try {
            int visitorId = f.EnterNewSiteVisitor();//Enter Site
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            f.Register(visitorId,Username2,password2);//Register 2 user
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);//register first user
            f.login(visitorId,Username,password);//login first user
            //TODO CHEKC WHY STORE OWNER DOESNT GET SAVED
            int storeId = f.OpenNewStore(visitorId,"MyStore");// open a new store
            f.CloseStore(visitorId,storeId);
            assertThrows(Exception.class,()->f.appointNewStoreOwner(visitorId,"FakeUsername",storeId));// appoint use2 to be store manager of store 1 which user 1 is the founder of
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void appointNewStoreManager_ok() {
        try {
            int visitorId = f.EnterNewSiteVisitor();//Enter Site
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            f.Register(visitorId,Username2,password2);//Register 2 user
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);//register first user
            f.login(visitorId,Username,password);//login first user
            int storeId = f.OpenNewStore(visitorId,"MyStore");// open a new store
            f.appointNewStoreManager(visitorId,Username2,storeId);// appoint use2 to be store manager of store 1 which user 1 is the founder of
            assertEquals(Role.StoreOwner,f.getEmploymentList().get(Username).get(storeId).getRole());
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void appointNewStoreManager_AppointTwice() {
        try {
            int visitorId = f.EnterNewSiteVisitor();//Enter Site
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            f.Register(visitorId,Username2,password2);//Register 2 user
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);//register first user
            f.login(visitorId,Username,password);//login first user
            int storeId = f.OpenNewStore(visitorId,"MyStore");// open a new store
            f.appointNewStoreManager(visitorId,Username2,storeId);// appoint use2 to be store manager of store 1 which user 1 is the founder of
            assertEquals(Role.StoreOwner,f.getEmploymentList().get(Username).get(storeId).getRole());
            assertThrows(Exception.class,()->f.appointNewStoreManager(visitorId,Username2,storeId));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void appointNewStoreManager_notAllowedToAppoint() {
        try {
            int visitorId = f.EnterNewSiteVisitor();//Enter Site
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            f.Register(visitorId,Username2,password2);//Register 2 user
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);//register first user
            f.login(visitorId,Username,password);//login first user
            int storeId = f.OpenNewStore(visitorId,"MyStore");// open a new store
            f.logout(visitorId);
            f.login(visitorId,Username2,password2);
            assertThrows(Exception.class,()->f.appointNewStoreManager(visitorId,Username2,storeId));// appoint use2 to be store manager of store 1 which user 1 is the founder of
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void appointNewStoreManager_badId() {
        try {
            int visitorId = f.EnterNewSiteVisitor();//Enter Site
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            f.Register(visitorId,Username2,password2);//Register 2 user
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);//register first user
            f.login(visitorId,Username,password);//login first user
            int storeId = f.OpenNewStore(visitorId,"MyStore");// open a new store
            assertThrows(Exception.class,()->f.appointNewStoreManager(100,Username2,storeId));// appoint use2 to be store manager of store 1 which user 1 is the founder of
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void appointNewStoreManager_badStoreId() {
        try {
            int visitorId = f.EnterNewSiteVisitor();//Enter Site
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            f.Register(visitorId,Username2,password2);//Register 2 user
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);//register first user
            f.login(visitorId,Username,password);//login first user
            int storeId = f.OpenNewStore(visitorId,"MyStore");// open a new store
            assertThrows(Exception.class,()->f.appointNewStoreManager(visitorId,Username2,100));// appoint use2 to be store manager of store 1 which user 1 is the founder of
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void appointNewStoreManager_closedStore() {
        try {
            int visitorId = f.EnterNewSiteVisitor();//Enter Site
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            f.Register(visitorId,Username2,password2);//Register 2 user
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);//register first user
            f.login(visitorId,Username,password);//login first user
            //TODO CHECK WHY STORE OWNER DOESNT GET SAVED
            int storeId = f.OpenNewStore(visitorId,"MyStore");// open a new store
            f.CloseStore(visitorId,storeId);
            assertThrows(Exception.class,()->f.appointNewStoreManager(visitorId,Username2,storeId));// appoint use2 to be store manager of store 1 which user 1 is the founder of
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void appointNewStoreManager_NonexistentUsername() {
        try {
            int visitorId = f.EnterNewSiteVisitor();//Enter Site
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            f.Register(visitorId,Username2,password2);//Register 2 user
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);//register first user
            f.login(visitorId,Username,password);//login first user
            //TODO CHECK WHY THE STORE OWNER DOESNT GET SAVED
            int storeId = f.OpenNewStore(visitorId,"MyStore");// open a new store
            f.CloseStore(visitorId,storeId);
            assertThrows(Exception.class,()->f.appointNewStoreManager(visitorId,"FakeUsername",storeId));// appoint use2 to be store manager of store 1 which user 1 is the founder of
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }

    @Test
    void changeStoreManagerPermission() {
        try {
            int visitorId = f.EnterNewSiteVisitor();//Enter Site
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            String Username3 = "ValidUsernamePerson3";
            String password3 = "123456789";
            f.Register(visitorId,Username2,password2);//Register 2 user
            f.Register(visitorId,Username3,password3);//Register 3 user
            f.Register(visitorId,Username,password);//register first user
            f.login(visitorId,Username,password);//login first user
            int storeId = f.OpenNewStore(visitorId,"MyStore");// open a new store
            f.appointNewStoreManager(visitorId,Username2,storeId);
            List<Permission> p = new ArrayList<>();
            p.add(Permission.CanAppointStoreOwner);
            f.changeStoreManagerPermission(visitorId,Username2,storeId,p);
            f.logout(visitorId);
            f.login(visitorId,Username2,password2);
            //TODO CHECK WHY USER2 CANT APPOINT USER3 TO BE STORE OWNER EVEN THO IT IS IN HIS PERMISSIONS
            f.appointNewStoreOwner(visitorId,Username3,storeId);
            assertEquals(Role.StoreOwner,f.getEmploymentList().get(Username3).get(storeId).getRole());
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void changeStoreManagerPermission_InvalidVisitiorId() {
        try {
            int visitorId = f.EnterNewSiteVisitor();//Enter Site
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            String Username3 = "ValidUsernamePerson3";
            String password3 = "123456789";
            f.Register(visitorId,Username2,password2);//Register 2 user
            f.Register(visitorId,Username3,password3);//Register 3 user
            f.Register(visitorId,Username,password);//register first user
            f.login(visitorId,Username,password);//login first user
            int storeId = f.OpenNewStore(visitorId,"MyStore");// open a new store
            f.appointNewStoreManager(visitorId,Username2,storeId);
            List<Permission> p = new ArrayList<>();
            p.add(Permission.CanAppointStoreOwner);
            assertThrows(Exception.class,()->f.changeStoreManagerPermission(1000,Username2,storeId,p));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void changeStoreManagerPermission_BadStoreId() {
        try {
            int visitorId = f.EnterNewSiteVisitor();//Enter Site
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            String Username3 = "ValidUsernamePerson3";
            String password3 = "123456789";
            f.Register(visitorId,Username2,password2);//Register 2 user
            f.Register(visitorId,Username3,password3);//Register 3 user
            f.Register(visitorId,Username,password);//register first user
            f.login(visitorId,Username,password);//login first user
            int storeId = f.OpenNewStore(visitorId,"MyStore");// open a new store
            f.appointNewStoreManager(visitorId,Username2,storeId);
            List<Permission> p = new ArrayList<>();
            p.add(Permission.CanAppointStoreOwner);
            assertThrows(Exception.class,()->f.changeStoreManagerPermission(visitorId,Username2,10000,p));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void changeStoreManagerPermission_ClosedStore() {
        try {
            int visitorId = f.EnterNewSiteVisitor();//Enter Site
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            String Username3 = "ValidUsernamePerson3";
            String password3 = "123456789";
            f.Register(visitorId,Username2,password2);//Register 2 user
            f.Register(visitorId,Username3,password3);//Register 3 user
            f.Register(visitorId,Username,password);//register first user
            f.login(visitorId,Username,password);//login first user
            int storeId = f.OpenNewStore(visitorId,"MyStore");// open a new store
            f.appointNewStoreManager(visitorId,Username2,storeId);//TODO FIX USER NOT HAVING A STORE
            List<Permission> p = new ArrayList<>();
            p.add(Permission.CanAppointStoreOwner);
            f.CloseStore(visitorId,storeId);
            assertThrows(Exception.class,()->f.changeStoreManagerPermission(visitorId,Username2,storeId,p));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void changeStoreManagerPermission_NotStoreOwner() {
        try {
            int visitorId = f.EnterNewSiteVisitor();//Enter Site
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            String Username3 = "ValidUsernamePerson3";
            String password3 = "123456789";
            f.Register(visitorId,Username2,password2);//Register 2 user
            f.Register(visitorId,Username3,password3);//Register 3 user
            f.Register(visitorId,Username,password);//register first user
            f.login(visitorId,Username,password);//login first user
            int storeId = f.OpenNewStore(visitorId,"MyStore");// open a new store
            f.appointNewStoreManager(visitorId,Username2,storeId);
            f.logout(visitorId);
            f.login(visitorId,Username2,password2);
            List<Permission> p = new ArrayList<>();
            assertThrows(Exception.class,()->f.changeStoreManagerPermission(visitorId,Username,storeId,p));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void changeStoreManagerPermission_nonExistedtUsername() {
        try {
            int visitorId = f.EnterNewSiteVisitor();//Enter Site
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            String Username3 = "ValidUsernamePerson3";
            String password3 = "123456789";
            f.Register(visitorId,Username2,password2);//Register 2 user
            f.Register(visitorId,Username3,password3);//Register 3 user
            f.Register(visitorId,Username,password);//register first user
            f.login(visitorId,Username,password);//login first user
            int storeId = f.OpenNewStore(visitorId,"MyStore");// open a new store
            f.appointNewStoreManager(visitorId,Username2,storeId);
            List<Permission> p = new ArrayList<>();
            p.add(Permission.CanAppointStoreOwner);
            assertThrows(Exception.class,()->f.changeStoreManagerPermission(visitorId,"randomUsername",storeId,p));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void changeStoreManagerPermission_nullUsername() {
        try {
            int visitorId = f.EnterNewSiteVisitor();//Enter Site
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            String Username3 = "ValidUsernamePerson3";
            String password3 = "123456789";
            f.Register(visitorId,Username2,password2);//Register 2 user
            f.Register(visitorId,Username3,password3);//Register 3 user
            f.Register(visitorId,Username,password);//register first user
            f.login(visitorId,Username,password);//login first user
            int storeId = f.OpenNewStore(visitorId,"MyStore");// open a new store
            f.appointNewStoreManager(visitorId,Username2,storeId);
            List<Permission> p = new ArrayList<>();
            p.add(Permission.CanAppointStoreOwner);
            assertThrows(Exception.class,()->f.changeStoreManagerPermission(visitorId,null,storeId,p));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }

    @Test
    void purchaseCart_ok() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            int pid =f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.addProductToCart(pid,storeId,visitorId);
            List<String> actual = f.purchaseCart(visitorId,123,"Adress");
            List<String> expected = new LinkedList<>();
            assertEquals(expected,actual);
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void purchaseCart_InvalidVisitorId() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            int pid =f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.addProductToCart(pid,storeId,visitorId);
            assertThrows(Exception.class,()->f.purchaseCart(1000,123,"Adress"));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void purchaseCart_MultipleProducts() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            String pName2 = "Bread";
            double pPrice2 = 6.0;
            String pCat2 = "Bread";
            int pQuan2 = 10;
            String pDesc2 = "Bread";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            int pid1 = f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            int pid2 = f.AddProduct(visitorId,storeId,pName2,pPrice2,pCat2,pQuan2,pDesc2);
            f.addProductToCart(pid1,storeId,visitorId);
            f.addProductToCart(pid2,storeId,visitorId);
            List<String> actual = f.purchaseCart(visitorId,123,"Adress");
            List<String> expected = new LinkedList<>();
            assertEquals(expected,actual);
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }

    @Test
    void openNewStore_ok() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            assertTrue(f.getStoresList().get(storeId)!=null);
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void openNewStore_normalVisitor() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            assertThrows(Exception.class,()->f.OpenNewStore(visitorId,"MyStore"));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void openNewStore_notLoggedIn() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            f.Register(visitorId,Username,password);
            assertThrows(Exception.class,()->f.OpenNewStore(visitorId,"MyStore"));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }

    @Test
    void closeStore_ok() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");//TODO STORE DOESNT GET SAVED AFTER OPENING
            f.CloseStore(visitorId,storeId);
            assertFalse(f.getStoresList().get(storeId).getActive());
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void closeStore_okStoreManager() {
        try {
            int visitorId = f.EnterNewSiteVisitor();//Enter Site
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            f.Register(visitorId,Username2,password2);//Register 2 user
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);//register first user
            f.login(visitorId,Username,password);//login first user
            int storeId = f.OpenNewStore(visitorId,"MyStore");// open a new store
            //TODO:Find out why store owner doesnt get saved
            f.appointNewStoreManager(visitorId,Username2,storeId);// appoint use2 to be store manager of store 1 which user 1 is the founder of
            f.logout(visitorId);// logout user1
            f.login(visitorId,Username2,password2);//login user2
            f.CloseStore(visitorId,storeId);
            assertFalse(f.getStoresList().get(storeId).getActive());
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void closeStore_okStoreOwner() {
        try {
            int visitorId = f.EnterNewSiteVisitor();//Enter Site
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            f.Register(visitorId,Username2,password2);//Register 2 user
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);//register first user
            f.login(visitorId,Username,password);//login first user
            int storeId = f.OpenNewStore(visitorId,"MyStore");// open a new store
            //TODO CHECK WHY STORE OWNER DOESNT GET SAVED
            f.appointNewStoreOwner(visitorId,Username2,storeId);// appoint use2 to be store owner of store 1 which user 1 is the founder of
            f.logout(visitorId);// logout user1
            f.login(visitorId,Username2,password2);//login user2
            f.CloseStore(visitorId,storeId);
            assertFalse(f.getStoresList().get(storeId).getActive());
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void closeStore_FakeStoreId() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            assertThrows(Exception.class,()->f.CloseStore(visitorId,100));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void closeStore_notLoggedIn() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.logout(visitorId);
            assertThrows(Exception.class,()->f.CloseStore(visitorId,storeId));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void closeStore_notMyStore() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.logout(visitorId);
            f.Register(visitorId,Username2,password2);
            f.login(visitorId,Username2,password2);
            assertThrows(Exception.class,()->f.CloseStore(visitorId,storeId));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }

    @Test
    void addProduct_ok() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            Integer actual = f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            assertNotNull(actual);
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            fail();
        }
    }
    @Test
    void addProduct_okAllowedUserStoreOwner() {
        try {
            int visitorId = f.EnterNewSiteVisitor();//Enter Site
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            f.Register(visitorId,Username2,password2);//Register 2 user
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);//register first user
            f.login(visitorId,Username,password);//login first user
            int storeId = f.OpenNewStore(visitorId,"MyStore");// open a new store
            f.appointNewStoreOwner(visitorId,Username2,storeId);// appoint use2 to be store owner of store 1 which user 1 is the founder of
            f.logout(visitorId);// logout user1
            f.login(visitorId,Username2,password2);//login user2
            Integer actual = f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);//add a product
            assertNotNull(actual);//check the product was added
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            fail();
        }
    }
    @Test
    void addProduct_okAllowedUserStoreManager() {
        try {
            int visitorId = f.EnterNewSiteVisitor();//Enter Site
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            f.Register(visitorId,Username2,password2);//Register 2 user
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);//register first user
            f.login(visitorId,Username,password);//login first user
            int storeId = f.OpenNewStore(visitorId,"MyStore");// open a new store
            //TODO FIND OUT WHY USER1 ISNT THE OWNER OF ITS OWN STORE
            f.appointNewStoreManager(visitorId,Username2,storeId);// appoint use2 to be store manager of store 1 which user 1 is the founder of
            f.logout(visitorId);// logout user1
            f.login(visitorId,Username2,password2);//login user2
            int actual = f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);//add a product
            //assertEquals("",actual);//check the product was added
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            fail();
        }
    }
    @Test
    void addProduct_nullPName() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = null;
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            assertThrows(Exception.class,()->f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            fail();
        }
    }
    @Test
    void addProduct_negPPrice() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = -5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            assertThrows(Exception.class,()->f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            fail();
        }
    }
    @Test
    void addProduct_nullPCat() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = null;
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            assertThrows(Exception.class,()->f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            fail();
        }
    }
    @Test
    void addProduct_negPQuan() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = -10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            assertThrows(Exception.class,()->f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void addProduct_nullPDesc() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = null;
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            assertThrows(Exception.class,()->f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void addProduct_notLoggedIN() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.logout(visitorId);
            assertThrows(Exception.class,()->f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void addProduct_notMyStore() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidUsernamePerson2";
            String password2 = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.logout(visitorId);
            f.Register(visitorId,Username2,password2);
            f.login(visitorId,Username2,password2);
            //TODO CHECK WHY VISITOR ID IS INVALID
            assertThrows(Exception.class,()->f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }

    @Test
    void removeProduct() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            int pid =f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.RemoveProduct(visitorId,storeId,pid);
            //TODO CHECK WHY ITS TRYING TO CLOSE STORE
            String actual = f.getCart(visitorId).cartToString();
            assertEquals("",actual);
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void removeProduct_notallowed() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.logout(visitorId);
            assertThrows(Exception.class,()->f.RemoveProduct(visitorId,0,0));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void removeProduct_invalidVisitorId() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            assertThrows(Exception.class,()->f.RemoveProduct(1000,0,0));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void removeProduct_invalidProductId() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            assertThrows(Exception.class,()->f.RemoveProduct(visitorId,0,100));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void removeProduct_ProductfromDiffrentStore() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String Username2 = "ValidSecondUser";
            String password2 = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            String pName2 = "Bread";
            double pPrice2 = 6.0;
            String pCat2 = "Bread";
            int pQuan2 = 10;
            String pDesc2 = "Bread";
            f.Register(visitorId,Username,password);
            f.Register(visitorId,Username2,password2);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.logout(visitorId);
            f.login(visitorId,Username2,password2);
            int storeId2 = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId2,pName2,pPrice2,pCat2,pQuan2,pDesc2);
            assertThrows(Exception.class,()->f.RemoveProduct(visitorId,0,0));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }

    @Test
    void updateProductQuantity() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            int pid = f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.UpdateProductQuantity(visitorId,pid,storeId,100);
            int actualQuan = f.getStoresList().get(storeId).getProducts().get(pid).getQuantity();
            int expectedQuan = 100;
            assertEquals(expectedQuan,actualQuan);
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void updateProductQuantity_NegativeQuan() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            assertThrows(Exception.class,()->f.UpdateProductQuantity(visitorId,0,0,-100));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void updateProductQuantity_NotAllowedToUpdate() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.logout(visitorId);
            assertThrows(Exception.class,()->f.UpdateProductQuantity(visitorId,0,0,100));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void IncreaseProductQuantity() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            int pid =f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.IncreaseProductQuantity(visitorId,pid,storeId,100);
            int actualQuan = f.getStoresList().get(storeId).getProducts().get(pid).getQuantity();
            int expectedQuan = 110;
            assertEquals(expectedQuan,actualQuan);
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void IncreaseProductQuantity_NegativeQuan() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            assertThrows(Exception.class,()->f.IncreaseProductQuantity(visitorId,0,0,-100));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void IncreaseProductQuantity_NotAllowedToUpdate() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.logout(visitorId);
            assertThrows(Exception.class,()->f.IncreaseProductQuantity(visitorId,0,0,100));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }

    @Test
    void UpdateProductName() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            int pid =f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.UpdateProductName(visitorId,pid,storeId,"NotMilk");
            String actualName = f.getStoresList().get(storeId).getProducts().get(pid).getName();
            String expectedName = "NotMilk";
            assertEquals(expectedName,actualName);
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void UpdateProductName_Nullname() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            assertThrows(Exception.class,()->f.UpdateProductName(visitorId,0,0,null));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void UpdateProductName_badPID() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            assertThrows(Exception.class,()->f.UpdateProductName(visitorId,0,100,"NotMilk"));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void UpdateProductName_NotAllowedToUpdate() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.logout(visitorId);
            assertThrows(Exception.class,()->f.UpdateProductName(visitorId,0,0,"NotMilk"));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }


    @Test
    void UpdateProductPrice() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            int pid =f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.UpdateProductPrice(visitorId,pid,storeId,10.0);
            double actual = f.getStoresList().get(storeId).getProducts().get(pid).getPrice();
            double expected = 10.0;
            assertEquals(expected,actual);
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void UpdateProducPrice_notNeg() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            assertThrows(Exception.class,()->f.UpdateProductPrice(visitorId,0,0,-10.0));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void UpdateProductPrice_badPID() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            assertThrows(Exception.class,()->f.UpdateProductPrice(visitorId,0,100,10));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void UpdateProductPrice_NotAllowedToUpdate() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.logout(visitorId);
            assertThrows(Exception.class,()->f.UpdateProductPrice(visitorId,0,0,10));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }

    @Test
    void UpdateProductCate() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            int pid =f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.UpdateProductCategory(visitorId,pid,storeId,"NotMilk");
            String actual = f.getStoresList().get(storeId).getProducts().get(pid).getCategory();
            String expected = "NotMilk";
            assertEquals(expected,actual);
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void UpdateProducCate_NullCate() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            assertThrows(Exception.class,()->f.UpdateProductCategory(visitorId,0,0,null));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void UpdateProductCate_badPID() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            assertThrows(Exception.class,()->f.UpdateProductCategory(visitorId,0,100,"NotMilk"));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void UpdateProductCate_NotAllowedToUpdate() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.logout(visitorId);
            assertThrows(Exception.class,()->f.UpdateProductCategory(visitorId,0,0,"NotMilk"));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }

    @Test
    void UpdateProductDesc() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            int pid =f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.UpdateProductDescription(visitorId,pid,storeId,"NotMilk");
            String actual = f.getStoresList().get(storeId).getProducts().get(pid).getDescription();
            String expected = "NotMilk";
            assertEquals(expected,actual);
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void UpdateProducDesc_NullCate() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            assertThrows(Exception.class,()->f.UpdateProductDescription(visitorId,0,0,null));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void UpdateProductDesc_badPID() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            assertThrows(Exception.class,()->f.UpdateProductDescription(visitorId,0,100,"NotMilk"));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void UpdateProductDesc_NotAllowedToUpdate() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.logout(visitorId);
            assertThrows(Exception.class,()->f.UpdateProductDescription(visitorId,0,0,"NotMilk"));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    /*
    @Test
    void searchProductByName_productExists() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            String actual = f.SearchProductByName(pName);
            String expected="";
            assertEquals(expected,actual);//TODO MAKE THIS RETURN VALUES AND NOT OBJECTS
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void searchProductByName_MultipleProdcts() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            double pPrice2 = 6.0;
            String pCat2 = "Bread";
            int pQuan2 = 10;
            String pDesc2 = "Bread";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.AddProduct(visitorId,storeId,pName,pPrice2,pCat2,pQuan2,pDesc2);
            String actual = f.SearchProductByName(pName);
            String expected="";
            assertEquals(expected,actual);//TODO MAKE THIS RETURN VALUES AND NOT OBJECTS
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void searchProductByName_nameDoesntExist() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            String actual = f.SearchProductByName("Bread");
            String expected="[]";
            assertEquals(expected,actual);
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }

    @Test
    void searchProductByCate_productExists() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            String actual = f.SearchProductByCategory(pCat);
            String expected="";
            assertEquals(expected,actual);//TODO MAKE SearchProducyByCat return vales and not Objects
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void searchProductByCate_MultipleProdcts() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            String pName2 = "Bread";
            double pPrice2 = 6.0;
            int pQuan2 = 10;
            String pDesc2 = "Bread";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.AddProduct(visitorId,storeId,pName2,pPrice2,pCat,pQuan2,pDesc2);
            String actual = f.SearchProductByCategory(pName);
            String expected="";
            assertEquals(expected,actual);//TODO MAKE THIS RETURN VALUES INSTEAD OF OBJECTS
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void searchProductByCate_CateDoesntExist() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            int pid =f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            String actual = f.SearchProductByCategory("Bread");
            String expected="[]";
            assertEquals(expected,actual);
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }

    @Test
    void searchProductByKey_productExists() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            List<String> actual = f.SearchProductBykey(pCat);
            List<String> expected=new ArrayList<>();
            assertEquals(expected,actual);//TODO MAKE THIS RETURN VALUES AND NOT OBJECTS
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void searchProductByKey_MultipleProdcts() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            String pName2 = "Bread";
            double pPrice2 = 6.0;
            int pQuan2 = 10;
            String pDesc2 = "Bread";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.AddProduct(visitorId,storeId,pName2,pPrice2,pCat,pQuan2,pDesc2);
            List<String> actual = f.SearchProductBykey(pCat);
            List<String> expected=new ArrayList<>();
            assertEquals(expected,actual);//TODO MAKE THIS RETURN VALUES AND NOT OBJECTS
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void searchProductByKey_KeyDoesntExist() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            List<String> actual = f.SearchProductBykey("SomethingRandom");
            List<String> expected=new ArrayList<>();
            assertEquals(expected,actual);
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
*/
    @Test
    void getInformation_ExistingStore() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            String actual = f.GetInformation(storeId);
            String expected = "Store Name is MyStoreStore Rate is:5.0 Product Name is :MilkThe Rate is : 0.0\n";
            assertEquals(expected,actual);
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void getInformation_FakeStore() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            assertThrows(Exception.class,()->f.GetInformation(100));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void getStoreHistoryPurchase_Admin() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);//TODO CHANGE TO ADMIN
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            int pid1 = f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.addProductToCart(pid1,storeId,visitorId);
            f.purchaseCart(visitorId,123,"Adress");
            List<String> actual = f.GetStoreHistoryPurchase(storeId,visitorId);
            List<String> expected = new LinkedList<>();
            assertEquals(expected,actual);
        }
        catch (Exception e)
        {//Shouldnt happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void getStoreHistoryPurchase_NotAdmin() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            int pid  =f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.addProductToCart(pid,storeId,visitorId);
            f.purchaseCart(visitorId,123,"Adress");
            assertThrows(Exception.class,()->f.GetStoreHistoryPurchase(storeId,visitorId));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void getStoreHistoryPurchase_FakeStore() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);//TODO CHANGE TO ADMIN
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            int pid =f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.addProductToCart(pid,storeId,visitorId);
            f.purchaseCart(visitorId,123,"Adress");
            assertThrows(Exception.class,()->f.GetStoreHistoryPurchase(1000,visitorId));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void getUserHistoryPurchase_Admin() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);//TODO CHANGE TO ADMIN
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            int pid=f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.addProductToCart(pid,storeId,visitorId);
            f.purchaseCart(visitorId,123,"Adress");
            String actual = f.GetUserHistoryPurchase(Username,visitorId);
            String expected = "";
            assertEquals(expected,actual);
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void getUserHistoryPurchase_NotAdmin() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            int pid =f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.addProductToCart(pid,storeId,visitorId);
            f.purchaseCart(visitorId,123,"Adress");
            assertThrows(Exception.class,()->f.GetUserHistoryPurchase(Username,visitorId));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
    @Test
    void getUserHistoryPurchase_FakeUserName() {
        try {
            int visitorId = f.EnterNewSiteVisitor();
            String Username = "ValidUsername";
            String password = "123456789";
            String pName = "Milk";
            double pPrice = 5.0;
            String pCat = "Milk";
            int pQuan = 10;
            String pDesc = "Milk";
            f.Register(visitorId,Username,password);
            f.login(visitorId,Username,password);//TODO CHANGE TO ADMIN
            int storeId = f.OpenNewStore(visitorId,"MyStore");
            int pid =f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.addProductToCart(pid,storeId,visitorId);
            f.purchaseCart(visitorId,123,"Adress");
            assertThrows(Exception.class,()->f.GetUserHistoryPurchase("NotRealUser",visitorId));
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
        }
    }
}