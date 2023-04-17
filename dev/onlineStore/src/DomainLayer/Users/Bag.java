package DomainLayer.Users;

import DomainLayer.Stores.StoreProduct;

import java.util.HashMap;
import java.util.Map;

public class Bag {
    Map<String,StoreProduct> productList;

    public Bag (){
        productList = new HashMap<>();
    }
    public void addProduct(StoreProduct product) {
        productList.put(product.getId(),product);
    }

    public String bagToString() {
        String s="";
        for (String i :productList.keySet()) {
            s+= productList.get(i).toString()+"/n";
        }
        return s;
    }
}
