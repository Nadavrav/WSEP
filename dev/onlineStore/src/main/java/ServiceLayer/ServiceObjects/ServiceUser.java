package ServiceLayer.ServiceObjects;

import DomainLayer.Users.RegisteredUser;

public class ServiceUser {
    public String userName;
    public String info;
  // public int userId;
    //public boolean connected;
    public ServiceUser(RegisteredUser registeredUser){
        this.userName = registeredUser.getUserName();
        this.info = registeredUser.toString();
        //userId= registeredUser.getVisitorId();
        //connected= (userId != 0);
    }
}
