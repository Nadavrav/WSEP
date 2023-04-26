package DomainLayer.Users;

import DomainLayer.Stores.StoreProduct;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Cart {
    Map<Integer,Bag> bagList;
    private static final Logger logger=Logger.getLogger("Cart logger");


  public Cart(){
       try{
            UniversalHandler.GetInstance().HandleError(logger);
            UniversalHandler.GetInstance().HandleInfo(logger);
        }
        catch (Exception ignored){
        }
            bagList= new HashMap<>();
      

    }


    public void addProductToCart(int storeId, StoreProduct product) {
        //check if there is bag to store
      public void addProductToCart(int storeId, StoreProduct product) {
        Bag bag = bagList.get(storeId);
        if (bag == null) {
            // If bag doesn't exist, create a new bag and add it to the bag list
            bag = new Bag(storeId);
            bagList.put(storeId, bag);
            logger.info("New bag created for store with ID: " + storeId);
        }

        // Add the product to the store bag
        bag.addProduct(product);
        bagList.put(storeId,bag);
        logger.info("Product added to cart for store with ID: " + storeId);
    }
    
    public void removeProductFromCart(int storeId,StoreProduct product){

        Bag b = getBag(storeId);
        logger.info("enter this function");
        if (b != null) {
            // If bag exists, remove the product from the bag
            b.removeProduct(product);
            logger.info("Product removed from cart for store with ID: " + storeId);
        } else {
            logger.warning("Bag not found for store with ID: " + storeId);
        }
    }
    public void changeCartProductQuantity(int storeId,StoreProduct product,int newAmount){
        Bag b = getBag(storeId);

        if (b != null) {
            // If bag exists, change the quantity of the product in the bag
            b.changeProductAmount(product, newAmount);
            logger.info("Product quantity changed in cart for store with ID: " + storeId + ", Product: " +
                    product.getName() + ", New quantity: " + newAmount);
        } else {
            logger.warning("Bag not found for store with ID: " + storeId);
        }
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
