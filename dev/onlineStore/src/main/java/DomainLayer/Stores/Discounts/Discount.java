package DomainLayer.Stores.Discounts;

import DomainLayer.Stores.Conditions.BasicConditions.FilterConditions.NoCondition;
import DomainLayer.Stores.Conditions.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;
import ServiceLayer.ServiceObjects.ServiceDiscounts.ServiceAppliedDiscount;
import ServiceLayer.ServiceObjects.ServiceDiscounts.ServiceDiscount;

import java.util.HashSet;

public abstract class Discount {
    protected Condition condition;
    protected String description;
    public Discount(String  description){
        this.description=description;
        condition=new NoCondition();
    }
    public Discount(String  description,Condition condition){
        this.description=description;
        this.condition=condition;
    }
    public Condition getConditions() {
        return condition;
    }
    public void SetCondition(Condition condition){
        this.condition=condition;
    }
    /**
     * calculates the total amount saves by the discount on a product list, for products who pass the condition list
     * intuitions: run this on all discount for a bag, and remove sum of all discount from total price
     * @param bag bag to calculate discount to
     * @return total saved by discounts
     */
    public abstract double calcDiscountAmount(Bag bag);
    public HashSet<CartProduct> getValidProducts(Bag bag) {
        return condition.passCondition(bag);
    }
}
