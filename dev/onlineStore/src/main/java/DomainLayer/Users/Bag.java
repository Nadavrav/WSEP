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
        productList = new HashMap<>();
       // productsAmount = new HashMap<>();
    }
    
    public void addProduct(StoreProduct product) {
        logger.info("Starting add product");
        if(productList.get(product)!=null)
            throw new RuntimeException("Cart already contains "+product.getName());
        productList.put(product,new CartProduct(product));
        logger.info("Add product Succeeded");
    }
    public void addProduct(CartProduct product) {
        logger.info("Starting add product");
        if(productList.get(product)!=null)
            throw new RuntimeException("Cart already contains "+product.getName());
        productList.put(product,product);
        logger.info("Add product Succeeded");
    }
    public void removeProduct(StoreProduct product) {
        if(product==null)
            throw new NullPointerException("attempted to remove null product");
        if(productList.get(product)==null)
            throw new RuntimeException("product to remove not in cart");
        CartProduct cartProduct=new CartProduct(product);
        if(productList.get(product).equals(cartProduct))
                productList.remove(cartProduct);
        else throw new RuntimeException();
    }
    public void removeProduct(CartProduct product) {
        if(product==null)
            throw new NullPointerException("attempted to remove null product");
        if(productList.get(product)==null)
            throw new RuntimeException("product to remove not in cart");
        if(productList.get(product).equals(product))
            productList.remove(product);
        else throw new RuntimeException();
    }
    public void changeProductAmount(Product product,int newAmount) {
       CartProduct cartProduct=productList.get(product);
       cartProduct.setAmount(newAmount);
    }
    public double calculateTotalAmount() {
        double totalAmount = 0;
        for (CartProduct cartProduct : productList.values()) {
            totalAmount += cartProduct.getPrice()*cartProduct.getAmount();
        }
        return totalAmount;
    }
    public Collection<CartProduct> getProducts(){
        return productList.values();
    }
    public Integer getStoreID(){
        return storeId;
    }
    
    public String bagToString() {
        StringBuilder s= new StringBuilder();
        for (Product cartProduct :productList.values()) {
            s.append(cartProduct.toString()).append("\n");
        }
        return s.toString();
    }
}
