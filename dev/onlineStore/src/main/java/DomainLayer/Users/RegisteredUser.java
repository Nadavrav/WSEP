package DomainLayer.Users;

import DomainLayer.Stores.Purchases.Purchase;
import DomainLayer.Logging.UniversalHandler;
import DomainLayer.WebSocket.MessageObserver;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.*;


public class RegisteredUser extends SiteVisitor implements MessageObserver {
    private static final Logger logger = Logger.getLogger("RegisteredUser logger");
    String userName;

    byte[] password;
    PurchaseHistory purchaseHistory;

    private boolean loggedIn;
    //add lock


    @Override
    public String toString() {
        String output = "UserName : " + userName + " Is logged in :" + loggedIn;
        return output;
    }

    public RegisteredUser(String userName, String password) throws NoSuchAlgorithmException {
        super(0);
        try {
            UniversalHandler.GetInstance().HandleError(logger);
            UniversalHandler.GetInstance().HandleInfo(logger);
        } catch (Exception ignored) {
        }
        checkUserName(userName);
        checkPassword(password);
        this.userName = userName;
        this.password = hashString(password);
        this.purchaseHistory = new PurchaseHistory();
        loggedIn = false;

    }

    private byte[] hashString(String str) throws NoSuchAlgorithmException {
        byte[] unHashedBytes = str.getBytes();

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(unHashedBytes);
    }

    public RegisteredUser(SiteVisitor visitor, String userName, String password) throws NoSuchAlgorithmException {
        super(visitor.getVisitorId());
        UniversalHandler.GetInstance().HandleError(logger);
        UniversalHandler.GetInstance().HandleInfo(logger);
        checkUserName(userName);
        checkPassword(password);
        this.userName = userName;
        this.password = hashString(password);
        this.purchaseHistory = new PurchaseHistory();
        if (super.getCart().getBags().isEmpty() && !visitor.getCart().getBags().isEmpty()) {
            super.ReplaceCart(visitor.getCart());
        }

    }

    private void checkPassword(String password) {
        if (password == null) {
            logger.severe("null password");
            throw new NullPointerException("Username cannot be null");
        }
        if (password.length() < 8) {
            logger.warning("invalid password");
            throw new IllegalArgumentException("the password is too short");
        }
        if (password.length() > 30) {
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
    public String getUserName() {
        return userName;
    }

    public void login(String password, int visitorId) throws Exception {//1.4
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

    public void logout() {//3.1
        super.setVisitorId(0);//Might not need this
        this.loggedIn = false;
    }

    public PurchaseHistory getPurchaseHistory() {
        return purchaseHistory;
    }

    public void addPurchaseToHistory(Purchase purchase) {
        purchaseHistory.addPurchaseToHistory(purchase);
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    @Override
    public void updateMessage(String message) { //check
        // Implement your custom logic to handle the received message
        System.out.println("Received message: " + message);
    }
}
