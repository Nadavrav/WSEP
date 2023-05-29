package DomainLayer.Stores.Discounts;

import DomainLayer.Stores.Conditions.Condition;
import DomainLayer.Users.Bag;

public class MaxSelectiveDiscount extends ManyDiscounts {

    public MaxSelectiveDiscount(String description) {
        super(description);
    }

    public MaxSelectiveDiscount(String description, Condition condition) {
        super(description, condition);
    }
    @Override
    public double calcDiscountAmount(Bag bag) {
        double maxDiscount=0;
        for(Discount discount:discounts){
            double discountAmount= discount.calcDiscountAmount(bag);
            if(discountAmount>maxDiscount)
                maxDiscount=discountAmount;
        }
        return maxDiscount;
    }

}
