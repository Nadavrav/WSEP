package Bridge;

import AcceptenceTests.ProxyClasses.CreditCardProxy;
import DomainLayer.Response;
import ServiceLayer.ServiceObjects.Fiters.Filter;
import ServiceLayer.Service;
import DomainLayer.Users.Permission;
import ServiceLayer.ServiceObjects.PurchaseRecord;
import ServiceLayer.ServiceObjects.ServiceProduct;
import org.opentest4j.TestAbortedException;
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
    public String AddProduct(int storeId, String productName, String description, int price, int amount) {
        Response<String> r= service.AddProduct(storeId,productName,price,"test",amount,description);
        if(!r.isError())
            return r.getValue();
        return "Error";
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
    public boolean RemoveProduct(String productId) {
        return !service.RemoveProduct(productId).isError();
    }

    @Override
    public boolean EditProductName(String productId, String newName) {
        return !service.UpdateProductName(productId, newName).isError();
    }

    @Override
    public boolean EditDescription(String productId, String newDesc) {
        return !service.UpdateProductName(productId, newDesc).isError();
    }

    @Override
    public boolean EditPrice(String productId, int newPrice) {
        return !service.UpdateProductPrice(productId, newPrice).isError();
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

    @Override
    public List<String> ProductSearch(String query) {

        Response<List<String>> r=service.SearchProductBykey(query);
        if(r.isError())
            throw new TestAbortedException("TEST SHOULD NOT FAIL");
        return r.getValue();
    }

    @Override
    public boolean RateProduct(int storeId, String productId, int rating) {
        return false; //TODO IMPL
    }

    @Override
    public List<String> UserPurchaseHistory(int storeId) {
        return null; //TODO: NIKITA
    }

    @Override
    public List<String> StorePurchaseHistory() {
        return null; //TODO: NIKITA
    }

    @Override
    public boolean addToCart(String productId) {
        return !service.addProductToCart(productId).isError();
    }

    @Override
    public boolean removeFromCart(String productId) {
        return !service.RemoveProduct(productId).isError();
    }

    @Override
    public Response<String> OpenCart() {
        return (Response<String>)service.getProductsInMyCart(); //TODO: NIKITA
    }

    @Override
    public boolean CartChangeItemQuantity(String productId, int newQuantity) {
        return false;
        //TODO WAITING FOR SERVICE IMPLEMENTATION
        //return CartChangeItemQuantity(String productId, int newQuantity);
    }

    @Override
    public boolean PurchaseCart(CreditCardProxy credit) {
        //TODO WAITING FOR SERVICE IMPLEMENTATION
        //return service.PurchaseCart(credit);
        return false; //TODO: NIKITA
    }

    @Override
    public int GetItemQuantity(String productId) {
        return 5;
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
    public Response<List<PurchaseRecord>> GetPurchaseHistory(int storeId) {
       // return service.GetStoreHistoryPurchase(storeId);
        return null; //TODO Waiting for
    }
    @Override
    public List<ServiceProduct> FilterSearch(List<Filter> filters){
        Response<List<ServiceProduct>> r= service.FilterProductSearch(filters);
        if(r.isError())
            return new ArrayList<>();
        return r.getValue();
    }

    @Override
    public boolean RateProduct(String productId, int rating) {
        //Response<?> r=service
        return false;
    }


}