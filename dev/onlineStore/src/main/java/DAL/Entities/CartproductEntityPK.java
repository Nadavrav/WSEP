package DAL.Entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

public class CartproductEntityPK implements Serializable {
    @Column(name = "userName")
    @Id
  //  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userName;
    @Column(name = "productId")
    @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
    public CartproductEntityPK(int productId,String userName){
        this.userName=userName;
        this.productId=productId;
    }
    public CartproductEntityPK(){}
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartproductEntityPK that = (CartproductEntityPK) o;

        if (productId != that.productId) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + productId;
        return result;
    }
}
