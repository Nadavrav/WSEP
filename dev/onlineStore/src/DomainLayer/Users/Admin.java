package DomainLayer.Users;

public class Admin extends RegisteredUser{
    public Admin(SiteVisitor visitor ,String userName, String password){
        super(visitor ,userName,  password);
    }
}
