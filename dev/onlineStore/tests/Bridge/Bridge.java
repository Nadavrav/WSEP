package Bridge;

import DomainLayer.Response;

import java.util.List;

public interface Bridge {


    Response<?> login(String UserName, String Password);

    /**
     *
     * @param userId id of calling user, function fails if user not logged in
     * @param query product or store search
     * @return return corresponding response (fails if no store and products were found)
     */
    Response<?> StoreAndProductSearch(int userId,String query);

    /**
     *
     * @param userId id of calling user, function fails if user not logged in
     * @param filters list of parameters such as: product name, product price range,
     *                  product name/manufacturer/category/description,key words, store name/rating/owner(s), and possibly more
     * @return  return corresponding response (fails if no product was found)
     */
    Response<?> SearchProduct(int userId,List<?> filters); //TODO: NEED TO CLOSE UP THE EXACT QUERY OPTIONS, CAN'T DO TESTS OTHERWISE

    /**
     *
     * @param userId id of calling user, function fails if user not logged in
     * @param storeName new store's name
     * @return return corresponding response (fails if a store with the same name exists) //TODO: can 2 stores with the same name exist? ask domain
     */
    Response<?> OpenNewStore(int userId,String storeName);

    /**
     *
     * @param userId id of calling user, function fails if user not logged in or does not have store permissions
     * @param productName The name of the product to be added
     * @param description The description of the product to be added //TODO: MAX LENGTH TO ALL OF THESE???
     * @return return corresponding response (fails if a product the same name exists)
     */
    Response<?> AddProduct(int userId,String productName,String description);

    /**
     *
     * @param userId id of calling user, function fails if user not logged
     * @param productName the name of the product to be removed
     * @return return corresponding response (fails if a product the name does not exist)
     */
    Response<?> RemoveProduct(int userId,String productName);

    /**
     *
     * @param userId id of calling user, function fails if user not logged
     * @param OldName name of the product whose name change is pending
     * @param newName new name for the product
     * @return return corresponding response (fails if a product the name does not exist, or new name is empty)
     */
    Response<?> EditProductName(int userId,String OldName,String newName);

    /**
     *
     * @param userId id of calling user, function fails if user not logged
     * @param productName product whose description change is pending
     * @param newDesc new description
     * @return return corresponding response (fails if a product the name does not exist, or new description is empty)
     */
    Response<?> EditDescription(int userId, String productName, String newDesc);

    /**
     *
     * @param userId id of calling user, function fails if user not logged
     * @param storeName store whose purchase history we expect to return
     * @return return corresponding response (fails if no store of such name exists, or if no purchases were made from store)
     */
    Response<?> PurchaseHistory(int userId,String storeName); //TODO: Store name optional? no? if optional need to add new tests without store name


}
