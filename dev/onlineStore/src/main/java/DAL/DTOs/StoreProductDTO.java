package DAL.DTOs;

import DAL.Entities.StoreproductEntity;

import javax.persistence.*;

public class StoreProductDTO {

    private final int productId;
    private final int storeId;
    private final String name;
    private final double price;
    private final String category;
    private final String desc;
    private final double avgRating;

    public StoreProductDTO(StoreproductEntity storeproduct) {
        productId=storeproduct.getProductId();
        storeId=storeproduct.getStoreId();
        name=storeproduct.getName();
        price=storeproduct.getPrice();
        category=storeproduct.getCategory();
        desc=storeproduct.getDesc();
        avgRating=storeproduct.getAvgRating();
    }

    public int getProductId() {
        return productId;
    }

    public int getStoreId() {
        return storeId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getDesc() {
        return desc;
    }

    public double getAvgRating() {
        return avgRating;
    }
}
