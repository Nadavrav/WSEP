package DAL.Entities;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(name = "registereduser", schema = "onlinestoredb", catalog = "")
public class RegistereduserEntity {
    @Id
    @Column(name = "userName", length = 30, nullable = false)
    private String userName;
    @Basic
    @Column(name = "password" ,length = 32)
    private byte[] password;

    public RegistereduserEntity(String userName,byte[] password)
    {
        this.userName = userName;
        this.password = password;
    }

    public RegistereduserEntity() {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegistereduserEntity that = (RegistereduserEntity) o;

        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (!Arrays.equals(password, that.password)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(password);
        return result;
    }
}
