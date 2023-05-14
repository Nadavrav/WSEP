package IntegrationTests;

import DomainLayer.Facade;
import DomainLayer.Users.RegisteredUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SystemIntegrationTests {
    Facade f = Facade.getInstance();
    private String adminUname = "admin";
    private String adminPass = "admin1234";
    private String Username1 = "ValidUsername";
    private String password1 = "123456789";
    private String Store1Name = "Super Store";
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

    @BeforeEach
    void setup() {
        f = Facade.getInstance();
    }

    @AfterEach
    void teardown() {
        f.resetData();
    }

    @Test
    void integrationTest1() {
        //This tests enter register login logout login openStore logout log in addItem logout exit, all should succeed
        try {
            int visitorId = f.EnterNewSiteVisitor();

            f.Register(visitorId, Username1, password1);
            assertTrue(f.getRegisteredUserList().get(Username1) != null);

            f.login(visitorId, Username1, password1);
            assertTrue(f.getOnlineList().get(visitorId) != null);

            f.logout(visitorId);
            assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.login(visitorId, Username1, password1);
            assertTrue(f.getOnlineList().get(visitorId) != null);

            int storeId = f.OpenNewStore(visitorId, Store1Name);
            assertTrue(f.getStoresList().get(storeId) != null);

            f.logout(visitorId);
            assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.login(visitorId, Username1, password1);
            assertTrue(f.getOnlineList().get(visitorId) != null);

            int pid1 = f.AddProduct(visitorId, storeId, pName, pPrice, pCat, pQuan, pDesc);
            assertNotNull(pid1);

            f.logout(visitorId);
            assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.ExitSiteVisitor(visitorId);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            fail();
        }

    }
    @Test
    void integrationTest2() {
        //This tests enter login logout register login getCart logout exit, first login and logout should fail the rest should succeed
        try {

            int visitorId = f.EnterNewSiteVisitor();

            assertThrows(Exception.class, () -> f.login(visitorId, Username1, password1));

            assertThrows(Exception.class, () -> f.logout(visitorId));

            f.Register(visitorId, Username1, password1);
            assertTrue(f.getRegisteredUserList().get(Username1) != null);

            f.login(visitorId, Username1, password1);
            assertTrue(f.getOnlineList().get(visitorId) != null);

            String actualCart = f.getCart(visitorId).cartToString();
            String expectedCart = "";
            assertEquals(expectedCart, actualCart);

            f.logout(visitorId);
            assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.ExitSiteVisitor(visitorId);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            fail();
        }
    }
    @Test
    void integrationTest3() {
        //This tests enter register logout login login getCart logout login openStore logout login addItems logout, first logout and second login should fail the rest should succeed
        try {

            int visitorId = f.EnterNewSiteVisitor();

            assertThrows(Exception.class, () -> f.login(visitorId, Username1, password1));

            assertThrows(Exception.class, () -> f.logout(visitorId));

            f.Register(visitorId, Username1, password1);
            assertTrue(f.getRegisteredUserList().get(Username1) != null);

            f.login(visitorId, Username1, password1);
            assertTrue(f.getOnlineList().get(visitorId) != null);

            String actualCart = f.getCart(visitorId).cartToString();
            String expectedCart = "";
            assertEquals(expectedCart, actualCart);

            f.logout(visitorId);
            assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.ExitSiteVisitor(visitorId);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            fail();
        }
    }
    @Test
    void integrationTest4() {
        //This tests enter register logout login addItemsToStore OpenStore addItemsToStore logout exit, first logout and first addItemsToStore should fail the rest should succeed
        try {
            int visitorId = f.EnterNewSiteVisitor();

            f.Register(visitorId, Username1, password1);
            assertTrue(f.getRegisteredUserList().get(Username1) != null);

            assertThrows(Exception.class, () -> f.logout(visitorId));

            f.login(visitorId, Username1, password1);
            assertTrue(f.getOnlineList().get(visitorId) != null);

            assertThrows(Exception.class, ()->f.AddProduct(visitorId,5,pName,pPrice,pCat,pQuan,pDesc));

            int storeId = f.OpenNewStore(visitorId, Store1Name);
            assertTrue(f.getStoresList().get(storeId) != null);

            Integer pid = f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            assertNotNull(pid);

            f.logout(visitorId);
            assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.ExitSiteVisitor(visitorId);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            fail();
        }
    }
    @Test
    void integrationTest5() {
        //This tests enter register loginOK logout loginOK logout LoginNullPass logout loginFakeUsername loginIncorrectPassword loginNullUsername loginOk logoutOk exit, all the logins that dont say Ok should fail the rest
        //should succeed
        try {
            int visitorId = f.EnterNewSiteVisitor();

            f.Register(visitorId, Username1, password1);
            assertTrue(f.getRegisteredUserList().get(Username1) != null);

            f.login(visitorId, Username1, password1);
            assertTrue(f.getOnlineList().get(visitorId) != null);
            assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            f.logout(visitorId);
            assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.login(visitorId, Username1, password1);
            assertTrue(f.getOnlineList().get(visitorId) != null);
            assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            f.logout(visitorId);
            assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            assertThrows(NullPointerException.class,()->f.login(visitorId, Username1, null));
            assertFalse(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            assertThrows(Exception.class,()->f.logout(visitorId));

            assertThrows(Exception.class,()->f.login(visitorId, "RandomName", password1));
            assertFalse(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            assertThrows(IllegalArgumentException.class,()->f.login(visitorId, Username1, "NotReallyMyPassword"));
            assertFalse(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            assertThrows(Exception.class,()->f.login(visitorId, null, password1));
            assertFalse(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            f.login(visitorId, Username1, password1);
            assertTrue(f.getOnlineList().get(visitorId) != null);
            assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            f.logout(visitorId);
            assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.ExitSiteVisitor(visitorId);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            fail();
        }
    }
    @Test
    void integrationTest6() {
        //This tests enter register login openStore logout login addItem logout login addItem logout exit, all should succeed
        try {
            int visitorId = f.EnterNewSiteVisitor();

            f.Register(visitorId, Username1, password1);
            assertTrue(f.getRegisteredUserList().get(Username1) != null);

            f.login(visitorId, Username1, password1);
            assertTrue(f.getOnlineList().get(visitorId) != null);
            assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            int storeId = f.OpenNewStore(visitorId, Store1Name);
            assertTrue(f.getStoresList().get(storeId) != null);

            f.logout(visitorId);
            assertFalse(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            f.login(visitorId, Username1, password1);
            assertTrue(f.getOnlineList().get(visitorId) != null);
            assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            int pid1 = f.AddProduct(visitorId, storeId, pName, pPrice, pCat, pQuan, pDesc);
            assertNotNull(pid1);

            f.logout(visitorId);
            assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.login(visitorId, Username1, password1);
            assertTrue(f.getOnlineList().get(visitorId) != null);
            assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            int pid2 = f.AddProduct(visitorId, storeId, pName2, pPrice2, pCat2, pQuan2, pDesc2);
            assertNotNull(pid1);

            f.logout(visitorId);
            assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.ExitSiteVisitor(visitorId);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            fail();
        }

    }
    @Test
    void integrationTest7() {
        //This tests enter register login openStore addItem logout addItemToCart logout exit, all should succeed
        try {
            int visitorId = f.EnterNewSiteVisitor();

            f.Register(visitorId, Username1, password1);
            assertTrue(f.getRegisteredUserList().get(Username1) != null);

            f.login(visitorId, Username1, password1);
            assertTrue(f.getOnlineList().get(visitorId) != null);
            assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            int storeId = f.OpenNewStore(visitorId, Store1Name);
            assertTrue(f.getStoresList().get(storeId) != null);

            int pid1 = f.AddProduct(visitorId, storeId, pName, pPrice, pCat, pQuan, pDesc);
            assertNotNull(pid1);

            f.logout(visitorId);
            assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.addProductToCart(pid1,storeId,visitorId);
            String actual = f.getProductsInMyCart(visitorId);
            String ExpectedstoreIdStr = "Store Id : "+storeId;
            String ExpectedproductsInCartStr = "Name: "+pName+" Description: "+pDesc+" Category: "+pCat+" price per unit: "+pPrice+" Amount: 1 total price: "+pPrice;
            assertTrue(actual.contains(ExpectedstoreIdStr));
            assertTrue(actual.contains(ExpectedproductsInCartStr));

            f.ExitSiteVisitor(visitorId);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            fail();
        }

    }
    @Test
    void integrationTest8() {
        //This tests enter register login openStore addItem logout addItemToCart exit enter checkIfCartIsEmpty, all should succeed
        try {
            int visitorId = f.EnterNewSiteVisitor();

            f.Register(visitorId, Username1, password1);
            assertTrue(f.getRegisteredUserList().get(Username1) != null);

            f.login(visitorId, Username1, password1);
            assertTrue(f.getOnlineList().get(visitorId) != null);
            assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            int storeId = f.OpenNewStore(visitorId, Store1Name);
            assertTrue(f.getStoresList().get(storeId) != null);

            int pid1 = f.AddProduct(visitorId, storeId, pName, pPrice, pCat, pQuan, pDesc);
            assertNotNull(pid1);

            f.logout(visitorId);
            assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.addProductToCart(pid1,storeId,visitorId);
            String actual = f.getProductsInMyCart(visitorId);
            String ExpectedstoreIdStr = "Store Id : "+storeId;
            String ExpectedproductsInCartStr = "Name: "+pName+" Description: "+pDesc+" Category: "+pCat+" price per unit: "+pPrice+" Amount: 1 total price: "+pPrice;
            assertTrue(actual.contains(ExpectedstoreIdStr));
            assertTrue(actual.contains(ExpectedproductsInCartStr));

            f.ExitSiteVisitor(visitorId);

            int newSiteVisitor = f.EnterNewSiteVisitor();
            String actualCart = f.getProductsInMyCart(newSiteVisitor);
            String expectedCart = "";
            assertEquals(actualCart,expectedCart);

            f.ExitSiteVisitor(newSiteVisitor);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            fail();
        }

    }
    @Test
    void integrationTest9() {
        //This tests enter register login openStore addItem logout addItemToCart purchaseCart exit, all should succeed
        try {
            int visitorId = f.EnterNewSiteVisitor();

            f.Register(visitorId, Username1, password1);
            assertTrue(f.getRegisteredUserList().get(Username1) != null);

            f.login(visitorId, Username1, password1);
            assertTrue(f.getOnlineList().get(visitorId) != null);
            assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            int storeId = f.OpenNewStore(visitorId, Store1Name);
            assertTrue(f.getStoresList().get(storeId) != null);

            int pid1 = f.AddProduct(visitorId, storeId, pName, pPrice, pCat, pQuan, pDesc);
            assertNotNull(pid1);

            f.logout(visitorId);
            assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.addProductToCart(pid1,storeId,visitorId);
            String actual = f.getProductsInMyCart(visitorId);
            String ExpectedstoreIdStr = "Store Id : "+storeId;
            String ExpectedproductsInCartStr = "Name: "+pName+" Description: "+pDesc+" Category: "+pCat+" price per unit: "+pPrice+" Amount: 1 total price: "+pPrice;
            assertTrue(actual.contains(ExpectedstoreIdStr));
            assertTrue(actual.contains(ExpectedproductsInCartStr));

            List<String> actualPurchase = f.purchaseCart(visitorId,123,"Adress");
            List<String> expectedPurchase = new LinkedList<>();
            assertEquals(expectedPurchase,actualPurchase);

            f.ExitSiteVisitor(visitorId);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            fail();
        }

    }
    @Test
    void integrationTest10() {
        //This tests enter register login openStore addItem logout addItemToCart changeItemAmountInCartToExceedActualAmount purchaseCart exit, all should succeed except purchaseCart
        try {
            int visitorId = f.EnterNewSiteVisitor();

            f.Register(visitorId, Username1, password1);
            assertTrue(f.getRegisteredUserList().get(Username1) != null);

            f.login(visitorId, Username1, password1);
            assertTrue(f.getOnlineList().get(visitorId) != null);
            assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            int storeId = f.OpenNewStore(visitorId, Store1Name);
            assertTrue(f.getStoresList().get(storeId) != null);

            int pid1 = f.AddProduct(visitorId, storeId, pName, pPrice, pCat, pQuan, pDesc);
            assertNotNull(pid1);

            f.logout(visitorId);
            assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.addProductToCart(pid1,storeId,visitorId);
            String actual = f.getProductsInMyCart(visitorId);
            String ExpectedstoreIdStr = "Store Id : "+storeId;
            String ExpectedproductsInCartStr = "Name: "+pName+" Description: "+pDesc+" Category: "+pCat+" price per unit: "+pPrice+" Amount: 1 total price: "+pPrice;
            assertTrue(actual.contains(ExpectedstoreIdStr));
            assertTrue(actual.contains(ExpectedproductsInCartStr));

            f.changeCartProductQuantity(pid1,storeId,100,visitorId);
            String actualInCart = f.getProductsInMyCart(visitorId);
            String ExpectedstoreIdStr1 = "Store Id : "+storeId;
            String ExpectedproductsInCartStr1 = "Name: "+pName+" Description: "+pDesc+" Category: "+pCat+" price per unit: "+pPrice+" Amount: "+100+" total price: "+100*pPrice;
            assertTrue(actualInCart.contains(ExpectedstoreIdStr1));
            assertTrue(actualInCart.contains(ExpectedproductsInCartStr1));

            List<String> actualPurchase = f.purchaseCart(visitorId,123,"Adress");
            List<String> expectedPurchase = new LinkedList<>();
            assertEquals(expectedPurchase,actualPurchase);

            f.ExitSiteVisitor(visitorId);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            fail();
        }

    }
}
