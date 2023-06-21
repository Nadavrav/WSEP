package DAL.DTOs;

import DAL.Entities.StoreEntity;
import DAL.Entities.StoreproductEntity;

import java.util.HashSet;
import java.util.Set;

public class StoreDTO {
    private final int id;
    private final String name;
    private final boolean active;
    private final double rate;
    private final Set<StoreProductDTO> products;

    public StoreDTO(StoreEntity storeEntity){
        id=storeEntity.getId();
        name=storeEntity.getName();
        active=storeEntity.isActive();
        rate=storeEntity.getRate();
        products=new HashSet<>();
        for(StoreproductEntity storeproduct:storeEntity.getProducts())
            products.add(new StoreProductDTO(storeproduct));
    }

    public StoreDTO(int id,String storeName,boolean active,double rate) {
        this.id=id;
        this.name=storeName;
        this.active=active;
        this.rate=rate;
        products=new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public double getRate() {
        return rate;
    }

    public Set<StoreProductDTO> getProducts() {
        return products;
    }
}
