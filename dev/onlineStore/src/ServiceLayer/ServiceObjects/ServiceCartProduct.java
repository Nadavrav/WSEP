package ServiceLayer.ServiceObjects;

import DomainLayer.Stores.Products.StoreProduct;

public class ServiceCartProduct extends ServiceProduct{
    private final int amount;
    public ServiceCartProduct(String name, Double price, String category, String description, double rating,int amount) {
        super(name, price, category, description, rating);
        this.amount=amount;
    }

    public ServiceCartProduct(StoreProduct product,int amount) {
        super(product);
        this.amount=amount;
    }
}
