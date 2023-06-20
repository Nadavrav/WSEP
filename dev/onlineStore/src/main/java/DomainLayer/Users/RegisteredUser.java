package DomainLayer.Users;

import DomainLayer.Stores.Bid;
import DomainLayer.Stores.Products.Product;
import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Stores.Purchases.Purchase;
import DomainLayer.Logging.UniversalHandler;
import DomainLayer.Stores.Store;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.*;

import DAL.DTOs.registeredUserDTO;


public class RegisteredUser extends SiteVisitor{
    private static final Logger logger=Logger.getLogger("RegisteredUser logger");
    String userName;

    public byte[] getPassword() {
        return password;
    }

    byte[] password;
    PurchaseHistory purchaseHistory;
    private final HashMap<Product,Bid> counterOffers;
    private boolean loggedIn;
    //add lock

    public boolean hasNewMessage() {
        return hasNewMessage;
    }

    boolean hasNewMessage = false;
    LinkedList<String> waitingMessages;

    public LinkedList<String> getWaitingMessages() {
        return waitingMessages;
    }

    @Override
    public String toString(){
        String output ="UserName : "+userName+" Is logged in :"+loggedIn;
        return output;
    }
    public RegisteredUser(String userName, String password) throws NoSuchAlgorithmException {
        super(0);
        try{
            UniversalHandler.GetInstance().HandleError(logger);
            UniversalHandler.GetInstance().HandleInfo(logger);
        }
        catch (Exception ignored){
        }
        checkUserName(userName);
        checkPassword(password);
        counterOffers=new HashMap<>();
        this.userName=userName;
        this.password=hashString(password);
        this.purchaseHistory = new PurchaseHistory();
        loggedIn=false;
        waitingMessages=new LinkedList<String>();
        
    }

    /**
     * A constructor that loads from db
     */
    public RegisteredUser(registeredUserDTO userDTO)
    {
        super(0);
        this.userName = userDTO.getUserName();
        this.password = userDTO.getPassword();
        this.loggedIn = false;
    }
    private byte[] hashString(String str) throws NoSuchAlgorithmException{
        byte[] unHashedBytes = str.getBytes();

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(unHashedBytes);
    }

    public void update(String message){
        waitingMessages.add(message);
        hasNewMessage = true;
    }

    public void setHasNewMessage(boolean b){
        hasNewMessage=b;
    }

     public RegisteredUser(SiteVisitor visitor,String userName, String password) throws NoSuchAlgorithmException {
        super(visitor.getVisitorId());
            UniversalHandler.GetInstance().HandleError(logger);
            UniversalHandler.GetInstance().HandleInfo(logger);
            checkUserName(userName);
            checkPassword(password);
            counterOffers=new HashMap<>();
            this.userName=userName;
            this.password=hashString(password);
            this.purchaseHistory = new PurchaseHistory();
            if(super.getCart().getBags().isEmpty() && !visitor.getCart().getBags().isEmpty()){
                super.ReplaceCart(visitor.getCart());
            }
    }
    public Map<Product,Bid> getCounterOffers(){
        return counterOffers;
    }
    private void checkPassword(String password) {
        if(password==null) {
            logger.severe("null password");
            throw new NullPointerException("Username cannot be null");
        }
        if(password.length()<8){
            logger.warning("invalid password");
            throw new IllegalArgumentException("the password is too short");
        }
        if(password.length()>30){
            logger.warning("invalid password");
            throw new IllegalArgumentException("the password is too long");
        }
    }

    private void checkUserName(String userName) {

        if (userName == null) {
            logger.severe("null username");
            throw new NullPointerException("Username cannot be null");
        }
       //if (userName.length() < 8) { //DONT UNCOMMENT THIS WE DON'T NEED THIS PART
       //    logger.warning("invalid username");
       //    throw new IllegalArgumentException("the userName is too short");
       //}
        if (userName.length() > 30) {
            logger.warning("invalid username");
            throw new IllegalArgumentException("the useName is too long");
        }
    }

    //public String getPassword() {
    //    return password;
    //}
    public String getUserName(){
        return userName;
    }

    public  void login(String password, int visitorId) throws Exception {//1.4
        logger.info("Attempting login for visitor with ID: " + visitorId);

        // Check password
        if (!Arrays.equals(this.password, hashString(password))) {
            // Log login failure
            logger.warning("Failed login attempt for visitor with ID: " + visitorId);
            throw new IllegalArgumentException("Wrong password");
        }
        // Check if user is already logged in
        if (loggedIn) {
            // Log login failure
            logger.warning("Failed login attempt for visitor with ID: " + visitorId + ". User is already logged in.");
            throw new Exception("This user is already logged in");
        }
        // Set visitor ID
        super.setVisitorId(visitorId);//Might not need this
        this.loggedIn = true;
        // Log successful login
        logger.info("Successfully logged in visitor with ID: " + visitorId);
    }
    public void logout(){//3.1
        super.setVisitorId(0);//Might not need this
        this.loggedIn = false;
    }

    public PurchaseHistory getPurchaseHistory(){
        return purchaseHistory;
    }

    public void addPurchaseToHistory(Purchase purchase) {
        purchaseHistory.addPurchaseToHistory(purchase);
    }
    public boolean isLoggedIn() {
        return loggedIn;
    }


    public void addCounterOffer(Bid bid, Product product) {
        counterOffers.put(product,bid);
    }
    public void acceptCounterOff(int productId, Store store){
        Bid bid=counterOffers.get(store.getProductByID(productId));
        if(bid==null)
            throw new RuntimeException("User has no product with id "+productId);
        getCart().addBidToCart(store.getId(),store.getProductByID(productId), bid.getAmount(), bid.getNewPrice());

    }
    public void rejectCounterOffer(int productId){
        for(Bid bid:counterOffers.values()){
            if(bid.getProductId()==productId) {
                counterOffers.remove(bid);
                break;
            }
        }
    }
}
