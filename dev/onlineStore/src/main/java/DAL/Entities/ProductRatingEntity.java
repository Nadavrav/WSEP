package DAL.Entities;

import javax.persistence.*;

@Entity
@Table(name = "product_rating", schema = "onlinestoredb", catalog = "")
@IdClass(ProductRatingEntityPK.class)
public class ProductRatingEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "userName")
    private String userName;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "productId")
    private int productId;
    @Basic
    @Column(name = "rating")
    private double rating;
    @Basic
    @Column(name = "comment")
    private String comment;

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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductRatingEntity that = (ProductRatingEntity) o;

        if (productId != that.productId) return false;
        if (Double.compare(that.rating, rating) != 0) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + productId;
        temp = Double.doubleToLongBits(rating);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }
}
