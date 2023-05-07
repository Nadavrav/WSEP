package DomainLayer.Stores.Discounts.Discounts;

import DomainLayer.Stores.Discounts.Conditions.BasicConditions.ProductConditions.NoCondition;
import DomainLayer.Stores.Discounts.Conditions.Condition;
import DomainLayer.Stores.Products.CartProduct;
import DomainLayer.Users.Bag;

public class BasicDiscount extends Discount {

    /**
     * 30 for a 30% discount, 55.5 for a 55.5% discount etc...
     */
    private double discount;

    public BasicDiscount(String description, double discount){
        super(description);
        this.discount=discount;
    }
    public BasicDiscount(String description, double discount, Condition condition){
        super(description,condition);
        this.discount=discount;
    }


    @Override
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



}
