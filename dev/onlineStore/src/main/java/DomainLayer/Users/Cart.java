package DomainLayer.Users;

import DAL.DALService;
import DomainLayer.Logging.UniversalHandler;
import DomainLayer.Stores.CallBacks.StoreCallbacks;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Stores.Products.StoreProduct;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Cart {
    Map<Integer,Bag> bagList;
    private static final Logger logger=Logger.getLogger("Cart logger");


  public Cart(){
      bagList = new HashMap<>();
      UniversalHandler.GetInstance().HandleInfo(logger);
      UniversalHandler.GetInstance().HandleError(logger);
      bagList = new HashMap<>();
  }

        //check if there is bag to store
        public void addProductToCart (int storeId, StoreProduct product,int amount, StoreCallbacks callback){
            Bag bag = bagList.get(storeId);
            if (bag == null) {
                // If bag doesn't exist, create a new bag and add it to the bag list
                bag = new Bag(storeId,callback);
                bagList.put(storeId, bag);
                logger.info("New bag created for store with ID: " + storeId);
            }
            bag.addProduct(product,amount);
            logger.info("Product added to cart for store with ID: " + storeId);
        }
        public void addProductToCart (int storeId, StoreProduct product,int amount){
            Bag bag = bagList.get(storeId);
            if (bag == null) {
                // If bag doesn't exist, create a new bag and add it to the bag list
                bag = new Bag(storeId);
                bagList.put(storeId, bag);
                logger.info("New bag created for store with ID: " + storeId);
            }
            bag.addProduct(product,amount);
            logger.info("Product added to cart for store with ID: " + storeId);
    }
    public void addBidToCart (int storeId, StoreProduct product,int amount,double newPrice,StoreCallbacks callback){
        Bag bag = bagList.get(storeId);
        if (bag == null) {
            // If bag doesn't exist, create a new bag and add it to the bag list
            bag = new Bag(storeId,callback);
            bagList.put(storeId, bag);
            logger.info("New bag created for store with ID: " + storeId);
        }
        bag.addBid(product,amount,newPrice);
        logger.info("Product added to cart for store with ID: " + storeId);
    }
        public void removeProductFromCart ( int storeId, StoreProduct product){
            Bag bag = bagList.get(storeId);
            if(bag==null)
                throw new RuntimeException("Cart has no bag from this store!");
            bag.removeProduct(product);
        }

        public void changeCartProductQuantity ( int storeId, StoreProduct product,int newAmount){
            Bag bag = bagList.get(storeId);
            if(bag==null)
                throw new RuntimeException("Cart has no bag from this store!");
            bag.changeProductAmount(product, newAmount);
        }

        public Map<Integer, Bag> getBags () {
            return bagList;
        }

        //public Bag getBags ( int storeId){
        //    return bagList.get(storeId);
        //}

        private boolean isEmpty(){
            return bagList.isEmpty();
        }

        public String cartToString () {
            String s = "";
            for (int i : bagList.keySet()) {
                s += "Store Id : " + i + "\n" + bagList.get(i).bagToString();

            }
            return s;
        }

        public void removeBag(int storeId,String userName) throws Exception {
            try {
                bagList.get(storeId).empty(userName);
                bagList.remove(storeId);
            }
            catch (Exception e)
            {
                throw new Exception("something bad at removeBag");
            }
        }
    public void removeBag(int storeId) throws Exception {
        try {
            bagList.remove(storeId);
        }
        catch (Exception e)
        {
            throw new Exception("something bad at removeBag");
        }
    }

        /**
         * A function that gets the price of the user's cart
         * @return - the price of the user's cart
         */
        public Double getTotalPrice()
        {
            Double total = 0.0;
            for (Bag b: bagList.values()) {
                total += b.calculateTotalAmount();
            }
            return total;
        }
}
