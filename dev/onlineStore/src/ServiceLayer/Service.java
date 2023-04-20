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
    int visitorId;

    public Service(){
        facade = Facade.getInstance();
    }

    public Response<Integer> EnterNewSiteVisitor() {//1.1
        Response<Integer> response= facade.EnterNewSiteVisitor();
        visitorId=response.getValue();
        return response;
    }

    public Response<?> ExitSiteVisitor(int id) {//1.2
        return facade.ExitSiteVisitor(id);
    }

    public Response<?> Register( String userName, String password) {//1.3
        return facade.Register(visitorId,userName,password);
    }

    public Response<?> login( String userName, String password) {//1.4
        return facade.login(visitorId,userName,password);
    }

    public Response<?> logout() {//3.1
        return facade.logout(visitorId);
    }

    public Response<?> addProductToCart(String productId) {//2.3
        return facade.addProductToCart(productId,visitorId);
    }

    public Response<?> getProductsInMyCart() {//2.4
        return facade.getProductsInMyCart(visitorId);
    }

    public Response<?> appointNewStoreOwner( String appointedUserName, int storeId) {//4.4
        return facade.appointNewStoreOwner(visitorId,appointedUserName,storeId);
    }

    public Response<?> appointNewStoreManager(String appointedUserName,int storeId){//4.6
        return facade.appointNewStoreManager(visitorId,appointedUserName,storeId);
    }



    public Response<?> changeStoreManagerPermission( String username, int storeID, Permission permission){
        return facade.changeStoreManagerPermission(visitorId,username,storeID,permission);
    }

    public Response<?> getRolesData(int storeId){//4.11
        return facade.getRolesData(visitorId,storeId);
    }


    //----------Store-----------
    // open Store
    public Response<?> OpenStore( int StoreId, String storeName) {
        return facade.OpenNewStore(visitorId, storeName);
    }

    //close store
    public Response<?> CloseStore(int StoreId) {
        return facade.CloseStore(visitorId, StoreId);
    }


    // ניהול מלאי 4.1
    public Response<?> AddProduct(int StoreId, StoreProduct storeProduct) {
        return facade.AddProduct(visitorId, StoreId, storeProduct);
    }

    public Response<?> RemoveProduct(int StoreId, String ProductId) {
        return facade.RemoveProduct(visitorId, StoreId, ProductId);
    }

    public Response<?> UpdateStore( int StoreId, String productID, String Id, String name, double price, String category, int quantity, String kws,String desc) {
        return facade.UpdateStore(visitorId, StoreId, productID, Id, name, price, category, quantity, kws,desc);
    }

    //2.2 search  product
    public Response<?> SearchProductByName( String Pid) {
        return facade.SearchProductByName(visitorId, Pid);
    }
    public Response<?> SearchProductByCategory( String Pid) {
        return facade.SearchProductByCategory(visitorId, Pid);
    }

    public Response<?> SearchProductBykey( String Pid) {
        return facade.SearchProductBykey(visitorId, Pid);
    }

    //2.1
    public Response<?> GetInformation(int StoreId) {
        return facade.GetInformation(StoreId);
    }

    //6.4
    public Response<?> GetIHistoryPurchase(int StoreId) {
        return facade.GetIHistoryPurchase(StoreId, visitorId);
    }
}
