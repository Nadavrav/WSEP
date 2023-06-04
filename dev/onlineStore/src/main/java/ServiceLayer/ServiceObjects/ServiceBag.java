package ServiceLayer.ServiceObjects;

import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;
import ServiceLayer.ServiceObjects.ServiceProducts.ServiceCartProduct;

import java.util.ArrayList;

public class ServiceBag {
    private final int storeId;
    private final ArrayList<ServiceCartProduct> productList;
    public ServiceBag(Bag bag)
    {
        this.storeId= bag.getStoreID();
        productList=new ArrayList<>();
      for(CartProduct cartProduct: bag.getProducts()){
            productList.add(new ServiceCartProduct(cartProduct));
      }
    }

    public ArrayList<ServiceCartProduct> getProductList() {
        return productList;
    }
    public int getStoreId() {
        return storeId;
    }

    @Override
    public String toString() {
        StringBuilder s= new StringBuilder();
        for (ServiceCartProduct cartProduct :productList) {
            s.append(cartProduct.toString()).append("\n");
        }
        return s.toString();
    }
}
