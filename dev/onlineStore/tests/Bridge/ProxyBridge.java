package Bridge;

import AcceptenceTests.ProxyClasses.CreditCardProxy;
import DomainLayer.Facade;
import DomainLayer.Response;

import java.util.List;

/**
 * all stubs, deprecated when real bridge is implemented
 */
public class ProxyBridge implements Bridge {

    Facade facade=Facade.getInstance();
    @Override
    public boolean initialize() {
       return true;
    }

    @Override
    public boolean EnterMarket() {
        return true;
    }

    @Override
    public boolean ExitMarket() {
        return true;
    }

    @Override
    public boolean Register(String UserName, String Password) {
        return true;
    }

    @Override
    public boolean Login(String UserName, String Password) {
        return true;
    }

    @Override
    public boolean Logout() {
        return true;
    }

    @Override
    public boolean IsOnline(String Username) {
        return true;
    }

    @Override
    public boolean StoreAndProductSearch(String query) {
        return true;
    }

    @Override
    public boolean SearchProduct(List<?> filters) {
        return true;
    }

    @Override
    public boolean OpenNewStore(String storeName) {
        return true;
    }

    @Override
    public boolean AddProduct(String storeName, String productName, String description, int price, int amount) {
        return true;
    }

    @Override
    public boolean RemoveProduct(String storeName, String productName) {
        return true;
    }

    @Override
    public boolean EditProductName(String storeName, String OldName, String newName) {
        return true;
    }

    @Override
    public boolean EditDescription(String storeName, String productName, String newDesc, int price, int quantity) {
        return true;
    }

    @Override
    public boolean RateProduct(String storeName, String productName, int rating) {
        return true;
    }

    @Override
    public Response<String> UserPurchaseHistory(String storeName) {
        return null;
    }

    @Override
    public Response<String> StorePurchaseHistory() {
        return null;
    }

    @Override
    public boolean addToCart(String productId) {
        return false;
    }

    @Override
    public boolean removeFromCart(String productId) {
        return false;
    }

    @Override
    public Response<String[]> OpenCart() {
        String [] arr = {};
        Response<String[]> r = new Response<>(arr);
        return r;
    }

    @Override
    public boolean CartChangeItemQuantity(String productId, int newQuantity) {
        return false;
    }

    @Override
    public boolean PurchaseCart(CreditCardProxy credit) {
        return false;
    }

    @Override
    public int GetItemQuantity(String productId) {
        return 0;
    }

    @Override
    public Response<String[]> GetEmployeeInfo(String EmployeeUserName, String StoreName) {
        String [] arr = {};
        Response<String[]> r = new Response<>(arr);
        return r;
    }

    @Override
    public Response<String[]> GetPurchaseHistory(String StoreName) {
        String [] arr = {};
        Response<String[]> r = new Response<>(arr);
        return r;
    }
}
