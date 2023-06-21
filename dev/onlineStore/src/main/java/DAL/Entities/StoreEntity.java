package DAL.Entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "store", schema = "onlinestoredb", catalog = "")
public class StoreEntity {
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "active")
    private boolean active;
    @Basic
    @Column(name = "rate")
    private double rate;
    @OneToMany
    @JoinTable(
            name = "storeproduct",
            joinColumns = @JoinColumn(name = "storeId"),
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    private Set<StoreproductEntity> products;

    public StoreEntity(int storeId, String name, boolean active, double rate)
    {
        this.id = storeId;
        this.name = name;
        this.active = active;
        this.rate = rate;

    }
    public StoreEntity() {

    }

    public Set<StoreproductEntity> getProducts() {
        return products;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StoreEntity that = (StoreEntity) o;

        if (id != that.id) return false;
        if (active != that.active) return false;
        if (Double.compare(that.rate, rate) != 0) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        temp = Double.doubleToLongBits(rate);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
