package ServiceLayer.ServiceObjects.ServiceProducts;

import DomainLayer.Stores.Products.CartProduct;

public class ServiceCartProduct extends ServiceProduct{
    private final int amount;

    public int getAmount() {
        return amount;
    }

    public ServiceCartProduct(String name, Double price, String category, String description , int amount) {
        super(name, price, category, description);
        this.amount=amount;
    }

    public ServiceCartProduct(CartProduct product) {
        super(product);
        this.amount=product.getAmount();
    }
}
