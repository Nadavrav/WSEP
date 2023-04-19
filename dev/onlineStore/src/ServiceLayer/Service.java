package ServiceLayer;

import DomainLayer.Facade;
import DomainLayer.Response;
import DomainLayer.Stores.Store;
import DomainLayer.Stores.StoreProduct;
import DomainLayer.Users.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static DomainLayer.Stores.StoreProduct.getStoreIdByProductId;
import static DomainLayer.Stores.StoreProduct.isValidProductId;

public class Service {

    Facade facade;

    public Service(){
        facade = Facade.getInstance();
    }

    public Response<Integer> EnterNewSiteVisitor() {//1.1
        return facade.EnterNewSiteVisitor();
    }

    public Response<?> ExitSiteVisitor(int id) {//1.2
        return facade.ExitSiteVisitor(id);
    }

    public Response<?> Register(int visitorId, String userName, String password) {//1.3
        return facade.Register(visitorId,userName,password);
    }

    public Response<?> login(int visitorId, String userName, String password) {//1.4
        return facade.login(visitorId,userName,password);
    }

    public Response<?> logout(int visitorId) {//3.1
        return facade.logout(visitorId);
    }

    public Response<?> addProductToCart(String productId, int visitorId) {//2.3
        return facade.addProductToCart(productId,visitorId);
    }

    public Response<?> getProductsInMyCart(int visitorId) {//2.4
        return facade.getProductsInMyCart(visitorId);
    }

    public Response<?> appointNewStoreOwner(int appointerId, String appointedUserName, int storeId) {//4.4
        return facade.appointNewStoreOwner(appointerId,appointedUserName,storeId);
    }

    public Response<?> appointNewStoreManager(int appointerId,String appointedUserName,int storeId){//4.6
        return facade.appointNewStoreManager(appointerId,appointedUserName,storeId);
    }



    public Response<?> changeStoreManagerPermission(int visitorID, String username, int storeID, Permission permission){
        return facade.changeStoreManagerPermission(visitorID,username,storeID,permission);
    }

    public Response<?> getRolesData(int visitorId,int storeId){//4.11
        return facade.getRolesData(visitorId,storeId);
    }


    //----------Store-----------
    // open Store
    public Response<?> OpenStore(String UserId, int StoreId) {
        return facade.OpenStore(UserId, StoreId);
    }

    //close store
    public Response<?> CloseStore(String UserId, int StoreId) {
        return facade.CloseStore(UserId, StoreId);
    }


    // ניהול מלאי 4.1
    public Response<?> AddProduct(String UserId, int StoreId, StoreProduct storeProduct) {
        return facade.AddProduct(UserId, StoreId, storeProduct);
    }

    public Response<?> RemoveProduct(String UserId, int StoreId, String ProductId) {
        return facade.RemoveProduct(UserId, StoreId, ProductId);
    }

    public Response<?> UpdateStore(String UserId, int StoreId, String productID, String Id, String name, double price, String category, int quantity, LinkedList<String> kws) {
        return facade.UpdateStore(UserId, StoreId, productID, Id, name, price, category, quantity, kws);
    }

    //2.2 search  product
    public Response<?> SearchProduct(int StoreId, String Pid) {
        return facade.SearchProduct(StoreId, Pid);
    }
    //2.1
    public Response<?> GetInformation(int StoreId) {
        return facade.GetInformation(StoreId);
    }

    //6.4
    public Response<?> GetIHistoryPurchase(int StoreId, String UserId) {
        return facade.GetIHistoryPurchase(StoreId, UserId);
    }



}
