package DomainLayer.Users;

public class RegisteredUser extends SiteVisitor{
    String userName;
    String password;

    public RegisteredUser(SiteVisitor visitor ,String userName, String password){
        super(visitor);
        this.userName=userName;
        this.password=password;
    }
    public String getPassword() {
        return password;
    }
}
