package DomainLayer.Stores.Discounts;

import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Users.Bag;

import java.util.HashSet;
import java.util.Set;

public class MinSelectiveDiscount extends Discount {
    private final Set<Discount> discounts;

    public MinSelectiveDiscount(String description) {
        super(description);
        discounts=new HashSet<>();
    }

    public MinSelectiveDiscount(String description, Condition condition) {
        super(description, condition);
        discounts=new HashSet<>();
    }
    public void addDiscount(Discount discount){
        discounts.add(discount);
    }
    public boolean removeDiscount(Discount discount){
        return discounts.remove(discount);
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
