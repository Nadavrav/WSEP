package DomainLayer.Users;

import DomainLayer.Logging.UniversalHandler;
import DomainLayer.Stores.StoreProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.*;

public class Bag {
    Map<String,StoreProduct> productList;
    Map<String,Integer> productsAmount;
    private static final Logger logger=Logger.getLogger("Bag logger");
    int storeId;

    public Bag (int storeId){
        try{
            UniversalHandler.GetInstance().HandleError(logger);
            UniversalHandler.GetInstance().HandleInfo(logger);
        }
        catch (Exception ignored){
        }
        this.storeId=storeId;
        productList = new HashMap<>();
        productsAmount = new HashMap<>();
    }
    
    public void addProduct(StoreProduct product) {

        logger.info("Starting add product");
         String productId = product.getId();

        if(productList.containsKey(product.getId())){
            int currentAmount = productsAmount.get(productId);
            productsAmount.put(productId,currentAmount + 1);
        }
        else{
            productList.put(productId,product);
            productsAmount.put(productId,1);
        }
        logger.info("Add product Succeeded");

    }
    public void removeProduct(StoreProduct product) {
        productList.remove(product);
        productsAmount.remove(product);
    }
    public void changeProductAmount(StoreProduct product,int newAmount) {
        productsAmount.put(product.getId(),newAmount);
    }
    public double calculateTotalAmount() {
        int totalAmount = 0;
        for (StoreProduct sp : productList.values()) {
            totalAmount += sp.getPrice()*productsAmount.get(sp.getId());
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
