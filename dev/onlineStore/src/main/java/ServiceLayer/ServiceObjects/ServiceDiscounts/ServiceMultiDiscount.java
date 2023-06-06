package ServiceLayer.ServiceObjects.ServiceDiscounts;

import DomainLayer.Stores.Conditions.ConditionFactory;
import DomainLayer.Stores.Discounts.Discount;
import DomainLayer.Stores.Discounts.ManyDiscounts;

import java.util.Collection;
import java.util.HashSet;

public class ServiceMultiDiscount extends ServiceDiscount{
    private final Collection<Integer> discounts;
    private final DiscountType discountType;
    public ServiceMultiDiscount(DiscountType discountType,String description) {
        super(description);
        this.discountType=discountType;
        discounts=new HashSet<>();
    }

    public ServiceMultiDiscount(DiscountType discountType,ManyDiscounts manyDiscounts) {
        super(manyDiscounts);
        discounts=new HashSet<>();
        for(Discount discount:manyDiscounts.getDiscounts())
            discounts.add(discount.getId());
        this.discountType=discountType;
    }

    public Collection<Integer> getDiscounts() {
        return discounts;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    @Override
    public Discount accept(ConditionFactory conditionFactory) {
        return conditionFactory.addDiscount(this);
    }
}
