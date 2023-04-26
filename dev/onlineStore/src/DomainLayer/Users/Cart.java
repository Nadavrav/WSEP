package DomainLayer.Users;

import DomainLayer.Stores.StoreProduct;

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
    
    public void removeProductFromCart(int storeId,StoreProduct product){

        Bag b = getBags(storeId);
        b.removeProduct(product);
    }

    public void changeCartProductQuantity(int storeId,StoreProduct product,int newAmount){
        Bag b = getBags(storeId);
        b.changeProductAmount(product,newAmount);
    }

    public Map<Integer,Bag> getBags(){
        return bagList;
    }

    public Bag getBags(int storeId){
        return bagList.get(storeId);
    }

    public String cartToString() {
        String s="";
        for (int i :bagList.keySet()) {
            s+= "Store Id : "+i+ "\n"+bagList.get(i).bagToString();

        }
        return s;
    }


}
