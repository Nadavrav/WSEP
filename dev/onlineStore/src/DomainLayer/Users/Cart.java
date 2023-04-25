package DomainLayer.Users;

import DomainLayer.Stores.StoreProduct;
import ServiceLayer.ServiceObjects.ServiceBag;

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
            bag = new Bag((storeId));
        }
        // add product to storebag
        bag.addProduct(product);
        bagList.put(storeId,bag);
    }
    
    public void removeProductFromCart(String productId){
        //getStoreIdByProductId()
        //getBag(storeId)
        //removeProductFromBag (-> removeBagFromCartIfEmpty)
    }

    public Map<Integer,Bag>  getBag(){
        return bagList;
    }
    
    public String cartToString() {
        String s="";
        for (int i :bagList.keySet()) {
            s+= "Store Id : "+i+ "\n"+bagList.get(i).bagToString();

        }
        return s;
    }

    public Map<Integer,Bag> getBags()
    {
        return bagList;
    }

}
