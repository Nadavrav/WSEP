package DomainLayer.Stores.Discounts;

import DomainLayer.Stores.Discounts.Conditions.BasicConditions.ProductConditions.NoCondition;
import DomainLayer.Stores.Discounts.Conditions.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Stores.Products.Product;
import DomainLayer.Users.Bag;

import java.util.HashSet;

public class Discount {
    private Condition condition;
    private String description;
    /**
     * 30 for a 30% discount, 55.5 for a 55.5% discount etc...
     */
    private double discount;

    public Discount(String description,double discount){
        this.description=description;
        this.discount=discount;
        condition=new NoCondition();
    }
    public Discount(String description,double discount,Condition condition){
        this.description=description;
        this.discount=discount;
        this.condition=condition;
    }

    /**
     * calculates the total amount saves by the discount on a product list, for products who pass the condition list
     * intuitions: run this on all discount for a bag, and remove sum of all discount from total price
     * @param bag bag to calculate discount to
     * @return total saved by discounts
     */
    public double calcDiscountAmount(Bag bag){
        if(bag==null)
            throw new NullPointerException("Null bag in discount calculation");
        double saved=0.0;
            for (CartProduct product : condition.passCondition(bag)) {
                saved += product.getAmount() * (product.getPrice() * (discount / 100)); //for price 40 with a 25% discount, 40*(25/100)=10 was saved by the discount
            }
            return saved;

    }
    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public Condition getConditions() {
        return condition;
    }
    public void SetCondition(Condition condition){
        this.condition=condition;
    }

}
