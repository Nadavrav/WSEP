package ServiceLayer.ServiceObjects;

import DomainLayer.Stores.Bid;
import ServiceLayer.ServiceObjects.ServiceProducts.ServiceStoreProduct;

public class ServiceBid {
    private final int amount;
    private final int productId;
    private final int userId;
    private final double newPrice;
    private final ServiceStoreProduct product;
    /**
     * user sending the bid
     */
    private final String userName;
    public ServiceBid(Bid bid,ServiceStoreProduct serviceStoreProduct){
        amount= bid.getAmount();
        productId= bid.getProductId();
        newPrice= bid.getNewPrice();
        userId=bid.getUserId();
        userName= bid.getUserName();
        this.product=serviceStoreProduct;
    }

    public double getNewPrice() {
        return newPrice;
    }

    public int getAmount() {
        return amount;
    }

    public int getProductId() {
        return productId;
    }

    public ServiceStoreProduct getProduct() {
        return product;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}

