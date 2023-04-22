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

    public Response<?> ExitSiteVisitor(int id) {//1.2
        try{
            facade.ExitSiteVisitor(id);

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
    public Response<?> changeStoreManagerPermission( String username, int storeID, Permission permission){

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


    //----------Store-----------
    // open Store
    public Response<?> OpenStore( int StoreId, String storeName) {

        try{
            facade.OpenNewStore(visitorId, storeName);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
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
    public Response<?> AddProduct(int storeId,String productName, double price, String category, int quantity, String kws,String description) {

        try{
            facade.AddProduct(visitorId,storeId,productName,price,category,quantity,kws,description);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }

    public Response<?> RemoveProduct(int StoreId, String ProductId) {

        try{
            facade.RemoveProduct(visitorId, StoreId, ProductId);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }

    public Response<?> UpdateStore( int StoreId, String productID, String Id, String name, double price, String category, int quantity, String kws,String desc) {

        try{
            facade.UpdateStore(visitorId, StoreId, productID, Id, name, price, category, quantity, kws,desc);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }

    //2.2 search  product
    public Response<?> SearchProductByName( String Pid) {
         String product;
        try{
            product =facade.SearchProductByName(visitorId, Pid);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>(product);
    }

    //Need to change from response
    public Response<?> SearchProductByCategory( String Pid) {

        try{
            facade.SearchProductByCategory(visitorId, Pid);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }

    //Need to change from response
    public Response<?> SearchProductBykey( String Pid) {

        try{
            facade.SearchProductBykey(visitorId, Pid);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }

    //2.1
    //Need to change from response
    public Response<?> GetInformation(int StoreId) {

        try{
            facade.GetInformation(StoreId);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }

    //6.4
    //Need to change from response
    public Response<?> GetIHistoryPurchase(int StoreId) {

        try{
            facade.GetIHistoryPurchase(StoreId, visitorId);

        }catch (Exception e){
            return new Response<>(e.getMessage(),true);
        }
        return new Response<>("Success");
    }
}
