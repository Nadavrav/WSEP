package DomainLayer.Users;

import DomainLayer.Response;
import DomainLayer.Stores.Purchase;
import DomainLayer.Logging.UniversalHandler;
import java.util.logging.*;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class RegisteredUser extends SiteVisitor{
    private static final Logger logger=Logger.getLogger("RegisteredUser logger");
    String userName;
    String password;
    PurchaseHistory purchaseHistory;
    //add lock


    public RegisteredUser(String userName, String password) throws Exception {
        super(0);
        try{
            UniversalHandler.GetInstance().HandleError(logger);
            UniversalHandler.GetInstance().HandleInfo(logger);
        }
        catch (Exception ignored){
        }
            checkUserName(userName);
            checkPassword(password);
            this.userName=userName;
            this.password=password;
            this.purchaseHistory = new PurchaseHistory();
        
    }

     public RegisteredUser(SiteVisitor visitor,String userName, String password) {
        super(visitor);
        try{
            UniversalHandler.GetInstance().HandleError(logger);
            UniversalHandler.GetInstance().HandleInfo(logger);
        }
        catch (Exception ignored){
        }
            checkUserName(userName);
            checkPassword(password);
            this.userName=userName;
            this.password=password;
            this.purchaseHistory = new PurchaseHistory();
       
    }

    private void checkPassword(String password) {
        if(password==null) {
            logger.warning("null password");
            throw new IllegalArgumentException("Username cannot be null");
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
            logger.warning("null username");
            throw new IllegalArgumentException("Username cannot be null");
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

    public String getPassword() {
        return password;
    }
    public String getUserName(){
        return userName;
    }

    public void login(String password, int visitorId) throws Exception {//1.4
        logger.info("Attempting login for visitor with ID: " + visitorId);

        // Check password
        if (!this.password.equals(password)) {
            // Log login failure
            logger.warning("Failed login attempt for visitor with ID: " + visitorId);
            throw new IllegalArgumentException("Wrong password");
        }
        // Check if user is already logged in
        if (getVisitorId() != 0) {
            // Log login failure
            logger.warning("Failed login attempt for visitor with ID: " + visitorId + ". User is already logged in.");
            throw new Exception("This user is already logged in");
        }
        // Set visitor ID
        setVisitorId(visitorId);
        // Log successful login
        logger.info("Successfully logged in visitor with ID: " + visitorId);
    }
    public void logout(){//3.1
        setVisitorId(0);

    }

    public PurchaseHistory getPurchaseHistory(){
        return purchaseHistory;
    }

    public void addPurchaseToHistory(Purchase purchase) {
        purchaseHistory.addPurchaseToHistory(purchase);
    }

}
