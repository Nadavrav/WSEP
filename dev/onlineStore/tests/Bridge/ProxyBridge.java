package Bridge;

import AcceptenceTests.ProxyClasses.CreditCardProxy;
import DomainLayer.Response;
import ServiceLayer.ServiceObjects.Fiters.Filter;
import ServiceLayer.ServiceObjects.PurchaseRecord;
import ServiceLayer.ServiceObjects.ServiceProduct;

import java.util.List;

/**
 * all stubs, deprecated when real bridge is implemented
 */
public class ProxyBridge implements Bridge {

    @Override
    public boolean initialize() {
        return false;
    }

    @Override
    public boolean EnterMarket() {
        return false;
    }

    @Override
    public boolean ExitMarket() {
        return false;
    }

    @Override
    public boolean Register(String UserName, String Password) {
        return false;
    }

    @Override
    public boolean Login(String UserName, String Password) {
        return false;
    }

    @Override
    public boolean Logout() {
        return false;
    }


    @Override
    public Integer OpenNewStore(String storeName) {
        return null;
    }

    @Override
    public String AddProduct(int storeId, String productName, String description, int price, int amount) {
        return null;
    }

    @Override
    public boolean AppointOwner(String username, int storeId) {
        return false;
    }

    @Override
    public boolean RemoveOwner(String username, int storeId) {
        return false;
    }

    @Override
    public boolean AppointManager(String username, int storeId) {
        return false;
    }

    @Override
    public boolean RemoveManager(String username, int storeId) {
        return false;
    }

    @Override
    public boolean RemoveProduct(String productId) {
        return false;
    }

    @Override
    public boolean EditProductName(String productId, String newName) {
        return false;
    }

    @Override
    public boolean EditDescription(String productId, String newDesc) {
        return false;
    }

    @Override
    public boolean EditPrice(String productId, int newPrice) {
        return false;
    }

    @Override
    public boolean CloseStore(int storeId) {
        return false;
    }

    @Override
    public boolean AddPermission(String username, int storeId, int[] index) {
        return false;
    }

    @Override
    public boolean RemovePermission(String username, int storeId, int[] index) {
        return false;
    }

    @Override
    public List<String> ProductSearch(String query) {
        return null;
    }

    @Override
    public boolean RateProduct(String productId, int rating) {
        return false;
    }

    @Override
    public List<String> UserPurchaseHistory(int storeId) {
        return null;
    }

    @Override
    public List<String> StorePurchaseHistory() {
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
    public Response<String> OpenCart() {
        return null;
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
    public Response<String[]> GetEmployeeInfo(String EmployeeUserName, int storeId) {
        return null;
    }

    @Override
    public Response<List<PurchaseRecord>> GetPurchaseHistory(int storeId) {
        return null;
    }
    @Override
    public List<ServiceProduct> FilterSearch(List<Filter> filters){
        return null;
    }

    @Override
    public boolean RateAndCommentOnProduct(String productId,String comment, int rating) {
        return false;
    }
    @Override
    public boolean RateStore(int storeId,int rating){
        return false;
    }

    @Override
    public boolean RateAndCommentOnStore(int storeId,String comment, int rating) {
        return false;
    }


}
