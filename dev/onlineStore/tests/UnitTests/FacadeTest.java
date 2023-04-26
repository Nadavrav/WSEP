package UnitTests;

import DomainLayer.Facade;
import DomainLayer.Stores.StoreProduct;
import DomainLayer.Users.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
            assertThrows(Exception.class,()->f.Register(100,Username,"123456789"));
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
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.addProductToCart("0-0",visitorId);
            String actual = f.getProductsInMyCart(visitorId);
            assertEquals("",actual);
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
            f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            f.AddProduct(visitorId,storeId,pName2,pPrice2,pCat2,pQuan2,pDesc2);
            f.addProductToCart("0-0",visitorId);
            f.addProductToCart("0-1",visitorId);
            String actual = f.getProductsInMyCart(visitorId);
            assertEquals("",actual);
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
            assertThrows(Exception.class,()->f.addProductToCart("0-1000",visitorId));
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
            assertThrows(Exception.class,()->f.addProductToCart("0-0",1000));
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
    void appointNewStoreManager() {
    }

    @Test
    void changeStoreManagerPermission() {
    }

    @Test
    void getRolesData() {
    }

    @Test
    void purchaseCart() {
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
            int storeId = f.OpenNewStore(visitorId,"MyStore");
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
            String actual = f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            assertEquals("",actual);
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
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
            String actual = f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);//add a product
            assertEquals("",actual);//check the product was added
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
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
            f.appointNewStoreManager(visitorId,Username2,storeId);// appoint use2 to be store manager of store 1 which user 1 is the founder of
            f.logout(visitorId);// logout user1
            f.login(visitorId,Username2,password2);//login user2
            String actual = f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);//add a product
            assertEquals("",actual);//check the product was added
        }
        catch (Exception e)
        {//Should happen
            System.out.println(e.getMessage());
            assertFalse(true);
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
            assertFalse(true);
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
            assertFalse(true);
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
            assertFalse(true);
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
    void searchProductByName() {
    }

    @Test
    void searchProductByCategory() {
    }

    @Test
    void searchProductBykey() {
    }

    @Test
    void getInformation() {
    }

    @Test
    void getStoreHistoryPurchase() {
    }

    @Test
    void getUserHistoryPurchase() {
    }

    @Test
    void filterProductSearch() {
    }
}