package Bridge;

import DomainLayer.Response;
import ServiceLayer.ServiceObjects.Fiters.ProductFilters.ProductFilter;
import ServiceLayer.Service;
import DomainLayer.Users.Permission;
import ServiceLayer.ServiceObjects.Fiters.StoreFilters.StoreFilter;
import ServiceLayer.ServiceObjects.PurchaseRecord;
import ServiceLayer.ServiceObjects.ServiceProducts.ServiceProduct;
import ServiceLayer.ServiceObjects.ServiceStore;
import org.opentest4j.TestSkippedException;

import java.util.ArrayList;
import java.util.List;

public class RealBridge implements Bridge {
    Service service=new Service();
    @Override
    public boolean initialize() {
        return false;
    }

    @Override
    public boolean EnterMarket() {
        return !service.EnterNewSiteVisitor().isError();
    }

    @Override
    public boolean ExitMarket() {
        return !service.ExitSiteVisitor().isError();
    }

    @Override
    public boolean Register(String UserName, String Password) {
        return !service.Register(UserName,Password).isError();
    }

    @Override
    public boolean Login(String UserName, String Password) {
        return !service.login(UserName,Password).isError();
    }

    @Override
    public boolean Logout() {
        return !service.logout().isError();
    }

    @Override
    public Integer OpenNewStore(String storeName) {
        Response<Integer> r=service.OpenStore(storeName);
        if(!r.isError())
            return r.getValue();
        else return -1;
    }

    @Override
    public Integer AddProduct(int storeId, String productName, String description, int price, int amount) {
        Response<Integer> r= service.AddProduct(storeId,productName,price,"test",amount,description);
        if(!r.isError())
            return r.getValue();
        return -1;
    }

    @Override
    public boolean AppointOwner(String username, int storeId) {
        return !service.appointNewStoreOwner(username, storeId).isError();
    }

    @Override
    public boolean RemoveOwner(String username, int storeId) {
        // return !service.appointNewStoreOwner(username, storeId).isError(); //NOT FOR THIS VER
        return false;
    }

    @Override
    public boolean AppointManager(String username, int storeId) {
        return !service.appointNewStoreManager(username, storeId).isError();
    }

    @Override
    public boolean RemoveManager(String username, int storeId) {
        // return false; NOT FOR THIS VER
        return false;
    }

    @Override
    public boolean RemoveProduct(int productId, int storeId) {
        return !service.RemoveProduct(productId,storeId ).isError();
    }

    @Override
    public boolean EditProductName(int productId, int storeId, String newName) {
        return !service.UpdateProductName(productId,storeId, newName).isError();
    }

    @Override
    public boolean EditDescription(int productId, int storeId, String newDesc) {
        return !service.UpdateProductName(productId,storeId, newDesc).isError();
    }

    @Override
    public boolean EditPrice(int productId, int storeId, int newPrice) {
        return !service.UpdateProductPrice(productId,storeId, newPrice).isError();
    }

    @Override
    public boolean CloseStore(int storeId) {
        return !service.CloseStore(storeId).isError();
    }

    @Override
    public boolean AddPermission(String username, int storeId, int[] index) {
        ArrayList<Permission> permissions=new ArrayList<>();
        boolean[] added ={false,false,false,false,false,false,false,false,false,false,false};
        for(int permission:index){
            if(added[permission])
                throw new TestSkippedException("TEST ERROR");
            permissions.add(Permission.values()[permission]);
            added[permission]=true;
        }
        return !service.changeStoreManagerPermission(username,storeId,permissions).isError();
    }

    @Override
    public boolean RemovePermission(String username, int storeId, int[] index) {
        ArrayList<Permission> permissions=new ArrayList<>();
        boolean[] added ={false,false,false,false,false,false,false,false,false,false,false};
        for(int permission:index){
            if(added[permission])
                throw new TestSkippedException("TEST ERROR");
            permissions.add(Permission.values()[permission]);
            added[permission]=true;
        }
        return !service.changeStoreManagerPermission(username,storeId,permissions).isError();
    }

    // @Override
    // public List<String> ProductSearch(String query) {
//
    //     Response<List<String>> r=service.SearchProductBykey(query);
    //     if(r.isError())
    //         throw new TestAbortedException("TEST SHOULD NOT FAIL");
    //     return r.getValue();
    // }

    @Override
    public boolean RateProduct(int productId, int storeId, int rating) {
        return service.addProductRateAndComment(productId,storeId,rating,"").isError();
    }

    @Override
    public String UserPurchaseHistory(String username) {
        Response r = service.GetUserHistoryPurchase(username);
        if (r.isError())
            return null;
        return (String)r.getValue();
    }

    @Override
    public List<String> StorePurchaseHistory(int storeId) {
        Response r = service.GetStoreHistoryPurchase(storeId);
        if (r.isError()) {
            return null;
        }
        return (List<String>)r.getValue();
    }

    @Override
    public boolean addToCart(int productId, int storeId) {
        return !service.addProductToCart(productId,storeId ).isError();
    }

    @Override
    public boolean removeFromCart(int productId, int storeId) {
        return !service.removeProductFromCart(productId,storeId).isError();
    }

    @Override
    public Response<String> OpenCart() {
        return service.getProductsInMyCart();
    }

    @Override
    public boolean CartChangeItemQuantity(int productId,int storeId, int newQuantity) {
        return !service.changeCartProductQuantity(productId,storeId,newQuantity).isError();
    }

    @Override
    public Response<List<String>> PurchaseCart(int creditCard, String address) {
        return service.PurchaseCart(creditCard,address);
    }

    @Override
    public int GetItemQuantity(int storeId, int productId) {
        Response r = service.getStoreProductQuantity(storeId,productId);
        if(r.isError())
            return -100;
        return (int)r.getValue();
        //return service.getItemQuantity(productId);
        //TODO WAITING FOR SERVICE IMPLEMENTATION
    }

    @Override
    public Response<String[]> GetEmployeeInfo(String EmployeeUserName, int storeId) {
        //return service.get;
        //return service.getEmployeeInfo(EmployeeUserName,storeId);
        return null;//TODO WAITING FOR SERVICE IMPLEMENTATION OF THIS METHOD
    }

    @Override
    public List<ServiceStore> FilterSearch(List<ProductFilter> productFilters, List<StoreFilter> storeFilters){
        Response<List<ServiceStore>> r= service.FilterProductSearch(productFilters,storeFilters);
        if(r.isError())
            return new ArrayList<>();
        return r.getValue();
    }

    @Override
    public boolean RateAndCommentOnProduct(int productId, int storeId, String comment, int rating) {
        return !service.addProductRateAndComment(productId,storeId,rating,comment).isError();

    }
    @Override
    public boolean RateStore(int storeId,int rating){
        return !service.addStoreRate(storeId,rating).isError();
    }

    @Override
    public boolean RateAndCommentOnStore(int storeId,String comment, int rating) {
        return !service.addStoreRateAndComment(storeId,rating,comment).isError();
    }


}