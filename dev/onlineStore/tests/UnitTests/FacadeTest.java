package UnitTests;

import DomainLayer.Facade;
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
    void addProductToCart() {

    }

    @Test
    void appointNewStoreOwner() {
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
    void addProduct() {
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