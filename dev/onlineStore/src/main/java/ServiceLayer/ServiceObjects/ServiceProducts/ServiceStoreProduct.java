package ServiceLayer.ServiceObjects.ServiceProducts;

import DomainLayer.Stores.Products.StoreProduct;

public class ServiceStoreProduct extends ServiceProduct{
    private final double rating;
    private final int quantity;

    public ServiceStoreProduct(String name, Double price, String category, String description,double rating,int quantity) {
        super(name, price, category, description);
        this.rating=rating;
        this.quantity=quantity;
    }

    public ServiceStoreProduct(StoreProduct product) {
        super(product);
        this.rating=product.getAverageRating();
        this.quantity=product.getQuantity();
    }

    public double getRating() {
        return rating;
    }
}
