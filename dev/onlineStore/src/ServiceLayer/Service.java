package ServiceLayer;

import DomainLayer.Facade;
import DomainLayer.Response;
import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Stores.Store;
import DomainLayer.Users.*;


import ServiceLayer.ServiceObjects.Fiters.ProductFilters.ProductFilter;



import ServiceLayer.ServiceObjects.Fiters.StoreFilters.StoreFilter;


import ServiceLayer.ServiceObjects.ServiceCart;
import ServiceLayer.ServiceObjects.Fiters.ProductFilters.ProductFilter;
import ServiceLayer.ServiceObjects.Fiters.StoreFilters.StoreFilter;

import ServiceLayer.ServiceObjects.ServiceProducts.ServiceProduct;

import ServiceLayer.ServiceObjects.ServiceStore;

import ServiceLayer.ServiceObjects.ServiceUser;

import java.util.*;

public class Service {

    private Facade facade;
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

    public Response<?> ExitSiteVisitor() {//1.2
        try{
            facade.ExitSiteVisitor(visitorId);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");

    }

    public Response<?> loadData() throws Exception {
        try{
            facade.loadData();

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

    public Response<?> addProductToCart(int productId, int storeId) {//2.3


        try{
            facade.addProductToCart(productId,storeId,visitorId);

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


    public Response<String> getProductsInMyCart() {//2.4
        String products;
        try {
            products = facade.getProductsInMyCart(visitorId);

        } catch (Exception e) {
            return new Response<>(e.getMessage(), true);
        }
        return new Response<>(products);
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
    public Response<?> getStoresName()  {
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

}
