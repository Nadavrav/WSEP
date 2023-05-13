package ServiceLayer.ServiceObjects;

import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Stores.Store;
import DomainLayer.Users.Bag;
import ServiceLayer.ServiceObjects.ServiceProducts.ServiceCartProduct;
import ServiceLayer.ServiceObjects.ServiceProducts.ServiceStoreProduct;

import java.util.*;

public class ServiceStore {
    private final int storeId;
    private final String storeName;
    private final ArrayList<ServiceStoreProduct> productList;
    public ServiceStore(Store store)
    {
        this.storeId= store.getId();
        this.storeName=store.getName();
        productList=new ArrayList<>();
        Map<Integer,StoreProduct> prductMap=store.getProducts();
        for(StoreProduct storeProduct: prductMap.values()){
            productList.add(new ServiceStoreProduct(storeProduct));
        }
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
