package DomainLayer.Stores.Products;

public interface StoreProductObserver {

    void updateFields(Product product);
    boolean stillInCart();
}
