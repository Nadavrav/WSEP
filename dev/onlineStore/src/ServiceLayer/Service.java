package ServiceLayer;

import DomainLayer.Facade;
import DomainLayer.Response;
import DomainLayer.Stores.StoreProduct;
import DomainLayer.Users.*;
import ServiceLayer.ServiceObjects.Fiters.Filter;
import ServiceLayer.ServiceObjects.ServiceProduct;

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

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");

    }

    public Response<?> logout() {//3.1

        try{
            visitorId=facade.logout(visitorId);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>(visitorId);
    }

    public Response<?> addProductToCart(String productId) {//2.3


        try{
            facade.addProductToCart(productId,visitorId);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");

    }

    public Response<?> removeProductFromCart(String productId){
        try{
            facade.removeProductFromCart(productId,visitorId);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");

    }

    public Response<?> changeCartProductQuantity(String productId,int newAmount){
        try{
            facade.changeCartProductQuantity(productId,newAmount,visitorId);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }


    public Response<?> getProductsInMyCart() {//2.4
        String products;
        try{
             products = facade.getProductsInMyCart(visitorId);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>(products);
    }

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


    public Response<?> addStoreRate(int storeID,int rate){

        try{
            facade.addStoreRate(visitorId,storeID,rate);

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
    public Response<?> addProductRateAndComment(String productID,int rate,String comment) {
        try{
            facade.addProductRateAndComment(visitorId,productID,rate,comment);

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

    public Response<?> GetStoreRate(int StoreId) throws Exception {
        try {
            return new Response<Double>(facade.GetStoreRate(visitorId, StoreId));
        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }

    }
    public Response<?> GetStoreProductRate(String ProductId) throws Exception{
        try {
            return new Response<Double>(facade.GetStoreProductRate(visitorId, ProductId));
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
    public Response<String> AddProduct(int storeId,String productName, double price, String category, int quantity, String description) {

        try{
            return new Response<>(facade.AddProduct(visitorId,storeId,productName,price,category,quantity,description));
        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }

    public Response<?> RemoveProduct(String ProductId) {

        try{
            facade.RemoveProduct(visitorId, ProductId);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }

    public Response<?> UpdateProductQuantity( String productID,int quantity) {
        try {
            facade.UpdateProductQuantity(visitorId,productID,quantity);
        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }
    public Response<?> IncreaseProductQuantity(String productID,int quantity) {
        try {
            facade.IncreaseProductQuantity(visitorId,productID,quantity);
        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");


    }
    public Response<?> UpdateProductName(String productID,String Name) {
        try {
            facade.UpdateProductName(visitorId,productID,Name);
        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");

    }

    public Response<?> UpdateProductPrice(String productID,double price) {
        try {
            facade.UpdateProductPrice(visitorId,productID,price);
        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }
    public Response<?> UpdateProductCategory(String productID,String category) {
        try {
            facade.UpdateProductCategory(visitorId,productID,category);
        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");

    }
    public Response<?> UpdateProductDescription(String productID,String description) {
        try {
            facade.UpdateProductDescription(visitorId,productID,description);
        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");

    }
    //2.2 search  product
    public Response<?> SearchProductByName( String query) {
         String product;
        try{
            product =facade.SearchProductByName(query);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>(product);
    }

    //Need to change from response
    public Response<?> SearchProductByCategory( String query) {

        try{
            facade.SearchProductByCategory(query);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }

    //Need to change from response
    public Response<List<String>> SearchProductBykey( String query) {

        try{
            return new Response<List<String>>(facade.SearchProductBykey(query))
            ;

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }

    }

    //2.1
    public Response<List<ServiceProduct>> FilterProductSearch(List<Filter> filters){
        try{
            List<StoreProduct> StoreProducts=Facade.getInstance().FilterProductSearch(filters);
            List<ServiceProduct> products=new ArrayList<>();
            for(StoreProduct product:StoreProducts){
                products.add(new ServiceProduct(product));
            }
            return new Response<>(products);
        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }
    public Response<?> GetInformation(int StoreId) {

        try{
            facade.GetInformation(StoreId);

        }catch (Exception e){
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
    public Response<List<String>> PurchaseCart(int visitorCard,String address){
        try{
            return new Response<>(facade.purchaseCart(visitorId,visitorCard,address));
        }
        catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
    }
}
