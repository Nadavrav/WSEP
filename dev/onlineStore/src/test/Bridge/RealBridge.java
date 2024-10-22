package Bridge;

import DAL.TestsFlags;
import DomainLayer.Response;
import ServiceLayer.ServiceObjects.Fiters.ProductFilters.ProductFilter;
import ServiceLayer.Service;
import DomainLayer.Users.Permission;
import ServiceLayer.ServiceObjects.Fiters.StoreFilters.StoreFilter;
import ServiceLayer.ServiceObjects.ServiceBid;
import ServiceLayer.ServiceObjects.ServiceCart;
import ServiceLayer.ServiceObjects.ServiceDiscounts.ServiceAppliedDiscount;
import ServiceLayer.ServiceObjects.ServiceDiscounts.ServiceDiscount;
import ServiceLayer.ServiceObjects.ServiceDiscounts.ServiceDiscountInfo;
import ServiceLayer.ServiceObjects.ServicePolicies.ServicePolicy;
import ServiceLayer.ServiceObjects.ServicePolicies.ServicePolicyInfo;
import ServiceLayer.ServiceObjects.ServiceStore;
import org.opentest4j.TestSkippedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RealBridge implements Bridge {
    Service service;
    public RealBridge(){
        TestsFlags.getInstance().setTests();
        service=new Service();
    }
    @Override
    public boolean initialize() {
        service.loadData();
        return true;
    }
    @Override
    public boolean reset() {
        service.reset();
        return true;
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
        return -1;
    }

    @Override
    public Integer AddProduct(int storeId, String productName, String description, double price, int amount) {
        Response<Integer> r= service.AddProduct(storeId,productName,price,"test",amount,description);
        if(!r.isError())
            return r.getValue();
        return -1;
    }
    @Override
    public Integer AddProduct(int storeId, String productName, String description,String category, double price, int amount){
        Response<Integer> r= service.AddProduct(storeId,productName,price,category,amount,description);
        if(!r.isError())
            return r.getValue();
        return -1;
    }


    @Override
    public boolean AppointOwner(String username, int storeId) {
        Response r =service.appointNewStoreOwner(username, storeId);
        return !r.isError();
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
        Response r = service.RemoveProduct(productId,storeId );
        return !r.isError();
    }

    @Override
    public boolean EditProductName(int productId, int storeId, String newName) {
        return !service.UpdateProductName(productId,storeId, newName).isError();
    }

    @Override
    public boolean EditDescription(int productId, int storeId, String newDesc) {
        return !service.UpdateProductDescription(productId,storeId, newDesc).isError();
    }

    @Override
    public boolean EditPrice(int productId, int storeId, int newPrice) {
        Response r = service.UpdateProductPrice(productId,storeId, newPrice);
        return !r.isError();
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
        Response r = service.changeStoreManagerPermission(username,storeId,permissions);
        return !r.isError();
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
        return !service.addProductToCart(productId,storeId,1 ).isError();
    }

    @Override
    public boolean removeFromCart(int productId, int storeId) {
        return !service.removeProductFromCart(productId,storeId).isError();
    }

    @Override
    public Response<ServiceCart> OpenCart() {
        return service.getProductsInMyCart();
    }
    @Override
    public Response<String> OpenStringCart() {
        Response<ServiceCart> r=service.getProductsInMyCart();
        if(!r.isError())
            return new Response<>(r.getValue().toString());
        return new Response<>(r.getMessage(),true);
    }

    @Override
    public boolean CartChangeItemQuantity(int productId,int storeId, int newQuantity) {
        return !service.changeCartProductQuantity(productId,storeId,newQuantity).isError();
    }

    @Override
    public Response<List<String>> PurchaseCart(String holderName,String visitorCard,String expireDate, int cvv , String id ,String address,String city,String country ,String zip) {

        return service.PurchaseCart(holderName, visitorCard, expireDate, cvv, id, address, city, country, zip);
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
    @Override
    public Response<ServiceDiscountInfo> addDiscount(ServiceDiscount serviceDiscount, int storeId){
        return service.addDiscount(serviceDiscount,storeId);
    }
    @Override
    public Response<ServiceDiscountInfo> removeDiscount(int discountId, int storeId){
        return service.removeDiscount(discountId,storeId);
    }
    @Override
    public Response<ServicePolicyInfo> addPolicy(ServicePolicy servicePolicy, int storeId){
        return service.addPolicy(servicePolicy,storeId);
    }
    @Override
    public Response<ServicePolicyInfo> removePolicy(int policyId, int storeId){
        return service.removePolicy(storeId,policyId);
    }
    @Override
    public Response<Collection<ServiceDiscountInfo>> getDiscountInfo(int storeId){
        return service.getStoreDiscountInfo(storeId);
    }
    @Override
    public Response<ServiceAppliedDiscount> getBagDiscountInfo(int storeId){
        return service.getBagDiscountInfo(storeId);
    }

    @Override
    public Response<ServiceBid> addNewBid(int productId, int storeId, int amount, int newPrice) {
        return service.addNewBid(productId,storeId,amount,newPrice);
    }

    @Override
    public Response<ServiceBid> counterOfferBid(int productId, int storeId, String userName, double newPrice, String message) {
        return service.counterOfferBid(productId,storeId,userName,newPrice,message);
    }

    @Override
    public Response<?> acceptCounterOffer(int productId, int storeId) {
        return service.acceptCounterOffer(productId,storeId);
    }

    @Override
    public Response<?> rejectCounterOffer(int productId,int storeId) {
        return service.rejectCounterOffer(productId,storeId);
    }

    @Override
    public Response<?> voteOnBid(int productId, int storeId, String userName, boolean vote) {
        return service.voteOnBid(productId,storeId,userName,vote);
    }

    @Override
    public Response<Collection<ServiceBid>> geStoreBids(int storeId) {
        return service.geStoreBids(storeId);
    }

    @Override
    public Response<Collection<ServiceBid>> getUserBids() {
        return service.getUserBids();
    }
    @Override
    public Response<ServiceCart> getCartProducts(){
        return service.getProductsInMyCart();
    }


}