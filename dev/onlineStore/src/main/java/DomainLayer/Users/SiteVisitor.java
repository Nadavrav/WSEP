package DomainLayer.Users;

import DomainLayer.Stores.CallBacks.StoreCallbacks;
import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Logging.UniversalHandler;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class SiteVisitor{
    private static AtomicInteger VisitorID_GENERATOR = new AtomicInteger(1);
    protected static LinkedList<AtomicInteger> FreeVisitorID= new LinkedList<>();
    private Cart cart;
    private int visitorId;
    private static final Logger logger=Logger.getLogger("SiteVisitor logger");
    
      public SiteVisitor() throws Exception {
       
        try{
            UniversalHandler.GetInstance().HandleError(logger);
            UniversalHandler.GetInstance().HandleInfo(logger);
        }
        catch (Exception ignored){
        }
        this.visitorId = getNewVisitorId();
        cart = new Cart();
    }
//    public SiteVisitor(int visitorId) throws Exception {//to do
//        if (checkVisitorId(visitorId)) {
//            throw new Exception("")
//        }
//    }
    
   //public SiteVisitor(SiteVisitor other){ //why do we need a copy constructor for site visitor??
   //    UniversalHandler.GetInstance().HandleError(logger);
   //    UniversalHandler.GetInstance().HandleError(logger);
   //    cart=new Cart();
   //}
    public SiteVisitor(int id){
        UniversalHandler.GetInstance().HandleError(logger);
        UniversalHandler.GetInstance().HandleInfo(logger);
        cart=new Cart();
    }
    private int getNewVisitorId() {
        if (FreeVisitorID.size() != 0) {
            return FreeVisitorID.removeFirst().get();
        }
        return VisitorID_GENERATOR.getAndIncrement();
    }

 //-------------- FreeVisitorID---------------------
    public void ExitSiteVisitor(int id) throws Exception {//1.2
        try {
            if (!checkVisitorId(id)) {
                logger.warning("exception occurred");
                throw new Exception("This id for user does not exist or is not online");
            }
            AtomicInteger atomicId = new AtomicInteger(id);
            FreeVisitorID.add(atomicId);
            resetCart();
        } catch (Exception e) {
            logger.warning("Error occurred: " + e.getMessage());
            throw e;
        }
    }
    private void resetCart()
    {
        this.cart = new Cart();
    }
    
    public static boolean checkVisitorId(int id){
        if(FreeVisitorID.contains(id)||VisitorID_GENERATOR.get()< id){//the user is not exist or not online
            logger.warning("Invalid visitor ID: " + id);
            return false;
        }
        return true;
    }
    public void addProductToCart(int storeId, StoreProduct product,int amount) {//2.3
        logger.info("Adding product with ID " + product.getProductId() + " to cart for store with ID " + storeId);

        cart.addProductToCart(storeId,product,amount);
    }
    public void addProductToCart(int storeId, StoreProduct product,int amount, StoreCallbacks callback) {//2.3
        logger.info("Adding product with ID " + product.getProductId() + " to cart for store with ID " + storeId);

        cart.addProductToCart(storeId,product,amount,callback);
    }

    public void removeProductFromCart(int storeId, StoreProduct product) {//2.3
        logger.info("Removing product with ID " + product.getProductId() + " from cart for store with ID " + storeId);

        cart.removeProductFromCart(storeId,product);
    }
    public void changeCartProductQuantity(int storeId, StoreProduct product,int newAmount) {//2.3
        logger.info("Changing quantity of product with ID " + product.getProductId() + " in cart for store with ID " + storeId
                + " to new amount: " + newAmount);

        cart.changeCartProductQuantity(storeId,product,newAmount);
    }

    public String cartToString() {
        return cart.cartToString();
    }

    public void removeBag(int storeId) throws Exception {
        cart.removeBag(storeId);
    }

    //-----------getter / setter-----------------
    public Cart getCart() {
        return cart;
    }
    public void ReplaceCart(Cart cart){
          this.cart=cart;
    }

    public int getVisitorId(){
        return visitorId;
    }

    public void setVisitorId(int visitorId) {
        this.visitorId = visitorId;
    }
    
    public static LinkedList<AtomicInteger> getFreeVisitorID() {
        return FreeVisitorID;
    }


}
