package DomainLayer.Stores.Discounts;

import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Users.Bag;

public class AdditiveDiscount extends ManyDiscounts {


    public AdditiveDiscount(String description) {
        super(description);
    }

    public AdditiveDiscount(String description, Condition condition) {
        super(description, condition);
    }

    @Override
    public double calcDiscountAmount(Bag bag) {
        double totalSaved=0;
        for(Discount discount:discounts){
            totalSaved+= discount.calcDiscountAmount(bag);
        }
        return totalSaved;
    }
}
