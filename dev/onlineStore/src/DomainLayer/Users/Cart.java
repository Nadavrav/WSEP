package DomainLayer.Users;

import DomainLayer.Stores.StoreProduct;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class Cart {
    Map<Integer,Bag> bagList;

    public Cart(){
        bagList= new HashMap<>();
    }

    public void addProductToCart(int storeId, StoreProduct product) {
        //check if there is bag to store
        Bag bag = bagList.get(storeId);
        if (bag==null)// if false add bag
        {
            bagList.put(storeId,new Bag());
        }
        // add product to storebag
        bag.addProduct(product);

    }

    public String cartToString() {
        String s="";
        for (int i :bagList.keySet()) {
            s+= "Store Id : "+i+ "/n"+bagList.get(i).bagToString()+"/n";

        }
        return s;
    }
}