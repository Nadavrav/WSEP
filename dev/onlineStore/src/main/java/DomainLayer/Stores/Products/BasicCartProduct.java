package DomainLayer.Stores.Products;

public class BasicCartProduct extends CartProduct{
    public BasicCartProduct(StoreProduct storeProduct) {
        super(storeProduct);
    }

    public BasicCartProduct(StoreProduct storeProduct, int amount) {
        super(storeProduct, amount);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Double getPrice() {
        return price;
    }
}
