package ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords;

import DomainLayer.Stores.Discounts.ConditionFactory;
import DomainLayer.Stores.Discounts.Discount;

public record MaxTotalProductAmountConditionRecord(double amount) implements ConditionRecord {
    @Override
    public Discount accept(ConditionFactory factory, String description, double amount) {
        return factory.addDiscount(this,description,amount);
    }
}
