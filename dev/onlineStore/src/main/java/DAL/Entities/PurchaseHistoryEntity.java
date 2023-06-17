package DAL.Entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "purchase_history", schema = "onlinestoredb", catalog = "")
@IdClass(PurchaseHistoryEntityPK.class)
public class PurchaseHistoryEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "storeId")
    private int storeId;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "userName")
    private String userName;
    @Basic
    @Column(name = "purchaseDate")
    private Timestamp purchaseDate;
    @Basic
    @Column(name = "totalAmount")
    private double totalAmount;
    @Basic
    @Column(name = "products")
    private String products;

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

    public Timestamp getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Timestamp purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PurchaseHistoryEntity that = (PurchaseHistoryEntity) o;

        if (storeId != that.storeId) return false;
        if (Double.compare(that.totalAmount, totalAmount) != 0) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (purchaseDate != null ? !purchaseDate.equals(that.purchaseDate) : that.purchaseDate != null) return false;
        if (products != null ? !products.equals(that.products) : that.products != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = storeId;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (purchaseDate != null ? purchaseDate.hashCode() : 0);
        temp = Double.doubleToLongBits(totalAmount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (products != null ? products.hashCode() : 0);
        return result;
    }
}
