package DomainLayer.Users;

import DomainLayer.Response;

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
    public String getUserName(){
        return userName;
    }

    public Response<?> login(String password) {
        if (!this.password.equals(password)){
            return new Response<>("wrong password",true);
        }
        if( getVisitorId()!=0){//visitorId =0 mean the user is logout
            return new Response<>("this user is already login",true);
        }
        return new Response<>("success");

    }
}
