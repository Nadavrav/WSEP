package IntegrationTests;

import DomainLayer.Facade;
import DomainLayer.Users.RegisteredUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
            Assertions.assertTrue(f.getRegisteredUserList().get(Username1) != null);

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);

            int storeId = f.OpenNewStore(visitorId, Store1Name);
            Assertions.assertTrue(f.getStoresList().get(storeId) != null);

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);

            int pid1 = f.AddProduct(visitorId, storeId, pName, pPrice, pCat, pQuan, pDesc);
            Assertions.assertNotNull(pid1);

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.ExitSiteVisitor(visitorId);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            Assertions.fail();
        }

    }
    @Test
    void integrationTest2() {
        //This tests enter login logout register login getCart logout exit, first login and logout should fail the rest should succeed
        try {

            int visitorId = f.EnterNewSiteVisitor();

            Assertions.assertThrows(Exception.class, () -> f.login(visitorId, Username1, password1));

            Assertions.assertThrows(Exception.class, () -> f.logout(visitorId));

            f.Register(visitorId, Username1, password1);
            Assertions.assertTrue(f.getRegisteredUserList().get(Username1) != null);

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);

            String actualCart = f.getCart(visitorId).cartToString();
            String expectedCart = "";
            Assertions.assertEquals(expectedCart, actualCart);

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.ExitSiteVisitor(visitorId);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            Assertions.fail();
        }
    }
    @Test
    void integrationTest3() {
        //This tests enter register logout login login getCart logout login openStore logout login addItems logout, first logout and second login should fail the rest should succeed
        try {

            int visitorId = f.EnterNewSiteVisitor();

            Assertions.assertThrows(Exception.class, () -> f.login(visitorId, Username1, password1));

            Assertions.assertThrows(Exception.class, () -> f.logout(visitorId));

            f.Register(visitorId, Username1, password1);
            Assertions.assertTrue(f.getRegisteredUserList().get(Username1) != null);

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);

            String actualCart = f.getCart(visitorId).cartToString();
            String expectedCart = "";
            Assertions.assertEquals(expectedCart, actualCart);

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.ExitSiteVisitor(visitorId);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            Assertions.fail();
        }
    }
    @Test
    void integrationTest4() {
        //This tests enter register logout login addItemsToStore OpenStore addItemsToStore logout exit, first logout and first addItemsToStore should fail the rest should succeed
        try {
            int visitorId = f.EnterNewSiteVisitor();

            f.Register(visitorId, Username1, password1);
            Assertions.assertTrue(f.getRegisteredUserList().get(Username1) != null);

            Assertions.assertThrows(Exception.class, () -> f.logout(visitorId));

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);

            Assertions.assertThrows(Exception.class, ()->f.AddProduct(visitorId,5,pName,pPrice,pCat,pQuan,pDesc));

            int storeId = f.OpenNewStore(visitorId, Store1Name);
            Assertions.assertTrue(f.getStoresList().get(storeId) != null);

            Integer pid = f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc);
            Assertions.assertNotNull(pid);

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.ExitSiteVisitor(visitorId);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            Assertions.fail();
        }
    }
    @Test
    void integrationTest5() {
        //This tests enter register loginOK logout loginOK logout LoginNullPass logout loginFakeUsername loginIncorrectPassword loginNullUsername loginOk logoutOk exit, all the logins that dont say Ok should fail the rest
        //should succeed
        try {
            int visitorId = f.EnterNewSiteVisitor();

            f.Register(visitorId, Username1, password1);
            Assertions.assertTrue(f.getRegisteredUserList().get(Username1) != null);

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            Assertions.assertThrows(NullPointerException.class,()->f.login(visitorId, Username1, null));
            Assertions.assertFalse(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            Assertions.assertThrows(Exception.class,()->f.logout(visitorId));

            Assertions.assertThrows(Exception.class,()->f.login(visitorId, "RandomName", password1));
            Assertions.assertFalse(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            Assertions.assertThrows(IllegalArgumentException.class,()->f.login(visitorId, Username1, "NotReallyMyPassword"));
            Assertions.assertFalse(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            Assertions.assertThrows(Exception.class,()->f.login(visitorId, null, password1));
            Assertions.assertFalse(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.ExitSiteVisitor(visitorId);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            Assertions.fail();
        }
    }
    @Test
    void integrationTest6() {
        //This tests enter register login openStore logout login addItem logout login addItem logout exit, all should succeed
        try {
            int visitorId = f.EnterNewSiteVisitor();

            f.Register(visitorId, Username1, password1);
            Assertions.assertTrue(f.getRegisteredUserList().get(Username1) != null);

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            int storeId = f.OpenNewStore(visitorId, Store1Name);
            Assertions.assertTrue(f.getStoresList().get(storeId) != null);

            f.logout(visitorId);
            Assertions.assertFalse(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            int pid1 = f.AddProduct(visitorId, storeId, pName, pPrice, pCat, pQuan, pDesc);
            Assertions.assertNotNull(pid1);

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            int pid2 = f.AddProduct(visitorId, storeId, pName2, pPrice2, pCat2, pQuan2, pDesc2);
            Assertions.assertNotNull(pid1);

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.ExitSiteVisitor(visitorId);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            Assertions.fail();
        }

    }
    @Test
    void integrationTest7() {
        //This tests enter register login openStore addItem logout addItemToCart logout exit, all should succeed
        try {
            int visitorId = f.EnterNewSiteVisitor();

            f.Register(visitorId, Username1, password1);
            Assertions.assertTrue(f.getRegisteredUserList().get(Username1) != null);

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            int storeId = f.OpenNewStore(visitorId, Store1Name);
            Assertions.assertTrue(f.getStoresList().get(storeId) != null);

            int pid1 = f.AddProduct(visitorId, storeId, pName, pPrice, pCat, pQuan, pDesc);
            Assertions.assertNotNull(pid1);

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.addProductToCart(pid1,storeId,1,visitorId);
            String actual = f.getStringProductsInMyCart(visitorId);
            String ExpectedstoreIdStr = "Store Id : "+storeId;
            String ExpectedproductsInCartStr = "Name: "+pName+" Description: "+pDesc+" Category: "+pCat+" price per unit: "+pPrice+" Amount: 1 total price: "+pPrice;
            Assertions.assertTrue(actual.contains(ExpectedstoreIdStr));
            Assertions.assertTrue(actual.contains(ExpectedproductsInCartStr));

            f.ExitSiteVisitor(visitorId);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            Assertions.fail();
        }

    }
    @Test
    void integrationTest8() {
        //This tests enter register login openStore addItem logout addItemToCart exit enter checkIfCartIsEmpty, all should succeed
        try {
            int visitorId = f.EnterNewSiteVisitor();

            f.Register(visitorId, Username1, password1);
            Assertions.assertTrue(f.getRegisteredUserList().get(Username1) != null);

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            int storeId = f.OpenNewStore(visitorId, Store1Name);
            Assertions.assertTrue(f.getStoresList().get(storeId) != null);

            int pid1 = f.AddProduct(visitorId, storeId, pName, pPrice, pCat, pQuan, pDesc);
            Assertions.assertNotNull(pid1);

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.addProductToCart(pid1,storeId,1,visitorId);
            String actual = f.getStringProductsInMyCart(visitorId);
            String ExpectedstoreIdStr = "Store Id : "+storeId;
            String ExpectedproductsInCartStr = "Name: "+pName+" Description: "+pDesc+" Category: "+pCat+" price per unit: "+pPrice+" Amount: 1 total price: "+pPrice;
            Assertions.assertTrue(actual.contains(ExpectedstoreIdStr));
            Assertions.assertTrue(actual.contains(ExpectedproductsInCartStr));

            f.ExitSiteVisitor(visitorId);

            int newSiteVisitor = f.EnterNewSiteVisitor();
            String actualCart = f.getStringProductsInMyCart(newSiteVisitor);
            String expectedCart = "";
            Assertions.assertEquals(actualCart,expectedCart);

            f.ExitSiteVisitor(newSiteVisitor);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            Assertions.fail();
        }

    }
    @Test
    void integrationTest9() {
        //This tests enter register login openStore addItem logout addItemToCart purchaseCart exit, all should succeed
        try {
            int visitorId = f.EnterNewSiteVisitor();

            f.Register(visitorId, Username1, password1);
            Assertions.assertTrue(f.getRegisteredUserList().get(Username1) != null);

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            int storeId = f.OpenNewStore(visitorId, Store1Name);
            Assertions.assertTrue(f.getStoresList().get(storeId) != null);

            int pid1 = f.AddProduct(visitorId, storeId, pName, pPrice, pCat, pQuan, pDesc);
            Assertions.assertNotNull(pid1);

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.addProductToCart(pid1,storeId,1,visitorId);
            String actual = f.getStringProductsInMyCart(visitorId);
            String ExpectedstoreIdStr = "Store Id : "+storeId;
            String ExpectedproductsInCartStr = "Name: "+pName+" Description: "+pDesc+" Category: "+pCat+" price per unit: "+pPrice+" Amount: 1 total price: "+pPrice;
            Assertions.assertTrue(actual.contains(ExpectedstoreIdStr));
            Assertions.assertTrue(actual.contains(ExpectedproductsInCartStr));

            List<String> actualPurchase = f.purchaseCart(visitorId,123,"Adress");
            List<String> expectedPurchase = new LinkedList<>();
            Assertions.assertEquals(expectedPurchase,actualPurchase);

            f.ExitSiteVisitor(visitorId);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            Assertions.fail();
        }

    }
    @Test
    void integrationTest10() {
        //This tests enter register login openStore addItem logout addItemToCart changeItemAmountInCartToExceedActualAmount purchaseCart exit, all should succeed except purchaseCart
        try {
            int visitorId = f.EnterNewSiteVisitor();

            f.Register(visitorId, Username1, password1);
            Assertions.assertTrue(f.getRegisteredUserList().get(Username1) != null);

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            int storeId = f.OpenNewStore(visitorId, Store1Name);
            Assertions.assertTrue(f.getStoresList().get(storeId) != null);

            int pid1 = f.AddProduct(visitorId, storeId, pName, pPrice, pCat, pQuan, pDesc);
            Assertions.assertNotNull(pid1);

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.addProductToCart(pid1,storeId,1,visitorId);
            String actual = f.getStringProductsInMyCart(visitorId);
            String ExpectedstoreIdStr = "Store Id : "+storeId;
            String ExpectedproductsInCartStr = "Name: "+pName+" Description: "+pDesc+" Category: "+pCat+" price per unit: "+pPrice+" Amount: 1 total price: "+pPrice;
            Assertions.assertTrue(actual.contains(ExpectedstoreIdStr));
            Assertions.assertTrue(actual.contains(ExpectedproductsInCartStr));

            f.changeCartProductQuantity(pid1,storeId,100,visitorId);
            String actualInCart = f.getStringProductsInMyCart(visitorId);
            String ExpectedstoreIdStr1 = "Store Id : "+storeId;
            String ExpectedproductsInCartStr1 = "Name: "+pName+" Description: "+pDesc+" Category: "+pCat+" price per unit: "+pPrice+" Amount: "+100+" total price: "+100*pPrice;
            Assertions.assertTrue(actualInCart.contains(ExpectedstoreIdStr1));
            Assertions.assertTrue(actualInCart.contains(ExpectedproductsInCartStr1));

            List<String> actualPurchase = f.purchaseCart(visitorId,123,"Adress");
            List<String> expectedPurchase = new LinkedList<>();
            expectedPurchase.add(String.valueOf(storeId));
            Assertions.assertEquals(expectedPurchase,actualPurchase);

            f.ExitSiteVisitor(visitorId);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            Assertions.fail();
        }

    }
    @Test
    void integrationTest11() {
        //This tests enter register login openStore addItem logout login CloseStore ChangeAmountInStore addItem2 addItemToCart logout exit,
        // all should succeed except hangeAmountInStore addItem addItemToCart
        try {
            int visitorId = f.EnterNewSiteVisitor();

            f.Register(visitorId, Username1, password1);
            Assertions.assertTrue(f.getRegisteredUserList().get(Username1) != null);

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            int storeId = f.OpenNewStore(visitorId, Store1Name);
            Assertions.assertTrue(f.getStoresList().get(storeId) != null);

            int pid1 = f.AddProduct(visitorId, storeId, pName, pPrice, pCat, pQuan, pDesc);
            Assertions.assertNotNull(pid1);

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            f.CloseStore(visitorId,storeId);
            assertFalse(f.getStoresList().get(storeId).getActive());

            Assertions.assertThrows(Exception.class,()->f.IncreaseProductQuantity(visitorId,pid1,storeId,100));

            Assertions.assertThrows(Exception.class,()->f.AddProduct(visitorId, storeId, pName2, pPrice2, pCat2, pQuan2, pDesc2));

            Assertions.assertThrows(Exception.class,()->f.addProductToCart(pid1,storeId,1,visitorId));

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.ExitSiteVisitor(visitorId);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            Assertions.fail();
        }

    }
    @Test
    void integrationTest12() {
        // This tests enter register login login logout loginFake logout login
        // addStoreComment1 logout login closeStore logout addStoreRate exit,
        // all should succeed loginFake logout after loginFake addStoreComment1 logout after addStoreRate
        // closeStore addComment
        try {
            int visitorId = f.EnterNewSiteVisitor();

            f.Register(visitorId, Username1, password1);
            Assertions.assertTrue(f.getRegisteredUserList().get(Username1) != null);

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            Assertions.assertThrows(Exception.class, ()->f.login(visitorId,Username1,password1));

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            Assertions.assertThrows(Exception.class, ()->f.login(visitorId,"fake",password1));

            Assertions.assertThrows(Exception.class, ()->f.logout(visitorId));

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            Assertions.assertThrows(Exception.class, ()->f.addStoreRateAndComment(visitorId,-5,5,""));

            Assertions.assertThrows(Exception.class,()->f.logout(visitorId));

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            Assertions.assertThrows(Exception.class,()->f.CloseStore(visitorId,-5));

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            Assertions.assertThrows(Exception.class,()->f.addStoreRate(visitorId,-5,4));

            f.ExitSiteVisitor(visitorId);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            Assertions.fail();
        }

    }
    @Test
    void integrationTest13() {
        // This tests enter register login login logout loginFake logout login
        // addProductComment1 logout login closeStore logout addProductComment2 exit,
        // all should succeed loginFake logout after loginFake addProductComment1 logout after addProductComment2
        // closeStore addComment
        try {
            int visitorId = f.EnterNewSiteVisitor();

            f.Register(visitorId, Username1, password1);
            Assertions.assertTrue(f.getRegisteredUserList().get(Username1) != null);

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            Assertions.assertThrows(Exception.class, ()->f.login(visitorId,Username1,password1));

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            Assertions.assertThrows(Exception.class, ()->f.login(visitorId,"fake",password1));

            Assertions.assertThrows(Exception.class, ()->f.logout(visitorId));

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            Assertions.assertThrows(Exception.class, ()->f.addProductRateAndComment(visitorId,-5,5,4,""));

            Assertions.assertThrows(Exception.class, ()->f.logout(visitorId));

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            Assertions.assertThrows(Exception.class,()->f.CloseStore(visitorId,-5));

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            Assertions.assertThrows(Exception.class,()->f.addProductRateAndComment(visitorId,1,-55,4,""));

            f.ExitSiteVisitor(visitorId);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            Assertions.fail();
        }

    }
    @Test
    void integrationTest14() {
        // This tests enter register removeProduct addProduct removeProduct login openStore removeProduct
        // addProduct removeProduct closeStore addProduct removeProduct openNewStore addProduct removeProductFromStore2
        // logout exit
        try {
            int visitorId = f.EnterNewSiteVisitor();

            f.Register(visitorId, Username1, password1);
            Assertions.assertTrue(f.getRegisteredUserList().get(Username1) != null);

            Assertions.assertThrows(Exception.class, ()->f.RemoveProduct(visitorId,5,1));

            Assertions.assertThrows(Exception.class,()->f.AddProduct(visitorId,5,pName,pPrice,pCat,pQuan,pDesc));

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            int storeId = f.OpenNewStore(visitorId, Store1Name);
            Assertions.assertTrue(f.getStoresList().get(storeId) != null);

            Assertions.assertThrows(Exception.class, ()->f.RemoveProduct(visitorId,5,1));

            int pid1 = f.AddProduct(visitorId, storeId, pName, pPrice, pCat, pQuan, pDesc);
            Assertions.assertNotNull(pid1);

            f.RemoveProduct(visitorId,storeId,pid1);
            assertFalse(f.getStoresList().get(storeId).getProducts().values().contains(pid1));

            f.CloseStore(visitorId,storeId);
            assertFalse(f.getStoresList().get(storeId).getActive());

            Assertions.assertThrows(Exception.class,()->f.AddProduct(visitorId,storeId,pName,pPrice,pCat,pQuan,pDesc));

            Assertions.assertThrows(Exception.class, ()->f.RemoveProduct(visitorId,storeId,pid1));

            int storeId2 = f.OpenNewStore(visitorId, "NewStore");
            Assertions.assertTrue(f.getStoresList().get(storeId2) != null);

            int pid2 = f.AddProduct(visitorId, storeId2, pName, pPrice, pCat, pQuan, pDesc);
            Assertions.assertNotNull(pid2);

            f.RemoveProduct(visitorId,storeId2,pid2);
            assertFalse(f.getStoresList().get(storeId2).getProducts().values().contains(pid2));

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.ExitSiteVisitor(visitorId);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            Assertions.fail();
        }

    }
    @Test
    void integrationTest15() {
        // This tests enter register login UpdateProductName UpdateProductPrice UpdateProductCate addProduct
        // UpdateProductName UpdateProductPrice UpdateProductCate UpdateDesc removeProduct UpdateProductPrice UpdateProductCate openStore addProduct
        // UpdateProductName UpdateProductPrice UpdateProductCate UpdateDesc removeProduct UpdateProductPrice UpdateProductCate addProduct2 CloseStore
        // UpdateProductName UpdateProductPrice UpdateProductCate UpdateDesc removeProduct logout
        // addProduct removeProductFromStore1
        // logout exit
        try {
            int fakePID= -5;
            int fakeStoreId = -5;
            int visitorId = f.EnterNewSiteVisitor();

            f.Register(visitorId, Username1, password1);
            Assertions.assertTrue(f.getRegisteredUserList().get(Username1) != null);

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            Assertions.assertThrows(Exception.class,()->f.UpdateProductName(visitorId,fakePID,fakeStoreId,"Fail"));

            Assertions.assertThrows(Exception.class,()->f.UpdateProductPrice(visitorId,fakePID,fakeStoreId,154));

            Assertions.assertThrows(Exception.class,()->f.UpdateProductCategory(visitorId,fakePID,fakeStoreId,"Fail"));

            Assertions.assertThrows(Exception.class,()->f.AddProduct(visitorId, fakeStoreId, pName, pPrice, pCat, pQuan, pDesc));

            Assertions.assertThrows(Exception.class,()->f.UpdateProductName(visitorId,fakePID,fakeStoreId,"Fail"));

            Assertions.assertThrows(Exception.class,()->f.UpdateProductPrice(visitorId,fakePID,fakeStoreId,154));

            Assertions.assertThrows(Exception.class,()->f.UpdateProductCategory(visitorId,fakePID,fakeStoreId,"Fail"));

            Assertions.assertThrows(Exception.class,()->f.UpdateProductDescription(visitorId,fakePID,fakeStoreId,"Fail"));

            Assertions.assertThrows(Exception.class,()->f.RemoveProduct(visitorId,fakeStoreId,fakePID));

            Assertions.assertThrows(Exception.class,()->f.UpdateProductPrice(visitorId,fakePID,fakeStoreId,154));

            Assertions.assertThrows(Exception.class,()->f.UpdateProductCategory(visitorId,fakePID,fakeStoreId,"Fail"));

            int storeId = f.OpenNewStore(visitorId, Store1Name);
            Assertions.assertTrue(f.getStoresList().get(storeId) != null);

            int pid1 = f.AddProduct(visitorId, storeId, pName, pPrice, pCat, pQuan, pDesc);
            Assertions.assertNotNull(pid1);

            f.UpdateProductName(visitorId,pid1,storeId,"Fail");
            assertEquals("Fail",f.getStoresList().get(storeId).getProducts().get(pid1).getName());

            f.UpdateProductPrice(visitorId,pid1,storeId,154);
            assertEquals(154,f.getStoresList().get(storeId).getProducts().get(pid1).getPrice());

            f.UpdateProductCategory(visitorId,pid1,storeId,"Fail2");
            assertEquals("Fail2",f.getStoresList().get(storeId).getProducts().get(pid1).getCategory());

            f.UpdateProductDescription(visitorId,pid1,storeId,"Fail3");
            assertEquals("Fail3",f.getStoresList().get(storeId).getProducts().get(pid1).getDescription());

            f.RemoveProduct(visitorId,storeId,pid1);
            assertFalse(f.getStoresList().get(storeId).getProducts().values().contains(pid1));

            Assertions.assertThrows(Exception.class,()->f.UpdateProductPrice(visitorId,pid1,storeId,154));

            Assertions.assertThrows(Exception.class,()->f.UpdateProductCategory(visitorId,pid1,storeId,"Fail"));

            int pid2 = f.AddProduct(visitorId, storeId, pName2, pPrice2, pCat2, pQuan2, pDesc2);
            Assertions.assertNotNull(pid2);

            f.CloseStore(visitorId,storeId);
            assertFalse(f.getStoresList().get(storeId).getActive());

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            Assertions.assertThrows(Exception.class,()->f.UpdateProductName(visitorId,pid1,storeId,"Fail"));

            Assertions.assertThrows(Exception.class,()->f.UpdateProductPrice(visitorId,pid1,storeId,154));

            Assertions.assertThrows(Exception.class,()->f.UpdateProductCategory(visitorId,pid1,storeId,"Fail"));

            Assertions.assertThrows(Exception.class,()->f.UpdateProductDescription(visitorId,pid1,storeId,"Fail"));

            f.ExitSiteVisitor(visitorId);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            Assertions.fail();
        }

    }
    @Test
    void integrationTest16() {
        //This tests enter register login openStore addItem logout addItemToCart purchaseCart addSameItemToCart(his quan should be 0 now) purchaseCart(should have a fail msg containing the item) exit
        try {
            int visitorId = f.EnterNewSiteVisitor();

            f.Register(visitorId, Username1, password1);
            Assertions.assertTrue(f.getRegisteredUserList().get(Username1) != null);

            f.login(visitorId, Username1, password1);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) != null);
            Assertions.assertTrue(f.getOnlineList().get(visitorId) instanceof RegisteredUser);

            int storeId = f.OpenNewStore(visitorId, Store1Name);
            Assertions.assertTrue(f.getStoresList().get(storeId) != null);

            int pid1 = f.AddProduct(visitorId, storeId, pName, pPrice, pCat, 1, pDesc);
            Assertions.assertNotNull(pid1);

            f.logout(visitorId);
            Assertions.assertTrue(!(f.getOnlineList().get(visitorId) instanceof RegisteredUser));

            f.addProductToCart(pid1,storeId,1,visitorId);
            String actual = f.getStringProductsInMyCart(visitorId);
            String ExpectedstoreIdStr = "Store Id : "+storeId;
            String ExpectedproductsInCartStr = "Name: "+pName+" Description: "+pDesc+" Category: "+pCat+" price per unit: "+pPrice+" Amount: 1 total price: "+pPrice;
            Assertions.assertTrue(actual.contains(ExpectedstoreIdStr));
            Assertions.assertTrue(actual.contains(ExpectedproductsInCartStr));


            List<String> actualPurchase = f.purchaseCart(visitorId,123,"Adress");
            List<String> expectedPurchase = new LinkedList<>();
            Assertions.assertEquals(expectedPurchase,actualPurchase);

            f.addProductToCart(pid1,storeId,1,visitorId);
            String actual1 = f.getStringProductsInMyCart(visitorId);
            String ExpectedstoreIdStr1 = "Store Id : "+storeId;
            String ExpectedproductsInCartStr1 = "Name: "+pName+" Description: "+pDesc+" Category: "+pCat+" price per unit: "+pPrice+" Amount: 1 total price: "+pPrice;
            Assertions.assertTrue(actual1.contains(ExpectedstoreIdStr1));
            Assertions.assertTrue(actual1.contains(ExpectedproductsInCartStr1));

            List<String> actualPurchase1 = f.purchaseCart(visitorId,123,"Adress");
            List<String> expectedPurchase1 = new LinkedList<>();
            expectedPurchase1.add(String.valueOf(storeId));
            Assertions.assertEquals(expectedPurchase1,actualPurchase1);

            f.ExitSiteVisitor(visitorId);
        } catch (Exception e) {//Should not happen
            System.out.println(e.getMessage());// a print to find out from what function
            Assertions.fail();
        }

    }
}
