package DAL.Entities;

import javax.persistence.*;

@Entity
@Table(name = "storeproduct", schema = "onlinestoredb", catalog = "")
public class StoreproductEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "productId")
    private int productId;
    @Basic
    @Column(name = "storeId")
    private int storeId;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "price")
    private double price;
    @Basic
    @Column(name = "category")
    private String category;
    @Basic
    @Column(name = "desc")
    private String desc;
    @Basic
    @Column(name = "avgRating")
    private double avgRating;
    public StoreproductEntity(int productId, int storeId, String name, double price, String category, String desc, double avgRating){
        this.productId=productId;
        this.storeId=storeId;
        this.name=name;
        this.price=price;
        this.category=category;
        this.desc=desc;
        this.avgRating=avgRating;
    }
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StoreproductEntity that = (StoreproductEntity) o;

        if (productId != that.productId) return false;
        if (storeId != that.storeId) return false;
        if (Double.compare(that.price, price) != 0) return false;
        if (Double.compare(that.avgRating, avgRating) != 0) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (category != null ? !category.equals(that.category) : that.category != null) return false;
        if (desc != null ? !desc.equals(that.desc) : that.desc != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = productId;
        result = 31 * result + storeId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        temp = Double.doubleToLongBits(avgRating);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
