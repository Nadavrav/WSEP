package DomainLayer.Users;

public class Admin extends RegisteredUser{
    public Admin(SiteVisitor visitor ,String userName, String password) throws Exception {
        super("" ,  password);
        //TODO:REMOVE THE CHANGED HERE
        //Original super : super(visitor ,userName,  password);
    }
}
