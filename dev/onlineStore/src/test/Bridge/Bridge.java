package Bridge;

import DomainLayer.Response;
import ServiceLayer.ServiceObjects.Fiters.ProductFilters.ProductFilter;
import ServiceLayer.ServiceObjects.Fiters.StoreFilters.StoreFilter;
import ServiceLayer.ServiceObjects.ServiceCart;
import ServiceLayer.ServiceObjects.ServiceDiscounts.ServiceAppliedDiscount;
import ServiceLayer.ServiceObjects.ServiceDiscounts.ServiceDiscount;
import ServiceLayer.ServiceObjects.ServiceDiscounts.ServiceDiscountInfo;
import ServiceLayer.ServiceObjects.ServicePolicies.ServicePolicy;
import ServiceLayer.ServiceObjects.ServicePolicies.ServicePolicyInfo;
import ServiceLayer.ServiceObjects.ServiceStore;

import java.util.Collection;
import java.util.List;
public interface Bridge {
    boolean reset();
    boolean initialize();
    /**
     * A function to enter the market
     * @return corresponding response(Should always be success)
     */
    boolean EnterMarket();
    /**
     * A function to exit the market
     * @return corresponding response(Should always be success)
     */
    boolean ExitMarket();

    /**
     * A function to register to the market
     * @param UserName - The username the user entered
     * @param Password -  the password the user entered
     * @return corresponding response(fail/success)
     */
    boolean Register(String UserName, String Password);

    /**
     * A function to login into market system
     * @param UserName - The username of the user you want to log into
     * @param Password - The password of the user you want to log into
     * @return corresponding response(fail/success)
     */
    boolean Login(String UserName, String Password);

    /**
     * A function to logout of market system
     * @return corresponding response(fail/success)
     */
    boolean Logout();


    /**
     *
     * @param storeName new store's name
     * @return true if done successfully, false otherwise
     */
    Integer OpenNewStore(String storeName);

    /**
     * @param description The description of the product to be added
     * @return true if done successfully, false otherwise
     */

    Integer AddProduct(int storeId, String productName, String description, double price, int amount);
    Integer AddProduct(int storeId, String productName, String description,String category, double price, int amount);


    /**
     *
     * @param username username of user to appoint
     * @return true if done successfully, false otherwise
     */
    boolean AppointOwner(String username,int storeId);

    /**
     *
     * @param username username of the user to remove
     * @return true if done successfully, false otherwise
     */
    boolean RemoveOwner(String username,int storeId);
    /**
     *
     * @param username username of user to appoint
     * @return true if done successfully, false otherwise
     */
    boolean AppointManager(String username,int storeId);

    /**
     *
     * @param username username of the user to remove
     * @return true if done successfully, false otherwise
     */
    boolean RemoveManager(String username,int storeId);

    /**
     * @param productId the name of the product to be removed
     * @param storeId
     * @return true if done successfully, false otherwise
     */
    boolean RemoveProduct(int productId, int storeId);

    /**
     * @param productId
     * @param storeId
     * @param newName   new name for the product
     * @return true if done successfully, false otherwise
     */
    boolean EditProductName(int productId, int storeId, String newName);

    /**
     * @param productId product whose description change is pending
     * @param storeId
     * @param newDesc   new description
     * @return true if done successfully, false otherwise
     */
    boolean EditDescription(int productId, int storeId, String newDesc);

    /**
     * @param productId
     * @param storeId
     * @param newPrice    new price
     * @return true if done successfully, false otherwise
     */
    boolean EditPrice(int productId, int storeId, int newPrice);
    /**
     *
     * @param storeId store to close
     * @return true if done successfully, false otherwise
     */
    boolean CloseStore(int storeId);

    /**
     *
     * @param username user to add permission to
     * @param storeId store where the user is manager at
     * @param index an array containing at most 11 indexed who range from 1 to 11, each representing a permission to be added. index values:
     *      *              1: Can Manage Stock,2: Can Change Policy And Discounts,3: Can Set Constraints,4: Can Appoint Store Owner,5: Can Remove Store Owner,
     *      *              6: Can Appoint Store Manager,7: Can Change Permissions For Store Manager,8: Can Remove Store Manager,9: Can See Staff And Permissions,
     *      *              10: Can See Comments And Rating, 11: Can See Purchase History
     * @return true if done successfully, false otherwise
     */
    boolean AddPermission(String username,int storeId,int[] index);

    /**
     *
     * @param index same as above
     * @return true if done successfully, false otherwise
     */
    boolean RemovePermission(String username,int storeId,int[] index);

    /**
     *
     * @param query search query
     * @return return corresponding response (fails if no store and products were found)
     */
    //  List<String> ProductSearch(String query);
    /**
     * @param productId the product's name to rate
     * @param storeId
     * @param rating    the rating to give to the product
     * @return expected return: list of all found product names
     */
    boolean RateProduct(int productId, int storeId, int rating);
    /**
     *
     * @param userName the username whos purchase history we want
     * @return return corresponding response (fails if no store of such name exists, or if no purchases were made from store)
     */
    String UserPurchaseHistory(String userName);

    /**
     * @param storeId the id of the store which we want the history of
     * @return Response either containing the purchase history of the user, or an error message
     */
    List<String> StorePurchaseHistory(int storeId);

    /**
     * A function to add a product to cart
     *
     * @param productId - The product id number
     * @param storeId
     * @return true is done successfully, false otherwise
     */
    boolean addToCart(int productId, int storeId);
    /**
     * A function to remove a product to cart
     *
     * @param productId - The product id number
     * @param storeId
     * @return true is done successfully, false otherwise
     */
    boolean removeFromCart(int productId, int storeId);

    /**
     * Function to open a user's cart
     * @return A string list of id's of the items in the user's cart
     */
    Response<ServiceCart> OpenCart();
    Response<String> OpenStringCart();
    /**
     * A function to change the quantity of an item in a user's cart
     * @param productId - the id of the item whose quantity we want to change
     * @param newQuantity - the new amount of the item
     * @return true is done successfully, false otherwise
     */
    boolean CartChangeItemQuantity(int productId,int storeId,int newQuantity);

    /**
     * A function to purchase the contents of the cart
     * @param creditCard - the credit card
     * @param address - address of the purchaser
     * @return a response that contains a list of the items it failed to purchase
     */
    Response PurchaseCart(int creditCard,String address);

    /**
     * A function to get a requested item quantity
     * @param storeId - the id of the store that the product belongs to
     * @param productId - the id of the item
     * @return the quantity of the item if successful, -1 otherwise
     */
    int GetItemQuantity(int storeId, int productId);

    /**
     * A function to get the info on an employee
     * @param EmployeeUserName - the employee username
     * @return a response with a string array of the employee data(UserName,Etc) as its value
     */
    Response<String[]> GetEmployeeInfo(String EmployeeUserName,int storeId);
    List<ServiceStore> FilterSearch(List<ProductFilter> productFilters, List<StoreFilter> storeFilters);
    boolean RateAndCommentOnProduct(int productId, int storeId, String comment, int rating);
    boolean RateStore(int storeId,int rating);
    boolean RateAndCommentOnStore(int storeId,String comment,int rating);
    Response<ServiceDiscountInfo> addDiscount(ServiceDiscount serviceDiscount, int storeId);
    Response<ServiceDiscountInfo> removeDiscount(int id, int storeId);
    Response<ServicePolicyInfo> addPolicy(ServicePolicy conditionRecord, int storeId);
    Response<ServicePolicyInfo> removePolicy(int id, int storeId);
    Response<Collection<ServiceDiscountInfo>> getDiscountInfo(int storeId);
    public Response<ServiceAppliedDiscount> getBagDiscountInfo(int storeId);


}