package ServiceLayer.ServiceObjects;

import DomainLayer.Stores.Policies.Policy;
import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Stores.Store;
import ServiceLayer.ServiceObjects.ServiceProducts.ServiceStoreProduct;

import java.util.*;

public class ServiceStore {
    private final int storeId;
    private final String storeName;
    private final ArrayList<ServiceStoreProduct> productList;
    private final HashSet<ServicePolicy> storePolicies;
    public ServiceStore(Store store)
    {
        this.storeId= store.getId();
        this.storeName=store.getName();
        storePolicies=new HashSet<>();
        for(Policy policy:store.getPolicies())
            storePolicies.add(new ServicePolicy(policy));
        productList=new ArrayList<>();
    }

    public HashSet<ServicePolicy> getStorePolicies() {
        return storePolicies;
    }

    public String getStoreName() {
        return storeName;
    }

    public ArrayList<ServiceStoreProduct> getProductList() {
        return productList;
    }
    public int getStoreId() {
        return storeId;
    }

    public void addAll(List<StoreProduct> storeProducts) {
        for(StoreProduct storeProduct:storeProducts){
            productList.add(new ServiceStoreProduct(storeProduct));
        }
    }
}
