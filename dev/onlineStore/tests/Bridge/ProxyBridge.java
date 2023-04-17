package Bridge;

import DomainLayer.Response;

import java.util.List;

/**
 * all stubs, to change when service and domain are implemented
 */
public class ProxyBridge implements Bridge {

    @Override
    public Response<?> login(String UserName, String Password) {
        return null;
    }

    @Override
    public Response<?> StoreAndProductSearch(int userId, String query) {
        return null;
    }

    @Override
    public Response<?> SearchProduct(int userId, List<?> filters) {
        return null;
    }

    @Override
    public Response<?> OpenNewStore(int userId, String storeName) {
        return null;
    }

    @Override
    public Response<?> AddProduct(int userId, String productName, String description) {
        return null;
    }

    @Override
    public Response<?> RemoveProduct(int userId, String productName) {
        return null;
    }

    @Override
    public Response<?> EditProductName(int userId, String OldName, String newName) {
        return null;
    }

    @Override
    public Response<?> EditDescription(int userId, String productName, String newDesc) {
        return null;
    }

    @Override
    public Response<?> PurchaseHistory(int userId, String storeName) {
        return null;
    }
}
