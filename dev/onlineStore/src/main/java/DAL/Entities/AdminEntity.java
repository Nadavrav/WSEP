package DAL.Entities;

import javax.persistence.*;

@Entity
@Table(name = "admin", schema = "onlinestoredb", catalog = "")
public class AdminEntity {

    @Id
    @Column(name = "userName")
    private String userName;

    public AdminEntity(String userName)
    {
        this.userName = userName;
    }
    public AdminEntity()
    {

    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdminEntity that = (AdminEntity) o;

        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return userName != null ? userName.hashCode() : 0;
    }
}
