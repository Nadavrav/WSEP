package DomainLayer.Users;

import DomainLayer.Logging.UniversalHandler;
import DomainLayer.Stores.CallBacks.StoreCallbacks;
import DomainLayer.Stores.Discounts.Discount;
import DomainLayer.Stores.Products.*;

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
    private final StoreCallbacks callback;
    int storeId;

    public Bag (int storeId, StoreCallbacks callback){
            UniversalHandler.GetInstance().HandleError(logger);
            UniversalHandler.GetInstance().HandleInfo(logger);
        this.storeId=storeId;
        this.callback = callback;
        productList = new HashMap<>();
       // productsAmount = new HashMap<>();
    }

    public Bag (int storeId){
        UniversalHandler.GetInstance().HandleError(logger);
        UniversalHandler.GetInstance().HandleInfo(logger);
        this.storeId=storeId;
        this.callback = new StoreCallbacks() {
            @Override
            public boolean checkStorePolicies(Bag bag) {
                logger.severe("Error: check policy callback used in a product initialized without store callback");
                throw new RuntimeException("system error: report error to developers. error logged");
            }
            @Override
            public double getDiscountAmount(Bag bag) {
                logger.severe("Error: get discount amount callback used in a product initialized without store callback");
                throw new RuntimeException("system error: report error to developers. error logged");
            }
            @Override
            public HashMap<CartProduct,Double> getSavingsPerProduct(Bag bag) {
                logger.severe("Error: get discount amount callback used in a product initialized without store callback");
                throw new RuntimeException("system error: report error to developers. error logged");
            }
        };
        productList = new HashMap<>();
    }
    public Bag (Bag bag){
        UniversalHandler.GetInstance().HandleError(logger);
        UniversalHandler.GetInstance().HandleInfo(logger);
        this.storeId=bag.storeId;
        this.callback =bag.getCallback();
        productList = new HashMap<>();
    }

    public StoreCallbacks getCallback() {
        return callback;
    }
    public boolean passesPolicy(){
        return callback.checkStorePolicies(this);
    }
    public HashMap<CartProduct,Double> getSavingsPerProducts(){
        return callback.getSavingsPerProduct(this);
    }

    public double calcDiscountSavings(){
        return callback.getDiscountAmount(this);
    }
    public void addProduct(StoreProduct product,int amount) {
        logger.info("Starting add product");
        if(productList.get(product)!=null)
            throw new RuntimeException("Cart already contains "+product.getName());
        productList.put(product,new BasicCartProduct(product,amount));
        logger.info("Add product Succeeded");
    }
    public void addProduct(CartProduct product) {
        logger.info("Starting add product");
        if(productList.get(product)!=null)
            throw new RuntimeException("Cart already contains "+product.getName());
        productList.put(product,product);
        logger.info("Add product Succeeded");
    }
    public void addBid(StoreProduct product, int amount, double newPrice) {
        logger.info("Starting add bid");
        if(productList.get(product)!=null)
            throw new RuntimeException("Cart already contains "+product.getName());
        productList.put(product,new BidProduct(product,amount,newPrice));
        logger.info("Add bid Succeeded");
    }
    public void removeProduct(StoreProduct product) {
        if(product==null)
            throw new NullPointerException("attempted to remove null product");
        if(productList.get(product)==null)
            throw new RuntimeException("product to remove not in cart");
        productList.remove(product);
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
        if(newAmount <= 0)
            throw new IllegalArgumentException("Cant set amount to 0 or negative");
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
