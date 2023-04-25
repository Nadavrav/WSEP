package DomainLayer.Users;

import DomainLayer.Stores.StoreProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bag {
    Map<String,StoreProduct> productList;
    Map<String,Integer> productsAmount;
    int storeId;

    public Bag (int storeId){
        this.storeId=storeId;
        productList = new HashMap<>();
        productsAmount = new HashMap<>();
    }
    
    public void addProduct(StoreProduct product) {
        String productId = product.getId();
        
        if(productList.containsKey(product.getId())){
            int currentAmount = productsAmount.get(productId);
            productsAmount.put(productId,currentAmount + 1);
        }
        else{
            productList.put(productId,product);
            productsAmount.put(productId,1);
        }
    }
    
    public int calculateTotalAmount(){
        int totalAmount = 0;
        for(StoreProduct sp : productList.values()){
            totalAmount += sp.getPrice();
        }
        return totalAmount;
    }
    
    public String getStoreID(){
        if(productList.isEmpty()){
            return "";
        }
        else{

            return ""+ storeId;

        }
    }
    
    public String bagToString() {
        String s="";
        for (String i :productList.keySet()) {
            s+= productList.get(i).toStringForCart()+"\n";
        }
        return s;
    }
}
