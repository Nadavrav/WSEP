package DAL.Entities;

import javax.persistence.*;

@Entity
@Table(name = "cartproduct", schema = "onlinestoredb", catalog = "")
@IdClass(CartproductEntityPK.class)
public class CartproductEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "userName")
    private String userName;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "productId")
    private int productId;
    @Basic
    @Column(name = "amount")
    private int amount;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartproductEntity that = (CartproductEntity) o;

        if (productId != that.productId) return false;
        if (amount != that.amount) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + productId;
        result = 31 * result + amount;
        return result;
    }
}
