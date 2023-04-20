package Bridge;

import DomainLayer.Response;

import java.util.List;

public interface Bridge {
    /**
     *
     * @return return corresponding response
     */
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
     * Function to check if the user is online
     * @param Username - The name of the user we want to check
     * @return A response with a value of true/false corresponding to if the user is online
     */
    boolean IsOnline(String Username);

    /**
     *
     * @param storeName new store's name
     * @return true if done successfully, false otherwise
     */
    boolean OpenNewStore(String storeName);

    /**
     *
     * @param productName The name of the product to be added
     * @param description The description of the product to be added
     * @return true if done successfully, false otherwise
     */

    boolean AddProduct(String storeName,String productName,String description, int price, int amount);

    /**
     *
     * @param username username of user to appoint
     * @param storeName store in which to appoint the user
     * @return true if done successfully, false otherwise
     */
    boolean AppointOwner(String username,String storeName);

    /**
     *
     * @param username username of the user to remove
     * @param storeName store in which the user works in
     * @return true if done successfully, false otherwise
     */
    boolean RemoveOwner(String username,String storeName);
    /**
     *
     * @param username username of user to appoint
     * @param storeName store in which to appoint the user
     * @return true if done successfully, false otherwise
     */
    boolean AppointManager(String username,String storeName);

    /**
     *
     * @param username username of the user to remove
     * @param storeName store in which the user works in
     * @return true if done successfully, false otherwise
     */
    boolean RemoveManager(String username,String storeName);

    /**
     *
     * @param productName the name of the product to be removed
     * @return true if done successfully, false otherwise
     */
    boolean RemoveProduct(String storeName,String productName);

    /**
     *
     * @param OldName name of the product whose name change is pending
     * @param newName new name for the product
     * @return true if done successfully, false otherwise
     */
    boolean EditProductName(String storeName,String OldName,String newName);

    /**
     *
     * @param productName product whose description change is pending
     * @param newDesc new description
     * @return true if done successfully, false otherwise
     */
    boolean EditDescription(String storeName,String productName, String newDesc);

    /**
     *
     * @param storeName store where product is
     * @param prodictName product to edit
     * @param newPrice new price
     * @return true if done successfully, false otherwise
     */
    boolean EditPrice(String storeName,String prodictName,int newPrice);
    /**
     *
     * @param storeName store to close
     * @return true if done successfully, false otherwise
     */
    boolean CloseStore(String storeName);

    /**
     *
     * @param username user to add permission to
     * @param storeName store where the user is manager at
     * @param index an array containing at most 11 indexed who range from 1 to 11, each representing a permission to be added. index values:
     *      *              1: Can Manage Stock,2: Can Change Policy And Discounts,3: Can Set Constraints,4: Can Appoint Store Owner,5: Can Remove Store Owner,
     *      *              6: Can Appoint Store Manager,7: Can Change Permissions For Store Manager,8: Can Remove Store Manager,9: Can See Staff And Permissions,
     *      *              10: Can See Comments And Rating, 11: Can See Purchase History
     * @return true if done successfully, false otherwise
     */
    boolean AddPermission(String username,String storeName,int[] index);

    /**
     *
     * @param index same as above
     * @return true if done successfully, false otherwise
     */
    boolean RemovePermission(String username,String storeName,int[] index);

    /**
     *
     * @param query product or store search
     * @return return corresponding response (fails if no store and products were found)
     */
    List<String> ProductSearch(String query);
    /**
     *
     * @param storeName name of the store the product is in
     * @param productName the product's name to rate
     * @param rating the rating to give to the product
     * @return expected return: list of all found product names
     */
    boolean RateProduct(String storeName,String productName, int rating);
    /**
     *
     * @param storeName store whose purchase history we expect to return
     * @return return corresponding response (fails if no store of such name exists, or if no purchases were made from store)
     */
    List<String> UserPurchaseHistory(String storeName);

    /**
     *
     * @return Response either containing the purchase history of the user, or an error message
     */
    List<String> StorePurchaseHistory();

}
