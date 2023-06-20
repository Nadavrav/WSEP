package DAL.DTOs;

public class registeredUserDTO {
    private String userName;
    private byte[] password;

    public registeredUserDTO(String userName, byte[] password) {
        this.userName = userName;
        this.password = password;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }
}
