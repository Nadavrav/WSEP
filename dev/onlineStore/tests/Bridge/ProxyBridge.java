package Bridge;

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
        Response<?> r=facade.logout(2023);
        return !facade.logout(2023).isError();
    }

    @Override
    public Response<?> login(String UserName, String Password) {
        return null;
    }

    @Override
    public Response<?> StoreAndProductSearch(String query) {
        return null;
    }

    @Override
    public Response<?> SearchProduct(List<?> filters) {
        return null;
    }

    @Override
    public Response<?> OpenNewStore(String storeName) {
        return null;
    }

    @Override
    public Response<?> AddProduct(String productName, String description, int price, int amount) {
        return null;
    }

    @Override
    public Response<?> RemoveProduct(String storeName, String productName) {
        return null;
    }

    @Override
    public Response<?> EditProductName(String storeName, String OldName, String newName) {
        return null;
    }

    @Override
    public Response<?> EditDescription(String storeName, String productName, String newDesc, int price, int quantity) {
        return null;
    }

    @Override
    public Response<?> RateProduct(String storeName, String productName, int rating) {
        return null;
    }

    @Override
    public Response<String> UserPurchaseHistory(String storeName) {
        return null;
    }

    @Override
    public Response<String> StorePurchaseHistory() {
        return null;
    }
}
