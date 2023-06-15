package ServiceLayer;

import DomainLayer.Facade;
import DomainLayer.Response;

import DomainLayer.Stores.Policies.Policy;

import DomainLayer.Stores.Discounts.Discount;

import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Stores.Store;
import DomainLayer.Users.*;


import ServiceLayer.ServiceObjects.Fiters.ProductFilters.ProductFilter;



import ServiceLayer.ServiceObjects.Fiters.StoreFilters.StoreFilter;


import ServiceLayer.ServiceObjects.ServiceDiscounts.*;
import ServiceLayer.ServiceObjects.ServicePolicies.ServicePolicy;

import ServiceLayer.ServiceObjects.ServiceCart;


import ServiceLayer.ServiceObjects.ServicePolicies.ServicePolicyInfo;
import ServiceLayer.ServiceObjects.ServiceStore;

import ServiceLayer.ServiceObjects.ServiceUser;

import java.util.*;

public class Service {

    private final Facade facade;
    private int visitorId;

    public Service(){
        facade = Facade.getInstance();
    }

    public Response<Integer> EnterNewSiteVisitor() {//1.1
        try{
            visitorId= facade.EnterNewSiteVisitor();

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>(visitorId);
    }

    public Response<Boolean> isAdmin(){
        Boolean isAdmin;
        try{
            isAdmin=facade.isAdmin(visitorId);
        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>(isAdmin);
    }

    public Response<?> ExitSiteVisitor() {//1.2
        try{
            facade.ExitSiteVisitor(visitorId);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");

    }

    public Response<?> loadData() {
        try{
            facade.loadData();

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }
    public Response<?> reset() {
        try{
            facade.resetData();

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }


    public Response<?> Register( String userName, String password) {//1.3

        try{
            facade.Register(visitorId,userName,password);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");

    }

    public Response<?> login( String userName, String password) {//1.4

        try{
            facade.login(visitorId,userName,password);

        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");

    }

    public Response<?> logout() {//3.1

        try{
            visitorId=facade.logout(visitorId);
        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>(visitorId);
    }

    public Response<?> addProductToCart(int productId, int storeId,int amount) {//2.3


        try{
            facade.addProductToCart(productId,storeId,amount,visitorId);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");

    }


    public Response<?> removeProductFromCart(int productId, int storeId){
        try{
            facade.removeProductFromCart(productId,storeId,visitorId);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");

    }

    public Response<?> changeCartProductQuantity(int productId, int storeId,int newAmount){
        try{
            facade.changeCartProductQuantity(productId,storeId,newAmount,visitorId);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }


    public Response<ServiceCart> getProductsInMyCart() {//2.4
        try {
            return new Response<>(new ServiceCart(facade.getProductsInMyCart(visitorId)));

        } catch (Exception e) {
            return new Response<>(e.getMessage(), true);
        }
    }
    /*public Response<?> getProductsInMyCart1()
    {
        try
        {
            Map<Integer,Bag> cart = facade.getCart(visitorId).getBags();//Map from store id to bag
            List<ServiceBag> serviceCart;
            for (Integer key: cart.keySet()) {
                //serviceCart.add(new ServiceBag(cart.get(key),key));
            }
        }
        catch (Exception e)
        {

        }
        return null;
    }
  */
    public Response<?> appointNewStoreOwner( String appointedUserName, int storeId) {//4.4

        try{
            facade.appointNewStoreOwner(visitorId,appointedUserName,storeId);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }

    public Response<?> appointNewStoreManager(String appointedUserName,int storeId){//4.6

        try{
            facade.appointNewStoreManager(visitorId,appointedUserName,storeId);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }


    // ??
    public Response<?> changeStoreManagerPermission( String username, int storeID, List<Permission> permission){

        try{
            facade.changeStoreManagerPermission(visitorId,username,storeID,permission);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");

    }

    public Response<?> getRolesData(int storeId){//4.11
        String rolesData;
        try{
            rolesData  = facade.getRolesData(visitorId,storeId);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>(rolesData);
    }


    public Response<?> addStoreRate(int storeID,double rating){

        try{
            facade.addStoreRate(visitorId,storeID,rating);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }
    public Response<?> addStoreRateAndComment(int storeID,int rate,String comment){
        try{
            facade.addStoreRateAndComment(visitorId,storeID,rate,comment);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }
    public Response<?> addProductRateAndComment(int productId,int storeId,int rate,String comment) {
        try{
            facade.addProductRateAndComment(visitorId,productId,storeId,rate,comment);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }

    //----------Store-----------
    // open Store
    public Response<Integer> OpenStore(String storeName) {

        try {
            return new Response<Integer>(facade.OpenNewStore(visitorId, storeName));
        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }

    public Response<?> GetStoreRate(int StoreId) {
        try {
            return new Response<Double>(facade.GetStoreRate(visitorId, StoreId));
        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }
    public Response<?> GetStoreProductRate(int productId,int storeId) {
        try {
            return new Response<Double>(facade.GetStoreProductRate(visitorId, productId,storeId));
        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }

    }


    //close store
    public Response<?> CloseStore(int StoreId) {

        try{
            facade.CloseStore(visitorId, StoreId);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }


    // ניהול מלאי 4.1
    public Response<Integer> AddProduct(int storeId,String productName, double price, String category, int quantity, String description) {

        try{
            return new Response<>(facade.AddProduct(visitorId,storeId,productName,price,category,quantity,description));
        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }

    public Response<?> RemoveProduct(int ProductId, int storeId) {

        try{
            facade.RemoveProduct(visitorId,storeId, ProductId);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }

    public Response<?> UpdateProductQuantity( int productId,int storeId,int quantity) {
        try {
            facade.UpdateProductQuantity(visitorId,productId,storeId,quantity);
        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }
    public Response<?> IncreaseProductQuantity(int productId,int storeId,int quantity) {
        try {
            facade.IncreaseProductQuantity(visitorId,productId,storeId,quantity);
        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");


    }
    public Response<?> UpdateProductName(int productId,int storeId,String Name) {
        try {
            facade.UpdateProductName(visitorId,productId,storeId,Name);
        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");

    }

    public Response<?> UpdateProductPrice(int productId,int storeId,double price) {
        try {
            facade.UpdateProductPrice(visitorId,productId,storeId,price);
        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }
    public Response<?> UpdateProductCategory(int productId,int storeId,String category) {
        try {
            facade.UpdateProductCategory(visitorId,productId,storeId,category);
        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");

    }
    public Response<?> UpdateProductDescription(int productId,int storeId,String description) {
        try {
            facade.UpdateProductDescription(visitorId,productId,storeId,description);
        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");

    }
    //2.2 search  product
    //public Response<?> SearchProductByName( String query) {
    //     String product;
    //    try{
    //        product =facade.SearchProductByName(query);
//
    //    }catch (Exception e){
    //        return new Response<>(e.getMessage(),true);
    //    }
    //    return new Response<>(product);
    //}

    //Need to change from response
   // public Response<?> SearchProductByCategory( String query) {
//
   //     try{
   //         facade.SearchProductByCategory(query);
//
   //     }catch (Exception e){
   //         return new Response<>(e.getMessage(),true);
   //     }
   //     return new Response<>("Success");
   // }

    /**
     * can be easily be replaced by adding a keyword filter search in filter search
     */
    //Need to change from response
   // public Response<List<String>> SearchProductBykey( String query) {
//
   //     try{
   //         return new Response<List<String>>(facade.SearchProductBykey(query))
   //         ;
//
   //     }catch (Exception e){
   //         return new Response<>(e.getMessage(),true);
   //     }
//
   // }

    //2.1

    public Response<List<ServiceStore>> FilterProductSearch(List<ProductFilter> productFilters, List<StoreFilter> storeFilters){
        try{
            Map<Store,List<StoreProduct>> productMap =Facade.getInstance().FilterProductSearch(storeFilters,productFilters);
            List<ServiceStore> stores=new ArrayList<>();
            for(Store store:productMap.keySet()){
                ServiceStore serviceStore=new ServiceStore(store);
                serviceStore.addAll(productMap.get(store));
                stores.add(serviceStore);
            }
            return new Response<>(stores);
        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }

    }
    public Response<?> GetInformation(int StoreId) {

        try{
            facade.GetInformation(StoreId);
        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }
    //6.4
    public Response<List<String>> GetStoreHistoryPurchase(int StoreId)  {
        try{
            return new Response<List<String>>(facade.GetStoreHistoryPurchase(StoreId,visitorId));

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }
    public Response<String> GetUserHistoryPurchase(String userName) {
        try{
            facade.GetUserHistoryPurchase(userName,visitorId);
        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }
    public Response<List<String>> PurchaseCart(int visitorCard, String address){
        try{
            return new Response<>(facade.purchaseCart(visitorId,visitorCard,address));
        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }

    public Response<?> deleteUser(String userName){
        try{
            facade.deleteUser(visitorId,userName);
            return new Response<>("Success");
        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }
    public Response<?> getStoresName(){
        try{
           return new Response<>(facade.getStoresName());
        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }

    public Response<?> getStoreProduct(int StoreId){
        try{
            return new Response<>(facade.getStoreProduct(StoreId));
        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }

    public Response<?> getStoreRatingList(int storeId){
        try{
            return new Response<>(facade.getStoreRatingList(storeId));
        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }

    public Response<?> getProductRatingList(int storeId ,int productId) {
        try{
            return new Response<>(facade.getProductRatingList(storeId,productId));
        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }
            

    public Response<List<ServiceUser>> getRegisteredUsersInfo(){
        try {
            ArrayList<ServiceUser> serviceUsers = new ArrayList<>();
            Map<String, RegisteredUser> userMap = facade.getRegisteredUserList(visitorId);
            for (RegisteredUser registeredUser : userMap.values())
                serviceUsers.add(new ServiceUser(registeredUser));
            return new Response<>(serviceUsers);

        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }
    public Response<?> removeEmployee(int storeId,String userName){
        try {
            facade.removeEmployee(visitorId,userName,storeId);
            return new Response<>("Success");

        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }
    public Response<Collection<ServiceStore>> getStoresByUserName(String userName) {
        try {
            ArrayList<ServiceStore> serviceStores = new ArrayList<>();
            List <Store> stores = facade.getStoresByUserName(visitorId,userName);
            for(Store s : stores) {
                serviceStores.add(new ServiceStore(s));
            }
            return new Response<>(serviceStores);
        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }

    public Response<HashSet<ServicePolicy>> getStorePolicy(int storeId){
        try {
           Collection<Policy> storePolicies=facade.getStorePolicies(visitorId,storeId);
           HashSet<ServicePolicy> servicePolicies=new HashSet<>();
           for(Policy policy:storePolicies){
               servicePolicies.add(new ServicePolicyInfo(policy));
           }
           return new Response<>(servicePolicies);
        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }
    public Response<ServicePolicyInfo> addPolicy(ServicePolicy servicePolicy,int storeId){
        try {
            return new Response<>(new ServicePolicyInfo(facade.AddStorePolicy(visitorId,storeId,servicePolicy)));

        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }
    public Response<ServicePolicyInfo> removePolicy(int storeId,int policyId){
        try {
            return new Response<>(new ServicePolicyInfo(facade.removeStorePolicy(visitorId,storeId,policyId)));

        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }

    public Response<Collection<ServiceDiscountInfo>> getStoreDiscountInfo(int storeId){

        try {
            HashSet<ServiceDiscountInfo> discounts=new HashSet<>();
            for(Discount discount:facade.getStoreDiscounts(storeId)){
                discounts.add(new ServiceDiscountInfo(discount));
            }
            return new Response<>(discounts);
        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }
    public Response<ServiceDiscountInfo> addDiscount(ServiceDiscount serviceDiscount,int storeId){
        try {
           return new Response<>(new ServiceDiscountInfo(facade.addDiscount(serviceDiscount,storeId)));

        }
        catch (StackOverflowError e){
            return new Response<>("CODE ERROR: STACK OVERFLOW. PROBABLE CAUSE: DISCOUNT ADDITION TYPE UNDEFINED { addDiscount( [Discount type] ,...) } FOR RECEIVED DISCOUNT",true);
        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }

    public Response<ServiceDiscountInfo> removeDiscount(int discountId,int storeId){
        try {
            return new Response<>(new ServiceDiscountInfo(facade.removeDiscount(discountId,storeId)));
        }
        catch (StackOverflowError e){
            return new Response<>("CODE ERROR: STACK OVERFLOW. PROBABLE CAUSE: DISCOUNT ADDITION TYPE UNDEFINED { addDiscount( [Discount type] ,...) } FOR RECEIVED DISCOUNT",true);
        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }

    }
    public Response<ServiceAppliedDiscount> getBagDiscountInfo(int storeId){
        try {
            return new Response<>(new ServiceAppliedDiscount(facade.getSavingsPerProduct(visitorId,storeId)));
        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }
    /**
     * A function to get the quantity of a product in a store
     * @param storeId - the store from which the product is
     * @param productId - the id of the product
     * @return - a response containing the amount of the product or an error response if the function failed
     */
    public Response<Integer> getStoreProductQuantity(int storeId,int productId)
    {
        try {
            Integer amount = facade.getStoreProductQuantity(storeId,productId);
            Response<Integer> r = new Response<>(amount);
            return r;
        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }

    /**
     * A function to get list of the online visitors in the system
     * @return - a response containing the list of the online visitors in the system
     */
    public Response<List<ServiceUser>> getOnlineUsers()
    {
        try {
            List<ServiceUser> serviceUsers = new ArrayList<>();
            for (SiteVisitor sv : facade.getOnlineUsers())
                serviceUsers.add(new ServiceUser(sv));
            Response<List<ServiceUser>> r = new Response(serviceUsers);
            return r;
        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }

    public Response<List<ServiceUser>> getOfflineUsers()
    {
        try {
            List<ServiceUser> serviceUsers = new ArrayList<>();
            for (RegisteredUser sv : facade.getOfflineUsers())
                serviceUsers.add(new ServiceUser(sv));
            Response<List<ServiceUser>> r = new Response(serviceUsers);
            return r;
        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }

    public Response<Boolean> checkForNewMessages(String userName) {
        try{
            return new Response<>(facade.checkForNewMessages(userName));
        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }

    public Response<LinkedList<String>> getNewMessages(String userName) {
        try{
            return new Response<>(facade.getNewMessages(userName));
        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }

    public Response<Double> getTotalPrice()
    {
        try{
            return new Response<>(facade.getTotalPrice(visitorId));
        }
        catch (Exception e)
        {
            return new Response<>(e.getMessage(),true);
        }
    }

    // TODO: implement
    public Response<?> addNewBid(int productId, int storeId,int amount,int newPrice) {
        return null;
    }

    // TODO: implement
    public Response<?> acceptBid(int productId,int storeId,String userName) {
        return null;
    }

    // TODO: implement
    public Response<?> declineBid(int productId,int storeId,String userName) {
        return null;
    }

    // TODO: implement
    public Response<?> updateBid(int productId,int storeId,int newPrice) {
        return null;
    }

    public Response<Integer> getDailyIncome(int day,int month,int year) {
        try{
            return new Response<>(facade.getDailyIncome(day,month,year,visitorId));
        }
        catch (Exception e)
        {
            return new Response<>(e.getMessage(),true);
        }
    }
    
    public Response<Integer> getDailyIncomeByStore(int day,int month,int year,int storeId) {
        try{
            return new Response<>(facade.getDailyIncomeByStore(day,month,year,storeId,visitorId));
        }
        catch (Exception e)
        {
            return new Response<>(e.getMessage(),true);
        }
    }


    public Response<?> acceptAppointment(int storeId,String appointedUserName) {
        try{
            facade.acceptEmploymentRequest(visitorId,storeId,appointedUserName);
            return new Response<>("Success");
        }
        catch (Exception e)
        {
            return new Response<>(e.getMessage(),true);
        }
    }
    
    public Response<?> declineAppointment(int storeId,String appointedUserName) {
        try{
           facade.declineEmploymentRequest(visitorId,storeId,appointedUserName);
            return new Response<>("Success");
        }
        catch (Exception e)
        {
            return new Response<>(e.getMessage(),true);
        }
    }

    // TODO: implement
    public Response<?> getPermissions(int storeId,String AppointedUserName) {
        return null;
    }

    // TODO: implement
    public Response<?> getVisitorsAmountBetweenDates(int dayStart,int monthStart,int yearStart,int dayEnd,int monthEnd, int yearEnd){
        return null;
    }

    // TODO: implement
    public Response<?> getUsersWithoutStoresAmountBetweenDates(int dayStart,int monthStart,int yearStart,int dayEnd,int monthEnd, int yearEnd){
        return null;
    }

    // TODO: implement
    public Response<?> getStoreManagersOnlyAmountBetweenDates(int dayStart,int monthStart,int yearStart,int dayEnd,int monthEnd, int yearEnd){
        return null;
    }

    // TODO: implement
    public Response<?> getStoreOwnersAmountBetweenDates(int dayStart,int monthStart,int yearStart,int dayEnd,int monthEnd, int yearEnd){
        return null;
    }

    // TODO: implement
    public Response<?> getAdminsAmountBetweenDates(int dayStart,int monthStart,int yearStart,int dayEnd,int monthEnd, int yearEnd){
        return null;
    }

}

