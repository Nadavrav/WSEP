package Bridge;

import DomainLayer.Response;

import java.util.List;

public interface Bridge {
    /**
     *
     * @return return corresponding response
     */
    Response<?> initialize();
    Response<?> login(String UserName, String Password);

    /**
     *
     * @param query product or store search
     * @return return corresponding response (fails if no store and products were found)
     */
    Response<?> StoreAndProductSearch(String query);

    /**
     *
     * @param filters list of parameters such as: product name, product price range,
     *                  product name/category/description store name/rating/owner(s)
     * @return  return corresponding response (fails if no product was found)
     */
    Response<?> SearchProduct(List<?> filters);

    /**
     *
     * @param storeName new store's name
     * @return return corresponding response (fails if a store with the same name exists)
     */
    Response<?> OpenNewStore(String storeName);

    /**
     *
     * @param productName The name of the product to be added
     * @param description The description of the product to be added
     * @return return corresponding response (fails if a product the same name exists)
     */
    Response<?> AddProduct(String productName,String description, int price, int amount);

    /**
     *
     * @param productName the name of the product to be removed
     * @return return corresponding response (fails if a product the name does not exist)
     */
    Response<?> RemoveProduct(String storeName,String productName);

    /**
     *
     * @param OldName name of the product whose name change is pending
     * @param newName new name for the product
     * @return return corresponding response (fails if a product the name does not exist, or new name is empty)
     */
    Response<?> EditProductName(String storeName,String OldName,String newName);

    /**
     *
     * @param productName product whose description change is pending
     * @param newDesc new description
     * @return return corresponding response (fails if a product the name does not exist, or new description is empty)
     */
    Response<?> EditDescription(String storeName,String productName, String newDesc,int price,int quantity);

    /**
     *
     * @param storeName name of the store the product is in
     * @param productName the product's name to rate
     * @param rating the rating to give to the product
     * @return  return corresponding response (fails if store/product/rating is not valid)
     */
    Response<?> RateProduct(String storeName,String productName, int rating);
    /**
     *
     * @param storeName store whose purchase history we expect to return
     * @return return corresponding response (fails if no store of such name exists, or if no purchases were made from store)
     */
    Response<String> UserPurchaseHistory(String storeName);

    /**
     *
     * @return Response either containing the purchase history of the user, or an error message
     */
    Response<String> StorePurchaseHistory();

}
