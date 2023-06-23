package DAL.Entities;

import javax.persistence.*;

@Entity
@Table(name = "store_rating", schema = "onlinestoredb", catalog = "")
@IdClass(StoreRatingEntityPK.class)
public class StoreRatingEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "userName")
    private String userName;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "storeID")
    private int storeId;
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

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
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

        StoreRatingEntity that = (StoreRatingEntity) o;

        if (storeId != that.storeId) return false;
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
        result = 31 * result + storeId;
        temp = Double.doubleToLongBits(rating);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }
}
