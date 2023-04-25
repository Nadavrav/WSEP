package DomainLayer.Users;

import DomainLayer.Response;
import DomainLayer.Stores.Purchase;

import java.util.concurrent.atomic.AtomicInteger;

public class RegisteredUser extends SiteVisitor{
    String userName;
    String password;
    PurchaseHistory purchaseHistory;
    //add lock


    public RegisteredUser(String userName, String password) throws Exception {
        super(0);

        checkUserName(userName);
        checkPassword(password);
        this.userName=userName;
        this.password=password;
        this.purchaseHistory = new PurchaseHistory();
    }

    private void checkPassword(String password) {
        if(password==null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        if(password.length()<8){
            throw new IllegalArgumentException("the password is too short");
        }
        if(password.length()>30){
            throw new IllegalArgumentException("the password is too long");
        }
    }

    private void checkUserName(String userName) {
        if(userName==null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        if (userName.length() < 8) {
            throw new IllegalArgumentException("the userName is too short");
        }
        if (userName.length() > 30) {
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
        if (!this.password.equals(password)){
            throw  new IllegalArgumentException("wrong password");
        }
        if( getVisitorId()!=0){//visitorId =0 mean the user is logged out
            throw  new Exception("this user is already login");
        }
        setVisitorId(visitorId);
    }
    public void logout(){//3.1
        setVisitorId(0);

    }

    public PurchaseHistory getPurchaseHistory(){
        return purchaseHistory;
    }
}
