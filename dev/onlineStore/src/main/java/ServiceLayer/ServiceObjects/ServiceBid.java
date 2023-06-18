package ServiceLayer.ServiceObjects;

import DomainLayer.Stores.Bid;
import DomainLayer.Stores.Products.Product;
import ServiceLayer.ServiceObjects.ServiceProducts.ServiceProduct;
import ServiceLayer.ServiceObjects.ServiceProducts.ServiceStoreProduct;

public class ServiceBid {
    private final int amount;
    private final int productId;
    private final int userId;
    private final double newPrice;
    private final ServiceProduct product;
    private final int storeId;
    /**
     * user sending the bid
     */
    private final String userName;
    public ServiceBid(Bid bid, ServiceProduct product){
        amount= bid.getAmount();
        productId= bid.getProductId();
        newPrice= bid.getNewPrice();
        userId=bid.getUserId();
        userName= bid.getUserName();
        storeId=bid.getStoreId();
        this.product=product;
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

    public ServiceProduct getProduct() {
        return product;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}

