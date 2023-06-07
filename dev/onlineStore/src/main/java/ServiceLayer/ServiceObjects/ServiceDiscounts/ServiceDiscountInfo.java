package ServiceLayer.ServiceObjects.ServiceDiscounts;

import DomainLayer.Stores.Conditions.ConditionFactory;
import DomainLayer.Stores.Discounts.Discount;

public class ServiceDiscountInfo extends ServiceDiscount{
    public final int id;

    public ServiceDiscountInfo(String description, int id) {
        super(description);
        this.id=id;
    }

    public ServiceDiscountInfo(Discount discount) {
        super(discount);
        this.id=discount.getId();
    }

    @Override
    public Discount accept(ConditionFactory conditionFactory) {
        throw new IllegalArgumentException("Illegal call to addDiscount with DiscountInfo");
    }
}
