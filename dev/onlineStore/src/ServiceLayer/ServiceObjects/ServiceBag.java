package ServiceLayer.ServiceObjects;

import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Users.Bag;

import java.util.ArrayList;

public class ServiceBag {

    ArrayList<ServiceCartProduct> productList;
    Integer StoreId;

    public ServiceBag(Bag bag, Integer storeID)
    {
      for(CartProduct cartProduct: bag.getProducts()){

      }
    }
}
