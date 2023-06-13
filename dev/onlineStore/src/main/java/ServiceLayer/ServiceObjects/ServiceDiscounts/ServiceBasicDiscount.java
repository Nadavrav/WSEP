package ServiceLayer.ServiceObjects.ServiceDiscounts;

import DomainLayer.Stores.Conditions.ConditionFactory;
import DomainLayer.Stores.Discounts.Discount;
import ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords.ConditionRecord;

public class ServiceBasicDiscount extends ServiceDiscount{
    public final ConditionRecord conditionRecord;
    public final double discountAmount;


    public ServiceBasicDiscount(String description,int discountAmount,ConditionRecord conditionRecord) {
        super(description);
        this.discountAmount=discountAmount;
        this.conditionRecord = conditionRecord;
    }

    @Override
    public Discount accept(ConditionFactory conditionFactory) {
        return conditionFactory.addDiscount(this);
    }
}
