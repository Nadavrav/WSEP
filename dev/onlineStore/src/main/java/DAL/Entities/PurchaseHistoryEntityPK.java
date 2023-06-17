package DAL.Entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

public class PurchaseHistoryEntityPK implements Serializable {
    @Column(name = "storeId")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int storeId;
    @Column(name = "userName")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userName;

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
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

        PurchaseHistoryEntityPK that = (PurchaseHistoryEntityPK) o;

        if (storeId != that.storeId) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = storeId;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        return result;
    }
}
