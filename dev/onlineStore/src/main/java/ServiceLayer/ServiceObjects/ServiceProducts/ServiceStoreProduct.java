package ServiceLayer.ServiceObjects.ServiceProducts;

import DomainLayer.Stores.Products.StoreProduct;

public class ServiceStoreProduct extends ServiceProduct{
    private final int productId;
    private final double rating;
    private final int quantity;

    public ServiceStoreProduct(int productId,String name, Double price, String category, String description,double rating,int quantity) {
        super(name, price, category, description);
        this.productId=productId;
        this.rating=rating;
        this.quantity=quantity;
    }

    public ServiceStoreProduct(StoreProduct product) {
        super(product);
        this.productId=product.getProductId();
        this.rating=product.getAverageRating();
        this.quantity=product.getQuantity();
    }

    public double getRating() {
        return rating;
    }

    public int getId(){
        return productId;
    }
}
