package ServiceLayer.ServiceObjects.ServiceProducts;

import DomainLayer.Stores.Products.StoreProduct;

public class ServiceStoreProduct extends ServiceProduct{
    private final double rating;

    public ServiceStoreProduct(String name, Double price, String category, String description,double rating) {
        super(name, price, category, description);
        this.rating=rating;
    }

    public ServiceStoreProduct(StoreProduct product) {
        super(product);
        this.rating=product.getAverageRating();

    }

    public double getRating() {
        return rating;
    }
}
