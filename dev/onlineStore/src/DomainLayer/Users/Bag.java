package DomainLayer.Users;

import DomainLayer.Stores.StoreProduct;

import java.util.HashMap;
import java.util.Map;

public class Bag {
    Map<String,StoreProduct> productList;
    Map<String,Integer> productAmount;

    public Bag (){
        productList = new HashMap<>();
        productsAmount = new HashMap<>();
    }
    
    public void addProduct(StoreProduct product) {
        
         productId = product.getId();
        
        if(productList.contains(product.getId())){
            int currentAmount = productAmount.get(productId);
            productAmount.put(productId,currentAmount + 1);
        }
        else{
            productList.put(productId,product);
            productAmount.put(productId,1);
        }
    }
    
    public int calculateTotalAmount(){
        int totalAmount = 0;
        foreach(StoreProduct sp : productList.Values){
            totalAmount += sp.getPrice();
        }
        return totalAmount;
    }
    
    public String getStoreID(){
        if(productList.isEmpty()){
            return "";
        }
        else{
            String productId = productList.get(0);
            String storeId = productId.substring(0,productId.indexOf('-'));
            return storeId;
        }
    }
    
    public String bagToString() {
        String s="";
        for (String i :productList.keySet()) {
            s+= productList.get(i).toString()+"/n";
        }
        return s;
    }
}
