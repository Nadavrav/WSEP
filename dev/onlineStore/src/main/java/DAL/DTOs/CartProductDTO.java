package DAL.DTOs;

import DAL.Entities.CartproductEntity;
import DAL.Entities.StoreproductEntity;

public class CartProductDTO {

    private final int productId;
    private final StoreProductDTO storeProduct;
    private final String userName;
    private final int amount;
    public CartProductDTO(CartproductEntity cartProduct) {
        productId=cartProduct.getProductId();
        storeProduct=cartProduct.getStoreProduct();
        userName=cartProduct.getUserName();
        amount=cartProduct.getAmount();
    }
    public int getProductId() {
        return productId;
    }

    public StoreProductDTO getStoreProduct() {
        return storeProduct;
    }

    public String getUserName() {
        return userName;
    }

    public int getAmount() {
        return amount;
    }




}
