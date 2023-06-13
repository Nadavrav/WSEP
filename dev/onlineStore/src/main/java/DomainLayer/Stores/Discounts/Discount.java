package DomainLayer.Stores.Discounts;


import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Discount {
    protected String description;
    protected final int id;
    public Discount(String description,int id){
        this.description=description;
        this.id=id;
    }
    public int getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }

    /**
     * calculates the total amount saves by the discount on a product list, for products who pass the condition list
     * intuitions: run this on all discount for a bag, and remove sum of all discount from total price
     * @param bag bag to calculate discount to
     * @return total saved by discounts
     */
    public abstract double calcDiscountAmount(Bag bag);
    public abstract HashMap<CartProduct,Double> calcDiscountPerProduct(Bag bag);
    public abstract HashSet<CartProduct> getValidProducts(Bag bag);
}
