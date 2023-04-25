package ServiceLayer.ServiceObjects;

public class ServicePurchaseProduct extends ServiceProduct {
    int amount;

    public ServicePurchaseProduct(String name, Double price, String category, String description, double rating,int amount) {
        super(name, price, category, description, rating);
        this.amount=amount;
    }
}
