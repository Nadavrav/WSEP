package DomainLayer.Users;

import DomainLayer.Logging.UniversalHandler;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Stores.Products.Product;
import DomainLayer.Stores.Products.StoreProduct;

import java.util.*;
import java.util.logging.*;

public class Bag {
    /**
     * key: StoreProduct extends Product, Product has only field getters and utility functions so no store product logic or field changes can be
     * performed on keys
     * value: cartProduct extends the same Product object (true in equals, not actually the same object) but also contains cart amount field- and uses
     * observer logic
     */
    Map<Product,CartProduct> productList;
    /**
     * deprecated: amounts implemented in CartProduct
     */
    //Map<String,Integer> productsAmount;
    private static final Logger logger=Logger.getLogger("Bag logger");
    int storeId;

    public Bag (int storeId){
            UniversalHandler.GetInstance().HandleError(logger);
            UniversalHandler.GetInstance().HandleInfo(logger);
        this.storeId=storeId;
        productList = new ;
       // productsAmount = new HashMap<>();
    }
    
    public void addProduct(StoreProduct product) {
        logger.info("Starting add product");
        CartProduct cartProduct=new CartProduct(product);
        if(productList.get(product)==null)
            throw new RuntimeException("Cart already contains "+product.getName());
        productList.put(product,new CartProduct(product));
        //String productId = product.getId();
        // if(productList.containsKey(product.getId())){
        //     int currentAmount = productsAmount.get(productId);
        //     productsAmount.put(productId,currentAmount + 1);
        // }
        // else{
        //     productList.put(productId,product);
        //     productsAmount.put(productId,1);
        // }
        logger.info("Add product Succeeded");

    }
    public void removeProduct(StoreProduct product) {
        CartProduct cartProduct=new CartProduct(product);
        if(productList.contains(cartProduct))
                productList.remove(cartProduct);
        else throw new RuntimeException();
    }
    public void changeProductAmount(Product product,int newAmount) {
       CartProduct cartProduct=productList.(product);
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
