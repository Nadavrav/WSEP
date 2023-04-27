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
        try {
            Handler handler = new FileHandler("Info.txt");
            Handler handler1 = new FileHandler("Error.txt");
            logger.addHandler(handler);
            logger.addHandler(handler1);
            handler.setFormatter(new SimpleFormatter());
            handler1.setFormatter(new SimpleFormatter());
            bagList= new HashMap<>();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }



        //check if there is bag to store
        public void addProductToCart ( int storeId, StoreProduct product){
            Bag bag = bagList.get(storeId);
            if (bag == null) {
                // If bag doesn't exist, create a new bag and add it to the bag list
                bag = new Bag(storeId);
                bagList.put(storeId, bag);
                logger.info("New bag created for store with ID: " + storeId);
            }

            // Add the product to the store bag
            bag.addProduct(product);
            bagList.put(storeId, bag);
            logger.info("Product added to cart for store with ID: " + storeId);
        }

        public void removeProductFromCart ( int storeId, StoreProduct product){

            Bag b = getBags(storeId);
            b.removeProduct(product);
        }

        public void changeCartProductQuantity ( int storeId, StoreProduct product,int newAmount){
            Bag b = getBags(storeId);
            b.changeProductAmount(product, newAmount);
        }

        public Map<Integer, Bag> getBags () {
            return bagList;
        }

        public Bag getBags ( int storeId){
            return bagList.get(storeId);
        }

        public String cartToString () {
            String s = "";
            for (int i : bagList.keySet()) {
                s += "Store Id : " + i + "\n" + bagList.get(i).bagToString();

            }
            return s;
        }

    }
}
