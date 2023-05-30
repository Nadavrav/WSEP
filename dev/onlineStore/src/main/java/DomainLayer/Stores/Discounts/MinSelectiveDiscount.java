package DomainLayer.Stores.Discounts;

import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Users.Bag;

public class MinSelectiveDiscount extends ManyDiscounts {

    public MinSelectiveDiscount(String description) {
        super(description);
    }

    public MinSelectiveDiscount(String description, Condition condition) {
        super(description, condition);
    }
    @Override
    public double calcDiscountAmount(Bag bag) {
        double minDiscount=0;
        for(Discount discount:discounts){
            double discountAmount= discount.calcDiscountAmount(bag);
            if(discountAmount<minDiscount)
                minDiscount=discountAmount;
        }
        return minDiscount;
    }
}
