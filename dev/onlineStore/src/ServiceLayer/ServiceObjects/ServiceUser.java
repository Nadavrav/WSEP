package ServiceLayer.ServiceObjects;

import DomainLayer.Users.RegisteredUser;
import DomainLayer.Users.SiteVisitor;
import ServiceLayer.Service;

public class ServiceUser {
    public String userName;
    public int userId;
    public boolean connected;
    public ServiceUser(RegisteredUser registeredUser){
        userName=registeredUser.getUserName();
        userId= registeredUser.getVisitorId();
        connected= (userId != 0);
    }
}
