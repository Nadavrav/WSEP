package DomainLayer.Users;

public class Admin extends RegisteredUser{
    public Admin(String userName, String password) throws Exception{
        super(userName,  password);
    }
}
