package ServiceLayer.ServiceObjects;

import DomainLayer.Users.RegisteredUser;
import ServiceLayer.Service;

public class ServiceUser {
    public String userName;
  // public int userId;
    //public boolean connected;
    public ServiceUser(RegisteredUser registeredUser){
        this.userName = registeredUser.getUserName();
        //userId= registeredUser.getVisitorId();
        //connected= (userId != 0);
    }
}
