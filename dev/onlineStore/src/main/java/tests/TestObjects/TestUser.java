package tests.TestObjects;

public class TestUser {
    private final String userName;
    private final String password;
     public TestUser(String userName,String password){
         this.userName=userName;
         this.password=password;
     }
    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }
}
